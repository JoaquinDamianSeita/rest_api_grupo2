package org.api.rest_api_grupo2.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NftTokenInCartDTO {
    private Long id;
    private String title;
    private Double price;
    private List<String> imageUrls;
    private int physicalPieces;
    private String artType;
}
