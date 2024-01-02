package ba.unsa.etf.ts.backend.response;

import ba.unsa.etf.ts.backend.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCartProductsResponse {
    private List<Cart> products;
    private Double amount;
}
