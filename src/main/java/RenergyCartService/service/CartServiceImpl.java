package RenergyCartService.service;

import RenergyCartService.dto.CartDto;
import RenergyCartService.dto.CartItemDto;
import RenergyCartService.dto.ProductDto;
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


    @Autowired
    private ProductServiceClient productServiceClient;

    @Override
    @Transactional
    public CartDto addItemToCart(Long userId, CartItemDto cartItemDto) {
        userId = userId != null ? userId : 1L;

        // Fetch product details
        ProductDto product = productServiceClient.getProductById(cartItemDto.getProductId());


        if (cartItemDto.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available for product: " + product.getName());
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
            int newQuantity = item.getQuantity() + cartItemDto.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Not enough stock available for product: " + product.getName());
            }
            item.setQuantity(newQuantity);
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

        // Find the specific item
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        // Revalidate stock availability
        ProductDto product = productServiceClient.getProductById(item.getProductId()); // Replace with Feign client call

        if (product.getStock() < quantity) {
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

        Cart updatedCart = cartRepository.save(cart);
        return CartMapper.toCartDto(updatedCart);
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