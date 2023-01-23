package com.nexient.springmongodockerrabbitmqgradle.service;

import com.nexient.springmongodockerrabbitmqgradle.domain.Order;
import com.nexient.springmongodockerrabbitmqgradle.domain.OrderStatus;
import com.nexient.springmongodockerrabbitmqgradle.repository.OrderRepository;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.AllArgsConstructor;
import net.jazdw.rql.parser.ASTNode;
import net.jazdw.rql.parser.RQLParser;
import net.jazdw.rql.parser.SimpleASTVisitor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OrderService handles all the services related to Order MongoDB repository.
 *
 * @author  Vraj Patel  vpatel@nexient.com
 * @author  Luke Clover lclover@nexient.com
 * @version 1.0
 * @since   02-28-2022
 *
 */
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private final MongoTemplate mongoTemplate;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public String saveOrder(Order order, String restaurantName) {

        OrderStatus orderStatus = new OrderStatus(order, "PROCESS",
                "Order successfully placed in " + restaurantName);
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);

        orderRepository.save(order);

        return "SUCCESS!";
    }

    public String updateOrder(Order order, String id) {
        orderRepository.save(order);
        return "SUCCESS!";
    }

    public Optional<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    public void deleteByPriceGreaterThan(String price) {
        orderRepository.deleteByPriceGreaterThan(Double.valueOf(price));
    }

    public Optional<Order> getOrderByItem(String item) {
        return orderRepository.findByItem(item);
    }

    public List<Order> getOrderByQtyGreaterThan(String qty) {
        return orderRepository.findByQtyGreaterThan(qty);
    }

    public List<Order> getOrderByQtyLessThan(String qty) {
        return orderRepository.findByQtyLessThan(qty);
    }

    public List<Order> getOrderByPriceGreaterThan(String price) {
        return orderRepository.findByPriceGreaterThan(price);
    }

    public List<Order> getOrderByPriceLessThan(String price) {
        return orderRepository.findByPriceLessThan(price);
    }

    public List<Order> getOrdersByPriceBetween(String price1, String price2) {
        return orderRepository.getOrdersByPriceBetween(price1, price2);
    }

    public List<Order> getOrdersByQtyBetween(String qty1, String qty2) {
        return orderRepository.getOrdersByQtyBetween(qty1, qty2);
    }

    public List<Order> getAllOrderPriceSorted() {
        return orderRepository.findAll(Sort.by(Direction.ASC, "price"));
    }

    public List<Order> getAllOrderItemSorted() {
        return orderRepository.findAll(Sort.by(Direction.ASC, "item"));
    }

    public List<Order> getAllOrderQtySorted() {
        return orderRepository.findAll(Sort.by(Direction.ASC, "qty"));
    }

    public List<Order> getOrderWithMultipleParams(String item, String qty, String price,
                                                  String itemOper, String qtyOper, String priceOper) {
        Query query = new Query();
        Map<String,List<String>> paramMap = new HashMap<>();

        if ((item != null && !"".equalsIgnoreCase(item)) && (itemOper != null && !"".equalsIgnoreCase(itemOper))) {
            paramMap.put("item", Arrays.asList(item,itemOper));
        }
        if ((qty != null && !"".equalsIgnoreCase(qty)) && (qtyOper != null && !"".equalsIgnoreCase(qtyOper))) {
            paramMap.put("qty", Arrays.asList(qty,qtyOper));
        }
        if ((price != null && !"".equalsIgnoreCase(price)) && (priceOper != null && !"".equalsIgnoreCase(priceOper))) {
            paramMap.put("price", Arrays.asList(price,priceOper));
        }

        Set<Map.Entry<String,List<String>>>paramEntries = paramMap.entrySet();
        List<Criteria> criteriaList = paramEntries.stream().map(e -> buildCriteria(e.getKey(), e.getValue())).collect(Collectors.toList());
        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));

        return mongoTemplate.find(query, Order.class);
    }

    public Criteria buildCriteria(String key, List<String>valAndOper) {

        if (key == null || "".equalsIgnoreCase(key)) {
            return null;
        }
        if (valAndOper == null) {
            return null;
        }
        if (valAndOper.size() != 2) {
            return null;
        }
        String val = valAndOper.get(0);
        String operator = valAndOper.get(1);
        if (val == null || "".equalsIgnoreCase(val)) {
            return null;
        }
        if (operator == null || "".equalsIgnoreCase(operator)) {
            return null;
        }

        Criteria criteria = new Criteria();

        switch (operator) {
            case "eq":
                criteria = Criteria.where(key).is(val);
                break;
            case "lt":
                criteria = Criteria.where(key).lt(val);
            case "lte":
                criteria = Criteria.where(key).lte(val);
                break;
            case "gt":
                criteria = Criteria.where(key).gt(val);
                break;
            case "gte":
                criteria = Criteria.where(key).gte(val);
                break;
        }

        return criteria;
    }

    public List<Order> findOrderDynamic(String queryString) {
        if (queryString == null || "".equalsIgnoreCase(queryString)) {
            return null;
        }

        RQLParser parser = new RQLParser();
        ASTNode node = parser.parse(queryString);

        Criteria criteria = buildDynamicCriteria(node);
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Order.class);
    }

    /**
     *
     * @param node
     * @return Criteria
     *
     * Accepts an ASTNode object and parses through recursively
     * to build Mongo Criteria
     */
    public Criteria buildDynamicCriteria(ASTNode node) {

        if (node == null) {
            return null;
        }
        List<Criteria> childrenCriteria = new ArrayList<>();
        List<Object> arguments = node.getArguments();

        // If Node is logical operator, traverse every child node
        // If comparator node, build criteria from terminal child nodes
        if ("and".equalsIgnoreCase(node.getName()) || "or".equalsIgnoreCase(node.getName())) {

            for (Object argumentObject : arguments) {
                ASTNode argumentNode = (ASTNode) argumentObject;
                childrenCriteria.add(buildDynamicCriteria(argumentNode));
            }

            if ("and".equalsIgnoreCase(node.getName())) {
                return new Criteria().andOperator(childrenCriteria.toArray(new Criteria[childrenCriteria.size()]));
            }
            if ("or".equalsIgnoreCase(node.getName())) {
                return new Criteria().orOperator(childrenCriteria.toArray(new Criteria[childrenCriteria.size()]));
            }
        } else {
            String key = arguments.get(0).toString();
            String value = arguments.get(1).toString();
            String operator = node.getName();

            return buildCriteria(key, Arrays.asList(value,operator));
        }

        return null;
    }

}
