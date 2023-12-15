package com.example.springsecurityexamples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain appSecurity(ServerHttpSecurity http) {
		http
				.headers(h -> h.disable())
				.requestCache(r -> r.disable())
				.logout(l -> l.disable())
				.csrf((csrf) -> csrf.disable()) // commenting this line makes the test pass
				.addFilterAt(new AuthorizationWebFilter(AuthenticatedReactiveAuthorizationManager.authenticated()), SecurityWebFiltersOrder.AUTHORIZATION);
//				.addFilterAt(new TestWebFilter(), SecurityWebFiltersOrder.CSRF);
//				.addFilterAt(new CsrfWebFilter(), SecurityWebFiltersOrder.CSRF); // uncommenting this line makes the test pass
		return http.build();
	}

static class TestWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//		return Mono.empty().switchIfEmpty(chain.filter(exchange)).then(); // this line succeeds
			return chain.filter(exchange); // this line makes the test fail
//			return this.requireCsrfProtectionMatcher.matches(exchange)
//					.filter(ServerWebExchangeMatcher.MatchResult::isMatch)
//					.filter((matchResult) -> !exchange.getAttributes().containsKey(CsrfToken.class.getName()))
//					.flatMap((m) -> validateToken(exchange))
////					.flatMap((m) -> chain.filter(exchange))
//					.switchIfEmpty(chain.filter(exchange));
	}

}



}
