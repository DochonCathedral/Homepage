package ga.dochon.homepage.model.repository;

import ga.dochon.homepage.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
