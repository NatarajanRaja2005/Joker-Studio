package com.projoker.joker_studio.service.order_details;

import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.model.Order;
import com.projoker.joker_studio.model.OrderDetails;

public interface IOrderDetailService {
    OrderDetails createOrderDetails(Order order, CartItem cartItem);
    // can change only quantity of an particular order
    OrderDetails changeOrderDetails(Long orderId,Long cartItemId);
}
