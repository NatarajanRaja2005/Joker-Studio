package com.projoker.joker_studio.service.order_details;

import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.model.Order;
import com.projoker.joker_studio.model.OrderDetails;
import com.projoker.joker_studio.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetails createOrderDetails(Order order, CartItem item) {
        OrderDetails details = new OrderDetails();
        details.setOrder(order);
        details.setProduct(item.getProduct());
        details.setQuantity(item.getQuantity());
        details.setUnitPrice(item.getUnitPrice());
        details.setTotalPrice(item.getTotalPrice());
        return orderDetailRepository.save(details);
    }

    //Done it for future
    @Override
    public OrderDetails changeOrderDetails(Long orderId, Long cartItemId) {
        return null;
    }
}
