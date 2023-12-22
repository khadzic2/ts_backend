package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
