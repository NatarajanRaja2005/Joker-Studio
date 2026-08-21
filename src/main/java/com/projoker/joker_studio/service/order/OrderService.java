package com.projoker.joker_studio.service.order;

import com.projoker.joker_studio.dto.OrderDto;
import com.projoker.joker_studio.dto.UserDto;
import com.projoker.joker_studio.enums.OrderStatus;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.exception.OutOfStockException;
import com.projoker.joker_studio.model.*;
import com.projoker.joker_studio.repository.OrderRepository;
import com.projoker.joker_studio.repository.ProductRepository;
import com.projoker.joker_studio.service.cart.ICartService;
import com.projoker.joker_studio.service.notification.INotificationService;
import com.projoker.joker_studio.service.order_details.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    private final ICartService cartService;
    private final IOrderDetailService orderDetailService;
    private final INotificationService notificationService;
    private final ModelMapper modelMapper;

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
        orderRepository.save(order);
        notificationService.notify(order.getUser(),new NotifyMessage("Order was created Successfully.",orderMessage(order)+"\n\nOrder created Successfully"));
        return order;
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
        Cart userCart=cartService.getCartByUserId(order.getUser().getId());
        for(OrderDetails od:order.getOrderDetails()) {
            Product product = productRepository.findById(od.getProduct().getId()).get();
            product.setInventory(product.getInventory()+od.getQuantity());
            //Here recreating cart for deleting order
            cartService.addItemsToCart(userCart.getId(),product.getId(),od.getQuantity());
        }
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
        orderRepository.save(order);
        notificationService.notify(order.getUser(),new NotifyMessage("Order was updated Successfully.",orderMessage(order)+"\n\nOrder updated Successfully"));

        return order;
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
        notificationService.notify(order.getUser(), new NotifyMessage("Order Status was Changed.",orderMessage(order)+"\n\nOrder Status updated Successfully, Kindly notice it."));
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

    @Override
    public List<Order> getAllCompletedOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.COMPLETED);
    }

    @Override
    public List<Order> getAllPendingOrder() {
        return orderRepository.findByOrderStatus(OrderStatus.PENDING);
    }

    @Override
    public OrderDto orderDto(Order order){
        OrderDto orderDto=new OrderDto();
        orderDto.setId(order.getId());
        UserDto userDto=modelMapper.map(order.getUser(), UserDto.class);
        orderDto.setUserDto(userDto);
        orderDto.setOrderAmount(order.getOrderAmount());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setInstruction(order.getInstruction());
        orderDto.setOrderDateTime(order.getOrderDateTime());
        orderDto.setOrderAddress(order.getOrderAddress());
        orderDto.setOrderDetails(order.getOrderDetails());

        return orderDto;
    }

    @Override
    public List<OrderDto> orderDtoList(List<Order> list){
        return list.stream().map(this::orderDto).toList();
    }
    private String orderMessage(Order order) {

        StringBuilder message = new StringBuilder();

        message.append("Order ID: ").append(order.getId())
                .append("\n\n");
        message.append("Order Date: ").append(order.getOrderDateTime())
                .append("\n\n");
        message.append("Order Status: ").append(order.getOrderStatus())
                .append("\n\n");
        message.append("Order Amount: ₹").append(order.getOrderAmount())
                .append("\n\n");
        message.append("Order Items:\n");

        for (OrderDetails details : order.getOrderDetails()) {
            message.append("\nProduct: ")
                    .append(details.getProduct().getName());
            message.append("\nQuantity: ")
                    .append(details.getQuantity());
            message.append("\nUnit Price: ₹")
                    .append(details.getUnitPrice());
            message.append("\nTotal Price: ₹")
                    .append(details.getTotalPrice());
            message.append("\n");
        }

        OrderAddress address = order.getOrderAddress();
        if (address != null) {
            message.append("\nDelivery Address:\n");
            message.append("Door No: ")
                    .append(address.getDoorNo())
                    .append("\n");
            message.append("Street: ")
                    .append(address.getStreetName())
                    .append("\n");
            message.append("City: ")
                    .append(address.getCity())
                    .append("\n");
            message.append("District: ")
                    .append(address.getDistrict())
                    .append("\n");
            message.append("Taluk: ")
                    .append(address.getTaluk())
                    .append("\n");
            message.append("State: ")
                    .append(address.getState())
                    .append("\n");
            message.append("PIN Code: ")
                    .append(address.getPinCode())
                    .append("\n");
            message.append("Landmark: ")
                    .append(address.getLandMark())
                    .append("\n");
        }
        if (order.getInstruction() != null && !order.getInstruction().isBlank()) {
            message.append("\nInstruction: ")
                    .append(order.getInstruction())
                    .append("\n");
        }

        return message.toString();
    }
}
