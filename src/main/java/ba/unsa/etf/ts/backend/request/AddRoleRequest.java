package ba.unsa.etf.ts.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddRoleRequest {
    private String name;
}
