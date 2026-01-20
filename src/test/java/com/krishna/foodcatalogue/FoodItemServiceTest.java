package com.krishna.foodcatalogue;

import com.krishna.foodcatalogue.dto.FoodCataloguePage;
import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.dto.RestaurantDTO;
import com.krishna.foodcatalogue.entity.FoodItem;
import com.krishna.foodcatalogue.mapper.FoodItemMapper;
import com.krishna.foodcatalogue.repository.FoodItemRepo;
import com.krishna.foodcatalogue.service.FoodItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceTest {

    @Mock
    private FoodItemRepo foodItemRepo;

    @Mock
    private FoodItemMapper foodItemMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FoodItemService foodItemService;

    @Test
    void add_shouldMapAndPersistFoodItem_andReturnSavedDTO() {
        // Arrange
        FoodItemDTO inputDto = new FoodItemDTO(null, "Pizza", "Cheese burst", true, 10, 1, 199);
        FoodItem entityBeforeSave = new FoodItem(null, "Pizza", "Cheese burst", true, 10, 1, 199);
        FoodItem entityAfterSave = new FoodItem(101, "Pizza", "Cheese burst", true, 10, 1, 199);
        FoodItemDTO expectedDto = new FoodItemDTO(101, "Pizza", "Cheese burst", true, 10, 1, 199);

        when(foodItemMapper.mapFoodItemDTOToFoodItem(inputDto)).thenReturn(entityBeforeSave);
        when(foodItemRepo.save(entityBeforeSave)).thenReturn(entityAfterSave);
        when(foodItemMapper.mapFoodItemtoFoodItemDTO(entityAfterSave)).thenReturn(expectedDto);

        // Act
        FoodItemDTO actual = foodItemService.add(inputDto);

        // Assert
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(foodItemMapper).mapFoodItemDTOToFoodItem(inputDto);
        verify(foodItemRepo).save(entityBeforeSave);
        verify(foodItemMapper).mapFoodItemtoFoodItemDTO(entityAfterSave);
        verifyNoMoreInteractions(foodItemRepo, foodItemMapper, restTemplate);
    }

    @Test
    void getFoodCatalogueByRestaurant_shouldReturnRestaurantAndMappedFoodItems() {
        // Arrange
        Integer restaurantId = 42;
        List<FoodItem> items = List.of(
                new FoodItem(1, "Burger", "Tasty", false, restaurantId, 5, 99),
                new FoodItem(2, "Salad", "Fresh", true, restaurantId, 3, 79)
        );
        FoodItemDTO dto1 = new FoodItemDTO(1, "Burger", "Tasty", false, restaurantId, 5, 99);
        FoodItemDTO dto2 = new FoodItemDTO(2, "Salad", "Fresh", true, restaurantId, 3, 79);
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantId, "Hub", "Addr", "City", "Desc");

        when(foodItemRepo.findAllByRestaurantId(restaurantId)).thenReturn(items);
        when(foodItemMapper.mapFoodItemtoFoodItemDTO(items.get(0))).thenReturn(dto1);
        when(foodItemMapper.mapFoodItemtoFoodItemDTO(items.get(1))).thenReturn(dto2);
        when(restTemplate.getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class))
                .thenReturn(restaurantDTO);

        // Act
        FoodCataloguePage page = foodItemService.getFoodCatalogueByRestaurant(restaurantId);

        // Assert
        assertNotNull(page);
        assertNotNull(page.getFoodItemList());
        assertEquals(2, page.getFoodItemList().size());
        assertTrue(page.getFoodItemList().containsAll(List.of(dto1, dto2)));
        assertEquals(restaurantDTO, page.getRestaurant());
        verify(foodItemRepo).findAllByRestaurantId(restaurantId);
        verify(foodItemMapper, times(2)).mapFoodItemtoFoodItemDTO(any(FoodItem.class));
        verify(restTemplate).getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class);
        verifyNoMoreInteractions(foodItemRepo, foodItemMapper, restTemplate);
    }

    @Test
    void getFoodCatalogueByRestaurant_shouldHandleEmptyFoodItemList() {
        // Arrange
        Integer restaurantId = 7;
        when(foodItemRepo.findAllByRestaurantId(restaurantId)).thenReturn(List.of());
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantId, "Name", "Addr", "City", "Desc");
        when(restTemplate.getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class))
                .thenReturn(restaurantDTO);

        // Act
        FoodCataloguePage page = foodItemService.getFoodCatalogueByRestaurant(restaurantId);

        // Assert
        assertNotNull(page);
        assertNotNull(page.getFoodItemList());
        assertTrue(page.getFoodItemList().isEmpty());
        assertEquals(restaurantDTO, page.getRestaurant());
        verify(foodItemRepo).findAllByRestaurantId(restaurantId);
        verify(restTemplate).getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class);
        verifyNoMoreInteractions(foodItemRepo, foodItemMapper, restTemplate);
    }

    @Test
    void getFoodCatalogueByRestaurant_shouldPropagateRestaurantServiceFailure() {
        // Arrange
        Integer restaurantId = 5;
        when(foodItemRepo.findAllByRestaurantId(restaurantId)).thenReturn(List.of());
        when(restTemplate.getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class))
                .thenThrow(new RuntimeException("Service down"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> foodItemService.getFoodCatalogueByRestaurant(restaurantId));
        assertEquals("Service down", ex.getMessage());
        verify(foodItemRepo).findAllByRestaurantId(restaurantId);
        verify(restTemplate).getForObject("http://restaurant/restaurant/get/" + restaurantId, RestaurantDTO.class);
        verifyNoMoreInteractions(foodItemRepo, foodItemMapper, restTemplate);
    }

    @Test
    void getFoodCatalogueByRestaurant_shouldCallRestaurantServiceWithCorrectUrl() {
        // Arrange
        Integer restaurantId = 88;
        when(foodItemRepo.findAllByRestaurantId(restaurantId)).thenReturn(List.of());
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantId, "R", "A", "C", "D");
        when(restTemplate.getForObject(anyString(), eq(RestaurantDTO.class))).thenReturn(restaurantDTO);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        foodItemService.getFoodCatalogueByRestaurant(restaurantId);

        // Assert
        verify(restTemplate).getForObject(urlCaptor.capture(), eq(RestaurantDTO.class));
        String calledUrl = urlCaptor.getValue();
        assertEquals("http://restaurant/restaurant/get/" + restaurantId, calledUrl);
        verify(foodItemRepo).findAllByRestaurantId(restaurantId);
        verifyNoMoreInteractions(foodItemRepo, foodItemMapper, restTemplate);
    }
}
