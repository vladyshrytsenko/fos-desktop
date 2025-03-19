package org.example.fosdesktop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class OrderDto {

    private Long id;
    private Double totalPrice;
    private Set<DessertDto> desserts = new HashSet<>();
    private String dessertId;
    private Set<String> dessertNames = new HashSet<>();
    private Set<MealDto> meals = new HashSet<>();
    private String mealId;
    private Set<String> mealNames = new HashSet<>();
    private Set<DrinkDto> drinks = new HashSet<>();
    private String drinkId;
    private Set<String> drinkNames = new HashSet<>();
    private Boolean iceCubes;
    private Boolean lemon;
    private PaymentDto payment;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

}
