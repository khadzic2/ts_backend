package ba.unsa.etf.ts.backend.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCartProductRequest {
    private Integer productId;
    private String userEmail;
    @NotNull(message = "Quantity of product must be specified")
    @PositiveOrZero(message = "Quantity of product must be positive number")
    private Integer quantity;
    private String size;
}
