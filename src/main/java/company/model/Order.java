package company.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Order {
    private UUID id;
    private UUID customerId;
    private Product product;
    private int quantity;
    private LocalDateTime orderDate;
}