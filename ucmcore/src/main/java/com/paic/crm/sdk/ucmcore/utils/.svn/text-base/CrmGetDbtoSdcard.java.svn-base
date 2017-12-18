package com.paic.crm.sdk.ucmcore.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ex-lisuyang001 on 17/4/26.
 * 用于测试使用
 */
public class CrmGetDbtoSdcard {
    /**
     * 将db文件复制到sd卡根目录
     *
     * @param context
     * @param name
     */
    public static void copyDBToSdcard(Context context, final String name) {
        final File dbFile = context.getDatabasePath(name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (dbFile != null && dbFile.exists()) {

                    String main = Environment.getExternalStorageDirectory().getPath() + File.separator + "crm_log";


                    try {
                        FileInputStream inputStream = new FileInputStream(dbFile);

                        //文件
                        String newName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + name;

                        //文件夹
                        File destDir = new File(main);
                        if (!destDir.exists()) {
                            destDir.mkdirs();//在根创建了文件夹
                        }

                        File newDb = new File(destDir, newName);

                        if (newDb.exists()) {
                            newDb.delete();
                        }
                        newDb.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(newDb);

                        byte[] buff = new byte[1024];

                        while (inputStream.read(buff) != -1) {
                            outputStream.write(buff);
                        }
                        inputStream.close();
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
