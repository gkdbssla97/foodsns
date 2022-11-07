package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.FoodTag;

public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {
}
