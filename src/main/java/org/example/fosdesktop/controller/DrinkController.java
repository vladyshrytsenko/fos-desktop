package org.example.fosdesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.DrinkDto;
import org.example.fosdesktop.service.DrinkService;

@RequiredArgsConstructor
public class DrinkController {

    @FXML
    private void onCreate() {
        String name = this.drinkNameField.getText();
        String priceStr = this.drinkPriceField.getText();

        if (name.isEmpty() || priceStr.isEmpty()) {
            this.messageLabel.setText("Please fill all fields.");
            return;
        }
        float price = Float.parseFloat(priceStr);

        DrinkDto drinkDto = DrinkDto.builder()
            .name(name)
            .price(price)
            .build();

        this.messageLabel.setText("Processing...");

        try {
            this.drinkService.create(drinkDto);
            this.messageLabel.setText("Created successfully!");
        } catch (Exception e) {
            this.messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private TextField drinkNameField, drinkPriceField;

    @FXML
    private Label messageLabel;

    private final DrinkService drinkService;
}
