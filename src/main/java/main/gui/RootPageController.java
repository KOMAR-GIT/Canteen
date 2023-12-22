package main.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import main.services.MainService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@FxmlView("root.fxml")
public class RootPageController implements Initializable {

    @FXML
    private BorderPane bp;

    private Parent mainPage;
    private Parent settingsPage;

    private final FxWeaver fxWeaver;

    public RootPageController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private void mainButtonClicked(ActionEvent event) {
        bp.setCenter(mainPage);
    }

    @FXML
    private void settingsButtonClicked(ActionEvent event) {
        bp.setCenter(settingsPage);
    }

    private Parent getPage(String pageName) {
        switch (pageName) {
            case "main_page":
            default:
                return fxWeaver.loadView(MainPageController.class);
            case "settings_page":
                return fxWeaver.loadView(SettingsPageController.class);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPage = getPage("main_page");
        settingsPage = getPage("settings_page");
        bp.setCenter(mainPage);
    }
}
