package com.moika22.canteen.controller;

import com.moika22.canteen.api.LastEmployee;
import com.moika22.canteen.service.MainService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

@Component
@FxmlView("main_page.fxml")
@Slf4j
public class MainPageController implements Initializable {

    @FXML
    private Label timer;

    @FXML
    private Label name;

    @FXML
    private Label eventTime;

    @FXML
    private Pane status;

    @FXML
    private Label employeeCount;
    private final MainService service;

    public MainPageController(MainService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeNow();
    }


    private void getLastEmployee() {
        Thread thread = new Thread(() -> {
            changeProgramStatusIcon(true);
            while (service.isProgramRunning()) {
                LastEmployee lastEmployee = service.getPrintablePerson();
                if (lastEmployee != null) {
                    Platform.runLater(() -> {
                        name.setText(lastEmployee.getName());
                        eventTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                        employeeCount.setText(String.valueOf(lastEmployee.getCount()));
                    });
                }
            }
            changeProgramStatusIcon(false);
            log.info("Программа закончила работу");
        });
        thread.start();
    }

    private void changeProgramStatusIcon(boolean isProgramRunning) {
        if (isProgramRunning) {
            status.setOpacity(1);
        } else {
            status.setOpacity(0);
        }
    }

    public void timeNow() {
        Thread thread = new Thread(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                final LocalTime timeNow = LocalTime.now();
                if (timeNow.isBefore(service.getStartTime()) || timeNow.isAfter(service.getEndTime())) {
                    service.setProgramRunning(false);
                } else if (!service.isProgramRunning()) {
                    service.setProgramRunning(true);
                    getLastEmployee();
                    log.info("Программа начала работу");
                } else if (timeNow.equals(service.getDeleteOldEventsTime())) {
                    service.deleteOldEvents();
                    log.info("Удалены старые ивенты");
                } else if (timeNow.equals(LocalTime.MIDNIGHT)) {
                    service.createReport(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                }
                Platform.runLater(() -> {
                    timer.setText(timeNow.format(formatter));
                });
            }
        });
        thread.start();
    }
}
