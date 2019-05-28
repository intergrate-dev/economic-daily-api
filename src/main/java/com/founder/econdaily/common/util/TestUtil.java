package com.founder.econdaily.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipOutputStream;

public class TestUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);

    public static void main(String[] args) {
        /*ExecutorService threadPool = Executors.newSingleThreadExecutor();
        List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
        for (int i = 10; i < 15; i++) {
            futures.add(threadPool.submit(new Task(i)));
        }
        try {
            Thread.sleep(1000);// 可能做一些事情

            int allSum = 0;
            for (Future<Integer> f : futures) {
                int fsum = f.get();
                System.out.println("sum:" + fsum);
                allSum += fsum;
            }
            System.out.println("allSum:" + allSum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();*/
        String source = "C:/Users/yuan-pc/Downloads/jjrb_2003-01-03/pdf/";
        String zip = "C:/Users/yuan-pc/Downloads/jjrb_2003-01-03/pdf-test.zip";
        InputStream fis = null;
        ZipOutputStream zos = null;
        ExecutorService threadPool = null;
        try {
            FileOutputStream fos = new FileOutputStream(zip);
            zos = new ZipOutputStream(fos);

            /* 循环读取文件路径集合，获取每一个文件的路径（将文件一个一个进行压缩） */
            for (String fp : new File(source).list()) {
                //this.fileTransToZip(new File(fp), zos);
                threadPool = Executors.newSingleThreadExecutor();
                threadPool.submit(new Task(new File(fp), zos));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (threadPool != null) {
                threadPool.shutdown();
            }
        }
    }


}
