package com.projects.flipkart.Repositories;

import com.projects.flipkart.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IProductRepository extends JpaRepository<Product, Integer> {
}
