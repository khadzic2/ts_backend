package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
