package web.app.currency.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateResponse {
    private String base;
    private String target;
    private Double rate;
    private Double totalAmount;
}
