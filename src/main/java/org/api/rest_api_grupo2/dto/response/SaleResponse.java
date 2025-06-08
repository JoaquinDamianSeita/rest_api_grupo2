package org.api.rest_api_grupo2.dto.response;

import java.time.LocalDateTime;

import java.util.List;
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
    private List<NFTSaleResponse> nfts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NFTSaleResponse {
        private Long id;
        private ArtistResponse artist;
        private String title;
        private String description;
        private double price;
        private LocalDateTime releaseDate;
        private List<String> imageUrls;
        private String artType;
        private int physicalPieces;
        private boolean sold;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistResponse {
        private String firstName;
        private String lastName;
    }
}