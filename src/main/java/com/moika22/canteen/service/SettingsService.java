package com.moika22.canteen.service;

import com.moika22.canteen.ApplicationProperties;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class SettingsService {

    private final ApplicationProperties applicationProperties;

    public SettingsService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void saveOldEventsTime(TextField textField) {
        LocalTime time = getParsedTime(textField);
        if (time != null) {
            save("time.old-events", time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

    public LocalTime getParsedTime(TextField textField) {
        LocalTime localTime = null;
        textField.getStyleClass().clear();
        try {
            localTime = LocalTime.parse(textField.getText());
            textField.getStyleClass().add("settings-text-field");
        } catch (DateTimeParseException exception) {
            textField.getStyleClass().add("settings-text-field-wrong-data");
        }
        return localTime;
    }

    public String getStartTime() {
        return applicationProperties.getProperty("time.start");
    }

    public String getEndTime() {
        return applicationProperties.getProperty("time.stop");
    }

    public String getReportsPath() {
        return applicationProperties.getProperty("reports.path");
    }

    public String getOldEventsTime() {
        return applicationProperties.getProperty("time.old-events");
    }

    public String getLogsPath() {
        return applicationProperties.getProperty("logging.path");
    }

    public void save(String propertyName, String value) {
        applicationProperties.setProperty(propertyName, value);
        applicationProperties.save();
    }


}
