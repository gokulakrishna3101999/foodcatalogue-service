package com.krishna.foodcatalogue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_item")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_item_id")
    private Integer id;
    @Column(name = "food_item_name")
    private String name;
    @Column(name = "food_item_description")
    private String description;
    @Column(name = "is_food_item_veg")
    private boolean isVeg;
    @Column(name = "restaurant_id")
    private Integer restaurantId;
    @Column(name = "food_item_quantity", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer quantity;
    @Column(name = "food_item_price", nullable = false)
    private Integer price;
}
