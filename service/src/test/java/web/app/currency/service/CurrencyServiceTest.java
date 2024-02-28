package web.app.currency.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import web.app.currency.client.CurrencyClient;
import web.app.currency.client.model.GetExchangeRatesRequest;
import web.app.currency.client.model.GetExchangeRatesResponse;
import web.app.currency.controller.model.ExchangeRateRequest;
import web.app.currency.controller.model.ExchangeRateResponse;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static web.app.currency.controller.model.ExchangeRateRequest.mapToClient;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {
    @Mock
    private CurrencyClient currencyClientTest;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private CurrencyService currencyService;

    @Test
    @DisplayName("Should return the rates response successfully")
    void shouldReturnSuccessIfTheServerRespondsOkay() {
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest("EUR", "USD", 23.98);
        GetExchangeRatesResponse getExchangeRatesResponse = new GetExchangeRatesResponse(new HashMap<>(), "EUR");
        getExchangeRatesResponse.getRates().put("USD", 1.04);
        when(currencyClientTest.getCurrencyResponse(mapToClient(exchangeRateRequest))).thenReturn(getExchangeRatesResponse);

        ExchangeRateResponse response = currencyService.getRateResponse(exchangeRateRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBase(), exchangeRateRequest.getBase());

        verify(currencyClientTest, times(1)).getCurrencyResponse(any());
    }

    @Test
    @DisplayName("Test if the mapToClient method works properly")
    void testMapperToClient(){
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest("EUR", "USD", 23.98);
        GetExchangeRatesRequest getExchangeRatesRequest = mapToClient(exchangeRateRequest);
        Assertions.assertNotNull(getExchangeRatesRequest);
        Assertions.assertEquals(exchangeRateRequest.getBase(), getExchangeRatesRequest.getBase());
    }
}
