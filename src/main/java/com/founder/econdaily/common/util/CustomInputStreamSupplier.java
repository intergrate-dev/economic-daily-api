package com.founder.econdaily.common.util;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CustomInputStreamSupplier implements InputStreamSupplier {
    private File currentFile;


    public CustomInputStreamSupplier(File currentFile) {
        this.currentFile = currentFile;
    }


    @Override
    public InputStream get() {
        try {
            // InputStreamSupplier api says:
            // 返回值：输入流。永远不能为Null,但可以是一个空的流
            return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
