package com.app.jueee.concurrency.chapter04.advanced;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *  获取一个周期性任务两次执行之间的时延。
 *	
 *	@author hzweiyongqiang
 */
public class Timer {

    /**
          *    该方法返回的是本次执行结束到下次执行开始之间的毫秒数。
     *	@return
     */
    public static long getPeriod() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        
        if ((hour >= 6) && (hour <=8)) {
            return TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        }
        
        if ((hour >= 13) && (hour <=14)) {
            return TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        }
        
        if ((hour >= 20) && (hour <=22)) {
            return TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        }
        
        return TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    }
}
