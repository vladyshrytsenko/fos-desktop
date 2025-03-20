package org.example.fosdesktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.DessertDto;
import org.example.fosdesktop.model.dto.DrinkDto;
import org.example.fosdesktop.model.dto.MealDto;
import org.example.fosdesktop.model.dto.MenuItem;
import org.example.fosdesktop.model.dto.OrderDto;
import org.example.fosdesktop.model.dto.UserDto;
import org.example.fosdesktop.service.AuthService;
import org.example.fosdesktop.service.CuisineService;
import org.example.fosdesktop.service.DessertService;
import org.example.fosdesktop.service.DrinkService;
import org.example.fosdesktop.service.MealService;
import org.example.fosdesktop.service.OrderService;
import org.example.fosdesktop.service.PaymentService;
import org.example.fosdesktop.service.StorageService;
import org.example.fosdesktop.service.UserService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MenuController {

    private void loadMenu() {
        this.loadDrinks();
        this.loadMeals();
        this.loadDesserts();

        this.orderListVBox.setFillWidth(true);
        this.loadOrders();
    }

    private void loadDrinks() {
        drinkListVBox.getChildren().clear();

        Map<String, Object> drinkMap = this.drinkService.findAll(this.drinkPage, this.pageSize);
        List<DrinkDto> drinkDtoList = this.objectMapper.convertValue(drinkMap.get("content"), new TypeReference<>() {});

        int totalPages = (int) drinkMap.get("totalPages");
        boolean isFirst = (boolean) drinkMap.get("first");
        boolean isLast = (boolean) drinkMap.get("last");

        this.btnPrevDrinks.setDisable(isFirst);
        this.btnNextDrinks.setDisable(isLast);

        VBox drinkCard = this.createDrinkCard(drinkDtoList);
        this.drinkListVBox.getChildren().add(drinkCard);

        this.lblDrinksPage.setText((this.drinkPage + 1) + "/" + totalPages);
    }

    private void loadMeals() {
        this.mealListVBox.getChildren().clear();

        Map<String, Object> mealMap = this.mealService.findAll(this.mealPage, this.pageSize);
        List<MealDto> mealDtoList = this.objectMapper.convertValue(mealMap.get("content"), new TypeReference<>() {});

        int totalPages = (int) mealMap.get("totalPages");
        boolean isFirst = (boolean) mealMap.get("first");
        boolean isLast = (boolean) mealMap.get("last");

        this.btnPrevMeals.setDisable(isFirst);
        this.btnNextMeals.setDisable(isLast);

        VBox mealCard = this.createMealCard(mealDtoList);
        this.mealListVBox.getChildren().add(mealCard);

        this.lblMealsPage.setText((this.mealPage + 1) + "/" + totalPages);
    }

    private void loadDesserts() {
        this.dessertListVBox.getChildren().clear();

        Map<String, Object> dessertMap = this.dessertService.findAll(this.dessertPage, this.pageSize);
        List<DessertDto> dessertDtoList = this.objectMapper.convertValue(dessertMap.get("content"), new TypeReference<>() {});

        int totalPages = (int) dessertMap.get("totalPages");
        boolean isFirst = (boolean) dessertMap.get("first");
        boolean isLast = (boolean) dessertMap.get("last");

        this.btnPrevDesserts.setDisable(isFirst);
        this.btnNextDesserts.setDisable(isLast);

        VBox dessertCard = this.createDessertCard(dessertDtoList);
        this.dessertListVBox.getChildren().add(dessertCard);

        this.lblDessertsPage.setText((this.dessertPage + 1) + "/" + totalPages);
    }

    private void loadOrders() {
        this.orderListVBox.getChildren().clear();

        Map<String, Object> orderMap = this.orderService.findAll(this.orderPage, this.pageSize);
        List<OrderDto> orderDtoList = this.objectMapper.convertValue(orderMap.get("content"), new TypeReference<>() {});

        int totalPages = (int) orderMap.get("totalPages");
        boolean isFirst = (boolean) orderMap.get("first");
        boolean isLast = (boolean) orderMap.get("last");

        this.btnPrevOrders.setDisable(isFirst);
        this.btnNextOrders.setDisable(isLast);

        orderDtoList.forEach(order -> {
            VBox orderCard = this.createOrderCard(order);
            this.orderListVBox.getChildren().add(orderCard);
        });
        this.lblOrdersPage.setText((this.orderPage + 1) + "/" + totalPages);
    }

    private <T> void toggleSelection(Label label, T item, Set<T> selectedSet) {
        if (selectedSet.contains(item)) {
            selectedSet.remove(item);
            String labelText = label.getText().substring(2);
            label.setText(labelText);
            label.setStyle("");
            this.totalPrice -= ((MenuItem) item).getPrice();
        } else {
            selectedSet.add(item);
            label.setStyle("-fx-background-color: lightblue;");
            label.setText("✅ " + label.getText());
            this.totalPrice += ((MenuItem) item).getPrice();
        }
        switch (item) {
            case DrinkDto ignored -> this.btnDeleteSelectedDrinks.setDisable(selectedSet.isEmpty());
            case DessertDto ignored -> this.btnDeleteSelectedDesserts.setDisable(selectedSet.isEmpty());
            default -> this.btnDeleteSelectedMeals.setDisable(selectedSet.isEmpty());
        }
        this.updateTotalPrice();
    }

    private void updateTotalPrice() {
        int totalPriceInCents = (int) this.totalPrice * 100;
        this.storageService.setTotalPrice(totalPriceInCents);
        this.totalPriceLabel.setText("Total Price: $" + new DecimalFormat("#.00").format(this.totalPrice));
    }

    private VBox createOrderCard(OrderDto order) {
        VBox orderCard = new VBox();
        orderCard.setSpacing(5);
        orderCard.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 5;");

        Label orderTitle = new Label("Order #" + order.getId());
        orderTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label createdAt = new Label("Created At: " + order.getCreatedAt());

        Set<String> combinedNames = Stream.of(
                order.getMeals().stream().map(MealDto::getName),
                order.getDrinks().stream().map(DrinkDto::getName),
                order.getDesserts().stream().map(DessertDto::getName)
            )
            .flatMap(Function.identity())
            .collect(Collectors.toSet());
        Label orderDescription = new Label(combinedNames.toString());

        Label totalPrice = new Label("Total Price: $" + order.getTotalPrice());
        totalPrice.setStyle("-fx-font-weight: bold;");

        Region spacer1 = new Region();
        spacer1.setMinHeight(10);

        Region spacer2 = new Region();
        spacer2.setMinHeight(10);

        orderCard.getChildren().addAll(orderTitle, createdAt, spacer1, orderDescription, spacer2, totalPrice);
        return orderCard;
    }

    private VBox createDessertCard(List<DessertDto> desserts) {
        VBox dessertCard = new VBox();
        dessertCard.setSpacing(5);
        dessertCard.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 5;");

        desserts.forEach(dessert -> {
            Label dessertLabel = new Label(dessert.getName() + " - $" + new DecimalFormat("#.00").format(dessert.getPrice()));
            dessertLabel.setOnMouseClicked(event -> this.toggleSelection(dessertLabel, dessert, this.order.getDesserts()));
            dessertCard.getChildren().add(dessertLabel);
        });

        return dessertCard;
    }

    private VBox createDrinkCard(List<DrinkDto> drinks) {
        VBox drinkCard = new VBox();
        drinkCard.setSpacing(5);
        drinkCard.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 5;");

        drinks.forEach(drink -> {
            Label drinkLabel = new Label(drink.getName() + " - $" + new DecimalFormat("#.00").format(drink.getPrice()));
            drinkLabel.setOnMouseClicked(event -> this.toggleSelection(drinkLabel, drink, this.order.getDrinks()));
            drinkCard.getChildren().add(drinkLabel);
        });

        return drinkCard;
    }

    private VBox createMealCard(List<MealDto> meals) {
        VBox mealCard = new VBox();
        mealCard.setSpacing(5);
        mealCard.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 5;");

        meals.forEach(meal -> {
            Label mealLabel = new Label(meal.getName() + " - $" + new DecimalFormat("#.00").format(meal.getPrice()));
            mealLabel.setOnMouseClicked(event -> this.toggleSelection(mealLabel, meal, this.order.getMeals()));
            mealCard.getChildren().add(mealLabel);
        });

        return mealCard;
    }

    private void loadPaymentModal() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/payment.fxml"));
                loader.setControllerFactory(param -> new PaymentController(
                    this.paymentService,
                    this.storageService
                ));
                Parent root = loader.load();
                Scene paymentScene = new Scene(root);

                Stage paymentStage = new Stage();
                paymentStage.setTitle("Confirm payment:");
                paymentStage.setResizable(false);
                paymentStage.setScene(paymentScene);
                paymentStage.initModality(Modality.APPLICATION_MODAL);
                paymentStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCreateDrinkModal(MenuController menuController) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/drink-create.fxml"));
                loader.setControllerFactory(param -> new DrinkController(this.drinkService));
                Parent root = loader.load();
                Scene drinkScene = new Scene(root);

                Stage drinkStage = new Stage();
                drinkStage.setTitle("Create a new drink:");
                drinkStage.setResizable(false);
                drinkStage.setScene(drinkScene);
                drinkStage.initModality(Modality.APPLICATION_MODAL);
                drinkStage.setOnHidden(event -> menuController.loadDrinks());
                drinkStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCreateDessertModal(MenuController menuController) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/dessert-create.fxml"));
                loader.setControllerFactory(param -> new DessertController(this.dessertService));
                Parent root = loader.load();
                Scene dessertScene = new Scene(root);

                Stage dessertStage = new Stage();
                dessertStage.setTitle("Create a new dessert:");
                dessertStage.setResizable(false);
                dessertStage.setScene(dessertScene);
                dessertStage.initModality(Modality.APPLICATION_MODAL);
                dessertStage.setOnHidden(event -> menuController.loadDesserts());
                dessertStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCreateMealModal(MenuController menuController) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/meal-create.fxml"));
                loader.setControllerFactory(param -> new MealController(
                    this.mealService,
                    this.cuisineService,
                    this.objectMapper
                ));
                Parent root = loader.load();
                Scene mealScene = new Scene(root);

                Stage mealStage = new Stage();
                mealStage.setTitle("Create a new meal:");
                mealStage.setResizable(false);
                mealStage.setScene(mealScene);
                mealStage.initModality(Modality.APPLICATION_MODAL);
                mealStage.setOnHidden(event -> menuController.loadMeals());
                mealStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCreateCuisineModal() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/cuisine-create.fxml"));
                loader.setControllerFactory(param -> new CuisineController(this.cuisineService));
                Parent root = loader.load();
                Scene cuisineScene = new Scene(root);

                Stage cuisineStage = new Stage();
                cuisineStage.setTitle("Create a new cuisine:");
                cuisineStage.setResizable(false);
                cuisineStage.setScene(cuisineScene);
                cuisineStage.initModality(Modality.APPLICATION_MODAL);
                cuisineStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void showMenu() {
        this.menuContentVBox.setVisible(true);
        this.historyContentVBox.setVisible(false);
    }

    @FXML
    private void showHistory() {
        this.menuContentVBox.setVisible(false);
        this.historyContentVBox.setVisible(true);
    }

    @FXML
    private VBox
        drinkListVBox, mealListVBox, dessertListVBox,
        orderListVBox, menuContentVBox, historyContentVBox;

    @FXML
    private Label
        totalPriceLabel, currentEmail, lblDrinksPage,
        lblMealsPage, lblDessertsPage, lblOrdersPage;

    @FXML
    private Button
        btnPrevDrinks, btnNextDrinks, btnPrevMeals,
        btnNextMeals, btnPrevDesserts, btnNextDesserts,
        btnPrevOrders, btnNextOrders, btnCreateDrink,
        btnCreateDessert, btnCreateMeal, btnCreateCuisine,
        btnDeleteSelectedDrinks, btnDeleteSelectedDesserts, btnDeleteSelectedMeals,
        btnLogout;

    @FXML
    public void initialize() {
        UserDto currentUser = this.userService.getCurrentUser();
        boolean isAdmin = "ADMIN".equals(currentUser.getRole());

        List<Button> adminButtons = List.of(
            this.btnCreateDrink,
            this.btnCreateDessert,
            this.btnCreateMeal,
            this.btnCreateCuisine,
            this.btnDeleteSelectedDrinks,
            this.btnDeleteSelectedDesserts,
            this.btnDeleteSelectedMeals
        );

        adminButtons
            .forEach(button -> button.setVisible(isAdmin));

        String welcomeText = String.format("Welcome, %s (%s)", currentUser.getEmail(), currentUser.getRole());
        this.currentEmail.setText(welcomeText);

        this.loadMenu();
    }

    @FXML
    private void onLogout() {
        this.authService.logout();
    }

    @FXML
    private void onSubmitOrder() {
        OrderDto created = this.orderService.create(this.order);
        this.loadPaymentModal();
    }

    @FXML
    private void onCreateDrink() {
        this.loadCreateDrinkModal(this);
    }

    @FXML
    private void onDeleteSelectedDrinks() {
        List<Long> ids = new ArrayList<>();
        this.order.getDrinks()
            .forEach(drink -> ids.add(drink.getId()));

        if (!ids.isEmpty()) {
            this.drinkService.deleteAllById(ids);
            this.loadDrinks();
        }
    }

    @FXML
    private void onDeleteSelectedDesserts() {
        List<Long> ids = new ArrayList<>();
        this.order.getDesserts()
            .forEach(dessert -> ids.add(dessert.getId()));

        if (!ids.isEmpty()) {
            this.dessertService.deleteAllById(ids);
            this.loadDesserts();
        }
    }

    @FXML
    private void onDeleteSelectedMeals() {
        List<Long> ids = new ArrayList<>();
        this.order.getMeals()
            .forEach(meal -> ids.add(meal.getId()));

        if (!ids.isEmpty()) {
            this.mealService.deleteAllById(ids);
            this.loadMeals();
        }
    }

    @FXML
    private void onCreateDessert() {
        this.loadCreateDessertModal(this);
    }

    @FXML
    private void onCreateMeal() {
        this.loadCreateMealModal(this);
    }

    @FXML
    private void onCreateCuisine() {
        this.loadCreateCuisineModal();
    }

    @FXML
    private void nextPageDrinks() {
        this.drinkPage++;
        this.loadDrinks();
    }

    @FXML
    private void prevPageDrinks() {
        if (this.drinkPage > 0) {
            this.drinkPage--;
            this.loadDrinks();
        }
    }

    @FXML
    private void nextPageMeals() {
        this.mealPage++;
        this.loadMeals();
    }

    @FXML
    private void prevPageMeals() {
        if (this.mealPage > 0) {
            this.mealPage--;
            this.loadMeals();
        }
    }

    @FXML
    private void nextPageDesserts() {
        this.dessertPage++;
        this.loadDesserts();
    }

    @FXML
    private void prevPageDesserts() {
        if (this.dessertPage > 0) {
            this.dessertPage--;
            this.loadDesserts();
        }
    }

    @FXML
    private void nextPageOrders() {
        this.orderPage++;
        this.loadOrders();
    }

    @FXML
    private void prevPageOrders() {
        if (this.orderPage > 0) {
            this.orderPage--;
            this.loadOrders();
        }
    }

    private int drinkPage = 0, mealPage = 0, dessertPage = 0, orderPage = 0;
    private final int pageSize = 5;

    private OrderDto order = new OrderDto();
    private double totalPrice = 0d;

    private final DrinkService drinkService;
    private final MealService mealService;
    private final DessertService dessertService;
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;
    private final CuisineService cuisineService;
    private final PaymentService paymentService;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
}
