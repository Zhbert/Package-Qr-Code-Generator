package ru.zhbert.PackageQrCodeGenerator;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main(String title) throws HeadlessException {
        setTitle(title);
        Container container = getContentPane();
        setSize(300,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main("Package QR Code Generator");
    }
}

