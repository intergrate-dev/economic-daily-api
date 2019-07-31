package com.founder.econdaily.common.controller;

import com.founder.econdaily.common.util.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipMethod;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletResponse;
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
            logger.info("---------------------- validErrorMsg: {} -----------------------", objectError.toString());
        }
        return RegxUtil.extractTargetBetweenSymbolRange(validResult.toString(), RegxUtil.REG_ZH, RegxUtil.REG_TX);
    }

    protected String extractPath(String code, String dateStr, String rootPath, Boolean isDelete) {
        return rootPath.concat(code).concat(RegxUtil.PATH_SPLIT).concat(RegxUtil.replaceAllToPath(dateStr, isDelete)).
                concat(RegxUtil.PATH_SPLIT);
    }

    public void writeOutputWithChannel(List<String> files, String serverPath, HttpServletResponse response, Boolean closeable, String fileName) {
        if (files == null || files.size() == 0) {
            logger.info("========================== writeOutputWithChannel error: files is empty =========================");
            return;
        }
        long time1 = System.currentTimeMillis();
        FileInputStream fin = null;
        FileChannel channel = null;
        OutputStream out = null;
        String tempZip = serverPath.concat(System.currentTimeMillis() + ".zip");
        try {
            out = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempZip));
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = null;
            try {
                for (String file : files) {
                    FileInputStream fis = new FileInputStream(file);
                    ze = new ZipEntry(file.substring(file.lastIndexOf(RegxUtil.PATH_SPLIT) + 1));
                    zos.putNextEntry(ze);
                    this.copyWithChannel(fis.getChannel(), zos);
                }
                zos.flush();
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment;filename=".concat(fileName));
            fin = new FileInputStream(tempZip);
            this.copyWithChannel(fin.getChannel(), out);
            new File(tempZip).delete();
            logger.info("------------------- delete tempZipFle: {} from disk -------------------------", tempZip);
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
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("==================  writeOutputWithChannel take time: {} ===================", System.currentTimeMillis() - time1);
    }

    private void copyWithChannel(FileChannel channel, OutputStream out) throws IOException {
        int capacity = 4096000;
        ByteBuffer bf = ByteBuffer.allocate(capacity);
        int length = -1;
        while ((length = channel.read(bf)) != -1) {
            bf.clear();
            byte[] bytes = bf.array();
            out.write(bytes, 0, length);
        }
    }

    public void recursion(String root, Map<String, List<String>> map) throws CustomException {
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
            logger.info("============================== this file: {} is not exist! ================", file);
            throw new CustomException(-1, "file path: ".concat(file.getPath()).concat(" is not exist"));
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
                /*threadPool = Executors.newSingleThreadExecutor();
                threadPool.submit(new Task(new File(fp), zos));*/
                this.fileTransToZip(new File(fp), zos);
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
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    fis = new FileInputStream(inputFile);
                    bis = new BufferedInputStream(fis);

                    ZipEntry ze = new ZipEntry(inputFile.getName());
                    zipoutputStream.putNextEntry(ze);

                    byte[] b = new byte[4096000];
                    int length = 0;
                    while ((length = bis.read(b)) != -1) {
                        zipoutputStream.write(b, 0, length);
                    }
                } else {
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

    public void createZipFile(final String rootPath, String date, final File result) throws Exception {
        File dstFolder = new File(result.getParent());
        if (!dstFolder.isDirectory()) {
            dstFolder.mkdirs();
        }
        File rootDir = new File(rootPath);
        final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());
        this.compressCurrentDirectory(rootDir, scatterSample);

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
        String relativePath = null;
        if (dir.isFile()) {
            relativePath = dir.getName();
            this.addEntry(relativePath, dir, scatterSample);
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "");
            this.addEntry(relativePath + File.separator, dir, scatterSample);
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                compressCurrentDirectory(f, scatterSample);
            } else {
                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");
                this.addEntry(relativePath + File.separator + f.getName(), f, scatterSample);
            }
        }
    }

}
