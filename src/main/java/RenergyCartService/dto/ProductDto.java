package RenergyCartService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id; // Product ID
    private String name; // Product name
    private Double price; // Product price
    private int stock;
}
