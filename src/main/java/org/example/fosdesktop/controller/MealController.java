package org.example.fosdesktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.CuisineDto;
import org.example.fosdesktop.model.dto.MealDto;
import org.example.fosdesktop.service.CuisineService;
import org.example.fosdesktop.service.MealService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MealController {

    @FXML
    public void initialize() {
        this.loadCuisines();
    }

    @FXML
    private void onCreate() {
        String name = this.mealNameField.getText();
        String priceStr = this.mealPriceField.getText();
        String portionWeightStr = this.mealPortionWeightField.getText();
        String cuisineName = this.cuisineNamesComboBox.getValue();

        if (name.isEmpty() || priceStr.isEmpty() || portionWeightStr.isEmpty() || cuisineName.isEmpty()) {
            this.messageLabel.setText("Please fill all fields.");
            return;
        }
        float price = Float.parseFloat(priceStr);
        int portionWeight = Integer.parseInt(portionWeightStr);

        MealDto mealDto = MealDto.builder()
            .name(name)
            .price(price)
            .portionWeight(portionWeight)
            .cuisineName(cuisineName)
            .build();

        this.messageLabel.setText("Processing...");

        try {
            this.mealService.create(mealDto);
            this.messageLabel.setText("Created successfully!");
        } catch (Exception e) {
            this.messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void loadCuisines() {
        Map<String, Object> cuisineMap = this.cuisineService.findAll();
        List<CuisineDto> cuisineDtoList = this.objectMapper.convertValue(cuisineMap.get("content"), new TypeReference<>() {});

        this.cuisineNamesComboBox.setItems(
            FXCollections.observableList(cuisineDtoList.stream()
                .map(CuisineDto::getName)
                .collect(Collectors.toList()))
        );
    }

    @FXML
    private TextField
        mealNameField, mealPriceField, mealPortionWeightField;

    @FXML
    private Label messageLabel;

    @FXML
    private ComboBox<String> cuisineNamesComboBox;

    private final MealService mealService;
    private final CuisineService cuisineService;
    private final ObjectMapper objectMapper;
}
