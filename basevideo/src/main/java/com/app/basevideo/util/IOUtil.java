package com.app.basevideo.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.app.basevideo.base.MFBaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * IO工具类
 *
 * @date 2013-04-17
 */
public class IOUtil {

    /**
     * 判断文件是否存在
     */
    public static boolean isExists(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        File file = new File(fileName);
        return file.exists();
    }


    private static boolean isNull(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名(包含路径)
     */
    public static boolean delete(String fileName) {
        if (isNull(fileName)) {
            return false;
        }
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param name 目录名，可以是多级目录
     */
    public static boolean mkDir(String name) {
        if (isNull(name)) {
            return false;
        }

        File file = new File(name);
        if (file.isDirectory()) {
            return true;
        }
        // mkdirs可以创建多级目录, mkdir只能创建一级目录
        return file.mkdirs();
    }


    public static byte[] readFileToByte(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        File file = new File(fileName);
        InputStream is;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            outStream.flush();
            outStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将数据流保存在指定文件里
     *
     * @param fileName 文件名(包含路径)
     * @param is       数据流
     * @return true：保存成功 false：保存失败
     */
    public static boolean saveFile(String fileName, InputStream is) {
        if (TextUtils.isEmpty(fileName) || is == null) {
            return false;
        }
        try {
            OutputStream outStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 合并文件
     *
     * @param file1 需要合并的文件
     * @param file2 需要合并的文件
     * @param file3 很并后的文件
     */
    public static void sequenceFile(String file1, String file2, String file3) {
        if (TextUtils.isEmpty(file1) || TextUtils.isEmpty(file2) || TextUtils.isEmpty(file2)) {
            return;
        }
        File f1 = new File(file1);
        File f2 = new File(file2);
        File f3 = new File(file3);

        try {
            InputStream in1 = new FileInputStream(f1);
            InputStream in2 = new FileInputStream(f2);
            OutputStream outStream = new FileOutputStream(f3);
            SequenceInputStream sis = new SequenceInputStream(in1, in2);
            int temp = 0;
            while ((temp = sis.read()) != -1) {
                outStream.write(temp);
            }
            in1.close();
            in2.close();
            outStream.close();
            sis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩多个文件
     *
     * @param srcfile 需要压缩的文件
     * @param zipFile 压缩后的文件
     */
    public static String zipFile(File[] srcfile, String zipFile) {
        if (srcfile == null || isNull(zipFile)) {
            return null;
        }

        byte[] buf = new byte[1024];
        try {
            File file = new File(zipFile);
            if (file.exists()) {
                file.delete();
            }

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
            int length = srcfile.length;
            for (int i = 0; i < length; i++) {
                try {
                    if (srcfile[i] == null || !srcfile[i].exists() || srcfile[i].isDirectory()
                            || srcfile[i].length() <= 0) {
                        continue;
                    }
                } catch (Exception e) {
                }
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    public static String zipFile(ArrayList<File> srcfile, String zipFile) {
        if (srcfile == null || isNull(zipFile)) {
            return null;
        }

        byte[] buf = new byte[1024];
        try {
            File file = new File(zipFile);
            if (file.exists()) {
                file.delete();
            }
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
            for (File f : srcfile) {
                try {
                    if (f == null || !f.exists() || f.isDirectory() || f.length() <= 0) {
                        continue;
                    }
                } catch (Exception e) {
                }
                FileInputStream in = new FileInputStream(f);
                out.putNextEntry(new ZipEntry(f.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    /**
     * 压缩单个文件
     *
     * @param fileName 需要压缩的文件
     * @param zipFile  压缩后的文件
     */
    public static void zipFile(String fileName, String zipFile) {
        if (isNull(fileName) || isNull(zipFile)) {
            return;
        }
        File file = new File(fileName);
        File zf = new File(zipFile);
        try {
            InputStream inputStream = new FileInputStream(file);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zf));
            out.putNextEntry(new ZipEntry(file.getName()));
            out.setComment("Zip File");
            int tmp = 0;
            while ((tmp = inputStream.read()) != -1) {
                out.write(tmp);
            }
            inputStream.close();
            out.finish();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件为Name.zip的格式
     */
    public static String zipFile(String filePath) {
        if (isNull(filePath)) {
            return null;
        }
        File file = new File(filePath);
        String zipFile = filePath + ".zip";
        File zf = new File(zipFile);
        try {
            InputStream inputStream = new FileInputStream(file);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zf));
            out.putNextEntry(new ZipEntry(file.getName()));
            out.setComment("Zip File");
            int tmp = 0;
            while ((tmp = inputStream.read()) != -1) {
                out.write(tmp);
            }
            inputStream.close();
            out.finish();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return zipFile;
    }

    /**
     * 解压缩文件
     *
     * @param zipFile  压缩文件
     * @param fileName 解压后的文件
     */
    public static void unZipFile(String zipFile, String fileName) {
        if (isNull(fileName) || isNull(zipFile)) {
            return;
        }
        File file = new File(zipFile);
        File outFile = new File(fileName);

        try {
            ZipFile zpFile = new ZipFile(file);
            @SuppressWarnings("resource")
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = zipInputStream.getNextEntry();
            InputStream input = zpFile.getInputStream(entry);
            OutputStream output = new FileOutputStream(outFile);
            int temp = 0;
            while ((temp = input.read()) != -1) {
                output.write(temp);
            }
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回文件大小单位KB
     */
    public static long getLogFileSize(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return 0;
        }

        File file = new File(fileName);
        if (file.isFile()) {
            return file.length() / 1024;
        } else {
            return 0;
        }
    }

    /**
     * 返回文件大小单位Byte
     */
    public static long getFileSize(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return 0;
        }

        File file = new File(fileName);
        try {
            return new FileInputStream(file).available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 输入输出流拷贝
     *
     * @param is
     * @param os
     */
    public static void copy(InputStream is, OutputStream os) throws Exception {
        if (is == null || os == null)
            return;
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buf = new byte[getAvailableSize(bis)];
        int i = 0;
        while ((i = bis.read(buf)) != -1) {
            bos.write(buf, 0, i);
        }
        bis.close();
        bos.close();
    }

    private static int getAvailableSize(InputStream is) throws IOException {
        if (is == null)
            return 0;
        int available = is.available();
        return available <= 0 ? 1024 : available;
    }

    /**
     * 保存数据到uri指定位置
     *
     * @param uri
     * @param data
     */
    public static void save(Uri uri, byte[] data) {
        String path = uri.getPath();
        try {
            BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(path));
            baos.write(data);
            baos.flush();
            baos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据写入指定的文件
     *
     * @param data 要写入的数据
     * @param path 要写入的文件路径
     */
    public static void save(byte[] data, String path) {
        if (data == null || TextUtils.isEmpty(path)) {
            return;
        }

        try {
            BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(path));
            baos.write(data);
            baos.flush();
            baos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeInputStream(InputStream is) {
        if (is == null)
            return;
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeOutStream(OutputStream os) {
        if (os == null)
            return;
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将二进制转换为音频文件
     *
     * @param b        待转换的二进制
     * @param fileName 转换后的文件名
     * @return fileName 成功后返回文件名，失败返回null
     */
    public static String byte2AudioFile(byte[] b, String fileName) {
        FileOutputStream outStream;
        try {
            outStream = MFBaseApplication.getInstance().openFileOutput(fileName + ".mp3",
                    Context.MODE_PRIVATE);
            outStream.write(b);
            outStream.flush();
            outStream.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取输入流
     *
     * @param uri
     * @return
     */
    public static InputStream getInputStream(Uri uri) {
        try {
            if (uri.getScheme().equals("file")) {
                return new FileInputStream(uri.getPath());
            } else {
                return MFBaseApplication.getInstance().getContentResolver().openInputStream(uri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param uri
     * @return
     */
    public static String getFilePath(Uri uri) {
        String path = "";
        if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        } else {
            try {
                Cursor cursor = MFBaseApplication.getInstance().getContentResolver()
                        .query(uri, null, null, null, null);
                cursor.moveToFirst();
                path = cursor.getString(1);
                IOUtil.closeCursor(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 关闭游标cursor
     *
     * @param cursor
     */
    public static void closeCursor(Cursor cursor) {
        try {
            /* 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15) */
            if (android.os.Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete file the file can be a directory
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static void deleteDir(String dir) {
        try {
            deleteDir(new File(dir));
        } catch (Exception e) {
        }
    }

    public static void deleteDir(File dir) {
        if (dir != null && dir.exists()) {
            if (dir.isDirectory()) {
                File files[] = dir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }
            // file.delete();
        }
    }

    /**
     * 获取指定扩展名的文件
     *
     * @param path   路径
     * @param suffix 文件扩展名
     */
    public static File[] getFiles4Filter(final String path, final String suffix) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(suffix)) {
            return null;
        }
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suffix);
            }
        };

        return new File(path).listFiles(filter);
    }

}
