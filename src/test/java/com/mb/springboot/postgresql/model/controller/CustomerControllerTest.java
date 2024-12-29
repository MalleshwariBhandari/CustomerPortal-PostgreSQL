package com.mb.springboot.postgresql.model.controller;

import com.mb.springboot.postgresql.model.domain.Customer;
import com.mb.springboot.postgresql.model.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Name");
        customer.setSurname("Surname");
        customer.setEmail("ns@example.com");
    }

    @Test
    void testShowSignUpForm() {
        String viewName = customerController.showSignUpForm(customer);
        assertEquals("add-customer", viewName);
    }

    @Test
    void testShowUpdateForm_NotFound() {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerController.showUpdateForm(1L, model);
        });

        assertEquals("Invalid customer Id:1", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerService.getAllCustomers()).thenReturn(customers);

        String viewName = customerController.deleteUser(1L, model);

        assertEquals("index", viewName);
        verify(customerService).deleteCustomer(1L);
        verify(model).addAttribute("customers", customers);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerController.deleteUser(1L, model);
        });
        assertEquals("Invalid customer Id:1", exception.getMessage());
    }

    @Test
    void testAddUser_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerService.getAllCustomers()).thenReturn(customers);
        String viewName = customerController.addUser(customer, bindingResult, model);
        assertEquals("index", viewName);
        verify(customerService).saveCustomer(customer);
        verify(model).addAttribute("customers", customers);
    }

    @Test
    void testAddUser_HasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = customerController.addUser(customer, bindingResult, model);
        assertEquals("add-customer", viewName);
        verify(customerService, never()).saveCustomer(any(Customer.class));
    }

    @Test
    void testUpdateUser_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerService.getAllCustomers()).thenReturn(customers);
        String viewName = customerController.updateUser(1L, customer, bindingResult, model);
        assertEquals("index", viewName);
        verify(customerService).saveCustomer(customer);
        verify(model).addAttribute("customers", customers);
    }

    @Test
    void testUpdateUser_HasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = customerController.updateUser(1L, customer, bindingResult, model);
        assertEquals("update-customer", viewName);
        verify(customerService, never()).saveCustomer(any(Customer.class));
    }
}