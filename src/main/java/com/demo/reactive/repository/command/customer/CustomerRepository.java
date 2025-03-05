package com.demo.reactive.repository.command.customer;

import com.demo.reactive.entity.customer.CustomerEntity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

public interface CustomerRepository {
    Mono<CustomerEntity> save(CustomerEntity customerEntity);
    Flux<CustomerEntity> saveAll(Iterable<CustomerEntity> customerEntities);
}
