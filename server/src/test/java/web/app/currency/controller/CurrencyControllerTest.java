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
import web.app.currency.controller.model.RateRequest;
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
            RateRequest rateRequest = new RateRequest("EUR", "USD", 23.98);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(rateRequest);
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Test not valid arguments")
        public void notValidArguments() throws Exception {
            RateRequest rateRequest = new RateRequest("EUR", "USD", null);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(rateRequest);

            //1. check amount is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setBase(null);
            rateRequest.setTarget("USD");
            rateRequest.setAmount(2.03);
            //2. check base is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget(null);
            rateRequest.setBase("EUR");
            rateRequest.setAmount(2.03);
            //3. check target is null
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("");
            rateRequest.setBase("EUR");
            rateRequest.setAmount(2.03);
            //4. check target is empty
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("USD");
            rateRequest.setBase("");
            rateRequest.setAmount(2.03);
            //5. check base is empty
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("USD");
            rateRequest.setBase("EUR");
            rateRequest.setAmount(0.00);
            //6. check amount is 0
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("USD");
            rateRequest.setBase("EUR");
            rateRequest.setAmount(-10.00);
            //7. check amount is negative
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("USDDD");
            rateRequest.setBase("EUR");
            rateRequest.setAmount(1.09);
            //8. check base size
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            rateRequest.setTarget("USD");
            rateRequest.setBase("EURRR");
            rateRequest.setAmount(1.09);
            //9. check target size
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

        }
    }
}
