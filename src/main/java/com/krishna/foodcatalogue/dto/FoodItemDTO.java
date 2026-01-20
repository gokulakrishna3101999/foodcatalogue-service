package com.krishna.foodcatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {
    private Integer id;
    private String name;
    private String description;
    private boolean isVeg;
    private Integer restaurantId;
    private Integer quantity;
    private Integer price;
}
