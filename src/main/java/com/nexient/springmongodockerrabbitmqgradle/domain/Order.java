package com.nexient.springmongodockerrabbitmqgradle.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 *
 * @author  Vraj Patel vpatel@nexient.com
 * @version 1.0
 * @since   02-28-2022
 *
 */
@Builder
@Data
@Document("ordertable")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;
    private String item;
    private String qty;
    private String price;
    
}

