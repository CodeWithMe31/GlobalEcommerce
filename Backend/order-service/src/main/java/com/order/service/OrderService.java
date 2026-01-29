package com.order.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.order.dto.*;
import com.order.model.*;
import com.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final InvoiceService invoiceService;
    private final RestTemplate rest = new RestTemplate();

    private final String CART_URL = "http://localhost:8083/cart";
    private final String REFUND_URL = "http://localhost:8085/payment/refund/";
    private final String EMAIL_URL = "http://localhost:8086/notify";

    public OrderService(OrderRepository repo, InvoiceService invoiceService) {
        this.repo = repo;
        this.invoiceService = invoiceService;
    }

    public Order place(String user, PlaceOrderRequest req) {
        // Set up headers to identify the user for the Cart Service
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER", user);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fetch cart items for this specific user
        CartItemDTO[] cart = rest.exchange(CART_URL, HttpMethod.GET, entity, CartItemDTO[].class).getBody();
        
        if (cart == null || cart.length == 0) {
            throw new RuntimeException("Cart empty");
        }

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (CartItemDTO c : cart) {
            OrderItem i = new OrderItem();
            i.setProductId(c.productId);
            i.setProductName(c.productName);
            i.setPrice(c.price);
            i.setQuantity(c.quantity);
            total += c.price * c.quantity;
            items.add(i);
        }

        Order order = new Order(); // Renamed to avoid conflict
        order.setUserEmail(user);
        order.setStatus("PLACED");
        order.setShippingAddress(req.shippingAddress);
        order.setPaymentIntentId(req.paymentIntentId);
        order.setItems(items);
        order.setTotalAmount(total);

        Order savedOrder = repo.save(order);

        // Notify User
        try {
            String notifyUrl = EMAIL_URL + "?email=" + user + "&subject=Order Confirmed&message=Your order #" + savedOrder.getId() + " is placed.";
            rest.postForObject(notifyUrl, null, Void.class);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }
        
        // Clear the user's cart after successful placement
        rest.exchange(CART_URL, HttpMethod.DELETE, entity, Void.class);
        
        return savedOrder;
    }

    public List<Order> history(String user) {
        return repo.findByUserEmail(user); //
    }

    public void cancel(Long id, String user) {
        Order o = repo.findById(id).orElseThrow();
        if (!o.getUserEmail().equals(user)) throw new RuntimeException("Forbidden");
        
        rest.postForObject(REFUND_URL + o.getPaymentIntentId(), null, Void.class);
        o.setStatus("REFUNDED");
        repo.save(o);
    }

    public byte[] invoice(Long id, String user) throws Exception {
        Order o = repo.findById(id).orElseThrow();
        if (!o.getUserEmail().equals(user)) throw new RuntimeException("Forbidden");
        return invoiceService.generate(o); //
    }
}
