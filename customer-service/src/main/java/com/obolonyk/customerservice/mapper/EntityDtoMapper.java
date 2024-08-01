package com.obolonyk.customerservice.mapper;

import com.obolonyk.customerservice.dto.CustomerInformation;
import com.obolonyk.customerservice.dto.Holding;
import com.obolonyk.customerservice.entity.Customer;
import com.obolonyk.customerservice.entity.PortfolioItem;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDtoMapper {

     public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items){
         List<Holding> holdings = items.stream()
                 .map(i -> new Holding(i.getTicker(), i.getQuantity()))
                 .collect(Collectors.toList());
         return new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holdings);
     }
}
