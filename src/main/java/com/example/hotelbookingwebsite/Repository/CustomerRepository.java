package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
