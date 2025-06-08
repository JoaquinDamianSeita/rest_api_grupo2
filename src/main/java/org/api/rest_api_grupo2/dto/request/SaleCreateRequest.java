package org.api.rest_api_grupo2.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
@Data
public class SaleCreateRequest {

    @NotNull(message = "El ID del comprador es obligatorio")
    private Long buyerId;

    @NotNull(message = "El ID del carrito es obligatorio")
    private Long cartId; 

    @NotEmpty(message = "Debe incluir al menos un token para la venta")
    private List<@NotNull(message = "El ID del token no puede ser nulo") Long> tokenIds;

}
