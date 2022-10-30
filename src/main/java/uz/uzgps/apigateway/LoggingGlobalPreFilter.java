package uz.uzgps.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Component
public class LoggingGlobalPreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
        String originalUri = uris.isEmpty() ? exchange.getRequest().getURI().toString() : uris.iterator().next().toString();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);


        StringBuilder sb = new StringBuilder();
        sb.append("Incoming request ").append(originalUri).append(" is routed to id: ").append(route.getId())
                .append(", uri:").append(routeUri).append(" for ");

        return exchange.getPrincipal().cast(BearerTokenAuthentication.class)
                .map(bearerTokenAuthentication -> {
                    String userName = (String) bearerTokenAuthentication.getTokenAttributes().get("name");
                    String userId = bearerTokenAuthentication.getName();
                    sb.append(" user name: ").append(userName).append(" user id: ").append(userId);

                    return exchange;
                }).defaultIfEmpty(exchange).flatMap(chain::filter);
    }
}