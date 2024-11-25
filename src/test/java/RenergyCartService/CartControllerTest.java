package RenergyCartService;

import RenergyCartService.controller.CartController;
import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItemToCart_ShouldReturnUpdatedCart() throws Exception {
        CartDto mockCart = new CartDto();
        mockCart.setUserId(1L);
        mockCart.setTotalAmount(200.0);

        CartItemDto mockCartItem = new CartItemDto();
        mockCartItem.setProductId(1L);
        mockCartItem.setName("Product 1");
        mockCartItem.setQuantity(2);
        mockCartItem.setPrice(100.0);

        Mockito.when(cartService.addItemToCart(eq(1L), any(CartItemDto.class))).thenReturn(mockCart);

        mockMvc.perform(post("/api/v1/cart/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCartItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.totalAmount", is(200.0)));
    }

    @Test
    void getCartByUserId_ShouldReturnCart() throws Exception {
        CartDto mockCart = new CartDto();
        mockCart.setUserId(1L);
        mockCart.setTotalAmount(200.0);

        Mockito.when(cartService.getCartByUserId(1L)).thenReturn(mockCart);

        mockMvc.perform(get("/api/v1/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.totalAmount", is(200.0)));
    }

    @Test
    void updateItemQuantity_ShouldReturnUpdatedCart() throws Exception {
        CartDto mockCart = new CartDto();
        mockCart.setUserId(1L);
        mockCart.setTotalAmount(400.0);

        Mockito.when(cartService.updateItemQuantity(1L, 1L, 5)).thenReturn(mockCart);

        mockMvc.perform(put("/api/v1/cart/1/items/1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.totalAmount", is(400.0)));
    }

    @Test
    void removeItemFromCart_ShouldReturnUpdatedCart() throws Exception {
        CartDto mockCart = new CartDto();
        mockCart.setUserId(1L);
        mockCart.setTotalAmount(100.0);

        Mockito.when(cartService.removeItemFromCart(1L, 1L)).thenReturn(mockCart);

        mockMvc.perform(delete("/api/v1/cart/1/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.totalAmount", is(100.0)));
    }

    @Test
    void clearCart_ShouldReturnNoContent() throws Exception {
        Mockito.when(cartService.clearCart(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/cart/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void calculateCartTotal_ShouldReturnTotalAmount() throws Exception {
        Mockito.when(cartService.calculateCartTotal(1L)).thenReturn(500.0);

        mockMvc.perform(get("/api/v1/cart/1/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }
}
