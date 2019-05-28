package com.founder.enondaily.multithread;

import com.founder.econdaily.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author JYB
 * @date 2018.11
 */
public class MultiThread {
    private static final Logger logger = LoggerFactory.getLogger(MultiThread.class);

    public static String srcFilePath = "C:/Users/yuan-pc/Downloads/jjrb_2003-01-03.zip";
    public static String destFilePath = "C:/Users/yuan-pc/Downloads/";
    public static String zipFilePath = "C:/Users/yuan-pc/Downloads/";

   /* String source = "C:/Users/yuan-pc/Downloads/jjrb_2003-01-03/pdf/";
    String zip = "C:/Users/yuan-pc/Downloads/jjrb_2003-01-03/pdf-test.zip";*/

    public static void main(String[] args) throws IOException {
        logger.info("source zipFile size: {}", new File(srcFilePath).length());
        destFilePath = destFilePath + "test_" + System.currentTimeMillis() + ".xml";
        zipFilePath = zipFilePath + "test_" + System.currentTimeMillis() + ".zip";
        //UpLoadFile uf = new UpLoadFile(srcFilePath,destFilePath);
        ZipFile zf = new ZipFile(srcFilePath, zipFilePath);
        //Thread th1 = new Thread(uf);
        Thread th2 = new Thread(zf);
        //th1.start();
        th2.start();
    }


}
