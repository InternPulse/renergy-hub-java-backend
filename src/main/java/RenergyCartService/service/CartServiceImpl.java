package RenergyCartService.service;

import RenergyCartService.dto.ProductDto;
import RenergyCartService.dto.StockDto;
import RenergyCartService.model.Cart;
import RenergyCartService.model.CartItem;
import RenergyCartService.repositories.CartItemRepository;
import RenergyCartService.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    //@Autowired
    //private ProductServiceClient productServiceClient;

    //@Autowired
    //private InventoryServiceClient inventoryServiceClient;

    // Mocked ProductServiceClient and InventoryServiceClient interactions
    private ProductDto mockProduct(Long productId) {
        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setName("Mock Product Name"); // Mocked product name
        product.setPrice(100.0); // Mocked price
        return product;
    }

    private StockDto mockStock(Long productId) {
        StockDto stock = new StockDto();
        stock.setProductId(productId);
        stock.setAvailableQuantity(50); // Mocked available quantity
        return stock;
    }

    @Override
    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        //ProductDto product = productServiceClient.getProductById(productId); (Fetch product details using Feign Client)
        // Fetch product details using the mock method
        ProductDto product = mockProduct(productId);

        //StockDto stock = inventoryServiceClient.getStockByProductId(productId); (Check stock availability using Feign Client)
        // Check stock availability using the mock method
        StockDto stock = mockStock(productId);

        if (stock.getAvailableQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        // Retrieve or create the cart
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        cart.setUserId(userId);

        // Check if the item already exists in the cart
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setProductId(productId);
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setCart(cart);
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        cart.calculateTotalAmount();

        cartRepository.save(cart);
        return cart;
    }


    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        return Optional.ofNullable(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId)));
    }

    @Override
    public Cart updateItemQuantity(Long userId, Long itemId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // Revalidate stock availability using Feign Client
        //StockDto stock = inventoryServiceClient.getStockByProductId(item.getProductId());
        StockDto stock = mockStock(item.getProductId());

        if (stock.getAvailableQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        item.setQuantity(quantity);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Cart removeItemFromCart(Long userId, Long itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Item not found"));

        cart.getItems().remove(item);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public double calculateCartTotal(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));

        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}