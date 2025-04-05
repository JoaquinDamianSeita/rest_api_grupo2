package org.api.rest_api_grupo2.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.rest_api_grupo2.model.serializables.SaleTokenId;

@Getter
@Setter
@Entity
@Table(name = "sale_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class SaleToken {
    @EmbeddedId
    private SaleTokenId id;

    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @MapsId("tokenId")
    @JoinColumn(name = "token_id")
    private NFTToken token;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;
}
