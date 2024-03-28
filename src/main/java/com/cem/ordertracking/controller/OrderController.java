package com.cem.ordertracking.controller;

import com.cem.ordertracking.auth.AuthenticationResponse;
import com.cem.ordertracking.config.JwtService;
import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.PurchaseRequest;
import com.cem.ordertracking.security.Customer;
import com.cem.ordertracking.service.CustomerService;
import com.cem.ordertracking.service.OrderService;
import com.cem.ordertracking.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;
    private final CustomerService customerService;


    @GetMapping("/orderInfos")
    public List<OrderInfo> findAllOrderInfos() {


        return orderService.findAllOrderInfos();
    }

    @GetMapping("/orderInfos/my")
    public List<OrderInfo> findMyAllOrderInfos(@RequestHeader("Authorization") String token) {
        return orderService.findMyAllOrderInfos(token);

    }


    @GetMapping("/orderInfos/{orderInfoId}")
    public OrderInfo findOrderInfo(@PathVariable long orderInfoId) {


        return orderService.findOrderInfoById(orderInfoId);
    }

    @GetMapping("/orderInfos/my/{orderInfoId}")
    public OrderInfo findMyOrderInfo(@RequestHeader("Authorization") String token, @PathVariable long orderInfoId) {

        return orderService.findMyOrderInfo(token,orderInfoId);

    }


    @PostMapping("/orderInfos")
    public void addOrderInfo(@RequestBody OrderInfo orderInfo) {

        orderInfo.setId(0L);
        orderService.saveOrderInfo(orderInfo);

    }


    @PutMapping("/orderInfos")
    public void updateOrderInfo(@RequestBody OrderInfo orderInfo) {
        orderService.saveOrderInfo(orderInfo);
    }


    @DeleteMapping("/orderInfos/{orderInfoId}")
    public void deleteOrderInfo(@PathVariable long orderInfoId) {

        orderService.deleteOrderInfo(orderInfoId);

    }


    @DeleteMapping("/orderInfos/my/{orderInfoId}")
    public void deleteMyOrderInfo(@RequestHeader("Authorization") String token, @PathVariable long orderInfoId) {

         orderService.deleteMyOrderInfo(token,orderInfoId);

    }

    @GetMapping("/orderItems")
    public List<OrderItem> findAllOrderItems() {
        return orderService.findAllOrderItems();
    }

    @GetMapping("/orderItems/my")
    public List<List<OrderItem>> findMyAllOrderItems(@RequestHeader("Authorization") String token) {

        return orderService.findMyAllOrderItems(token);

    }

    @GetMapping("/orderItems/{orderItemId}")
    public OrderItem findOrderItem(@PathVariable long orderItemId) {

        return orderService.findOrderItemById(orderItemId);
    }

    @GetMapping("/orderItems/my/{orderItemId}")
    public OrderItem findMyOrderItem(@RequestHeader("Authorization") String token,@PathVariable long orderItemId) {

        return orderService.findMyOrderItem(token,orderItemId);

    }



    @PostMapping("/orderItems")
    public void addOrderItem(@RequestBody OrderItem orderItem) {

        orderItem.setId(0L);
        orderService.saveOrderItem(orderItem);

    }

    @PostMapping("/orderItems/my")
    public void addMyOrderItem(@RequestHeader("Authorization") String token,@RequestBody OrderItem orderItem) {

        orderItem.setId(0L);
        orderService.saveMyOrderItem(token,orderItem);

    }

    @PutMapping("/orderItems/my")
    public void updateMyOrderItem(@RequestHeader("Authorization") String token,@RequestBody OrderItem orderItem) {


        orderService.updateMyOrderItem(token,orderItem);

    }




    @PutMapping("/orderItems")
    public void updateOrderItem(@RequestBody OrderItem orderItem) {
        orderService.saveOrderItem(orderItem);
    }

    @DeleteMapping("/orderItems/{orderItemId}")
    public void deleteOrderItem(@PathVariable long orderItemId) {



        orderService.deleteOrderItem(orderItemId);

    }

    @DeleteMapping("/orderItems/my/{orderItemId}")
    public void deleteMyOrderItem(@RequestHeader("Authorization") String token, @PathVariable long orderItemId) {

        orderService.deleteMyOrderItem(token,orderItemId);

    }

    @PostMapping("/cart")
    public void createOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody List<PurchaseRequest> request
    ){

        orderService.createOrderFromCart(token,request);

    }


}
