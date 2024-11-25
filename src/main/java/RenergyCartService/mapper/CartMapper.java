package RenergyCartService.mapper;

import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.model.Cart;
import RenergyCartService.model.CartItem;

import java.util.stream.Collectors;

public class CartMapper {

    // Converts a Cart Entity to CartDto
    public static CartDto toCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        cartDto.setTotalAmount(cart.getTotalAmount());
        cartDto.setItems(cart.getItems().stream()
                .map(CartMapper::toCartItemDto)
                .collect(Collectors.toList()));
        return cartDto;
    }

    // Converts a CartItem Entity to CartItemDto
    private static CartItemDto toCartItemDto(CartItem item) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(item.getProductId());
        cartItemDto.setName(item.getName());
        cartItemDto.setQuantity(item.getQuantity());
        cartItemDto.setPrice(item.getPrice());
        cartItemDto.setTotalPrice(item.getTotalPrice());
        return cartItemDto;
    }
}