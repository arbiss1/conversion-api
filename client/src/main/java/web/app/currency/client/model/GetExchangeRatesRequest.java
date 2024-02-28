package web.app.currency.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetExchangeRatesRequest {
    private String base;
}
