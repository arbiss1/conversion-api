package web.app.currency.client;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import web.app.currency.client.model.ClientError;
import web.app.currency.client.model.GetExchangeRatesRequest;
import web.app.currency.client.model.GetExchangeRatesResponse;
import web.app.currency.config.ApplicationEnv;
import web.app.currency.exceptions.GenericClientException;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@EnableScheduling
@EnableCaching
public class CurrencyClient {
    private final ApplicationEnv applicationEnv;
    private final WebClient webClient;
    private final CacheManager cacheManager;
    public CurrencyClient(ApplicationEnv applicationEnv, CacheManager cacheManager, WebClient.Builder webClientBuilder){
       this.applicationEnv = applicationEnv;
       this.cacheManager = cacheManager;
       this.webClient = webClientBuilder.baseUrl(applicationEnv.getCurrencyClientBaseUrl()).build();
    }

    @Cacheable(value = "exchangeRates", key = "#date")
    public GetExchangeRatesResponse getCurrencyResponse(LocalDate date, GetExchangeRatesRequest getExchangeRatesRequest) {
        GetExchangeRatesResponse cachedResponse = Objects.requireNonNull(cacheManager.getCache("exchangeRates")).get(date, GetExchangeRatesResponse.class);
        if (cachedResponse != null) {
            return cachedResponse;
        }
        GetExchangeRatesResponse getExchangeRatesResponse =  webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_key", applicationEnv.getCurrencyClientAccessKey())
                        .queryParam("base", getExchangeRatesRequest.getBase())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(ClientError.class)
                                .flatMap(errorBody -> Mono.error(new GenericClientException("Client responded with error status: " + errorBody.getError().getCode() + " and body: " + errorBody.getError().getMessage())))
                )
                .bodyToMono(GetExchangeRatesResponse.class)
                .onErrorMap(WebClientResponseException.class, ex -> {
                    throw new RuntimeException("Error occurred while fetching currency response", ex);
                }).block();

        clearCache(date.minusDays(1));
        Objects.requireNonNull(cacheManager.getCache("exchangeRates")).put(date, getExchangeRatesResponse);

        return getExchangeRatesResponse;
    }

    @CacheEvict(value = "exchangeRates", key = "#date")
    public void clearCache(LocalDate date) {
    }
}
