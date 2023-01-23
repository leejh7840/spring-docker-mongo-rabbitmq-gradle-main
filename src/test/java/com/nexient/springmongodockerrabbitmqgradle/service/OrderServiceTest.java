package com.nexient.springmongodockerrabbitmqgradle.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    private String key = "testKey";
    private String val = "testVal";
    private String operator = "eq";

    @Test
    void getOrderWithMultipleParamsTest() {
        Criteria criteria = Criteria.where(key).is(val);
        Criteria testCriteria = orderService.buildCriteria(key, Arrays.asList(val,operator));
        assertEquals(criteria, testCriteria);
    }

    @Test
    void getOrderWithMultipleParamsTest_badValues() {
        Criteria emptyKeyCriteria = orderService.buildCriteria("",Arrays.asList(val,operator));
        Criteria nullKeyCriteria = orderService.buildCriteria(null,Arrays.asList(val,operator));
        Criteria nullListCriteria = orderService.buildCriteria(key, null);
        Criteria badListCriteria = orderService.buildCriteria(key, Arrays.asList("testVal"));

        assertEquals(null, emptyKeyCriteria);
        assertEquals(null, nullKeyCriteria);
        assertEquals(null, nullListCriteria);
        assertEquals(null, badListCriteria);
    }


}