package com.obolonyk.webflux_playground.sec05.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
public class WebFilterDemo01 implements WebFilter {
    public static final Logger log = LoggerFactory.getLogger(WebFilterDemo01.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("received");
        return Mono.empty();
    }
}
