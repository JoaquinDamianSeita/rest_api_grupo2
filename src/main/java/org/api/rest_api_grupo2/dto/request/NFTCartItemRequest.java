package org.api.rest_api_grupo2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFTCartItemRequest {
    private Long nftId;
    private int physicalPieces;
}
