package com.krishna.foodcatalogue.service;

import com.krishna.foodcatalogue.dto.FoodCataloguePage;
import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.dto.RestaurantDTO;
import com.krishna.foodcatalogue.entity.FoodItem;
import com.krishna.foodcatalogue.mapper.FoodItemMapper;
import com.krishna.foodcatalogue.repository.FoodItemRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FoodCatalogueServiceTest {
    @Mock
    private FoodItemRepo foodItemRepo;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FoodItemMapper foodItemMapper;

    @InjectMocks
    private FoodItemService foodCatalogueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFoodItem_ShouldSaveFoodItemAndReturnMappedDTO() {
        // Arrange
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        FoodItem foodItem = new FoodItem();
        FoodItemDTO mappedDTO = new FoodItemDTO();
        
        when(foodItemMapper.mapFoodItemDTOToFoodItem(foodItemDTO)).thenReturn(foodItem);
        when(foodItemRepo.save(any(FoodItem.class))).thenReturn(foodItem);
        when(foodItemMapper.mapFoodItemtoFoodItemDTO(foodItem)).thenReturn(mappedDTO);

        // Act
        FoodItemDTO result = foodCatalogueService.add(foodItemDTO);

        // Assert
        verify(foodItemRepo, times(1)).save(foodItem);
        verify(foodItemMapper, times(1)).mapFoodItemDTOToFoodItem(foodItemDTO);
        verify(foodItemMapper, times(1)).mapFoodItemtoFoodItemDTO(foodItem);
        Assertions.assertEquals(mappedDTO, result);
    }

    @Test
    void fetchFoodCataloguePageDetails_ShouldReturnFoodCataloguePage() {
        // Arrange
        int restaurantId = 123;
        FoodItem foodItem = new FoodItem();
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        List<FoodItem> foodItemList = List.of(foodItem);
        List<FoodItemDTO> foodItemDTOList = List.of(foodItemDTO);
        RestaurantDTO restaurant = new RestaurantDTO();
        
        when(foodItemRepo.findAllByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(foodItemMapper.mapFoodItemtoFoodItemDTO(foodItem)).thenReturn(foodItemDTO);
        when(restTemplate.getForObject(anyString(), eq(RestaurantDTO.class))).thenReturn(restaurant);

        // Act
        FoodCataloguePage result = foodCatalogueService.getFoodCatalogueByRestaurant(restaurantId);

        // Assert
        verify(foodItemRepo, times(1)).findAllByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(RestaurantDTO.class));
        Assertions.assertEquals(foodItemDTOList, result.getFoodItemList());
        Assertions.assertEquals(restaurant, result.getRestaurant());
    }
}
