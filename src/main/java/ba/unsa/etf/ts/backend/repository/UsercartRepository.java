package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Usercart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsercartRepository extends JpaRepository<Usercart,Integer> {
    List<Usercart> findAllByUser_Id(Integer id);
}
