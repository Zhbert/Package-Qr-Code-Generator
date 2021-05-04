package ru.zhbert.PackageQrCodeGenerator;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class Main extends JFrame {

    public Main(String title) throws HeadlessException {
        setTitle(title);
        Container container = getContentPane();
        setSize(640,480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Main menu
        JMenuBar mb = new JMenuBar();
        JMenu jm1 = new JMenu("File");
        JMenuItem jm11 = new JMenuItem("Choose source file");
        JMenuItem jm12 = new JMenuItem("Close");
        jm1.add(jm11);
        jm1.add(jm12);
        JMenu jm2 = new JMenu("About");
        JMenuItem jm21 = new JMenuItem("About the utility");
        jm2.add(jm21);
        JMenu jm3 = new JMenu("Qr Codes");
        JMenuItem jm31 = new JMenuItem("Create Qr Codes");
        jm3.add(jm31);
        mb.add(jm1);
        mb.add(jm3);
        mb.add(jm2);
        //Main table
        Vector<String> id = new Vector<>();
        id.add("");
        id.add("");
        id.add("");
        Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
        dataVector.add(id);
        Vector<String> header = new Vector<>(2);
        header.add("FILE NAME");
        header.add("TARGET STRING");
        header.add("PATH TO QR CODE FILE");
        JTable jTable = new JTable(dataVector, header);
        jTable.setRowHeight(20);
        jTable.setShowGrid(true);
        JTableHeader th = jTable.getTableHeader();
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(BorderLayout.NORTH, th);
        jPanel.add(BorderLayout.CENTER, jTable);
        //StatusBar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel statusLabel = new JLabel("Rows loaded: ");
        statusBar.add(statusLabel);
        //Placement
        container.add(BorderLayout.NORTH, mb);
        container.add(BorderLayout.CENTER, jPanel);
        container.add(BorderLayout.SOUTH, statusBar);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main("Package QR Code Generator");
    }
}

