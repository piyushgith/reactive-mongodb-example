package com.example.reactive.mongodb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Component
public class CorrelationFilter implements WebFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //To make MDC work in webflux & No Need of Threadcontext or ThreadLocal
        //Example https://github.com/archie-swif/webflux-mdc/blob/master/src/main/java/com/example/webfluxmdc/WebfluxContextMdcApplication.java
        //You must have logback.xml and MdcContextLifter & MdcContextLifterConfiguration
        //You can do some operations before return as well like calling methods
        logger.info("List of Incoming Headers: "+exchange.getRequest().getHeaders());
        logger.info("Ip Address: "+exchange.getRequest().getRemoteAddress().toString());

        return chain.filter(exchange)
                 //it can accept up to 5 key pair
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
