package com.paypal.api_gateway.filters;

import com.paypal.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/signup",
            "/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        String normalizedPath = path.replaceAll("/+$", "");
//        System.out.println("Gateway path = " + normalizedPath);

        //IF request is coming from public path
        if(PUBLIC_PATHS.contains(normalizedPath)){
            return chain.filter(exchange)
                    .doOnSubscribe(s -> System.out.println("Proceeding without Check"))
                    .doOnSuccess(v -> System.out.println("Successfully Passed"))
                    .doOnError(e -> System.err.println("Error Occurred"));
        }

        //If it's not a public path, it's authentication is checked
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

//        System.out.println("Auth header = " + authHeader);

        if(authHeader==null || !authHeader.startsWith("Bearer "))
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try{
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.validateToken(token);
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                    .header("X-User-Email", claims.getSubject())
                     .header("X-User-Id", String.valueOf(claims.get("userId")))
                     .header("X-User-Role", claims.get("role", String.class))
                    //Forward authorization Too
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build())
            .build();

            return chain.filter(mutatedExchange)
                    .doOnSubscribe(s -> System.out.println("Proceeding with check"))
                    .doOnSuccess(v -> System.out.println("Successfully Passed"))
                    .doOnError(e -> System.err.println("Error Occurred"));

        } catch (Exception e){
            e.printStackTrace(); // VERY IMPORTANT
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

    @Override
    public int getOrder() {
        return -100;
    }


}
