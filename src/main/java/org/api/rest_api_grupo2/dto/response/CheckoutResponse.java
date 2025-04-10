package org.api.rest_api_grupo2.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private String message;
    private Long cartId;
    private double salePrice;
    private LocalDateTime confirmedAt;
}
