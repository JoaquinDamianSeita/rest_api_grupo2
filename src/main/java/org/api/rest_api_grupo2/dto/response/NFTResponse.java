package org.api.rest_api_grupo2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFTResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String artType;
    private Integer physicalPieces;
    private Boolean available;
    private List<String> imageUrls;
}
