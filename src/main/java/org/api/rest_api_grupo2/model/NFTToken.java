package org.api.rest_api_grupo2.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.api.rest_api_grupo2.enums.ArtType;

@Getter
@Setter
@Entity
@Table(name = "nft_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class NFTToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "art_type")
    @Enumerated(EnumType.STRING)
    private ArtType artType;

    @Column(name = "physical_pieces")
    private Integer physicalPieces;

    @Column(name = "available")
    private Boolean available;

    @OneToMany(mappedBy = "nftToken", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageUrl> imageUrls;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "tokens")
    private List<Cart> carts;

    @OneToMany(mappedBy = "token")
    private List<SaleToken> saleTokens;
}
