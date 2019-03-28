package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
