package ru.zhbert.PackageQrCodeGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

public class Main extends JFrame {

    private File qrFile;
    private JTable jTable;
    private JLabel statusLabel;

    public Main(String title) throws HeadlessException {
        setTitle(title);
        Container container = getContentPane();
        setSize(640,480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Main menu
        JMenuBar mb = new JMenuBar();
        JMenu jm1 = new JMenu("File");
        JMenuItem jm11 = new JMenuItem("Choose source file");
        jm11.addActionListener(new ChooseFileListener());
        JMenuItem jm12 = new JMenuItem("Close");
        jm12.addActionListener(new ExitListener());
        jm1.add(jm11);
        jm1.add(jm12);
        JMenu jm2 = new JMenu("About");
        JMenuItem jm21 = new JMenuItem("About the utility");
        jm2.add(jm21);
        JMenu jm3 = new JMenu("Qr Codes");
        JMenuItem jm31 = new JMenuItem("Create Qr Codes");
        jm31.addActionListener(new QRGenerateListener());
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
        jTable = new JTable(dataVector, header);
        jTable.setRowHeight(20);
        jTable.setShowGrid(true);
        JTableHeader th = jTable.getTableHeader();
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(BorderLayout.NORTH, th);
        jPanel.add(BorderLayout.CENTER, jTable);
        //StatusBar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusLabel = new JLabel("Rows loaded: 0");
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

    public class ChooseFileListener implements ActionListener {

        private  JFileChooser fileChooser = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new CsvFileFilter());
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                qrFile = fileChooser.getSelectedFile();
                try {
                    FileReader fileReader = new FileReader(qrFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();
                    DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
                    dtm.getDataVector().removeAllElements();
                    dtm.fireTableDataChanged();
                    Vector<String> data;
                    int lineCounter = 0;
                    while (line != null) {
                        data = new Vector<>();
                        String[] params = line.split(";");
                        for (String param : params) {
                            data.add(param);
                        }
                        data.add(qrFile.getParent()+File.separator
                                +"qrCodes"+File.separator+data.firstElement()+".png");
                        line = bufferedReader.readLine();
                        dtm.addRow(data);
                        lineCounter++;
                    }
                    statusLabel.setText("Rows loaded: " + lineCounter);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        }
    }

    public class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    public class CsvFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f != null) {
                String name = f.getName();
                int i = name.lastIndexOf('.');
                if (i>0 && i<name.length()-1) {
                    return name.substring(i+1).equalsIgnoreCase("csv");
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Файлы CSV";
        }
    }

    public class QRGenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
            
        }
    }

}

