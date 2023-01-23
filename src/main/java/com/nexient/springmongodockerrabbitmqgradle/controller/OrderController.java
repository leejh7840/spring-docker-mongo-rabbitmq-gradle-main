package com.nexient.springmongodockerrabbitmqgradle.controller;

import com.nexient.springmongodockerrabbitmqgradle.domain.Order;
import com.nexient.springmongodockerrabbitmqgradle.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * OrderController is a rest controller and handles calls
 * specific services depending on the end point.
 *
 * @author  Vraj Patel  vpatel@nexient.com
 * @author  Luke Clover lclover@nexient.com
 * @version 1.0
 * @since   02-28-2022
 *
 */
@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * getAllOrder() method will be called from -
     * OrderService when /all-order endpoint is accessed.
     *
     * @return Lists of all the orders stored from -
     * table named ordertable in MongoDB.
     */
    @GetMapping("/all-order")
    public List<Order> getAllOrder() {
        return orderService.getAllOrder();
    }

    /**
     * getAllOrderItemSorted() method will be called from -
     * OrderService when /all-order-item-sorted endpoint is accessed.
     *
     * @return Lists of all the orders sorted by the item names from -
     * table named ordertable in MongoDB.
     */
    @GetMapping("/all-order-item-sorted")
    public List<Order> getAllOrdertemSorted() {
        return orderService.getAllOrderItemSorted();
    }

    /**
     * @return 
     */
    @GetMapping("/all-order-qty-sorted")
    public List<Order> getAllOrderSorted() {
        return orderService.getAllOrderQtySorted();
    }

    @GetMapping("/all-order-price-sorted")
    public List<Order> getAllOrderPriceSorted() {
        return orderService.getAllOrderPriceSorted();
    }

    @PostMapping("/make-order/{restaurantName}")
    public String saveOrder(@RequestBody Order order, @PathVariable String restaurantName) {
        return orderService.saveOrder(order, restaurantName);
    }

    @PutMapping("update-order/{id}")
    public String updateOrder(@RequestBody Order order, @PathVariable String id) {
        return orderService.updateOrder(order, id);
    }

    @GetMapping("/get-order/{id}")
    public Optional<Order> getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    @DeleteMapping("/delete-order/{id}")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/get-order-by-item/{item}")
    public Optional<Order> getOrderByItem(@PathVariable String item) {
        return orderService.getOrderByItem(item);
    }

    @GetMapping("/get-order-by-qty-gt/{qty}")
    public List<Order> getOrderByQtyGreaterThan(@PathVariable String qty) {
        return orderService.getOrderByQtyGreaterThan(qty);
    }

    @GetMapping("/get-order-by-qty-lt/{qty}")
    public List<Order> getOrderByQtyLessThan(@PathVariable String qty) {
        return orderService.getOrderByQtyLessThan(qty);
    }

    @GetMapping("/get-order-by-qty-between/{qty1}/{qty2}")
    public List<Order> getOrdersByQtyBetween(@PathVariable String qty1, @PathVariable String qty2) {
        return orderService.getOrdersByQtyBetween(qty1, qty2);
    }

    @GetMapping("/get-order-by-price-gt/{price}")
    public List<Order> getOrderByPriceGreaterThan(@PathVariable String price) {
        return orderService.getOrderByPriceGreaterThan(price);
    }

    @GetMapping("/get-order-by-price-lt/{price}")
    public List<Order> getOrderByPriceLessThan(@PathVariable String price) {
        return orderService.getOrderByPriceLessThan(price);
    }

    @GetMapping("/get-order-by-price-between/{price1}/{price2}")
    public List<Order> getOrdersByPriceBetween(@PathVariable String price1, @PathVariable String price2) {
        return orderService.getOrdersByPriceBetween(price1, price2);
    }

    @DeleteMapping("/delete-by-price-gt/{price}")
    public void deleteByPriceGreaterThan(@PathVariable String price) {
        orderService.deleteByPriceGreaterThan(price);
    }

    @GetMapping("/find-order-dynamic")
    public List<Order> findOrderDynamic(@RequestParam String queryString) {
        return orderService.findOrderDynamic(queryString);
    }

    @GetMapping("/get-order")
    public List<Order> getOrderWithMultipleParams(@RequestParam(required = false) String item, @RequestParam(required = false) String qty,
                                                  @RequestParam(required = false) String price, @RequestParam(required = false) String itemOper,
                                                  @RequestParam(required = false) String qtyOper, @RequestParam(required = false) String priceOper) {
        return orderService.getOrderWithMultipleParams(item, qty, price, itemOper, qtyOper, priceOper);
    }
}
