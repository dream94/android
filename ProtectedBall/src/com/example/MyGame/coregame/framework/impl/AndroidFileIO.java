package com.example.MyGame.coregame.framework.impl;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.example.MyGame.coregame.framework.FileIO;

import java.io.*;

/**
 * Created by Admin on 2015/4/7.
 */
public class AndroidFileIO implements FileIO {
    private AssetManager assetManager;
    private String externalStoragePath;

    public AndroidFileIO(AssetManager assetManager) {
        this.assetManager = assetManager;
        externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
        Log.i("huang", "file address: " + externalStoragePath);
    }

    @Override
    public InputStream readAsset(String filename) {      //获取读取asset目录下文件的输入流
        try {
            return assetManager.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream readFile(String filename) {
        try {
            return new FileInputStream(externalStoragePath+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OutputStream writeFile(String filename) {
        try {
            return new FileOutputStream(externalStoragePath+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
