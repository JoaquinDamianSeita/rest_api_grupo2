package org.api.rest_api_grupo2.model.serializables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class SaleTokenId {
    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "token_id")
    private Long tokenId;
}
