package main.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.api.LastEmployee;
import main.services.MainService;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

@Component
@FxmlView("main_page.fxml")
public class MainPageController implements Initializable {

    @FXML
    private Label timer;

    @FXML
    private Label name;

    @FXML
    private Label eventTime;

    @FXML
    private Pane status;

    @Value("${time.start}")
    private String startProgramTime;

    @Value("${time.stop}")
    private String finishProgramTime;
    private final MainService service;

    public MainPageController(MainService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeNow();
        getLastEmployee();
    }


    private void getLastEmployee() {
        Thread thread = new Thread(() -> {
            while (service.isProgramRunning()) {
                try {
                    LastEmployee lastEmployee = service.getPrintablePerson(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                    if (lastEmployee != null) {
                        name.setText(lastEmployee.getName());
                        eventTime.setText(lastEmployee.getTime());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    private void changeProgramStatusIcon(boolean isProgramRunning) {
        if (isProgramRunning) {
            status.setOpacity(100.0);
        }else {
            status.setOpacity(0.0);
        }
    }

    private void timeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                final String timeNow = dateFormat.format(new Date());
                if (timeNow.equals(startProgramTime)) {
                    service.setProgramRunning(true);
                    changeProgramStatusIcon(service.isProgramRunning());
                } else if (timeNow.equals(finishProgramTime)) {
                    service.setProgramRunning(false);
                    changeProgramStatusIcon(service.isProgramRunning());
                }
                Platform.runLater(() -> {
                    timer.setText(timeNow);
                });
            }
        });
        thread.start();
    }
}
