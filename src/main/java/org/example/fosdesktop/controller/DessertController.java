package org.example.fosdesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.DessertDto;
import org.example.fosdesktop.service.DessertService;

@RequiredArgsConstructor
public class DessertController {

    @FXML
    private void onCreate() {
        String name = this.dessertNameField.getText();
        String priceStr = this.dessertPriceField.getText();
        String portionWeightStr = this.dessertPortionWeightField.getText();

        if (name.isEmpty() || priceStr.isEmpty() || portionWeightStr.isEmpty()) {
            this.messageLabel.setText("Please fill all fields.");
            return;
        }
        float price = Float.parseFloat(priceStr);
        int portionWeight = Integer.parseInt(portionWeightStr);

        DessertDto dessertDto = DessertDto.builder()
            .name(name)
            .price(price)
            .portionWeight(portionWeight)
            .build();

        this.messageLabel.setText("Processing...");

        try {
            this.dessertService.create(dessertDto);
            this.messageLabel.setText("Created successfully!");
        } catch (Exception e) {
            this.messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private TextField dessertNameField, dessertPriceField, dessertPortionWeightField;

    @FXML
    private Label messageLabel;

    private final DessertService dessertService;
}
