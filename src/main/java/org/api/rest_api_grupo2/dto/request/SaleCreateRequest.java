package org.api.rest_api_grupo2.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SaleCreateRequest {

    @NotNull(message = "El ID del comprador es obligatorio")
    private Long buyerId;

    @NotEmpty(message = "Debe incluir al menos un token para la venta")
    private List<@NotNull(message = "El ID del token no puede ser nulo") Long> tokenIds;

    
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public List<Long> getTokenIds() {
        return tokenIds;
    }

    public void setTokenIds(List<Long> tokenIds) {
        this.tokenIds = tokenIds;
    }
}
