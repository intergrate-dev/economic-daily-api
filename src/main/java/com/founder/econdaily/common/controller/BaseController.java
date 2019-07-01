package com.founder.econdaily.common.controller;

import com.founder.econdaily.common.util.CustomInputStreamSupplier;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.common.util.ScatterSample;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipMethod;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public String validErrorMsg(BindingResult validResult) {
        List<ObjectError> list = validResult.getAllErrors();
        for (ObjectError objectError : list) {
            logger.info(objectError.toString());
            ;
        }
        return RegxUtil.extractTargetBetweenSymbolRange(validResult.toString(), RegxUtil.REG_ZH, RegxUtil.REG_TX);
    }

    protected String extractPath(String code, String dateStr, String rootPath, Boolean isDelete) {
        return rootPath.concat(code).concat(RegxUtil.PATH_SPLIT).concat(RegxUtil.replaceAllToPath(dateStr, isDelete)).
                concat(RegxUtil.PATH_SPLIT);
    }

    //public void writeOutputWithChannel(InputStream in, OutputStream out, Boolean closeable) {
    public void writeOutputWithChannel(List<String> files, OutputStream out, Boolean closeable) {
        //String pathname = "C:\\Users\\adew\\Desktop\\jd-gui.cfg";
        FileInputStream fin = null;
        FileChannel channel = null;
        try {
            for (String file : files) {
                File f = new File(file);
                if (f.isDirectory()) {
                    File[] fileList = f.listFiles();
                    for (File file1 : fileList) {
                        fin = new FileInputStream(file1);
                        channel = fin.getChannel();

                        int capacity = 2048000;// 字节
                        ByteBuffer bf = ByteBuffer.allocate(capacity);
                        int length = -1;
                        while ((length = channel.read(bf)) != -1) {
                            bf.clear();
                            byte[] bytes = bf.array();
                            out.write(bytes, 0, length);
                        }
                    }
                } else {
                    fin = new FileInputStream(f);
                    channel = fin.getChannel();

                    int capacity = 2048000;// 字节
                    ByteBuffer bf = ByteBuffer.allocate(capacity);
                    int length = -1;
                    while ((length = channel.read(bf)) != -1) {
                        bf.clear();
                        byte[] bytes = bf.array();
                        out.write(bytes, 0, length);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (closeable && out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    public void recursion(String root, List<String> list) {
    public void recursion(String root, Map<String, List<String>> map) {
        List<String> pdfList = new ArrayList<String>();
        List<String> xmlList = new ArrayList<String>();
        List<String> contAttList = new ArrayList<String>();
        String path = root;
        File file = new File(root);
        if (file.exists()) {
            File[] subFile = file.listFiles();
            for (int i = 0; i < subFile.length; i++) {
                if (subFile[i].isDirectory()) {
                    recursion(subFile[i].getAbsolutePath(), map);
                } else {
                    String filename = subFile[i].getName();
                    if (filename.endsWith(RegxUtil.FILE_PDF)) {
                        pdfList.add(path + filename);
                    }
                    if (filename.endsWith(RegxUtil.FILE_XML)) {
                        xmlList.add(path + filename);
                    }
                    if (filename.endsWith(RegxUtil.FILE_JPG) || filename.endsWith(RegxUtil.FILE_DOC)) {
                        contAttList.add(path + filename);
                    }

                }
            }
        } else {
            logger.info("this file is not exit!");
        }
        map.put("pdfList", pdfList);
        map.put("xmlList", xmlList);
        map.put("contAttList", contAttList);
    }

    public void queryFilesAndZip(List<String> list, String zipFileName) throws IOException {
        File zip = new File(zipFileName);
        if (!zip.getParentFile().exists()) {
            zip.getParentFile().mkdir();
        }
        if (!zip.exists()) {
            zip.createNewFile();
        }

        /* 创建zip文件输出流 */
        ZipOutputStream zos = null;
        ExecutorService threadPool = null;
        try {
            FileOutputStream fos = new FileOutputStream(zip);
            zos = new ZipOutputStream(fos);

            /* 循环读取文件路径集合，获取每一个文件的路径（将文件一个一个进行压缩） */
            for (String fp : list) {
                this.fileTransToZip(new File(fp), zos);
                /*threadPool = Executors.newSingleThreadExecutor();
                threadPool.submit(new Task(new File(fp), zos));*/
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (threadPool != null) {
                threadPool.shutdown();
            }
        }
    }


    public void fileTransToZip(File inputFile, ZipOutputStream zipoutputStream) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            if (inputFile.exists()) { // 判断文件是否存在
                if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹

                    // 创建输入流读取文件
                    fis = new FileInputStream(inputFile);
                    bis = new BufferedInputStream(fis);

                    // 将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
                    zipoutputStream.putNextEntry(ze);

                    // 写入文件的方法，同上
                    byte[] b = new byte[4094000];
                    int length = 0; //代表实际读取的字节数
                    while ((length = bis.read(b)) != -1) {
                        //length 代表实际读取的字节数
                        zipoutputStream.write(b, 0, length);
                    }
                    //this.writeOutputWithChannel(new FileInputStream(inputFile), zipoutputStream, false);
                    /*final ScatterSample scatterSample = new ScatterSample(inputFile.getAbsolutePath());
                    addEntry(inputFile.getName(), inputFile, scatterSample);*/

                } else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            //zipFile(files[i], zipoutputStream);
                            fileTransToZip(files[i], zipoutputStream);
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createZipFile(final String rootPath, final File result) throws Exception {
        File dstFolder = new File(result.getParent());
        if (!dstFolder.isDirectory()) {
            dstFolder.mkdirs();
        }
        File rootDir = new File(rootPath);
        final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());
        compressCurrentDirectory(rootDir, scatterSample);
        final ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(result);
        scatterSample.writeTo(zipArchiveOutputStream);
        zipArchiveOutputStream.close();
    }

    private void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws IOException {
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
        //archiveEntry.setMethod(ZipEntry.DEFLATED);
        archiveEntry.setMethod(ZipMethod.STORED.getCode());
        //final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
        InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
        scatterSample.addEntry(archiveEntry, supp);
    }


    private void compressCurrentDirectory(File dir, ScatterSample scatterSample) throws IOException {
        if (dir == null) {
            throw new IOException("源路径不能为空！");
        }
        String relativePath = "";
        if (dir.isFile()) {
            relativePath = dir.getName();
            addEntry(relativePath, dir, scatterSample);
            return;
        }


        // 空文件夹
        if (dir.listFiles().length == 0) {
            relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "");
            addEntry(relativePath + File.separator, dir, scatterSample);
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                compressCurrentDirectory(f, scatterSample);
            } else {
                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");
                addEntry(relativePath + File.separator + f.getName(), f, scatterSample);
            }
        }
    }

}
