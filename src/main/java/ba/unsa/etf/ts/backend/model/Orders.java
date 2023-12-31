package ba.unsa.etf.ts.backend.model;

import ba.unsa.etf.ts.backend.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String code;
    private LocalDateTime dateAndTime;

    private Double amount;
    private Boolean shipped;
    private Boolean delivered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ordersproduct> products;
}
