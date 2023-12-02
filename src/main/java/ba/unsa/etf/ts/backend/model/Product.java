package ba.unsa.etf.ts.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Name of product must be specified")
    private String name;
    private String info;
    @NotNull(message = "Price of product must be specified")
    private Double price;
    @NotNull(message = "Quantity of product must be specified")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;
}
