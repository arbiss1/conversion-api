package web.app.currency.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.app.currency.controller.model.ExchangeRateRequest;
import web.app.currency.controller.model.ExchangeRateResponse;
import web.app.currency.controller.model.ApiError;
import web.app.currency.service.CurrencyService;

@RestController
@RequestMapping("/api/currency")
@Tag(name = "Currency converter Api", description = "Currency converter routes")
@OpenAPIDefinition(info = @Info(title = "Currency Converter"))
public class CurrencyController {
    private final CurrencyService currencyService;
    public CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @PostMapping("/convert")
    @Operation(description = "Converts the provided amount from a base currency into a target currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The requested currency rate and amount were successfully retrieved", content = @Content(schema = @Schema(implementation = ExchangeRateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public ResponseEntity<ExchangeRateResponse> getRates(@Valid @RequestBody ExchangeRateRequest exchangeRateRequest) {
        return ResponseEntity.ok(currencyService.getRateResponse(exchangeRateRequest));
    }
}
