package web.app.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import web.app.currency.controller.model.ExchangeRateRequest;
import web.app.currency.service.CurrencyService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {
    @Mock
    private CurrencyService currencyService;
    @InjectMocks
    private CurrencyController currencyController;
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Test endpoint /api/currency/convert")
    class GetCurrencies {
        @Test
        @DisplayName("Test getCurrenciesEndpointSuccess")
        public void getCurrencies() throws Exception {
            ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest("EUR", "USD", 23.98);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(exchangeRateRequest);
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Test not valid arguments")
        public void notValidArguments() throws Exception {
            ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest("EUR", "USD", null);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(exchangeRateRequest);

            //1. check amount is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setBase(null);
            exchangeRateRequest.setTarget("USD");
            exchangeRateRequest.setAmount(2.03);
            //2. check base is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget(null);
            exchangeRateRequest.setBase("EUR");
            exchangeRateRequest.setAmount(2.03);
            //3. check target is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("");
            exchangeRateRequest.setBase("EUR");
            exchangeRateRequest.setAmount(2.03);
            //4. check target is empty
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("USD");
            exchangeRateRequest.setBase("");
            exchangeRateRequest.setAmount(2.03);
            //5. check base is empty
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("USD");
            exchangeRateRequest.setBase("EUR");
            exchangeRateRequest.setAmount(0.00);
            //6. check amount is 0
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("USD");
            exchangeRateRequest.setBase("EUR");
            exchangeRateRequest.setAmount(-10.00);
            //7. check amount is negative
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("USDDD");
            exchangeRateRequest.setBase("EUR");
            exchangeRateRequest.setAmount(1.09);
            //8. check base size
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            exchangeRateRequest.setTarget("USD");
            exchangeRateRequest.setBase("EURRR");
            exchangeRateRequest.setAmount(1.09);
            //9. check target size
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

        }
    }
}
