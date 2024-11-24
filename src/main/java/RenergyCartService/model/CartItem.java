package RenergyCartService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonManagedReference
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column (nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date addedDate;

    @PrePersist
    @PreUpdate
    protected void calculateTotalPrice() {
        if (price != null && quantity != null) {
            this.totalPrice = price * quantity; // Calculate total price
        } else {
            this.totalPrice = 0.0; // Default value if data is missing
        }

        if (addedDate == null) {
            this.addedDate = new Date(); // Automatically set addedDate
        }
    }

    public CartItem(Long itemId, String testProduct, double v, int newQuantity) {
    }

    public CartItem(long l, long l1, String testProduct, double v, int i) {
    }

    public CartItem(long l, long l1, String newProduct, int i, double v, double v1) {
    }
}

