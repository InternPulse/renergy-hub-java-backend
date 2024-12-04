package RenergyCartService.controller;

import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.model.Cart;
import RenergyCartService.service.CartService;
import RenergyCartService.service.CartServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;


    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(
            @RequestHeader("userId") Long userId,
            @RequestBody CartItemDto cartItemDto) {
        CartDto updatedCart = cartService.addItemToCart(userId, cartItemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        CartDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{userId}/update/{itemId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam ("quantity") int quantity) {
        CartDto updatedCart = cartService.updateItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<CartDto> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        CartDto updatedCart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartDto> clearCart(@PathVariable Long userId) {
        CartDto updatedCart = cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> calculateCartTotal(@PathVariable Long userId) {
        double total = cartService.calculateCartTotal(userId);
        return ResponseEntity.ok(total);
    }
}
