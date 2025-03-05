package com.demo.reactive.controller.command.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    private String walletNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String gender;
}
