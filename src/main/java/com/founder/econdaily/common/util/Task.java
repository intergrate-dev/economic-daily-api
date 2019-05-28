package com.founder.econdaily.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Task implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private int i;
    private File inputFile;
    private ZipOutputStream zipOutputStream;

    public Task(File inputFile, ZipOutputStream zipOutputStream) {
        this.inputFile = inputFile;
        this.zipOutputStream = zipOutputStream;
    }

    public Task(int i) {
        this.i = i;
    }

    /*@Override
    public Integer call() throws Exception {
        // 替换成db的查询
        int sum = 0;
        for (int j = 0; j <= i; j++) {
            sum += j;
        }
        return sum;
    }*/

    @Override
    public Integer call() throws Exception {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            if (inputFile.exists()) { // 判断文件是否存在
                if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹
                    logger.info("zipOutputStream...., file enter .......", inputFile.getName());
                    // 创建输入流读取文件
                    fis = new FileInputStream(inputFile);
                    bis = new BufferedInputStream(fis);

                    // 将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
                    zipOutputStream.putNextEntry(ze);

                    // 写入文件的方法，同上
                    logger.info("zipOutputStream...., file : {}", inputFile.getName());
                    byte[] b = new byte[4094000];
                    int length = 0; //代表实际读取的字节数
                    while ((length = bis.read(b)) != -1) {
                        //length 代表实际读取的字节数
                        zipOutputStream.write(b, 0, length);
                    }
                    logger.info("zipOutputStream...., file : {}, end ..............", inputFile.getName());
                }
                /*else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            fileTransToZip(files[i], zipOutputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (bis != null) {
                            bis.close();
                        }
                        if (fis != null) {
                            fis.close();
                        }
                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
