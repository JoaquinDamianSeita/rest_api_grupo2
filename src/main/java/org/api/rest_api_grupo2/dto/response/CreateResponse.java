package org.api.rest_api_grupo2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateResponse {
    private String message;
    private Long cartId;
}
