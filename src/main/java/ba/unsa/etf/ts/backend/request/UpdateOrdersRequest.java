package ba.unsa.etf.ts.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOrdersRequest {
    private Double amount;
    private Boolean shipped;
    private Boolean delivered;
}
