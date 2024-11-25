package RenergyCartService.service;

import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.dto.ProductDto;
import RenergyCartService.dto.StockDto;
import RenergyCartService.mapper.CartMapper;
import RenergyCartService.model.Cart;
import RenergyCartService.model.CartItem;
import RenergyCartService.repositories.CartItemRepository;
import RenergyCartService.repositories.CartRepository;
import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private static final Logger log = (Logger) LoggerFactory.getLogger(CartServiceImpl.class);

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
    @Transactional
    public CartDto addItemToCart(Long userId, CartItemDto cartItemDto) {
        //ProductDto product = productServiceClient.getProductById(productId); (Fetch product details using Feign Client)
        // Fetch product details using the mock method
        ProductDto product = mockProduct(cartItemDto.getProductId());

        //StockDto stock = inventoryServiceClient.getStockByProductId(productId); (Check stock availability using Feign Client)
        // Check stock availability using the mock method
        StockDto stock = mockStock(cartItemDto.getProductId());

        if (stock.getAvailableQuantity() < cartItemDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        // Retrieve or create the cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(userId, 0.0));

        // Check if the item already exists in the cart
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(cartItemDto.getProductId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setProductId(cartItemDto.getProductId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItemDto.getQuantity());
            item.setCart(cart);
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
        }
        cart.calculateTotalAmount();
        return mapToDto(cartRepository.save(cart));
    }


    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Long userId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        // Find the cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));


        // Find the specific item (alternative: query directly if filtering fails)
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        // Revalidate stock availability
        StockDto stock = mockStock(item.getProductId());
        if (stock.getAvailableQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        // Update quantity and recalculate total
        item.setQuantity(quantity);
        cart.calculateTotalAmount();

        return mapToDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));

        boolean itemRemoved = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (!itemRemoved) {
            throw new IllegalArgumentException("Item not found in cart");
        }

        cart.calculateTotalAmount();
        Cart updatedCart = cartRepository.save(cart);
        return CartMapper.toCartDto(updatedCart);
    }


    @Override
    @Transactional
    public CartDto clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));
        cart.getItems().clear();
        cartRepository.save(cart);
        return null;
    }

    @Override
    public double calculateCartTotal(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user " + userId));

        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private CartDto mapToDto(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cannot map a null cart to DTO");
        }
        return CartMapper.toCartDto(cart);
    }
}