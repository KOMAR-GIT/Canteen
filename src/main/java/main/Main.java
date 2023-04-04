package main;

import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.print.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class Main {

    public static void main(String[] args) throws IOException, PrintException {

        SpringApplication.run(Main.class, args);
        MainService mainService;



    }
}

