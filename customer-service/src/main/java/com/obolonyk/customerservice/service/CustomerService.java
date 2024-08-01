package com.obolonyk.customerservice.service;

import com.obolonyk.customerservice.dto.CustomerInformation;
import com.obolonyk.customerservice.entity.Customer;
import com.obolonyk.customerservice.exception.ApplicationExceptionsFactory;
import com.obolonyk.customerservice.mapper.EntityDtoMapper;
import com.obolonyk.customerservice.repository.CustomerRepository;
import com.obolonyk.customerservice.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<CustomerInformation> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .switchIfEmpty(ApplicationExceptionsFactory.notFound(id))
                .flatMap(this::buildCustomerInformation);
    }

    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        return portfolioItemRepository.getAllByCustomerId(customer.getId())
                .collectList()
                .map(list ->
                        EntityDtoMapper.toCustomerInformation(customer, list));
    }


}
