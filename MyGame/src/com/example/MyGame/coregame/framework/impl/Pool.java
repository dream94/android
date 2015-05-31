package com.example.MyGame.coregame.framework.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public class Pool<T> {
    public interface PoolObjectFactory<T>{
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(int maxSize, PoolObjectFactory<T> factory) {
        this.maxSize = maxSize;
        this.factory = factory;
        freeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject(){
        T object = null;
        if(freeObjects.size() == 0){
            object = factory.createObject();                //创建一个元素,在其他地方实现其实是创建一个实例
        }else{
            object = freeObjects.remove(freeObjects.size()-1);    //移走最后一个元素
        }
        return object;
    }

    public void addfreeObject(T object){
        if(freeObjects.size()<maxSize){
            freeObjects.add(object);
        }
    }
}
