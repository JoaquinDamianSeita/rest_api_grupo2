package org.api.rest_api_grupo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    private String firstName;

    @Size(min = 1, max = 50, message = "El apellido debe tener entre 1 y 50 caracteres")
    private String lastName;

    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 50, message = "El correo electrónico no puede exceder los 50 caracteres")
    private String email;

    @Size(min = 1, max = 50, message = "La dirección debe tener entre 1 y 50 caracteres")
    private String address;

    @Min(1)
    private Long roleId;

    private String biography;
}
