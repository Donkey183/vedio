package com.app.basevideo.util;

import android.os.Environment;
import android.os.StatFs;

import com.app.basevideo.framework.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


/**
 * 文件辅助类
 *
 * @Note 所有接口的传参约定： appath: 表示应用内部的目录结构（比如，有聊某群组的语音存储/verifyData/group/{gid}/voice
 * filename: 表示文件名（包含扩展名）
 * @see 1、约定一级目录结构：SDCarDir/APP_DIR/ 2、所有接口都支持传入二级目录+文件名，path+file
 */
public class FileHelper {
    private static String APP_DIR = "didi";
    public static final File EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory();
    public static final int SD_MIN_AVAILAALE_SIZE = 2;

    /**
     * @brief 错误号定义
     */
    public static final int ERR_FILE_OK = 0;
    // SD卡相关错误
    public static final int ERR_FILE_NO_SD = 1; // 无法找到存储卡
    public static final int ERR_FILE_SHARED_SD = 2; // 你的存储卡被USB占用，请更改数据线连接方式
    public static final int ERR_FILE_IO_SD = 3; // 存储卡读写失败

    /**
     * The extension separator character.
     *
     * @since 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

    /**
     * @brief 设置应用根目录 比如："tieba", "local", "baidu/youliao"
     */
    public static void setTmpDir(String dir) {
        APP_DIR = dir;
    }

    /**
     * 检查SD卡是否存在
     *
     * @return true：存在； false：不存在
     */
    public static boolean checkSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @brief 返回错误号
     */
    public static int getSdError() {
        String status = Environment.getExternalStorageState();
        int error = ERR_FILE_OK;

        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return error;
        }

        if (status.equals(Environment.MEDIA_UNMOUNTED) || status.equals(Environment.MEDIA_UNMOUNTABLE)
                || status.equals(Environment.MEDIA_REMOVED)) {
            error = ERR_FILE_NO_SD;
        } else if (status.equals(Environment.MEDIA_SHARED)) {
            error = ERR_FILE_SHARED_SD;
        } else {
            error = ERR_FILE_IO_SD;
        }
        return error;
    }

    /**
     * @param appath : 应用内路径，为null表示使用应用根目录
     * @brief 获取全路径
     */
    public static String getPath(String appath) {
        String path = null;
        if (appath != null) {
            path = EXTERNAL_STORAGE_DIRECTORY + "/" + APP_DIR + "/" + appath + "/";
        } else {
            path = EXTERNAL_STORAGE_DIRECTORY + "/" + APP_DIR + "/";
        }
        return path;
    }

    /**
     * @param appath   : 应用内路径，为null表示使用应用根目录
     * @param filename : 文件名
     * @brief 获取文件全路径
     */
    public static String getFilePath(String appath, String filename) {
        String file = null;
        if (appath != null) {
            file = EXTERNAL_STORAGE_DIRECTORY + "/" + APP_DIR + "/" + appath + "/" + filename;
        } else {
            file = EXTERNAL_STORAGE_DIRECTORY + "/" + APP_DIR + "/" + filename;
        }
        return file;
    }

    public static boolean checkSDHasSpace() {
        try {
            StatFs statfs = new StatFs(EXTERNAL_STORAGE_DIRECTORY.getPath());
            long availaBlock = statfs.getAvailableBlocks();
            long blocSize = statfs.getBlockSize();
            long sdFreeSize = availaBlock * blocSize / 1024 / 1024;

            return sdFreeSize > SD_MIN_AVAILAALE_SIZE;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getFilePath(String file) {
        return getFilePath(null, file);
    }

    /**
     * @return true：存在； false：不存在
     * @brief 检查文件夹是否存在
     */
    public static boolean checkDir(String appath) {
        String dir = getPath(appath);

        if (checkSD() == false) {
            return false;
        }
        File tf = new File(dir);

        if (tf.exists()) {
            return true;
        }

        boolean ret = tf.mkdirs();
        if (ret == false) {
            // LogUtil.e("error fulldirObj.mkdirs:" + dir);
            return false;
        }

        return true;
    }

    private static String getDir(String fullfile) {
        int index = fullfile.lastIndexOf("/");
        if (index > 0 && index < fullfile.length()) {
            return fullfile.substring(0, index);
        }
        return null;
    }

    public static boolean checkAndMkdirs(String appath, String filename) {
        String fullfile = getFilePath(appath, filename);
        String fulldir = getDir(fullfile);
        File fulldirObj = new File(fulldir);
        boolean ret = false;
        if (!fulldirObj.exists()) {
            try {
                ret = fulldirObj.mkdirs();
                if (ret == false) {
                    // LogUtil.e("error fulldirObj.mkdirs:" + fulldir);
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.e(ex.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * 检查文件夹中的文件是否存在
     *
     * @return true：存在； false：不存在
     */
    public static boolean checkFile(String appath, String filename) {
        if (checkSD() == false) {
            return false;
        }
        try {
            File tf = getFile(appath, filename);

            if (tf.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
            return false;
        }
    }

    public static boolean checkFile(String file) {
        return checkFile(null, file);
    }

    /**
     * 获得文件夹中的文件对象（不区分文件是否存在）
     *
     * @param filename 文件名
     * @return 成功：File； 失败：空
     */
    public static File getFile(String appath, String filename) {
        if (checkDir(appath) == false) {
            return null;
        }

        try {
            String file = getFilePath(appath, filename);
            File fileObj = new File(file);
            return fileObj;
        } catch (SecurityException ex) {
            LogUtil.e(ex.getMessage());
            return null;
        }
    }

    public static File getFile(String file) {
        return getFile(null, file);
    }

    /**
     * 在文件夹中创建新文件，如果存在先删除
     *
     * @param filename 新文件名字
     * @return 成功：File； 失败：空
     */
    public static File createFile(String appath, String filename) {
        if (checkDir(appath) == false) {
            // LogUtil.e("error checkDir");
            return null;
        }
        try {
            // 先解决目录问题
            boolean ret = checkAndMkdirs(appath, filename);
            if (ret == false) {
                // LogUtil.e("error checkAndMkdirs");
                return null;
            }

            // 再创建文件
            File file = getFile(appath, filename);
            if (file.exists()) {
                if (file.delete() == false) {
                    // LogUtil.e("error file.delete");
                    return null;
                }
            }

            if (file.createNewFile() == true) {
                return file;
            } else {
                // LogUtil.e("error createNewFile" + appath + filename);
                return null;
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
            return null;
        }
    }

    public static File createFile(String file) {
        return createFile(null, file);
    }

    /**
     * 在文件夹中创建新文件，如果文件存在直接返回
     *
     * @param filename 新文件名字
     * @return 成功：File； 失败：空
     */
    public static File createFileIfNotFound(String appath, String filename) {
        if (checkDir(appath) == false) {
            return null;
        }
        try {
            File file = getFile(appath, filename);
            if (file.exists()) {
                return file;
            } else {
                if (file.createNewFile() == true) {
                    return file;
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
            return null;
        }
    }

    public static File createFileIfNotFound(String file) {
        return createFileIfNotFound(null, file);
    }

    /**
     * 在文件夹中保存图片文件
     *
     * @param appath    路径
     * @param filename  临时文件名
     * @param imageData 图片数据
     * @return 成功：保存文件的全路径 ； 失败：空
     */
    public static boolean saveGifFile(String appath, String filename, byte[] imageData) {
        if (checkDir(appath) == false) {
            return false;
        }
        if (imageData == null) {
            return false;
        }

        FileOutputStream fOut = null;
        try {

            File file = createFile(appath, filename);
            if (file != null) {
                fOut = new FileOutputStream(file, true);
            } else {
                return false;
            }

            fOut.write(imageData);
            fOut.flush();
            fOut.close();
            fOut = null;
            return true;
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
            return false;
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (Exception ex2) {
                LogUtil.e(ex2.getMessage());
            }
        }
    }

    public static boolean saveGifFile(String file, byte[] imageData) {
        return saveGifFile(null, file, imageData);
    }

    /**
     * 查看路径下图片是否是Gif
     *
     * @param appath   路径
     * @param filename 临时文件名
     * @return 是：true 否：false
     */
    public static boolean isGif(String appath, String filename) {
        boolean result = false;
        InputStream fStream = null;
        File file = getFile(appath, filename);
        try {
            fStream = new FileInputStream(file);

            byte[] temp = new byte[7];
            int num = fStream.read(temp, 0, 6);
            if (num == 6) {
                result = UtilHelper.isGif(temp);
            }
            if (fStream != null) {
                fStream.close();
                fStream = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fStream != null) {
                    fStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean isGif(String file) {
        return isGif(null, file);
    }

    public static boolean saveFile(String appath, String filename, byte[] data) {
        if (checkDir(appath) == false) {
            return false;
        }

        // 先处理创建目录
        if (FileHelper.checkAndMkdirs(appath, filename) != true) {
            // LogUtil.e("checkAndMkdirs fail:" + appath + filename);
            return false;
        }

        File file = getFile(appath, filename);
        FileOutputStream fOut = null;
        try {
            if (file.exists()) {
                if (file.delete() == false) {
                    return false;
                }
            }
            if (file.createNewFile() == false) {
                return false;
            }
            fOut = new FileOutputStream(file);
            fOut.write(data, 0, data.length);
            fOut.flush();
            fOut.close();
            fOut = null;
            return true;
        } catch (IOException ex) {
            LogUtil.e(ex.getMessage());
            return false;
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (Exception ex) {
                LogUtil.e(ex.getMessage());
            }
        }
    }

    public static boolean saveFile(String file, byte[] data) {
        return saveFile(null, file, data);
    }

    public static byte[] getFileData(String appath, String filename) {
        if (checkDir(appath) == false) {
            return null;
        }

        File file = getFile(appath, filename);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fStream = null;
        ByteArrayOutputStream outputstream = null;
        try {
            fStream = new FileInputStream(file);
            outputstream = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int num = -1;
            while ((num = fStream.read(temp, 0, 1024)) != -1) {
                outputstream.write(temp, 0, num);
            }
            return outputstream.toByteArray();
        } catch (IOException ex) {
            LogUtil.e(ex.getMessage());
            return null;
        } finally {
            CloseUtil.close(fStream);
            CloseUtil.close(outputstream);
        }
    }

    public static byte[] getFileData(String file) {
        return getFileData(null, file);
    }

    public static boolean copyFile(String srcAppath, String srcFilename, String dstAppath, String dstFilename) {
        boolean result = false;
        InputStream fosfrom = null;
        OutputStream fosto = null;
        try {
            File srcFile = getFile(srcAppath, srcFilename);
            File dstFile = getFile(dstAppath, dstFilename);
            if (!srcFile.exists()) {
                return result;
            }
            fosfrom = new FileInputStream(srcFile);
            fosto = new FileOutputStream(dstFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosfrom = null;
            fosto.close();
            fosto = null;
        } catch (Exception e) {
            LogUtil.e(e.toString());
        } finally {
            try {
                if (fosfrom != null) {
                    fosfrom.close();
                }
            } catch (Exception e) {
                LogUtil.e(e.toString());
            }
            try {
                if (fosto != null) {
                    fosto.close();
                }
            } catch (Exception e) {
                LogUtil.e(e.toString());
            }
        }
        return result;
    }

    /**
     * Copies a file to a new location preserving the file date.
     * <p/>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p/>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that the operation will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * @param srcFile  an existing file to copy, must not be {@code null}
     * @param destFile the new file, must not be {@code null}
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File)
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    /**
     * Copies a file to a new location.
     * <p/>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p/>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified date/times using
     * {@link File#setLastModified(long)}, however it is not guaranteed that the
     * operation will succeed. If the modification operation fails, no
     * indication is provided.
     *
     * @param srcFile          an existing file to copy, must not be {@code null}
     * @param destFile         the new file, must not be {@code null}
     * @param preserveFileDate true if the file date of the copy should be the same as the
     *                         original
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File, boolean)
     */
    public static void copyFile(File srcFile, File destFile,
                                boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * Internal copy file method.
     *
     * @param srcFile          the validated source file, must not be {@code null}
     * @param destFile         the validated destination file, must not be {@code null}
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException if an error occurs
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } catch (Error e) {
            // LT18i 定制有问题，java sdk找不到方法，增加下保护。
            // http://cq01-rdqa-dev039.cq01.baidu.com:8000/android/?act=detailPage&version=6.2.2&hash=3712790065
            // http://cq01-rdqa-dev039.cq01.baidu.com:8000/android/?act=detailPage&version=6.2.2&hash=664744344

            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        } finally {
            CloseUtil.close(output);
            CloseUtil.close(fos);
            CloseUtil.close(input);
            CloseUtil.close(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    public static boolean copyFile(String srcFile, String dstFile) {
        return copyFile(null, srcFile, null, dstFile);
    }

    public static boolean renameFile(String srcAppath, String srcFilename, String dstAppath, String dstFilename) {
        boolean result = false;
        try {
            // 确保目录存在，否则逐级创建
            boolean ret = checkAndMkdirs(dstAppath, dstFilename);
            if (ret == false) {
                // LogUtil.e("error checkAndMkdirs");
                return result;
            }

            File srcFile = getFile(srcAppath, srcFilename);
            File dstFile = getFile(dstAppath, dstFilename);
            if (!srcFile.exists()) {
                // LogUtil.e("src File not exist:" + srcAppath + srcFilename + " "
                // + dstAppath
                // + dstFilename);
                return result;
            }
            if (dstFile.exists()) {
                // LogUtil.e("dst File exist:" + srcAppath + srcFilename + " " +
                // dstAppath
                // + dstFilename);
                return result;
            }
            result = srcFile.renameTo(dstFile);
            return result;
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return result;
    }

    public static boolean renameFile(String srcFile, String dstFile) {
        return renameFile(null, srcFile, null, dstFile);
    }

    /**
     * 获得文件夹中文件的输入流
     *
     * @param filename 文件名
     * @return 成功：输入流； 失败：空
     */
    public static InputStream getInStreamFromFile(String appath, String filename) {
        File f = getFile(appath, filename);
        return getInStreamFromFile(f);
    }

    /**
     * 获得文件夹中文件的输入流
     *
     * @param file 文件
     * @return 成功：输入流； 失败：空
     */
    public static InputStream getInStreamFromFile(File file) {
        if (file != null) {
            try {
                return new FileInputStream(file);
            } catch (Exception ex) {
                LogUtil.e(ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获得文件夹中文件的输出流
     *
     * @param filename 文件名
     * @return 成功：输入流； 失败：空
     */
    public static OutputStream getOutStreamFromFile(String appath, String filename) {
        File f = getFile(appath, filename);
        return getOutStreamFromFile(f);
    }

    /**
     * 获得文件夹中文件的输出流
     *
     * @param file 文件
     * @return 成功：输入流； 失败：空
     */
    public static OutputStream getOutStreamFromFile(File file) {
        if (file != null) {
            try {
                return new FileOutputStream(file);
            } catch (Exception ex) {
                LogUtil.e(ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 删除临时文件夹中的文件
     *
     * @param filename 文件名
     * @return 成功：true； 失败：false
     */
    public static boolean delFile(String appath, String filename) {
        if (checkDir(appath) == false) {
            return false;
        }
        File file = getFile(appath, filename);
        try {
            if (file.exists()) {
                return file.delete();
            } else {
                return false;
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
            return false;
        }
    }

    public static boolean delFile(String file) {
        return delFile(null, file);
    }

    public static boolean deleteDir(String appath, String filename) {
        File file = getFile(appath, filename);
        return deleteDir(file);
    }

    private static boolean deleteDir(File dir) {
        if (null == dir) return false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                // 递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void writeAmrFileHeader(OutputStream out) throws IOException {
        byte[] header = new byte[6];
        header[0] = '#'; // AMR header
        header[1] = '!';
        header[2] = 'A';
        header[3] = 'M';
        header[4] = 'R';
        header[5] = '\n';
        out.write(header, 0, 6);
    }

    public static void writeWaveFileHeader(DataOutputStream out, long totalAudioLen, long totalDataLen,
                                           long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    public static long getDirectorySize(String path, boolean isIgnoreDirectory) {
        return getDirectorySize(new File(path), isIgnoreDirectory);
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @param isIgnoreDirectory 忽略字文件夹
     * @return
     */
    public static long getDirectorySize(File f, boolean isIgnoreDirectory) {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist == null) {
            return size;
        }

        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory() && !isIgnoreDirectory) {
                size = size + getDirectorySize(flist[i], false);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 确保目录存在
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
        }
    }

    public static long getFileSize(File f) {
        long s = 0;
        FileInputStream fis = null;
        try {
            if (f.exists()) {
                fis = new FileInputStream(f);
                s = fis.available();
            }
        } catch (Exception ex) {
            s = 0;
        } finally {
            CloseHelper.close(fis);
        }
        return s;
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory,
     * delete it and all sub-directories.
     * <p/>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     * {@code false}
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Deletes a file. If file is a directory, delete it and all
     * sub-directories.
     * <p/>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file file or directory to delete, must not be {@code null}
     * @throws NullPointerException  if the directory is {@code null}
     * @throws FileNotFoundException if the file was not found
     * @throws IOException           in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Deletes a directory recursively.
     *
     * @param directory directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Determines whether the specified file is a Symbolic Link rather than an
     * actual file.
     * <p/>
     * Will not return true if there is a Symbolic Link anywhere in the path,
     * only if the specific file is.
     * <p/>
     * <b>Note:</b> the current implementation always returns {@code false} if
     * the system is detected as Windows using
     * {@link FilenameUtils#isSystemWindows()}
     *
     * @param file the file to check
     * @return true if the file is a Symbolic Link
     * @throws IOException if an IO error occurs while checking the file
     */
    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (isSystemWindows()) {
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is not a
     * directory then an IOException is thrown. If the directory cannot be
     * created (or does not already exist) then an IOException is thrown.
     *
     * @param directory directory to create, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     * @throws IOException          if the directory cannot be created or the file already exists
     *                              but is not a directory
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message =
                        "File "
                                + directory
                                + " exists and is "
                                + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    String message =
                            "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }

    /**
     * Gets the extension of a filename.
     * <p/>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <p/>
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p/>
     * The output will be the same irrespective of the machine that the code is
     * running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or
     * {@code null} if the filename is {@code null}.
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * Returns the index of the last extension separator character, which is a
     * dot.
     * <p/>
     * This method also checks that there is no directory separator after the
     * last dot. To do this it uses {@link #indexOfLastSeparator(String)} which
     * will handle a file in either Unix or Windows format.
     * <p/>
     * The output will be the same irrespective of the machine that the code is
     * running on.
     *
     * @param filename the filename to find the last path separator in, null returns
     *                 -1
     * @return the index of the last separator character, or -1 if there is no
     * such character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? -1 : extensionPos;
    }

    /**
     * Returns the index of the last directory separator character.
     * <p/>
     * This method will handle a file in either Unix or Windows format. The
     * position of the last forward or backslash is returned.
     * <p/>
     * The output will be the same irrespective of the machine that the code is
     * running on.
     *
     * @param filename the filename to find the last path separator in, null returns
     *                 -1
     * @return the index of the last separator character, or -1 if there is no
     * such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

}
