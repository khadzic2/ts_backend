package ba.unsa.etf.ts.backend.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProductRequest {
    @NotBlank(message = "Name of product must be specified")
    private String name;
    private String info;
    @NotNull(message = "Price of product must be specified")
    @PositiveOrZero(message = "Price of product must be positive number")
    private Double price;
    @NotNull(message = "Quantity of product must be specified")
    @PositiveOrZero(message = "Quantity of product must be positive number")
    private Integer quantity;
    private String size;
    private String color;
}
