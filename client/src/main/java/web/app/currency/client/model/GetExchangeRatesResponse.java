package web.app.currency.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class GetExchangeRatesResponse {
    private HashMap<String, Double> rates;
    private String base;
}
