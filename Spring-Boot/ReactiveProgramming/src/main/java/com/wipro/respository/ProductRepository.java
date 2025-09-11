package com.wipro.respository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.entities.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product , Long> {

}