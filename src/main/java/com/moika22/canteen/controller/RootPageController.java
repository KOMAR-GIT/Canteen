package com.moika22.canteen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("root.fxml")
public class RootPageController implements Initializable {

    @FXML
    private BorderPane bp;

    private Parent mainPage;
    private Parent settingsPage;
    private Parent consolePage;

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

    @FXML
    private void consoleButtonClicked(ActionEvent event) {
        bp.setCenter(consolePage);
    }

    private Parent getPage(String pageName) {
        return switch (pageName) {
            default -> fxWeaver.loadView(MainPageController.class);
            case "settings_page" -> fxWeaver.loadView(SettingsPageController.class);
            case "console_reader" -> fxWeaver.loadView(ConsoleReaderController.class);
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPage = getPage("main_page");
        settingsPage = getPage("settings_page");
        consolePage = getPage("console_reader");
        bp.setCenter(mainPage);
    }
}
