package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Order;
import com.projoker.joker_studio.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails,Long> {
}
