package RenergyCartService.service;

import RenergyCartService.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${external.services.product-service}")
public interface ProductServiceClient {

    @GetMapping("/api/products/{productId}")
    ProductDto getProductById(@PathVariable("productId") Long productId);
}
