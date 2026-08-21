package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.enums.OrderStatus;
import com.projoker.joker_studio.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);

    @Query("""
SELECT o
FROM Order o
WHERE o.orderStatus = :status
AND o.user.id = :userId
""")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId,@Param("status") OrderStatus status);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
