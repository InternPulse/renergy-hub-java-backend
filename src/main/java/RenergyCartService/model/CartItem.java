package RenergyCartService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cart_id", "product_id"})
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
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
    // Automatically set `totalPrice` and `addedDate` during persist
    protected void onCreate() {
        if (this.price != null && this.quantity != null) {
            this.totalPrice = this.price * this.quantity;
        } else {
            this.totalPrice = 0.0;
        }
        this.addedDate = new Date();
    }

    public CartItem(Long itemId, String testProduct, double v, int newQuantity) {
    }

    public CartItem(long l, long l1, String testProduct, double v, int i) {
    }

    public CartItem(long l, long l1, String newProduct, int i, double v, double v1) {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", addedDate=" + addedDate +
                '}';
    }

}

