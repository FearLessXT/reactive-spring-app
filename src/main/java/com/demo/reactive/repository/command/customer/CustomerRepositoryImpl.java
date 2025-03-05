package com.demo.reactive.repository.command.customer;

import com.demo.reactive.entity.customer.CustomerEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final R2dbcCustomerRepository repository;

    public CustomerRepositoryImpl(R2dbcCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<CustomerEntity> save(CustomerEntity customerEntity) {
        return repository.save(customerEntity);
    }

    @Override
    public Flux<CustomerEntity> saveAll(Iterable<CustomerEntity> customerEntities) {
        return repository.saveAll(customerEntities);
    }
}
