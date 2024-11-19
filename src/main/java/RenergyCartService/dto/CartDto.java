package RenergyCartService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long userId; // User ID
    private List<CartItemDto> items; // List of cart items
    private Double total; // Total price of the cart
}
