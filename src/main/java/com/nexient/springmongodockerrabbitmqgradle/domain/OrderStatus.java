package com.nexient.springmongodockerrabbitmqgradle.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author  Vraj Patel vpatel@nexient.com
 * @version 1.0
 * @since   02-28-2022
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus {

    private Order order;
    private String status;
    private String message;

}

