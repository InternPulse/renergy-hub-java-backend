package RenergyCartService.controller;

import RenergyCartService.dto.CartItemDto;
import RenergyCartService.model.Cart;
import RenergyCartService.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long userId,
                                               @RequestBody CartItemDto cartItemDto) {
        Cart cart = cartService.addItemToCart(userId, cartItemDto.getProductId(), cartItemDto.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user " + userId));
        return ResponseEntity.ok(cart);
    }


    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemDto cartItemDto) {
        Cart cart = cartService.updateItemQuantity(userId, itemId, cartItemDto.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable Long userId, @PathVariable Long itemId) {
        Cart cart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> calculateCartTotal(@PathVariable Long userId) {
        double total = cartService.calculateCartTotal(userId);
        return ResponseEntity.ok(total);
    }
}
