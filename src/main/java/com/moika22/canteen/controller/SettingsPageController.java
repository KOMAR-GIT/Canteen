package com.moika22.canteen.controller;

import com.moika22.canteen.service.SettingsService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

@Component
@FxmlView("settings_page.fxml")
public class SettingsPageController implements Initializable {

    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField reportPathTextField;
    @FXML
    private TextField logsPathTextField;
    @FXML
    private TextField oldEventsTimeTextField;
    private final SettingsService settingsService;

    public SettingsPageController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @FXML
    public void ScheduleSaveButtonClicked() {
        LocalTime startTime = settingsService.getParsedTime(startTimeTextField);
        LocalTime endTime = settingsService.getParsedTime(endTimeTextField);
        if ((startTime != null && endTime != null) && endTime.isAfter(startTime)) {
            settingsService.save("time.start", startTime.toString());
            settingsService.save("time.stop", endTime.toString());
        }
    }

    @FXML
    public void oldEventsSaveButtonClicked() {
        settingsService.saveOldEventsTime(oldEventsTimeTextField);
    }

    @FXML
    public void saveReportsPathButtonClicked() {
        settingsService.save("reports.path", reportPathTextField.getText());
    }

    @FXML
    public void saveLogsPathButtonClicked() {
        settingsService.save("logging.path", logsPathTextField.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeTextField.setText(settingsService.getStartTime());
        endTimeTextField.setText(settingsService.getEndTime());
        reportPathTextField.setText(settingsService.getReportsPath());
        oldEventsTimeTextField.setText(settingsService.getOldEventsTime());
        logsPathTextField.setText(settingsService.getLogsPath());
    }
}
