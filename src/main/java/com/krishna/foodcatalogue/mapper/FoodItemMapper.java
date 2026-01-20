package com.krishna.foodcatalogue.mapper;

import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.entity.FoodItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodItemMapper {
    FoodItem mapFoodItemDTOToFoodItem(FoodItemDTO foodItemDTO);
    FoodItemDTO mapFoodItemtoFoodItemDTO(FoodItem foodItem);
}
