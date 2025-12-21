package company.repository;

import company.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ProductRepository {
    private static ProductRepository productRepository;

    private ProductRepository() {
    }

    public static ProductRepository getInstance() {
        if (productRepository == null) productRepository = new ProductRepository();
        return productRepository;
    }

    public boolean hasProduct(String name, int price) {
        return false;
    }

    public boolean createNewProduct(Product product) {
        String filePath = "src/main/java/company/data/products.txt";

        String line = String.format("%s|%s|%s|%d%n", product.getId(), product.getName(), product.getPrice(), product.getQuantity());

        try {
            Files.writeString(Paths.get(filePath), line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean deleteProductByName(String nameProduct) {
        String filePath = "src/main/java/company/data/products.txt";

        List<Product> products = new ArrayList<>();
        boolean found = false;

        try (var lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] parts = line.split("\\|");
                        if (parts.length == 4) {
                            Product p = new Product(
                                    UUID.fromString(parts[0].trim()),
                                    parts[1].trim(),
                                    Integer.parseInt(parts[2].trim()),
                                    Integer.parseInt(parts[3].trim())
                            );
                            products.add(p);
                        }
                    });

            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()) {
                Product p = iterator.next();
                if (p.getName().equalsIgnoreCase(nameProduct.trim())) {
                    iterator.remove();
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }

            try {
                Files.writeString(Paths.get(filePath), "");

                for (Product p : products) {
                    String line = String.format("%s|%s|%.2f|%d%n",
                            p.getId(),
                            p.getName(),
                            p.getPrice(),
                            p.getQuantity());

                    Files.writeString(Paths.get(filePath), line,
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
                return true;

            } catch (IOException e) {
                return false;
            }

        } catch (IOException e) {
            return false;
        }
    }

    public Optional<List<Product>> getAllProducts() {
        String filePath = "src/main/java/company/data/products.txt";

        if (!Files.exists(Paths.get(filePath))) {
            return Optional.empty();
        }

        List<Product> products = new ArrayList<>();

        try (var lines = Files.lines(Paths.get(filePath))) {

            lines
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] parts = line.split("\\|");
                        if (parts.length == 4) {
                            try {
                                Product product = new Product(
                                        UUID.fromString(parts[0].trim()),
                                        parts[1].trim(),
                                        Integer.parseInt(parts[2].trim()),
                                        Integer.parseInt(parts[3].trim())
                                );
                                products.add(product);
                            } catch (Exception e) {
                            }
                        }
                    });

            if (products.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(products);

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean updatedProduct(List<Product> products) {
        String filePath = "src/main/java/company/data/products.txt";

        try {
            Files.writeString(Paths.get(filePath), "");

            for (Product p : products) {
                String line = String.format("%s|%s|%d|%d%n",
                        p.getId(),
                        p.getName(),
                        (long) p.getPrice(),   // %.2f o‘rniga %d va long casting
                        p.getQuantity());

                Files.writeString(Paths.get(filePath), line,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Faylga yozishda xatolik: " + e.getMessage());
            return false;
        }
    }
}
