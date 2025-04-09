package org.api.rest_api_grupo2.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFTCreateRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @NotNull(message = "Las URLs de imagen son obligatorias")
    @Size(min = 1, message = "¡No te olvides de la imagen!")
    private List<@NotBlank(message = "Las URLs no pueden estar vacías") String> imageUrls;

    @NotBlank(message = "El tipo de arte es obligatorio")
    private String artType;

    @NotNull(message = "Debe especificar la cantidad de piezas físicas")
    @Min(value = 0, message = "Las piezas físicas no pueden ser negativas")
    private Integer physicalPieces;

    @NotNull(message = "Debe indicar si está disponible")
    private Boolean available;
}
