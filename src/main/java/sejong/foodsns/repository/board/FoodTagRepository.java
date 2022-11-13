package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.FoodTag;

public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {
}
