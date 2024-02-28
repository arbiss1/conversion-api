package web.app.currency.service;

import org.springframework.stereotype.Service;
import web.app.currency.client.CurrencyClient;
import web.app.currency.client.model.GetExchangeRatesResponse;
import web.app.currency.controller.model.ExchangeRateRequest;
import web.app.currency.controller.model.ExchangeRateResponse;

import static web.app.currency.controller.model.ExchangeRateRequest.mapToClient;

@Service
public class CurrencyService {
    private final CurrencyClient currencyClient;
    public CurrencyService(CurrencyClient currencyClient){
        this.currencyClient = currencyClient;
    }

    public ExchangeRateResponse getRateResponse(ExchangeRateRequest exchangeRateRequest) {
            GetExchangeRatesResponse getExchangeRatesResponse = currencyClient.getCurrencyResponse(mapToClient(exchangeRateRequest));
            String toCurrency = exchangeRateRequest.getTarget();

            Double rate = getExchangeRatesResponse.getRates().get(toCurrency);
            Double totalRate = rate * exchangeRateRequest.getAmount();

            return new ExchangeRateResponse(exchangeRateRequest.getBase(), toCurrency, rate, totalRate);
    }
}
