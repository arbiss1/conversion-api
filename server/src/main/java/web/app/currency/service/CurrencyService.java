package web.app.currency.service;

import org.springframework.stereotype.Service;
import web.app.currency.client.CurrencyClient;
import web.app.currency.client.model.GetExchangeRatesResponse;
import web.app.currency.controller.model.RateRequest;
import web.app.currency.controller.model.RateResponse;

import static web.app.currency.controller.model.RateRequest.mapToClient;

@Service
public class CurrencyService {
    private final CurrencyClient currencyClient;
    public CurrencyService(CurrencyClient currencyClient){
        this.currencyClient = currencyClient;
    }

    public RateResponse getRateResponse(RateRequest rateRequest) {
            GetExchangeRatesResponse getExchangeRatesResponse = currencyClient.getCurrencyResponse(mapToClient(rateRequest));
            String toCurrency = rateRequest.getTarget();

            Double rate = getExchangeRatesResponse.getRates().get(toCurrency);
            Double totalRate = rate * rateRequest.getAmount();

            return new RateResponse(rateRequest.getBase(), toCurrency, rate, totalRate);
    }
}
