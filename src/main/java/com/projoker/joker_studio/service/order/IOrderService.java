package com.projoker.joker_studio.service.order;

import com.projoker.joker_studio.dto.OrderDto;
import com.projoker.joker_studio.model.Order;
import com.projoker.joker_studio.model.OrderAddress;

import java.util.List;

public interface IOrderService {
    Order orderProductsInCart(Long cartId);
    Order orderProducts(Long userId,Long productId,int quantity);
    void deleteOrder(Long orderId);
    Order updateOrderAddress(Long orderId, OrderAddress address);
    List<Order> getAllOrderByUserId(Long userId);
    void addInstructionForDelivery(Long orderId,String instructions);
    List<Order> getPendingOrders(Long userId);
    List<Order> getCompletedOrders(Long userId);
    void updateOrderStatus(Long orderId,String status);
    Order getOrderById(Long orderId);

    List<Order> getAllCompletedOrders();

    List<Order> getAllPendingOrder();

    OrderDto orderDto(Order order);

    List<OrderDto> orderDtoList(List<Order> list);
}
