package main.com.util.file;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Auther: admin
 * @Date: 2019/5/13 14:04
 * @Description:
 */
public class FileUtils {

    public static void main(String[] args) {
        File source = new File("F:\\workspace\\SSMT Maven Webapp\\src\\main\\resources\\a.txt");
        File targe = new File("F:\\workspace\\SSMT Maven Webapp\\src\\main\\resources\\b.txt");
        try {
//            testIOFileCopy(source,targe);
            testNIOFileCopy(source,targe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**\
     * 将原文件拷贝到目标文件,传统IO操作
     * @param source
     * @param targe
     */
    public static void testIOFileCopy(File source,File targe) throws IOException {
        if(!source.exists()){
            System.out.println("源文件不存在！！");
        }
        if(!targe.exists()){
            targe.createNewFile();
        }
        try(
                InputStream is = new FileInputStream(source);
                OutputStream os = new FileOutputStream(targe)
        ){
            byte[] buff = new byte[1024];
            int length;
            while ((length = is.read(buff)) > 0 ){
                os.write(buff,0,length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO实现文件复制
     * @param source
     * @param targe
     */
    public static void testNIOFileCopy(File source,File targe) throws IOException {
        if(!source.exists()){
            System.out.println("源文件不存在！");
        }
        if(!targe.exists()){
            targe.createNewFile();
        }
        try(FileChannel sourceChannel = new FileInputStream(source).getChannel();
            FileChannel targetChannel = new FileOutputStream(targe).getChannel()
        ){
            for (long count = sourceChannel.size();count>0;){
                long transferred = sourceChannel.transferTo(sourceChannel.position(),count,targetChannel);
                sourceChannel.position(sourceChannel.position()+transferred);
                count -= transferred;
            }
            System.out.println("复制完成！！");
        }

    }

    /**
     *
     * 拷贝文件夹中的所有文件到另外一个文件夹
     *
     * @param srcDirector
     *            源文件夹
     *
     * @param desDirector
     *            目标文件夹
     */

    public static void copyDir(String srcDirector, String desDirector)
            throws IOException {

        (new File(desDirector)).mkdirs();

        File[] file = (new File(srcDirector)).listFiles();

        for (int i = 0; i < file.length; i++) {

            if (file[i].isFile()) {

                System.out.println("拷贝：" + file[i].getAbsolutePath() + "-->"
                        + desDirector + "/" + file[i].getName());

                FileInputStream input = new FileInputStream(file[i]);

                FileOutputStream output = new FileOutputStream(desDirector
                        + "/" + file[i].getName());

                byte[] b = new byte[1024 * 5];

                int len;

                while ((len = input.read(b)) != -1) {

                    output.write(b, 0, len);

                }

                output.flush();

                output.close();

                input.close();

            }

            if (file[i].isDirectory()) {

                System.out.println("拷贝：" + file[i].getAbsolutePath() + "-->"
                        + desDirector + "/" + file[i].getName());

                copyDir(srcDirector + "/" + file[i].getName(), desDirector
                        + "/" + file[i].getName());

            }

        }

    }

    /**
     * 复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
     *
     * @param source
     *            源文件（夹）
     * @param target
     *            目标文件（夹）
     * @param isFolder
     *            若进行文件夹复制，则为True；反之为False
     * @throws Exception
     */
    public static void copy(String source, String target, boolean isFolder)
            throws Exception {
        if (isFolder) {
            (new File(target)).mkdirs();
            File a = new File(source);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file[i]);
                } else {
                    temp = new File(source + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + "/"
                            + (temp.getName()).toString());
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copy(source + "/" + file[i], target + "/" + file[i], true);
                }
            }
        } else {
            int byteread = 0;
            File oldfile = new File(source);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(source);
                File file = new File(target);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }
    }

    /**
     * 删除文件,如果是目录则失败
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if(file == null)	return false;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                return false;
            }
            file.delete();
            return true;
        } else {
            System.out.println("FileUtil.deleteFile.FileNotFoundException :找不到" + file.getPath() + "文件！");
            return false;
        }
    }

    /**
     * 删除文件,如果是目录则失败
     *
     * @param fileName
     */
    public static boolean deleteFile(String fileName) {
        if(fileName == null || fileName.trim().length() == 0)	return false;
        File file = new File(fileName);
        return deleteFile(file);
    }

    /**
     * 删除文件或文件夹，如果是文件夹则删除目录下所有文件，包括子文件
     *
     * @param file
     */
    public static boolean delete(File file) {
        if(file == null)	return false;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
            return true;
        } else {
            System.out.println("FileUtil.deleteFileOrDirectory.FileNotFoundException :找不到"
                    + file.getPath() + "文件！");
            return false;
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param fileName
     */
    public static boolean delete(String fileName) {
        if(fileName == null || fileName.trim().length() == 0)	return false;
        File file = new File(fileName);
        return delete(file);
    }

    /**
     * 移动指定的文件（夹）到目标文件（夹）
     *
     * @param source
     *            源文件（夹）
     * @param target
     *            目标文件（夹）
     * @param isFolder
     *            若为文件夹，则为True；反之为False
     * @return
     * @throws Exception
     */
    public static boolean move(String source, String target, boolean isFolder)
            throws Exception {
        copy(source, target, isFolder);
        if (isFolder) {
            return delete(source);
        } else {
            return deleteFile(source);
        }
    }
}
