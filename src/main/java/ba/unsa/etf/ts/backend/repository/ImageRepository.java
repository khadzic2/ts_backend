package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
