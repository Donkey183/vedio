package com.app.basevideo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 压缩辅助类
 */
public class GzipHelper {
    private static final int BUFFERSIZE = 1024;
    private static final String APP_DIR = "didi";
    private String path;

    /**
     * 解压缩
     *
     * @param is 输入流
     * @param os 输出流
     * @throws Exception
     */
    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gin = new GZIPInputStream(is);
        int count;
        byte data[] = new byte[BUFFERSIZE];
        while ((count = gin.read(data, 0, BUFFERSIZE)) != -1) {
            os.write(data, 0, count);
        }
        gin.close();
    }

    /**
     * 压缩
     *
     * @param is 输入流
     * @param os 输出流
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte data[] = new byte[BUFFERSIZE];
        while ((count = is.read(data, 0, BUFFERSIZE)) != -1) {
            gos.write(data, 0, count);
        }
        gos.flush();
        gos.finish();
        gos.close();
    }

    /**
     * 压缩
     *
     * @param is 输入字节数组
     * @param os 输出流
     * @throws Exception
     */
    public static void compress(byte[] is, OutputStream os) throws Exception {
        if (is == null || is.length == 0) {
            return;
        }

        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(is, 0, is.length);
        gos.flush();
        gos.finish();
        gos.close();
    }

    /**
     * DeCompress the ZIP to the path
     *
     * @param inputStream name of ZIP
     * @throws Exception
     */
    public static void UnZipFolder(InputStream inputStream) {
        try {
            ZipInputStream inZip = new ZipInputStream(inputStream);
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    FileHelper.checkAndMkdirs(null, szName);
//                    String path = outPathString + File.separator + szName;
//                    File folder = new File(path);
//                    folder.mkdirs();
                } else {

//                    File file = new File(outPathString + File.separator + szName);
//                    if (!file.exists()) {
//                        new File(file.getParent()).mkdirs();
//                        file.createNewFile();
//                    }
                    File file = FileHelper.createFile(null, szName);
                    // get the output stream of the file
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[BUFFERSIZE];
                    // read (len) bytes into buffer
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
        } catch (IOException e) {
            e.toString();
        }
    }
}
