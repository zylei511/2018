package com.paic.crm.sdk.ucmcore.crash;

/**
 * Created by yueshaojun on 17/3/1.
 */

public class ExceptionThreadInfoCollector {
    public static String colletTreadInfo(Thread exceptionThread){
        StringBuffer threadInfo = new StringBuffer();
        threadInfo.append("threadId:").append(exceptionThread.getId()+"").append("\n")
                .append("threadName:").append(exceptionThread.getName()).append("\n")
                .append("threadPriority:").append(exceptionThread.getPriority()+"").append("\n")
                .append("threadState:").append(exceptionThread.getState().toString()).append("\n")
                .append("threadIsAlive:").append(exceptionThread.isAlive()+"").append("\n")
                .append("threadsGroup:").append(exceptionThread.getThreadGroup().getName()).append("\n");
        return  threadInfo.toString();
    }
}
