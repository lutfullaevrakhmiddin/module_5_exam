package company.service;

import company.model.Order;
import company.repository.OrderRepository;
import company.utils.Util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderService {
    private static OrderService orderService;
    private static OrderRepository orderRepository = OrderRepository.getInstance();

    private OrderService() {
    }

    public static OrderService getInstance() {
        if (orderService == null) orderService = new OrderService();
        return orderService;
    }

    public Optional<List<Order>> getMyAllOrders() {
        Optional<List<Order>> allOrdersOpt = orderRepository.getAllOrders();

        if (allOrdersOpt.isEmpty()) {
            return Optional.empty();
        }

        UUID currentCustomerId = Util.activeUser.getId();

        List<Order> myOrders = allOrdersOpt.get().stream()
                .filter(order -> order.getCustomerId().equals(currentCustomerId))
                .toList();

        return myOrders.isEmpty() ? Optional.empty() : Optional.of(myOrders);
    }
}