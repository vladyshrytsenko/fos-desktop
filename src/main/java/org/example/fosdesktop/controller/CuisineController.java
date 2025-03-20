package org.example.fosdesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.CuisineDto;
import org.example.fosdesktop.service.CuisineService;

@RequiredArgsConstructor
public class CuisineController {

    @FXML
    private void onCreate() {
        String name = this.cuisineNameField.getText();

        if (name.isEmpty()) {
            this.messageLabel.setText("Please fill all fields.");
            return;
        }

        CuisineDto cuisineDto = CuisineDto.builder()
            .name(name)
            .build();

        this.messageLabel.setText("Processing...");

        try {
            this.cuisineService.create(cuisineDto);
            this.messageLabel.setText("Created successfully!");
        } catch (Exception e) {
            this.messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private TextField cuisineNameField;

    @FXML
    private Label messageLabel;

    private final CuisineService cuisineService;
}
