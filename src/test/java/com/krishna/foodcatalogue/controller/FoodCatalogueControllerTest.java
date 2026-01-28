package com.krishna.foodcatalogue.controller;

import com.krishna.foodcatalogue.dto.FoodCataloguePage;
import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.service.FoodItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FoodCatalogueControllerTest {
    @Mock
    private FoodItemService foodCatalogueService;

    @InjectMocks
    private FoodCatalogueController foodCatalogueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFoodItem_ShouldReturnCreatedStatus() {
        // Arrange
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        when(foodCatalogueService.add(any(FoodItemDTO.class))).thenReturn(foodItemDTO);

        // Act
        ResponseEntity<FoodItemDTO> response = foodCatalogueController.addFoodItem(foodItemDTO);

        // Assert
        verify(foodCatalogueService, times(1)).add(foodItemDTO);
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals(foodItemDTO, response.getBody());
    }

    @Test
    void fetchRestauDetailsWithFoodMenu_ShouldReturnOkStatus() {
        // Arrange
        int restaurantId = 123;
        FoodCataloguePage foodCataloguePage = new FoodCataloguePage();
        when(foodCatalogueService.getFoodCatalogueByRestaurant(restaurantId)).thenReturn(foodCataloguePage);

        // Act
        ResponseEntity<FoodCataloguePage> response = foodCatalogueController.getFoodCatalogueByRestaurant(restaurantId);

        // Assert
        verify(foodCatalogueService, times(1)).getFoodCatalogueByRestaurant(restaurantId);
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals(foodCataloguePage, response.getBody());
    }
}
