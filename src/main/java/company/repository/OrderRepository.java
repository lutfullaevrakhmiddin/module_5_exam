package company.repository;

import company.model.Order;
import company.model.Product;
import company.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderRepository {

    private static OrderRepository instance;

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    private OrderRepository() {}

    private final String filePath = "src/main/java/company/data/orders.txt";

    public boolean writeOrder(Order order) {
        String line = String.format("%s|%s|%s|%d|%s%n",
                order.getId(),
                order.getCustomerId(),
                order.getProduct().getId(),
                order.getQuantity(),
                order.getOrderDate());

        try {
            Files.createDirectories(Paths.get("src/main/java/company/data"));
            Files.writeString(Paths.get(filePath), line,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Optional<List<Order>> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String filePath = "src/main/java/company/data/orders.txt";

        if (!Files.exists(Paths.get(filePath))) {
            return Optional.empty();
        }

        try (var lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] parts = line.split("\\|");
                        if (parts.length == 5) {
                            try {
                                UUID orderId = UUID.fromString(parts[0].trim());
                                UUID customerId = UUID.fromString(parts[1].trim());
                                UUID productId = UUID.fromString(parts[2].trim());
                                int quantity = Integer.parseInt(parts[3].trim());
                                LocalDateTime date = LocalDateTime.parse(parts[4].trim());

                                Product product = ProductRepository.getInstance()
                                        .getAllProducts()
                                        .orElse(List.of())
                                        .stream()
                                        .filter(p -> p.getId().equals(productId))
                                        .findFirst()
                                        .orElse(null);

                                if (product != null) {
                                    Order order = new Order(orderId, customerId, product, quantity, date);
                                    orders.add(order);
                                }
                            } catch (Exception ignored) {}
                        }
                    });

            return orders.isEmpty() ? Optional.empty() : Optional.of(orders);

        } catch (IOException e) {
            return Optional.empty();
        }
    }
}