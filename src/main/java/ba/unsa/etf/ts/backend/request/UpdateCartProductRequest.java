package ba.unsa.etf.ts.backend.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCartProductRequest {
    @NotNull(message = "Quantity of product must be specified")
    @PositiveOrZero(message = "Quantity of product must be positive number")
    private Integer quantity;
    private String size;
}
