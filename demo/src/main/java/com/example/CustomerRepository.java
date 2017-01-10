package com.example;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface CustomerRepository extends MongoRepository<Customer, String> {
	public Customer findByCustomerId(String id);
}
