package com.paic.crm.utils;

import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.NewMessageBean;

import java.util.Comparator;

/**
 *自定义的集合比较器
 * 
 * @author yueshaojun
 * 
 */
public class UCMComparator implements Comparator {


    @Override
    public int compare(Object lhs, Object rhs) {
        NewMessageBean msg1=(NewMessageBean)lhs;
        NewMessageBean msg2=(NewMessageBean)rhs;
        if(msg1!=null&msg2!=null) {
//            String time1 = DateFormatUtil.format(msg1.customMsgContent.sendTime, DateFormatUtil.YYYY_MM_DD2_HH_MM_SS);
//            String time2 = DateFormatUtil.format(msg2.customMsgContent.sendTime, DateFormatUtil.YYYY_MM_DD2_HH_MM_SS);
            return msg1.customMsgContent.createTime.compareTo(msg2.customMsgContent.createTime);
        }
        return 1;
    }
}
