package com.cem.ordertracking;

import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.exception.ResourceNotFoundException;
import com.cem.ordertracking.repository.OrderInfoRepository;
import com.cem.ordertracking.repository.OrderItemRepository;
import com.cem.ordertracking.security.Customer;
import com.cem.ordertracking.security.Role;
import com.cem.ordertracking.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderInfoRepository orderInfoRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService; // The service class to be tested


    @Test
    public void testFindOrderItemById_success() {
        LocalDate today = LocalDate.now();
        BigDecimal productPrice = new BigDecimal("27.50");
        Product product = new Product("Mouse", "Gaming Mouse", "Electronic",productPrice,100);
        Customer customer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);
        OrderInfo orderInfo = new OrderInfo(customer,"pending",today);

        OrderItem mockOrderItem = new OrderItem(orderInfo,product,5);

        mockOrderItem.setId(1L);
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(mockOrderItem));

        OrderItem foundOrderItem = orderService.findOrderItemById(1L);

        assertNotNull(foundOrderItem);
        assertEquals(5, foundOrderItem.getQuantity());

    }

    @Test
    public void testFindOrderItemById_failure(){
        Long orderItemId = 456L;  // Non-existent product ID

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findOrderItemById(orderItemId);
        });


    }

    @Test
    public void testFindOrderInfoById_success() {
        LocalDate today = LocalDate.now();
        Customer customer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);
        OrderInfo orderInfo = new OrderInfo(customer,"pending",today);
        orderInfo.setId(1L);



        when(orderInfoRepository.findById(1L)).thenReturn(Optional.of(orderInfo));

        OrderInfo foundOrderInfo = orderService.findOrderInfoById(1L);

        assertNotNull(foundOrderInfo);
        assertEquals("pending", foundOrderInfo.getOrderStatus());

    }

    @Test
    public void testFindOrderInfoById_failure(){
        Long orderInfoId = 456L;  // Non-existent product ID

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findOrderInfoById(orderInfoId);
        });


    }





}
