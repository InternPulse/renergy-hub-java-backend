package RenergyCartService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Long userId;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Double totalAmount = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Cart(Long userId, Double totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    // Automatically set dates before persistence and updates
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }




    // Calculates the total amount based on the items in the cart
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Convenience method to add an item to the cart
    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
        calculateTotalAmount();
    }

    // Convenience method to remove an item from the cart
    public void removeItem(CartItem item) {
        this.items.remove(item);
        calculateTotalAmount();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

}
