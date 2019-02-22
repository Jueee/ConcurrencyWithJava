package com.app.jueee.concurrency.chapter04.common;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *  缓存的项可通过 NewsWriter 类写入磁盘
 *	
 *	@author hzweiyongqiang
 */
public class NewsWriter implements Runnable{

    private NewsBuffer buffer;
    
    public NewsWriter(NewsBuffer buffer) {
        this.buffer = buffer;
    }
    
    /**
     *  从缓存中获取 CommonInformationItem 实例并且将其保存到磁盘。
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                CommonInformationItem item = buffer.get();
                Path path = Paths.get("output\\" + item.getFileName());
                
                try(BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE)){
                    fileWriter.write(item.toXML());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
