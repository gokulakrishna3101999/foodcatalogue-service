package com.krishna.foodcatalogue.controller;

import com.krishna.foodcatalogue.dto.FoodCataloguePage;
import com.krishna.foodcatalogue.dto.FoodItemDTO;
import com.krishna.foodcatalogue.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodCatalogue")
@RequiredArgsConstructor
@CrossOrigin
public class FoodCatalogueController {
    private final FoodItemService foodItemService;

    @PostMapping("/addFoodItem")
    public ResponseEntity<FoodItemDTO> addFoodItem(@RequestBody FoodItemDTO foodItemDTO) {
        return new ResponseEntity<>(foodItemService.add(foodItemDTO), HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/get/{restaurantId}")
    public ResponseEntity<FoodCataloguePage> getFoodCatalogueByRestaurant(@PathVariable Integer restaurantId) {
        return new ResponseEntity<>(foodItemService.getFoodCatalogueByRestaurant(restaurantId), HttpStatus.OK);
    }
}
