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
}
