package web.app.currency.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientError {
    private Error error;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error{
        private String code;
        private String message;
    }
}
