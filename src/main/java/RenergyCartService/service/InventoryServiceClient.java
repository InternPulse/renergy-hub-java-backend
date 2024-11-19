package RenergyCartService.service;

import RenergyCartService.dto.StockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", url = "${external.services.inventory-service}")
public interface InventoryServiceClient {

    @GetMapping("/api/inventory/{productId}/stock")
    StockDto getStockByProductId(@PathVariable("productId") Long productId);
}
