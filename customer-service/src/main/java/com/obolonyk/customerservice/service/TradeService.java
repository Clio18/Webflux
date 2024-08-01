package com.obolonyk.customerservice.service;

import com.obolonyk.customerservice.dto.StockTradeRequest;
import com.obolonyk.customerservice.dto.StockTradeResponse;
import com.obolonyk.customerservice.entity.Customer;
import com.obolonyk.customerservice.entity.PortfolioItem;
import com.obolonyk.customerservice.exception.ApplicationExceptionsFactory;
import com.obolonyk.customerservice.mapper.EntityDtoMapper;
import com.obolonyk.customerservice.repository.CustomerRepository;
import com.obolonyk.customerservice.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request){
         return switch (request.action()) {
             case BUY -> buyStock(customerId, request);
             case SELL -> sell(customerId, request);
         };
    }

    private Mono<StockTradeResponse> sell(Integer customerId, StockTradeRequest request) {


    }

    private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request) {
        var customerMono = customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptionsFactory.notFound(customerId))
                .filter(c -> c.getBalance() >= request.getTotalPrice())
                .switchIfEmpty(ApplicationExceptionsFactory.insufficientBalance(customerId));

        var portfolioItemMono = portfolioItemRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker()));

        // firstly subscribe to customerMono if its gives customer
        // then subscribe to portfolioItemMono
        // to create a Tuple -> contains both
      return customerMono.zipWhen(customer -> portfolioItemMono)
              .flatMap(t -> executeBuy(t.getT1(), t.getT2(), request));
    }

    private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request){
        customer.setBalance(customer.getBalance() - request.getTotalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
        var response = EntityDtoMapper.toStockTradeResponse(request, customer.getId(), customer.getBalance());

        //to save updated entities in parallel way
        return Mono.zip(customerRepository.save(customer), portfolioItemRepository.save(portfolioItem))
                .thenReturn(response);
    }


}
