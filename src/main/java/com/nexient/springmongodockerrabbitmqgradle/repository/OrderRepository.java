package com.nexient.springmongodockerrabbitmqgradle.repository;

import com.nexient.springmongodockerrabbitmqgradle.domain.Order;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 * @author  Vraj Patel  vpatel@nexient.com
 * @author  Luke Clover lclover@nexient.com
 * @version 1.0
 * @since   02-28-2022
 *
 */
@Repository
public interface OrderRepository extends MongoRepository<Order,String> {

    List<Order> findAll();

    @Query(value="{code:'?0'}")
    List<Order> findAll(String code);

    @DeleteQuery(value = "{price:{$gte:?0}}")
    void deleteByPriceGreaterThan(Double price);

    @Query(value = "{item:'?0'}")
    Optional<Order> findByItem(String item);

    @Query(value = "{qty:{$gte:?0}}")
    List<Order> findByQtyGreaterThan(String qty);

    @Query(value = "{qty:{$lte:?0}}")
    List<Order> findByQtyLessThan(String qty);

    @Query(value = "{price:{$gte:?0}}")
    List<Order> findByPriceGreaterThan(String price);

    @Query(value = "{price:{$lte:?0}}")
    List<Order> findByPriceLessThan(String price);

    @Query(value = "{'price': {$gte:?0, $lte:?1}}")
    List<Order> getOrdersByPriceBetween(String price1, String price2);

    @Query(value = "{'qty': {$gte:?0, $lte:?1}}")
    List<Order> getOrdersByQtyBetween(String qty1, String qty2);
}

