package com.projoker.joker_studio.service.order;

import com.projoker.joker_studio.enums.OrderStatus;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.exception.OutOfStockException;
import com.projoker.joker_studio.model.*;
import com.projoker.joker_studio.repository.OrderRepository;
import com.projoker.joker_studio.repository.ProductRepository;
import com.projoker.joker_studio.service.cart.ICartService;
import com.projoker.joker_studio.service.order_details.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    private final ICartService cartService;
    private final IOrderDetailService orderDetailService;

    @Override
    public Order orderProductsInCart(Long cartId) {
        Cart cart=cartService.getCartById(cartId);
        if(cart.getItemList().isEmpty()){
            throw new ItemNotExistException("Cart is Empty!");
        }
        Order order=new Order();
        order.setUser(cart.getUser());
        orderRepository.save(order);
        for(CartItem item:cart.getItemList()){
            order.getOrderDetails().add(orderDetailService.createOrderDetails(order,item));
            Product product=productRepository.findById(item.getProduct().getId()).get();
            int newInventory=product.getInventory()-item.getQuantity();
            if(newInventory<0){
                throw new OutOfStockException("There is Not Enough item. Available only: "+product.getInventory());
            }
            product.setInventory(newInventory);
        }
        updateOrderStatus(order.getId(), "PENDING");
        order.setOrderAddress(cart.getUser().getAddress());
        order.setOrderAmount(cart.getTotalAmount());
        order.setOrderDateTime(LocalDateTime.now());
        cartService.clearCart(cartId);
        return orderRepository.save(order);
    }

    @Override
    public Order orderProducts(Long userId,Long productId, int quantity) {
        Cart cart=cartService.getCartByUserId(userId);
        cartService.addItemsToCart(cart.getId(),productId,quantity);
        return orderProductsInCart(cart.getId());
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order=getOrderById(orderId);
        for(OrderDetails od:order.getOrderDetails()) {
            Product product = productRepository.findById(od.getProduct().getId()).get();
            product.setInventory(product.getInventory()+od.getQuantity());
            //Here recreating cart for deleting order
            cartService.addItemsToCart(order.getUser().getId(),product.getId(),od.getQuantity());
        }
        orderRepository.delete(order);
    }

    @Override
    public Order updateOrderAddress(Long orderId, OrderAddress address) {
        Order order=getOrderById(orderId);
        OrderAddress newAddress=order.getOrderAddress();
        newAddress.setCity(address.getCity());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setState(address.getState());
        newAddress.setTaluk(address.getTaluk());
        newAddress.setDoorNo(address.getDoorNo());
        newAddress.setLandMark(address.getLandMark());
        newAddress.setPinCode(address.getPinCode());
        newAddress.setStreetName(address.getStreetName());
        order.setOrderAddress(newAddress);
        order.setOrderDateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void addInstructionForDelivery(Long orderId, String instructions) {
        Order order=getOrderById(orderId);
        order.setInstruction(instructions);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getPendingOrders(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId,OrderStatus.PENDING);
    }

    @Override
    public List<Order> getCompletedOrders(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId,OrderStatus.COMPLETED);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order=getOrderById(orderId);
        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> order=orderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new ItemNotExistException("Order item is not exists.");
        }
        return order.get();
    }
}
