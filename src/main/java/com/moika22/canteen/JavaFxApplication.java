package com.moika22.canteen;

import com.moika22.canteen.controller.RootPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder().sources(Main.class).run(args);
    }

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(RootPageController.class);
        stage.getIcons().add(new Image("static/img/icon.png"));
        stage.setTitle("Canteen");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
        this.applicationContext.close();
        Platform.exit();
    }
}
