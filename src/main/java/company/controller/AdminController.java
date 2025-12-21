package company.controller;

import company.model.Product;
import company.service.ProductService;
import company.utils.Util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static company.utils.Util.*;

public class AdminController {
    private static AdminController adminController;
    private static ProductService productService = ProductService.getInstance();

    private AdminController() {
    }

    public static AdminController getInstance() {
        if (adminController == null) adminController = new AdminController();
        return adminController;
    }

    public void adminMenu() {
        boolean b = true;
        while (b) {
            System.out.println(ANSI_BRIGHT_BLUE + "-------> admin menu (" + Util.activeUser.getName() + ") <-------" + ANSI_RESET);
            System.out.println("""
                    1. Mahsulot qo‘shish
                    2. Mahsulotni o‘chirish
                    3. Mahsulotni yangilash
                    4. Mahsulotlar ro‘yxatini ko‘rish
                    5. Savdo tarixini ko‘rish
                    6. Chiqish
                    """);
            int tanlang = Util.getInteger("Tanlang");
            switch (tanlang) {
                case 1 -> addProduct();
                case 2 -> deleteProduct();
                case 3 -> updateProduct();
                case 4 -> viewAllProducts();
//                case 5 -> viewOrderHistory();
                case 6 -> b = false;
            }
        }
    }

    private void viewAllProducts() {
        Optional<List<Product>> allProducts = productService.getAllProducts();
        if (allProducts.isPresent()) {
            List<Product> products = allProducts.get();
            products.forEach(System.out::println);
        } else {
            System.out.println("maxsulot yo'q");
        }
    }

    private void updateProduct() {
        String name = getString("maxsulot nomi");
        int price = getInteger("maxsulot narxi");
        int newPrice = getInteger("maxsulotning yangi narxi");
        int newQuantity = getInteger("maxsulotning yangi miqdori");
        if (productService.updateProduct(name, price, newPrice, newQuantity)) {
            System.out.println("yangilandi");
        } else {
            System.err.println("xatolik !!!");
        }
    }

    private void deleteProduct() {
        if (productService.deleteProductByName(getString("maxsulot nomi"))) {
            System.out.println("o'chirildi");
        } else {
            System.out.println("xatolik");
        }
    }

    private void addProduct() {
        String name = getString("maxsulot nomi");
        int price = getInteger("maxulot narxi");
        int quantity = getInteger("maxsulot miqdori");
        if (productService.addProduct(name, price, quantity)) {
            System.out.println("maxsulot qo'shildi");
        } else {
            System.err.println("xatolik !!!");
        }
    }
}
