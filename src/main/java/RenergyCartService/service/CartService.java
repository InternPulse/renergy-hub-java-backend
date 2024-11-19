package RenergyCartService.service;

import RenergyCartService.model.Cart;

import java.util.Optional;

public interface CartService {
    Cart addItemToCart(Long userId, Long productId, int quantity);

    Optional<Cart> getCartByUserId(Long userId);

    Cart updateItemQuantity(Long userId, Long itemId, int quantity);

    Cart removeItemFromCart(Long userId, Long itemId);

    void clearCart(Long userId);

    double calculateCartTotal(Long userId);
}

