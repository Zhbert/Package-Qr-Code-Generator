package ru.zhbert.PackageQrCodeGenerator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    private String qrCodesPath;

    public QRCodeGenerator() {

    }

    public QRCodeGenerator(String qrCodesPath) {
        this.qrCodesPath = qrCodesPath;
        File path = new File(qrCodesPath);
        if (!path.exists()) {
            path.mkdir();
        }
    }


    public void generateQRCodeImage(String text, int width, int height, String fileName)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(qrCodesPath + File.separator + fileName);

        File file = new File(path.toString());
        if (file.exists()) {
           file.delete();
        }
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public String getQrCodesPath() {
        return qrCodesPath;
    }

    public void setQrCodesPath(String qrCodesPath) {
        this.qrCodesPath = qrCodesPath;
    }
}
