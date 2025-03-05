package com.demo.reactive.service.command.customer;

import com.demo.reactive.common.DataValidation;
import com.demo.reactive.dto.param.RegisterCustomerParam;
import com.demo.reactive.dto.result.customer.CustomerDataResult;
import com.demo.reactive.entity.customer.CustomerEntity;
import com.demo.reactive.repository.command.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    static String SHEET = "Sheet1";

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerDataResult> registerCustomer(RegisterCustomerParam param) {
        return customerRepository.save(CustomerEntity.builder().firstName(param.getFirstName()).lastName(param.getLastName()).walletNumber(param.getWalletNumber()).gender(param.getGender()).build()).map(saveEntity -> CustomerDataResult.builder().message("Successfully Register").status("Success").build()).doOnSuccess(result -> log.info("Customer registered")).onErrorResume(e -> {
            log.error("Registration failed for email {}: {}", param.getEmail(), e.getMessage());
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed. Please try again"));
        });
    }

    @Override
    public Mono<CustomerDataResult> batchRegisterCustomer(Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> Mono.fromCallable(() -> Files.createTempFile(null, filePart.filename())) // Run in separate thread
                .subscribeOn(Schedulers.boundedElastic()) // Prevents blocking main event loop
                .flatMap(tempFilePath -> filePart.transferTo(tempFilePath.toFile())// Save file
                                .subscribeOn(Schedulers.boundedElastic()).then(parseExcelFile(tempFilePath).collectList()) // Parse Excel
                                .flatMap(customerEntities -> {
                                    List<String> validationErrors = new ArrayList<>();
                                    List<CustomerEntity> validCustomers = new ArrayList<>();

                                    for (ParseResult result : customerEntities) {
                                        if (!result.validationErrors().isEmpty()) {
                                            validationErrors.add("Skipping row " + result.rowNumber() + " due to validation errors: " + result.validationErrors());
                                        } else {
                                            validCustomers.add(result.entity());
                                        }
                                    }

//                                    if (customerEntities.isEmpty()) {
//                                        return Mono.error(new IllegalArgumentException("Uploaded file contains no valid customer data"));
//                                    }

                                    if (validCustomers.isEmpty()) {
                                        return Mono.error(new IllegalArgumentException("Uploaded file contains no valid customer data. Errors: " + validationErrors));
                                    }

                                    // handle business logic here
                                    return customerRepository.saveAll(validCustomers)
                                            .collectList().map(saveCustomers ->
                                                    new CustomerDataResult(
                                                            "File Upload Completed",
                                                            "Partial Success",
                                                            saveCustomers.size(), filePart.filename(),
                                                            validationErrors))
                                            .subscribeOn(Schedulers.boundedElastic()); // Run in separate thread
                                })
                                //.map(savedCustomers -> new CustomerDataResult("File Upload Successfully", "success", savedCustomers.size(), filePart.filename()))
                                //.onErrorResume(e -> Mono.just(new CustomerDataResult("Failed to upload", "Failed", 0, null)))
                                .onErrorResume(e -> Mono.just(new CustomerDataResult("Failed to upload", "Failed", 0, null, List.of(e.getMessage())))).doFinally(signalType -> deleteTempFile(tempFilePath)) // Cleanup temp file
                ));
    }

    // Helper to delete the temp file after processing
    private void deleteTempFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Failed to delete temp file: " + path);
        }
    }

    private Flux<ParseResult> parseExcelFile(Path filePath) {
        return Flux.using(() -> new XSSFWorkbook(Files.newInputStream(filePath)), // Read from temp file
                workbook -> Flux.fromIterable(workbook.getSheet(SHEET)), workbook -> {
                    try {
                        workbook.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Error closing workbook: " + e.getMessage());
                    }
                }).subscribeOn(Schedulers.boundedElastic()).skip(1L).index().flatMap(tuple -> {
            Row row = tuple.getT2();
            long rowNum = tuple.getT1() + 1;
            return Mono.fromCallable(() -> parseRow(row, rowNum)).subscribeOn(Schedulers.boundedElastic());
        }, 5);
    }

    private record ParseResult(CustomerEntity entity, List<String> validationErrors, long rowNumber) {
    }

    private ParseResult parseRow(Row row, long rowNum) {
        CustomerEntity entity = new CustomerEntity();
        DataFormatter dataFormatter = new DataFormatter();
        List<String> validationErrors = new ArrayList<>();

        try {

            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                String cellValue = dataFormatter.formatCellValue(cell).trim();

                switch (columnIndex) {
                    case 0 -> {
                        Pattern walletPattern = Pattern.compile("^855\\d{8,9}$");
                        if (cellValue.isEmpty() || !walletPattern.matcher(cellValue).matches()) {
                            validationErrors.add("Invalid wallet number at column " + columnIndex);
                        } else {
                            entity.setWalletNumber(cellValue);
                        }
                    }
                    case 1 -> {
                        if (cellValue.isEmpty()) {
                            validationErrors.add("Invalid firstname at column " + columnIndex);
                        } else {
                            entity.setFirstName(cellValue);
                        }
                    }
                    case 2 -> {
                        if (cellValue.isEmpty()) {
                            validationErrors.add("Invalid lastname at column " + columnIndex);
                        } else {
                            entity.setLastName(cellValue);
                        }
                    }
                    case 3 -> {
                        if (cellValue.isEmpty()) {
                            validationErrors.add("Invalid Gender at column " + columnIndex);
                        } else {
                            entity.setGender(cellValue);
                        }
                    }
                    case 4 -> entity.setDob(cellValue);
                    case 5 -> {
                        if (cellValue.isEmpty()) {
                            validationErrors.add("Invalid Nationality at column " + columnIndex + ": " + cellValue);
                        }
                        entity.setNationality(cellValue);
                    }
                    case 6 -> {
                        if (cellValue.isEmpty() || DataValidation.isValid(cellValue)) {
                            validationErrors.add("Invalid Nationality at column " + columnIndex + ": " + cellValue);
                        }
                        entity.setIdType(cellValue);
                    }
                    case 7 -> entity.setIdNumber(cellValue);
                    case 8 -> entity.setIdExpirationDate(cellValue);
                    case 9 -> entity.setAccountType(cellValue);
                    case 10 -> entity.setPhotoOfId(cellValue);
                    case 11 -> entity.setSelfiePhoto(cellValue);
                    case 12 -> {
                        if(cellValue.isEmpty()) {
                            validationErrors.add("Invalid Pin at column " + columnIndex + ": " + cellValue);
                        }
                        entity.setPin(cellValue);
                    }
                    default ->
                            throw new IllegalArgumentException("Unexpected column index: " + columnIndex + "in row " + rowNum);
                }
            }

            if (!validationErrors.isEmpty()) {
                return new ParseResult(null, validationErrors, rowNum);
            }

            validateRequiredFields(entity, rowNum);
            return new ParseResult(entity, List.of(), rowNum);
        } catch (Exception e) {
            return new ParseResult(null, List.of("Error processing row " + rowNum + ": " + e.getMessage()), rowNum);
        }
    }

    private void validateRequiredFields(CustomerEntity entity, long rowNumber) {
        if (entity.getFirstName() == null) {
            throw new IllegalArgumentException("Missing first name in row " + rowNumber);
        }
    }
}