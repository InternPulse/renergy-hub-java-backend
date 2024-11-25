package RenergyCartService.service;

import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;

public interface CartService {
    CartDto addItemToCart(Long userId, CartItemDto cartItemDto);

    CartDto getCartByUserId(Long userId);

    CartDto updateItemQuantity(Long userId, Long productId, int quantity);

    CartDto removeItemFromCart(Long userId, Long itemId);

    CartDto clearCart(Long userId);

    double calculateCartTotal(Long userId);
}