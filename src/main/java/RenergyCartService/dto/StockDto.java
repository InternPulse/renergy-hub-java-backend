package RenergyCartService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Long productId; // ID of the product
    private Integer availableQuantity; // Quantity available in stock
}

