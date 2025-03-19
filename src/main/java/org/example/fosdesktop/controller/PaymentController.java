package org.example.fosdesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.service.PaymentService;
import org.example.fosdesktop.service.StorageService;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class PaymentController {

    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> digitsOnlyFilter = change ->
            (change.getText().matches("\\d*")) ? change : null;

        this.expMonthField.setTextFormatter(new TextFormatter<>(digitsOnlyFilter));
        this.expYearField.setTextFormatter(new TextFormatter<>(digitsOnlyFilter));
        this.cvcField.setTextFormatter(new TextFormatter<>(digitsOnlyFilter));

        this.cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digits = newValue.replaceAll("\\D", "");
            StringBuilder formatted = new StringBuilder();

            IntStream.range(0, digits.length()).forEach(i -> {
                if (i > 0 && i % 4 == 0) {
                    formatted.append("-");
                }
                formatted.append(digits.charAt(i));
            });

            this.cardNumberField.setText(formatted.toString());
            this.cardNumberField.positionCaret(formatted.length());
        });
    }

    @FXML
    private void onSubmitPayment() {
        String cardNumber = this.cardNumberField.getText().replace("-", "");
        String expMonth = this.expMonthField.getText();
        String expYear = this.expYearField.getText();
        String cvc = this.cvcField.getText();

        if (cardNumber.isEmpty() || expMonth.isEmpty() || expYear.isEmpty() || cvc.isEmpty()) {
            this.messageLabel.setText("Please fill all fields.");
            return;
        }

        this.messageLabel.setText("Processing...");

        try {
            int totalPrice = this.storageService.getTotalPrice();
            boolean success = this.paymentService.fetchPaymentIntent(totalPrice);
            this.messageLabel.setText(success ? "Payment succeeded!" : "Payment failed.");
        } catch (Exception e) {
            this.messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private TextField
        cardNumberField,
        expMonthField,
        expYearField,
        cvcField;

    @FXML
    private Label messageLabel;

    private final PaymentService paymentService;
    private final StorageService storageService;
}
