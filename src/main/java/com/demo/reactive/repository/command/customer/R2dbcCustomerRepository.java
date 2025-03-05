package com.demo.reactive.repository.command.customer;

import com.demo.reactive.entity.customer.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface R2dbcCustomerRepository extends ReactiveCrudRepository<CustomerEntity, Integer> {
}
