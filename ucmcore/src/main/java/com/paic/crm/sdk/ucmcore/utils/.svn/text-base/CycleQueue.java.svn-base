package com.paic.crm.sdk.ucmcore.utils;

import java.io.Serializable;

/**
 * Created by ex-lisuyang001 on 2017/4/29.
 * 循环队列
 */

public class CycleQueue<E> implements Serializable {

    /**
     * 数组下一个索引位置
     */
    private int index;
    /**
     * 循环队列的大小
     */
    private int length;
    /**
     * 是否队列已处于循环转态
     */
    private boolean cycle;
    /**
     * 存取具体数据的容器
     */
    private Object[] data;


    /**
     * 利用指定大小的值初始化循环队列容器
     *
     * @param length
     */
    public CycleQueue(int length) {
        this.length = length;
        data = new Object[length];
    }

    /**
     * 线程安全的存入最新值到容器中
     *
     * @param value
     */
    public synchronized void push(E value) {
        if (index >= length) {
            index = index - length;
            cycle = true;
        }
        data[index] = value;
        index++;
    }

    /**
     * 获取目前容器内的所有值
     * 注意当集合未满时，未存放值得地方会返回null
     *
     * @return
     */
    public Object[] get() {
        if (cycle) {
            Object[] truthData = new Object[length];
            int currentIndex = (index - 1);
            for (int i = (length - 1); i >= 0; i--) {
                if (currentIndex < 0) {
                    truthData[i] = data[currentIndex + length];
                } else {
                    truthData[i] = data[currentIndex];
                }
                currentIndex--;
            }
            return truthData;
        } else {
            return data;
        }

    }

    /**
     * 获取容器内的最新值
     *
     * @return the latest value
     */
    public E getCurrentValue() {
        int currentIndex = (index - 1);
        if (currentIndex < 0) currentIndex = 0;
        return (E) data[currentIndex];
    }

    @Override
    public String toString() {
        Object[] data = get();
        String res = "[ ";
        for (Object object : data) {
            res += object + ",";
        }
        res = res.substring(0, res.length() - 1);
        res += " ]";
        return res;
    }
}
