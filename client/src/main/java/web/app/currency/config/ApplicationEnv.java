package web.app.currency.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@Getter
@Setter
@ToString
public class ApplicationEnv {
    private String currencyClientBaseUrl;
    private String currencyClientAccessKey;

    public ApplicationEnv(
            @Value("${currency.client.baseUrl}") String currencyClientBaseUrl,
            @Value("${currency.client.api.key}") String currencyClientAccessKey
    ){
        this.currencyClientBaseUrl = currencyClientBaseUrl;
        this.currencyClientAccessKey = currencyClientAccessKey;
    }
}