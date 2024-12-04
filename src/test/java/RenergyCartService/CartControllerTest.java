package RenergyCartService;

import RenergyCartService.controller.CartController;
import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


class CartControllerTest {

        @Mock
        private CartServiceImpl cartService;

        @InjectMocks
        private CartController cartController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void addItemToCart_ShouldReturnUpdatedCart() {
            Long userId = 1L;
            CartItemDto cartItemDto = new CartItemDto();
            CartDto cartDto = new CartDto();

            when(cartService.addItemToCart(userId, cartItemDto)).thenReturn(cartDto);

            ResponseEntity<CartDto> response = cartController.addItemToCart(userId, cartItemDto);

            assertEquals(ResponseEntity.ok(cartDto), response);
            verify(cartService, times(1)).addItemToCart(userId, cartItemDto);
        }

        @Test
        void getCartByUserId_ShouldReturnCart() {
            Long userId = 1L;
            CartDto cartDto = new CartDto();

            when(cartService.getCartByUserId(userId)).thenReturn(cartDto);

            ResponseEntity<CartDto> response = cartController.getCartByUserId(userId);

            assertEquals(ResponseEntity.ok(cartDto), response);
            verify(cartService, times(1)).getCartByUserId(userId);
        }

        @Test
        void updateItemQuantity_ShouldReturnUpdatedCart() {
            Long userId = 1L;
            Long productId = 1L;
            int quantity = 5;
            CartDto cartDto = new CartDto();

            when(cartService.updateItemQuantity(userId, productId, quantity)).thenReturn(cartDto);

            ResponseEntity<CartDto> response = cartController.updateItemQuantity(userId, productId, quantity);

            assertEquals(ResponseEntity.ok(cartDto), response);
            verify(cartService, times(1)).updateItemQuantity(userId, productId, quantity);
        }

        @Test
        void removeItemFromCart_ShouldReturnUpdatedCart() {
            Long userId = 1L;
            Long productId = 1L;
            CartDto cartDto = new CartDto();

            when(cartService.removeItemFromCart(userId, productId)).thenReturn(cartDto);

            ResponseEntity<CartDto> response = cartController.removeItemFromCart(userId, productId);

            assertEquals(ResponseEntity.ok(cartDto), response);
            verify(cartService, times(1)).removeItemFromCart(userId, productId);
        }

        @Test
        void clearCart_ShouldReturnNoContent() {
            Long userId = 1L;

            when(cartService.clearCart(userId)).thenReturn(null);

            ResponseEntity<CartDto> response = cartController.clearCart(userId);

            assertEquals(ResponseEntity.noContent().build(), response);
            verify(cartService, times(1)).clearCart(userId);
        }

        @Test
        void calculateCartTotal_ShouldReturnTotal() {
            Long userId = 1L;
            double total = 100.0;

            when(cartService.calculateCartTotal(userId)).thenReturn(total);

            ResponseEntity<Double> response = cartController.calculateCartTotal(userId);

            assertEquals(ResponseEntity.ok(total), response);
            verify(cartService, times(1)).calculateCartTotal(userId);
        }
    }