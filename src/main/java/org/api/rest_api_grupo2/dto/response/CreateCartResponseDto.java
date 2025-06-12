package org.api.rest_api_grupo2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCartResponseDto {
    private String message;
    private Long cartId;
}
