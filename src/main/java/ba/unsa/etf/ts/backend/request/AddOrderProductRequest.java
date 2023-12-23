package ba.unsa.etf.ts.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddOrderProductRequest {
    private Integer orderId;

    private Integer productId;

    private Integer quantity;

    private String size;
}
