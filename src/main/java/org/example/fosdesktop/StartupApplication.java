package org.example.fosdesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication(proxyBeanMethods = false)
public class StartupApplication extends Application {

    @Getter
    private static Stage primaryStage;

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        springContext = SpringApplication.run(StartupApplication.class, args);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("FOS Desktop: Login");
        stage.setResizable(false);
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/login.fxml"));
        loader.setControllerFactory(param -> springContext.getBean(param));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
    }

}
