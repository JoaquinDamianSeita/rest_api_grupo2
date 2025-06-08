package org.api.rest_api_grupo2.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cglib.core.Local;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private long saleId;
    private long cartId;
    private double salePrice;
    private LocalDateTime saleDate;
    private List<NFTResponse> nfts; // Lista de NFTs vendidos
}
