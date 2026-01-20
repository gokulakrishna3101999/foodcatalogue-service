package com.krishna.foodcatalogue.service;

import com.krishna.foodcatalogue.dto.FoodCataloguePage;
import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.dto.RestaurantDTO;
import com.krishna.foodcatalogue.entity.FoodItem;
import com.krishna.foodcatalogue.mapper.FoodItemMapper;
import com.krishna.foodcatalogue.repository.FoodItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodItemService {
    private final FoodItemRepo foodItemRepo;
    private final FoodItemMapper foodItemMapper;
    private final RestTemplate restTemplate;

    public FoodItemDTO add(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = foodItemMapper.mapFoodItemDTOToFoodItem(foodItemDTO);
        return foodItemMapper.mapFoodItemtoFoodItemDTO(foodItemRepo.save(foodItem));
    }

    public FoodCataloguePage getFoodCatalogueByRestaurant(Integer restaurantId) {
        List<FoodItemDTO> foodItemDTOList = getFoodItemByRestaurantId(restaurantId);
        RestaurantDTO restaurantDTO = getRestaurantById(restaurantId);
        FoodCataloguePage catalogue = new FoodCataloguePage();
        catalogue.setFoodItemList(foodItemDTOList);
        catalogue.setRestaurant(restaurantDTO);
        return catalogue;
    }

    private RestaurantDTO getRestaurantById(Integer restaurantId) {
        return restTemplate.getForObject("http://restaurant/restaurant/get/"+restaurantId,RestaurantDTO.class);
    }

    private List<FoodItemDTO> getFoodItemByRestaurantId(Integer restaurantId) {
        List<FoodItem> foodItemList = foodItemRepo.findAllByRestaurantId(restaurantId);
        return foodItemList.stream().map(foodItemMapper::mapFoodItemtoFoodItemDTO).toList();
    }
}
