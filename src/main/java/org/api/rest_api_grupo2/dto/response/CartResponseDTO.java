package org.api.rest_api_grupo2.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartResponseDTO {
    private Long id;
    private List<NftTokenInCartDTO> nfts;
}
