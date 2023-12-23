package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Ordersproduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersProductRepository extends JpaRepository<Ordersproduct,Integer> {
    List<Ordersproduct> findOrdersproductByOrders_Id(Integer ordersId);
}
