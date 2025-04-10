package org.api.rest_api_grupo2.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private long id;
    private LocalDateTime saleDate;
    private long userId;
}
