package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
}
