package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Order;
import com.projoker.joker_studio.model.OrderAddress;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/cart/place/{cartId}")
    public ResponseEntity<ApiResponse> placeOrderByCartid(@PathVariable Long cartId){
        try {
            Order order=orderService.orderProductsInCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Order placed Successfully!",order));
        }
        catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart not exists",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Placing order is failed!",e.getMessage()));
        }
    }

    @PostMapping("/place/")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId,
                                                  @RequestParam Long productId,
                                                  @RequestParam int quantity){
        try {
            Order order=orderService.orderProducts(userId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Order placed Successfully!",order));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Placing order is failed!",e.getMessage()));
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId){
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order cancelled Successful.",orderId));
        }
        catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not exists",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Order cancel is failed!",e.getMessage()));
        }
    }

    @PutMapping("/update/address/{orderId}")
    public ResponseEntity<ApiResponse> updateOrderAddress(@PathVariable Long orderId,@RequestBody OrderAddress address){
        try {
            Order order=orderService.updateOrderAddress(orderId,address);
            return ResponseEntity.ok(new ApiResponse("Order Address Updated Successfully.",null));
        }
        catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not exists",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Address updation is failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/all/{userId}")
    public ResponseEntity<ApiResponse> getAllOrders(@PathVariable Long userId){
        try {
            List<Order> orders=orderService.getAllOrderByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("All orders are retrived Successfully.",orders));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrival of orders is failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/pending/{userId}")
    public ResponseEntity<ApiResponse> getPendingOrders(@PathVariable Long userId){
        try {
            List<Order> orders=orderService.getPendingOrders(userId);
            return ResponseEntity.ok(new ApiResponse("All orders are retrived Successfully.",orders));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrival of orders is failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/completed/{userId}")
    public ResponseEntity<ApiResponse> getCompletedOrders(@PathVariable Long userId){
        try {
            List<Order> orders=orderService.getCompletedOrders(userId);
            return ResponseEntity.ok(new ApiResponse("All orders are retrived Successfully.",orders));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrival of orders is failed!",e.getMessage()));
        }
    }

    //Ensure that this should only accessed by admin
    @PutMapping("/update/status/")
    public ResponseEntity<ApiResponse> updateOrderStatus(@RequestParam Long orderId,@RequestParam String status){
        try {
            orderService.updateOrderStatus(orderId,status);
            return ResponseEntity.ok(new ApiResponse("Order Id: "+orderId+" status is Updated Successfully.",null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Order Status Updation is failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
        try {
            Order order=orderService.getOrderById(orderId);
            return ResponseEntity.ok(new ApiResponse("Order retrived succesfully.",order));
        }
        catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not exists",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Order cancel is failed!",e.getMessage()));
        }
    }
}
