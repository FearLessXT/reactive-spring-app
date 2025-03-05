package com.demo.reactive.dto.result.customer;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class CustomerDataResult {
    private String message;
    private String status;
    private int processedRecords;
    private String fileName;
    private List<String> validationErrors; // NEW FIELD

}
