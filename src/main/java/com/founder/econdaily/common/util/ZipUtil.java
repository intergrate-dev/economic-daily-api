package com.founder.econdaily.common.util;

import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.modules.historySource.controller.HistorySourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private static final int BUFFER_SIZE = 1000 * 1024;

    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         <p>
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)

            throws RuntimeException {


        long start = System.currentTimeMillis();

        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            File sourceFile = new File(srcDir);

            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) + " ms");

        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils", e);

        } finally {

            if (zos != null) {

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }


    }


    /**
     * 压缩成ZIP 方法2
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */

    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {

        long start = System.currentTimeMillis();

        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            for (File srcFile : srcFiles) {

                byte[] buf = new byte[BUFFER_SIZE];

                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int len;

                FileInputStream in = new FileInputStream(srcFile);

                while ((len = in.read(buf)) != -1) {

                    zos.write(buf, 0, len);

                }

                zos.closeEntry();

                in.close();

            }

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) + " ms");

        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils", e);

        } finally {

            if (zos != null) {

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         <p>
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name,

                                 boolean KeepDirStructure) throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];

        if (sourceFile.isFile()) {

            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字

            zos.putNextEntry(new ZipEntry(name));

            // copy文件到zip输出流中

            int len;

            FileInputStream in = new FileInputStream(sourceFile);

            while ((len = in.read(buf)) != -1) {

                zos.write(buf, 0, len);

            }

            // Complete the entry

            zos.closeEntry();

            in.close();

        } else {

            File[] listFiles = sourceFile.listFiles();

            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);

                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }

        }

    }

    public static void main(String[] args) throws IOException {
        /*long time_1 = System.currentTimeMillis();
        FileOutputStream fos1 = new FileOutputStream(new File("D:/z-files/paper/z-test.zip"));
        ZipUtil.toZip("D:/z-files/paper/z-test/", fos1, true);
        long time_2 = System.currentTimeMillis();
        logger.info("doCompress take time in total: {}", time_2 - time_1);*/


            ThreadUtil cf = new ThreadUtil("D:/720.txt","D:/test/1.txt");
            ThreadUtil cf2 = new ThreadUtil("D:/721.txt","D:/test/2.txt");
            ThreadUtil cf3 = new ThreadUtil("D:/123.txt","D:/test/3.txt");
            cf.start();
            cf2.start();
             cf3.start();
    }

}