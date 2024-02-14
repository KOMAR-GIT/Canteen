package com.moika22.canteen.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@FxmlView("console_reader.fxml")
@Slf4j
public class ConsoleReaderController implements Initializable {

    @FXML
    private TextArea consoleTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        redirectStandardOut(consoleTextArea);
    }

    private void redirectStandardOut(TextArea area) {
        try {
            PipedInputStream in = new PipedInputStream();
            System.setOut(new PrintStream(new PipedOutputStream(in), true, UTF_8));

            Thread thread = new Thread(new StreamReader(in, area));
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static class StreamReader implements Runnable {

        private final StringBuilder buffer = new StringBuilder();
        private boolean notify = true;

        private final BufferedReader reader;
        private final TextArea textArea;

        StreamReader(InputStream input, TextArea textArea) {
            this.reader = new BufferedReader(new InputStreamReader(input, UTF_8));
            this.textArea = textArea;
        }

        @Override
        public void run() {
            try (reader) {
                int charAsInt;
                while ((charAsInt = reader.read()) != -1) {
                    synchronized (buffer) {
                        buffer.append((char) charAsInt);
                        if (notify) {
                            notify = false;
                            Platform.runLater(this::appendTextToTextArea);
                        }
                    }
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }

        private void appendTextToTextArea() {
            synchronized (buffer) {
                textArea.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                notify = true;
            }
        }
    }

}
