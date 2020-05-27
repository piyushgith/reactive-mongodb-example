package com.example.reactive.mongodb.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Component
public class CorrelationFilter implements WebFilter {
//correlationId
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .map(s->{System.out.println("List of Incoming Headers: "+exchange.getRequest().getHeaders());  return s; })
                .subscriberContext(Context.of("correlationId", "[correlationId: "+getCorrelationId(exchange)+"]", "ipAddress","[ipAddress: "+ getIpAddress(exchange)+"]"));
    }

    private String getCorrelationId(ServerWebExchange exchange){
        String correlationID=exchange.getRequest().getHeaders().getFirst("correlationId");
        if(null!=correlationID){
            return correlationID;
        }else {
            return UUID.randomUUID().toString();
        }
    }

    private String getIpAddress(ServerWebExchange exchange){
        return exchange.getRequest().getRemoteAddress().toString();
    }


}
