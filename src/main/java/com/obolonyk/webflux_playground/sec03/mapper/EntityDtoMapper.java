package com.obolonyk.webflux_playground.sec03.mapper;

import com.obolonyk.webflux_playground.sec03.dto.CustomerDto;
import com.obolonyk.webflux_playground.sec03.entity.Customer;

public class EntityDtoMapper {
    public static CustomerDto entityToDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }

    public static Customer dtoToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.id());
        customer.setName(customerDto.name());
        customer.setEmail(customerDto.email());
        return customer;
    }
}
