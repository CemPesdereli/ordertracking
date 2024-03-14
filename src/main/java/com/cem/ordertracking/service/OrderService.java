package com.cem.ordertracking.service;

import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.PurchaseRequest;

import java.util.List;

public interface OrderService {

    OrderInfo saveOrderInfo(OrderInfo orderInfo);

    List<OrderInfo> findAllOrderInfos();

    OrderInfo findOrderInfoById(Long id);

    void deleteOrderInfo(Long id);

    OrderItem saveOrderItem(OrderItem orderItem);

    List<OrderItem> findAllOrderItems();

    OrderItem findOrderItemById(Long id);

    void deleteOrderItem(Long id);

    void createOrderFromCart(String token,List<PurchaseRequest> purchaseRequestList);

    List<OrderInfo> findMyAllOrderInfos(String token);

    OrderInfo findMyOrderInfo(String token, Long id);

    void deleteMyOrderInfo(String token, Long id);

    List<List<OrderItem>> findMyAllOrderItems(String token);

    OrderItem findMyOrderItem(String token, Long id);

    OrderItem saveMyOrderItem(String token, OrderItem orderItem);
    OrderItem updateMyOrderItem(String token, OrderItem orderItem);

    void deleteMyOrderItem(String token, Long id);


}
