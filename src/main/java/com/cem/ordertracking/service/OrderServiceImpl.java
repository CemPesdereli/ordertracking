package com.cem.ordertracking.service;

import com.cem.ordertracking.config.JwtService;
import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.entity.PurchaseRequest;
import com.cem.ordertracking.exception.InsufficientStockException;
import com.cem.ordertracking.repository.CustomerRepository;
import com.cem.ordertracking.repository.OrderInfoRepository;
import com.cem.ordertracking.repository.OrderItemRepository;
import com.cem.ordertracking.repository.ProductRepository;
import com.cem.ordertracking.security.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderInfoRepository orderInfoRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final CustomerService customerService;


    @Override
    @Transactional
    public OrderInfo saveOrderInfo(OrderInfo orderInfo) {
        System.out.println("OrderInfo saving");
        return orderInfoRepository.save(orderInfo);
    }

    @Override
    public List<OrderInfo> findAllOrderInfos() {
        return orderInfoRepository.findAll();
    }

    @Override
    public OrderInfo findOrderInfoById(Long id) {
        return orderInfoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteOrderInfo(Long id) {


        OrderInfo orderInfo = orderInfoRepository.findById(id).orElseThrow();
        List<OrderItem> orderItems = orderItemRepository.findByOrderInfo(orderInfo);

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }

        orderItemRepository.deleteAll(orderItems);

        orderInfoRepository.deleteById(id);
        System.out.println("OrderInfo deleted with Id: "+id);
    }

    @Override
    @Transactional
    public OrderItem saveOrderItem(OrderItem orderItem) {
        System.out.println("Saving OrderItem");
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> findAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {

        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow();
        Product product = orderItem.getProduct();
        product.setStockQuantity(orderItem.getQuantity()+ product.getStockQuantity());
        productRepository.save(product);


        orderItemRepository.deleteById(id);
        System.out.println("Deleted orderItem with id: "+id);
    }

    @Override
    public void createOrderFromCart(String token,List<PurchaseRequest> purchaseRequestList) {


        Customer customer= getCustomerFromToken(token);
        OrderInfo orderInfo = new OrderInfo();


        List<OrderItem> orderItems = new ArrayList<>();

        for(PurchaseRequest request: purchaseRequestList){

            Product product = productRepository.findById(request.getProductId()).orElseThrow();
            if(product.getStockQuantity() < request.getProductQuantity()){
                throw new InsufficientStockException("Product " + product.getName() + " has insufficient stock");
            }
            OrderItem orderItem = new OrderItem(orderInfo,product, request.getProductQuantity());
            orderItems.add(orderItem);
            product.setStockQuantity(product.getStockQuantity()- request.getProductQuantity());

        }
        orderInfo.setCustomer(customer);
        orderInfo.setOrderStatus("pending");
        orderInfo.setCreatedAt(LocalDateTime.now());
        orderInfo.setEstimatedDeliveryDate(LocalDate.now().plusDays(3));
        orderInfo.setOrderItems(orderItems);
        orderInfoRepository.save(orderInfo);

        System.out.println("OrderItem with related OrderInfo created");

    }

    @Override
    public List<OrderInfo> findMyAllOrderInfos(String token) {
        Customer customer= getCustomerFromToken(token);
        return orderInfoRepository.findByCustomer(customer);



    }

    @Override
    public OrderInfo findMyOrderInfo(String token, Long id) {
        Customer customer = getCustomerFromToken(token);
        OrderInfo orderInfo= orderInfoRepository.findById(id).orElseThrow();

        if(orderInfo.getCustomer()==customer){
            return orderInfo;
        }
        else{
            throw new RuntimeException("Id for Order Info not found");
        }

    }

    @Override
    public void deleteMyOrderInfo(String token, Long id) {
        Customer customer = getCustomerFromToken(token);
        OrderInfo orderInfo= orderInfoRepository.findById(id).orElseThrow();
        if(orderInfo.getCustomer()==customer){

            List<OrderItem> orderItems = orderItemRepository.findByOrderInfo(orderInfo);
            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
                productRepository.save(product);
            }

            orderItemRepository.deleteAll(orderItems);

            orderInfoRepository.deleteById(id);
            System.out.println("OrderInfo deleted with id: "+id);
        }
        else{
            throw new RuntimeException("Id to be deleted not found");
        }






    }

    @Override
    public List<List<OrderItem>> findMyAllOrderItems(String token) {
        Customer customer= getCustomerFromToken(token);
        List<OrderInfo> orderInfos = orderInfoRepository.findByCustomer(customer);
        List<List<OrderItem>> orderItemList = new ArrayList<>();
        for(OrderInfo orderInfo: orderInfos){
            orderItemList.add(orderInfo.getOrderItems());
        }


        return orderItemList;
    }

    @Override
    public OrderItem findMyOrderItem(String token, Long id) {
        Customer customer= getCustomerFromToken(token);

        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow();
        if(orderItem.getOrderInfo().getCustomer()==customer){
            return orderItem;
        }
        else{
            throw new RuntimeException("Id for OrderItem not found");
        }



    }

    @Override
    public OrderItem saveMyOrderItem(String token, OrderItem orderItem) {

        Customer customer= getCustomerFromToken(token);


        OrderInfo orderInfo = orderInfoRepository.findById(orderItem.getOrderInfo().getId()).orElseThrow();
        if(orderInfo.getCustomer()==customer){
            System.out.println("OrderItem saving");
           return orderItemRepository.save(orderItem);
        }
        else {
            throw new RuntimeException("You do not have such OrderInfo");
        }

    }

    @Override
    public OrderItem updateMyOrderItem(String token, OrderItem orderItem) {

        Customer customer= getCustomerFromToken(token);




        OrderInfo orderInfo = orderInfoRepository.findById(orderItem.getOrderInfo().getId()).orElseThrow();
        if(orderInfo.getCustomer()==customer){
            System.out.println("OrderItem updating");
            return orderItemRepository.save(orderItem);
        }
        else {
            throw new RuntimeException("You do not have such OrderInfo to update");
        }
    }

    @Override
    public void deleteMyOrderItem(String token, Long id) {



        Customer customer = getCustomerFromToken(token);
        OrderItem orderItem= orderItemRepository.findById(id).orElseThrow();
        if(orderItem.getOrderInfo().getCustomer()==customer){


            Product product = orderItem.getProduct();
            product.setStockQuantity(orderItem.getQuantity()+ product.getStockQuantity());
            productRepository.save(product);


            orderItemRepository.deleteById(id);
            System.out.println("OrderItem deleted with id: "+id);
        }
        else{
            throw new RuntimeException("Id to be deleted not found");
        }






    }

    private Customer getCustomerFromToken(String token){
        String jwt = token.substring(7);
        String username= jwtService.extractUsername(jwt);
        return customerService.findByUsername(username);
    }

}
