package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findAllByCategory_Name(String name);
}
