package com.mb.springboot.postgresql.model.controller;

import com.mb.springboot.postgresql.model.domain.Customer;
import com.mb.springboot.postgresql.model.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/new")
    public String showSignUpForm(Customer customer) {
        return "add-customer";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if(customer.isPresent()){
            model.addAttribute("customer", customer);
            return "update-customer";
        }else {
            throw new IllegalArgumentException("Invalid customer Id:" + id);
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if(customer.isPresent()){
            customerService.deleteCustomer(id);
            model.addAttribute("customers", customerService.getAllCustomers());
            return "index";
        }else {
            throw new IllegalArgumentException("Invalid customer Id:" + id);
        }
    }

    @PostMapping("/addcustomer")
    public String addUser(@Valid Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-customer";
        }
        customerService.saveCustomer(customer);
        model.addAttribute("customers", customerService.getAllCustomers());
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") long id, @Valid Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            customer.setId(id);
            return "update-customer";
        }
        customerService.saveCustomer(customer);
        model.addAttribute("customers", customerService.getAllCustomers());
        return "index";
    }
}
