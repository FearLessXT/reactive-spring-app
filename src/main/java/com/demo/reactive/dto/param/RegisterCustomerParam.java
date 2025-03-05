package com.demo.reactive.dto.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCustomerParam {
    private Long customerId;
    private String walletNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
}
