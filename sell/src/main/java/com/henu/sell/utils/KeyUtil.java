package com.henu.sell.utils;

import java.util.Random;

public class KeyUtil {
    /*生成主键
    * 格式：时间+随机数
    * */
    /*synchronized 防止多线程情况下key的重复*/
    public static  synchronized String genUniqueKey(){
        Random random = new Random();

        Integer key=random.nextInt(900000)+100000;
        return System.currentTimeMillis()+String.valueOf(key);
    }
}
