package com.demo.reactive.controller.command.customer;

import com.demo.reactive.dto.param.RegisterCustomerParam;
import com.demo.reactive.dto.result.customer.CustomerDataResult;
import com.demo.reactive.service.command.customer.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(path = "/register")
    Mono<ResponseEntity<CustomerDataResult>> registerCustomer(@RequestBody CustomerRequest customerRequest) {

        RegisterCustomerParam param = new RegisterCustomerParam();
        BeanUtils.copyProperties(customerRequest, param);

        Mono<CustomerDataResult> result = customerService.registerCustomer(param);
        return result.map(ResponseEntity::ok);
    }

    @PostMapping(path = "/file/upload")
    Mono<ResponseEntity<CustomerDataResult>> uploadFile(@RequestPart("file") Mono<FilePart> file) {
        Mono<CustomerDataResult> result = customerService.batchRegisterCustomer(file);
        return result.map(ResponseEntity::ok);
    }
}
