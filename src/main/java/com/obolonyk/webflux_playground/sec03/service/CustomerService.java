package com.obolonyk.webflux_playground.sec03.service;

import com.obolonyk.webflux_playground.sec03.dto.CustomerDto;
import com.obolonyk.webflux_playground.sec03.mapper.EntityDtoMapper;
import com.obolonyk.webflux_playground.sec03.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllPageable(Integer page, Integer size){
        return customerRepository.findAllBy(PageRequest.of(page-1, size))
                .map(EntityDtoMapper::entityToDto);
    }

    public Flux<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .map(EntityDtoMapper::entityToDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(EntityDtoMapper::entityToDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> mono) {
        return mono.map(EntityDtoMapper::dtoToEntity)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::entityToDto);
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> mono) {
        return customerRepository.findById(id)
                // mono is Mono<CustomerDto>, so in case of map it will be Mono<Mono<CustomerDto>>, that why to inline
                // that why to inline use flatMap
                .flatMap(c -> mono)
                .map(EntityDtoMapper::dtoToEntity)
                .doOnNext(e -> e.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::entityToDto);
    }

    public Mono<Boolean> deleteCustomer(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }


}
