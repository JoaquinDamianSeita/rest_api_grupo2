package org.api.rest_api_grupo2.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaleCreateRequest {
    @NotNull(message = "El ID del carrito es obligatorio")
    private Long cartId;
}