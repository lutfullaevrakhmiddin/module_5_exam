package company.service;

import company.model.Order;
import company.model.Product;
import company.repository.OrderRepository;
import company.repository.ProductRepository;
import company.utils.Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductService {
    private static ProductService productService;
    private static ProductRepository productRepository = ProductRepository.getInstance();
    private static OrderRepository orderRepository = OrderRepository.getInstance();

    private ProductService() {
    }

    public static ProductService getInstance() {
        if (productService == null) productService = new ProductService();
        return productService;
    }

    public boolean addProduct(String name, int price, int quantity) {
        if (productRepository.hasProduct(name, price)) {
            return false;
        } else {
            Product product = new Product(UUID.randomUUID(), name.toLowerCase(), price, quantity);
            return productRepository.createNewProduct(product);
        }
    }

    public boolean deleteProductByName(String nameProduct) {
        return productRepository.deleteProductByName(nameProduct);
    }

    public boolean updateProduct(String name, int price, int newPrice, int newQuantity) {
        Optional<List<Product>> optionalProducts = productRepository.getAllProducts();

        if (optionalProducts.isEmpty()) {
            return false;
        }

        List<Product> products = optionalProducts.get();
        boolean updated = false;

        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name.trim()) && p.getPrice() == price) {
                if (newPrice > 0) {
                    p.setPrice(newPrice);
                }
                if (newQuantity >= 0) {
                    p.setQuantity(newQuantity);
                }
                updated = true;
                break;
            }
        }

        if (!updated) {
            return false;
        }

        return productRepository.updatedProduct(products);
    }

    public Optional<List<Product>> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public boolean buyProduct(String name, int price, int quantity) {
        Optional<List<Product>> optionalProducts = productRepository.getAllProducts();

        if (optionalProducts.isEmpty()) {
            return false;
        }

        List<Product> products = optionalProducts.get();
        Product targetProduct = null;

        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name.trim()) && p.getPrice() == price) {
                targetProduct = p;
                break;
            }
        }

        if (targetProduct == null) {
            return false;
        }

        if (targetProduct.getQuantity() < quantity) {
            return false;
        }

        targetProduct.setQuantity(targetProduct.getQuantity() - quantity);

        boolean productsUpdated = productRepository.updatedProduct(products);
        if (!productsUpdated) {
            return false;
        }

        Order newOrder = new Order(
                UUID.randomUUID(),
                Util.activeUser.getId(),
                targetProduct,
                quantity,
                LocalDateTime.now()
        );

        return orderRepository.writeOrder(newOrder);
    }
}
