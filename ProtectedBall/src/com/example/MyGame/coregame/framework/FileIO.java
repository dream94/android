package com.example.MyGame.coregame.framework;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 2015/4/7.
 */
public interface FileIO {
    public InputStream readAsset(String filename);
    public InputStream readFile(String filename);
    public OutputStream writeFile(String filename);
}
