package com.obolonyk.customerservice.controller;


import com.obolonyk.customerservice.dto.CustomerInformation;
import com.obolonyk.customerservice.dto.StockTradeRequest;
import com.obolonyk.customerservice.dto.StockTradeResponse;
import com.obolonyk.customerservice.service.CustomerService;
import com.obolonyk.customerservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final TradeService tradeService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId){
        return customerService.getCustomerById(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(
            @PathVariable Integer customerId,
            @RequestBody Mono<StockTradeRequest> mono){

        return mono.flatMap(request -> tradeService.trade(customerId, request));
    }
}
