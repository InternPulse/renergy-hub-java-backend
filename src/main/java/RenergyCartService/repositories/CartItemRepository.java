package RenergyCartService.repositories;

import RenergyCartService.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
