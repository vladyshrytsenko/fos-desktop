package org.example.fosdesktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.fosdesktop.model.dto.DessertDto;
import org.example.fosdesktop.model.dto.DrinkDto;
import org.example.fosdesktop.model.dto.MealDto;
import org.example.fosdesktop.model.dto.OrderDto;
import org.example.fosdesktop.model.dto.PopularDishDto;
import org.example.fosdesktop.model.dto.UserDto;
import org.example.fosdesktop.service.AuthService;
import org.example.fosdesktop.service.DessertService;
import org.example.fosdesktop.service.DrinkService;
import org.example.fosdesktop.service.MealService;
import org.example.fosdesktop.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuController {

    private final DrinkService drinkService;
    private final MealService mealService;
    private final DessertService dessertService;
    private final UserService userService;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public MenuController(
        DrinkService drinkService,
        MealService mealService,
        DessertService dessertService,
        UserService userService,
        AuthService authService,
        ObjectMapper objectMapper) {

        this.drinkService = drinkService;
        this.mealService = mealService;
        this.dessertService = dessertService;
        this.userService = userService;
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @FXML
    private VBox drinkListVBox, mealListVBox, dessertListVBox, popularDishesListVBox;

    @FXML
    private Label
        totalPriceLabel,
        currentEmail,
        lblDrinksPage,
        lblMealsPage,
        lblDessertsPage;

    @FXML
    private Button
        btnPrevDrinks,
        btnNextDrinks,
        btnPrevMeals,
        btnNextMeals,
        btnPrevDesserts,
        btnNextDesserts,
        btnLogout;

    private int drinkPage = 0, mealPage = 0, dessertPage = 0;
    private final int pageSize = 5;

    private Set<DrinkDto> selectedDrinks = new HashSet<>();
    private Set<MealDto> selectedMeals = new HashSet<>();
    private Set<DessertDto> selectedDesserts = new HashSet<>();
    private Set<PopularDishDto> popularDishes = new HashSet<>();

    private OrderDto order = new OrderDto();

    @FXML
    public void initialize() {
        UserDto currentUser = this.userService.getCurrentUser();
        currentEmail.setText("Welcome, " + currentUser.getEmail());
        loadMenu();
        updateTotalPrice();
    }

    @FXML
    private void onLogout() {
        this.authService.logout();
    }

    private void loadMenu() {
        loadDrinks();
        loadMeals();
        loadDesserts();
    }

    private void loadDrinks() {
        drinkListVBox.getChildren().clear();
        Map<String, Object> drinkMap = drinkService.findAll(drinkPage, pageSize);
        List<DrinkDto> drinkDtoList = objectMapper.convertValue(drinkMap.get("content"), new TypeReference<>() {});

        drinkDtoList.forEach(drink -> {
            Label drinkLabel = new Label(drink.getName() + " - $" + drink.getPrice());
            drinkLabel.setOnMouseClicked(event -> toggleSelection(drinkLabel, drink, selectedDrinks));
            drinkListVBox.getChildren().add(drinkLabel);
        });
        lblDrinksPage.setText("Page " + (drinkPage + 1));
    }

    private void loadMeals() {
        mealListVBox.getChildren().clear();
        Map<String, Object> mealMap = mealService.findAll(mealPage, pageSize);
        List<MealDto> mealDtoList = objectMapper.convertValue(mealMap.get("content"), new TypeReference<>() {});

        mealDtoList.forEach(meal -> {
            Label mealLabel = new Label(meal.getName() + " - $" + meal.getPrice());
            mealLabel.setOnMouseClicked(event -> toggleSelection(mealLabel, meal, selectedMeals));
            mealListVBox.getChildren().add(mealLabel);
        });
        lblMealsPage.setText("Page " + (mealPage + 1));
    }

    private void loadDesserts() {
        dessertListVBox.getChildren().clear();
        Map<String, Object> dessertMap = dessertService.findAll(dessertPage, pageSize);
        List<DessertDto> dessertDtoList = objectMapper.convertValue(dessertMap.get("content"), new TypeReference<>() {});

        dessertDtoList.forEach(dessert -> {
            Label dessertLabel = new Label(dessert.getName() + " - $" + dessert.getPrice());
            dessertLabel.setOnMouseClicked(event -> toggleSelection(dessertLabel, dessert, selectedDesserts));
            dessertListVBox.getChildren().add(dessertLabel);
        });
        lblDessertsPage.setText("Page " + (dessertPage + 1));
    }

    private <T> void toggleSelection(Label label, T item, Set<T> selectedSet) {
        if (selectedSet.contains(item)) {
            selectedSet.remove(item);
            label.setStyle("");
        } else {
            selectedSet.add(item);
            label.setStyle("-fx-background-color: lightblue;");
        }
        updateTotalPrice();
    }

    @FXML
    private void nextPageDrinks() {
        drinkPage++;
        loadDrinks();
    }

    @FXML
    private void prevPageDrinks() {
        if (drinkPage > 0) {
            drinkPage--;
            loadDrinks();
        }
    }

    @FXML
    private void nextPageMeals() {
        mealPage++;
        loadMeals();
    }

    @FXML
    private void prevPageMeals() {
        if (mealPage > 0) {
            mealPage--;
            loadMeals();
        }
    }

    @FXML
    private void nextPageDesserts() {
        dessertPage++;
        loadDesserts();
    }

    @FXML
    private void prevPageDesserts() {
        if (dessertPage > 0) {
            dessertPage--;
            loadDesserts();
        }
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText("Total Price: $" + order.getTotalPrice());
    }

    @FXML
    private void submitOrder() {
        //TODO
    }
}
