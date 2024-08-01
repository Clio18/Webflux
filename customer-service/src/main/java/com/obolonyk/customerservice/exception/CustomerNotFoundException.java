package com.obolonyk.customerservice.exception;

public class CustomerNotFoundException extends RuntimeException{
    public static final String MESSAGE = "Customer [id = %id] is not found";

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}