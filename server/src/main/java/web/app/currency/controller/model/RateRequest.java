package web.app.currency.controller.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import web.app.currency.client.model.GetExchangeRatesRequest;

@Data
@AllArgsConstructor
public class RateRequest {
    @NotNull(message = "Base is required")
    @NotEmpty(message = "Base cannot be empty")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "EUR")
    @Size(min = 3, max = 3)
    @Parameter(required = true, description = "Base currency")
    private String base;
    @NotNull(message = "Target is required")
    @NotEmpty(message = "Target cannot be empty")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "USD")
    @Size(min = 3, max = 3)
    @Parameter(required = true, description = "Target currency")
    private String target;
    @NotNull(message = "Amount is required")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "1")
    @Parameter(required = true, description = "Amount")
    @Positive(message = "Amount should be bigger then 0 and not a negative value")
    private Double amount;

    public static GetExchangeRatesRequest mapToClient(RateRequest rateRequest){
        return new GetExchangeRatesRequest(
                rateRequest.getBase()
        );
    }
}
