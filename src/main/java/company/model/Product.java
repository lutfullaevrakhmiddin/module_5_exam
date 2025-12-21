package company.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Product {

    private UUID id;
    private String name;
    private int price;
    private int quantity;
}