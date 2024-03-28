package com.cem.ordertracking.service;

import com.cem.ordertracking.config.JwtService;
import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.entity.PurchaseRequest;
import com.cem.ordertracking.exception.InsufficientStockException;
import com.cem.ordertracking.exception.ResourceNotFoundException;
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
        try {
            return orderInfoRepository.save(orderInfo);
        }
        catch (Exception e) {
            // Handle other unexpected exceptions (e.g., log error)
            throw new RuntimeException("Failed to save OrderInfo: " + e.getMessage());
        }
    }

    @Override
    public List<OrderInfo> findAllOrderInfos() {
        return orderInfoRepository.findAll();
    }

    @Override
    public OrderInfo findOrderInfoById(Long id) {
        return orderInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteOrderInfo(Long id) {


        OrderInfo orderInfo = orderInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found with Id: "+id));
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



        long id = orderItem.getId();
        OrderItem dbOrderItem = orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with Id: "+id));
        Product dbProduct = productRepository.findById(dbOrderItem.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        long productId = orderItem.getProduct().getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with Id: "+productId));




        dbProduct.setStockQuantity(dbProduct.getStockQuantity()+ dbOrderItem.getQuantity());
        if(product.getStockQuantity() < orderItem.getQuantity()){
            throw new InsufficientStockException("Product " + product.getName() + " has insufficient stock");
        }
        productRepository.save(dbProduct);



        product.setStockQuantity(product.getStockQuantity()-orderItem.getQuantity());
        productRepository.save(product);


        System.out.println("Updating OrderItem");
         return orderItemRepository.save(orderItem);


    }

    @Override
    public List<OrderItem> findAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {

        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with Id: "+id));
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

            Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
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
        OrderInfo orderInfo= orderInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found with ID: " + id));

        if(orderInfo.getCustomer()==customer){
            return orderInfo;
        }
        else{
            throw new ResourceNotFoundException("You are not authorized to access this OrderInfo");
        }

    }

    @Override
    public void deleteMyOrderInfo(String token, Long id) {
        Customer customer = getCustomerFromToken(token);
        OrderInfo orderInfo= orderInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found with Id: "+id));
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
            throw new ResourceNotFoundException("Id to be deleted not found");
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

        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + id));
        if(orderItem.getOrderInfo().getCustomer()==customer){
            return orderItem;
        }
        else{
            throw new RuntimeException("You are not authorized to access this OrderItem");
        }



    }

    @Override
    public OrderItem saveMyOrderItem(String token, OrderItem orderItem) {

        Customer customer= getCustomerFromToken(token);


        OrderInfo orderInfo = orderInfoRepository.findById(orderItem.getOrderInfo().getId()).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found"));
        if(orderInfo.getCustomer()==customer){
            System.out.println("OrderItem saving");
           return orderItemRepository.save(orderItem);
        }
        else {
            throw new RuntimeException("You do not have such OrderInfo");
        }

    }

    @Override
    @Transactional
    public OrderItem updateMyOrderItem(String token, OrderItem orderItem) {

        Customer customer= getCustomerFromToken(token);

        OrderItem dbOrderItem = orderItemRepository.findById(orderItem.getId()).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));
        OrderInfo orderInfo = orderInfoRepository.findById(orderItem.getOrderInfo().getId()).orElseThrow(() -> new ResourceNotFoundException("OrderInfo not found"));



        if(orderInfo.getCustomer()!= customer  || dbOrderItem.getOrderInfo().getCustomer() !=customer    ){

            throw new RuntimeException("You do not have such OrderItem to update or this OrderInfo is not yours.");

        }
        else {


            Product dbProduct = productRepository.findById(dbOrderItem.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            long productId = orderItem.getProduct().getId();
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));




            dbProduct.setStockQuantity(dbProduct.getStockQuantity()+ dbOrderItem.getQuantity());
            if(product.getStockQuantity() < orderItem.getQuantity()){
                throw new InsufficientStockException("Product " + product.getName() + " has insufficient stock");
            }
            productRepository.save(dbProduct);



            product.setStockQuantity(product.getStockQuantity()-orderItem.getQuantity());
            productRepository.save(product);


            System.out.println("Updating OrderItem");
            return orderItemRepository.save(orderItem);






        }
    }

    @Override
    public void deleteMyOrderItem(String token, Long id) {



        Customer customer = getCustomerFromToken(token);
        OrderItem orderItem= orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: "+id));
        if(orderItem.getOrderInfo().getCustomer()==customer){


            Product product = orderItem.getProduct();
            product.setStockQuantity(orderItem.getQuantity()+ product.getStockQuantity());
            productRepository.save(product);


            orderItemRepository.deleteById(id);
            System.out.println("OrderItem deleted with id: "+id);
        }
        else{
            throw new ResourceNotFoundException("Id to be deleted not found");
        }






    }

    private Customer getCustomerFromToken(String token){
        String jwt = token.substring(7);
        String username= jwtService.extractUsername(jwt);
        return customerService.findByUsername(username);
    }

}
