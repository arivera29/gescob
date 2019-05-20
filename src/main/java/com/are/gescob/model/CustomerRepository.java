package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
