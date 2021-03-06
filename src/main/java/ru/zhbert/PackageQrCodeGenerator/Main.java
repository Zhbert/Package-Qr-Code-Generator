package ru.zhbert.PackageQrCodeGenerator;

import com.google.zxing.WriterException;
import ru.zhbert.PackageQrCodeGenerator.service.QRCodeGenerator;

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
    private String qrCodesPath;
    private JTable jTable;
    private JLabel statusLabel;

    public Main(String title) throws HeadlessException {
        setTitle(title);
        setLocationRelativeTo(null);
        Container container = getContentPane();
        setSize(640, 480);
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
        jm21.addActionListener(new AboutListener());
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
        header.add("FILENAME");
        jTable = new JTable(dataVector, header);
        jTable.setRowHeight(20);
        jTable.setShowGrid(true);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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
        new Main("Package QR-code Generator");
    }

    public class ChooseFileListener implements ActionListener {

        private JFileChooser fileChooser = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new CsvFileFilter());
            int ret = fileChooser.showDialog(null, "?????????????? ????????");
            if (ret == JFileChooser.APPROVE_OPTION) {
                qrFile = fileChooser.getSelectedFile();
                qrCodesPath = qrFile.getParent() + File.separator + "qrCodes";
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
                        data.add(data.firstElement() + ".png");
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

    public class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog();
            dialog.setTitle("About");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(150, 100);
            dialog.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setSize(400,  180);

            String text = "<html><center><h2>Package QR-code Generator v. 1.0.0</h2>" +
                    "<br>" +
                    "A simple utility for batch generation of QR codes" +
                    "<br>" +
                    "Licensed by <a href=https://www.gnu.org/licenses/gpl-3.0.html>GPLv3</a>" +
                    "<br>" +
                    "Developed by <a href=https://zhbert.ru>Zhbert</a><br>" +
                    "Email: zhbert@yandex.ru" +
                    "<center></html>";
            Label label = new Label(text, SwingConstants.CENTER);
            Font f = new Font("Arial", Font.PLAIN, 14);
            label.setFont(f);
            label.setAlignment(Label.CENTER);

            Box box = Box.createVerticalBox();
            box.add(label);
            dialog.add(box);

            dialog.setVisible(true);
        }
    }

    public class CsvFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f != null) {
                String name = f.getName();
                int i = name.lastIndexOf('.');
                if (i > 0 && i < name.length() - 1) {
                    return name.substring(i + 1).equalsIgnoreCase("csv");
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "?????????? CSV";
        }
    }

    public class QRGenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            QRCodeGenerator qrCodeGenerator = new QRCodeGenerator(qrCodesPath);
            int count;
            for (count = 0; count < jTable.getRowCount(); count++) {
                try {
                    qrCodeGenerator.generateQRCodeImage(jTable.getValueAt(count, 0).toString(),
                            350, 350, jTable.getValueAt(count, 2).toString());
                } catch (WriterException writerException) {
                    writerException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            statusLabel.setText("Files processed: " + count++);
        }
    }

}

