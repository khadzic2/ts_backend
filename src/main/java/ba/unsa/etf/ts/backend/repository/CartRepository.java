package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findAllByUser_Id(Integer id);
}
