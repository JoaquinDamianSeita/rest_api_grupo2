import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaleCreateRequest {

    @NotNull(message = "El ID del comprador es obligatorio")
    private Long buyerId;

    @NotEmpty(message = "Debe incluir al menos un token para la venta")
    private List<@NotNull(message = "El ID del token no puede ser nulo") Long> tokenIds;

}
