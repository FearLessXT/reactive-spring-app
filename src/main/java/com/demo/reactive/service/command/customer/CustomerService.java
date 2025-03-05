package com.demo.reactive.service.command.customer;

import com.demo.reactive.dto.param.RegisterCustomerParam;
import com.demo.reactive.dto.result.customer.CustomerDataResult;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDataResult> registerCustomer(RegisterCustomerParam param);
    Mono<CustomerDataResult> batchRegisterCustomer(Mono<FilePart> file);
}
