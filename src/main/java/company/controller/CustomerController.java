package company.controller;

import company.model.Order;
import company.model.Product;
import company.repository.OrderRepository;
import company.service.OrderService;
import company.service.ProductService;
import company.utils.Util;

import java.util.List;
import java.util.Optional;

import static company.utils.Util.*;

public class CustomerController {
    private static CustomerController customerController;
    private static ProductService productService = ProductService.getInstance();
    private static OrderService orderService = OrderService.getInstance();

    private CustomerController() {
    }

    public static CustomerController getInstance() {
        if (customerController == null) customerController = new CustomerController();
        return customerController;
    }

    public void customerMenu() {
        boolean b = true;
        while (b) {
            System.out.println(ANSI_BRIGHT_BLUE + "-------> customer menu (" + Util.activeUser.getName() + ") <-------" + ANSI_RESET);
            System.out.println("""
                    1. Barcha mahsulotlarni ko‘rish
                    2. Mahsulot qidirish
                    3. Mahsulot sotib olish
                    4. Xaridlar tarixini ko‘rish
                    5. Filter
                    6. Chiqish""");
            int choose = getInteger("Tanlang");
            switch (choose) {
                case 1 -> showAllProducts();
//                case 2 -> searchProduct();
                case 3 -> buyProduct();
                case 4 -> showMyHistory();
            }
        }
    }

    private void showMyHistory() {
        Optional<List<Order>> optionalOrders = orderService.getMyAllOrders();

        if (optionalOrders.isEmpty() || optionalOrders.get().isEmpty()) {
            System.out.println("Siz hali hech narsa sotib olmadingiz.");
            return;
        }

        List<Order> myOrders = optionalOrders.get();

        System.out.printf("%-20s %-30s %-10s %-15s%n",
                "Sana", "Mahsulot", "Soni", "Umumiy narx");
        System.out.println("-".repeat(80));

        for (Order order : myOrders) {
            String date = order.getOrderDate()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            long total = (long) order.getQuantity() * order.getProduct().getPrice();

            System.out.printf("%-20s %-30s %-10d %-15d so'm%n",
                    date,
                    order.getProduct().getName(),
                    order.getQuantity(),
                    total);
        }
    }

    private void buyProduct() {
        showAllProducts();
        String name = getString("maxsulot nomi");
        int price = getInteger("maxsulot narxi");
        int quantity = getInteger("qancha olmoqchisiz");
        if (productService.buyProduct(name, price, quantity)) {
            System.out.println("bajarildi");
        } else {
            System.err.println("xatolik");
        }
    }

    private void searchProduct() {

    }

    private void showAllProducts() {
        Optional<List<Product>> allProducts = productService.getAllProducts();
        if (allProducts.isPresent()) {
            List<Product> products = allProducts.get();
            products.forEach(System.out::println);
        } else {
            System.out.println("maxsulot yo'q");
        }
    }

}