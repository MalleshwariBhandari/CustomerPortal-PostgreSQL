package com.mb.springboot.postgresql.model.service;

import com.mb.springboot.postgresql.model.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Optional<Customer> getCustomerById(long id);
    List<Customer> getAllCustomers();
    void deleteCustomer(long id);
}
