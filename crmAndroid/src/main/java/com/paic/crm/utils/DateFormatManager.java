/*
 * 文件名: DateFormatManager
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: [常量类]
 * 创建人: LiJinHua
 * 创建时间:  2014-3-22
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.paic.crm.utils;



/**
 * DateFormatManager时间日期格式
 * @author hanyh
 *
 */
public interface DateFormatManager {
    
    enum Model{
        /** 聊天列表 **/
        ChatList,
        /** 聊天消息 **/
        ChatMessage,
        /** 朋友圈 **/
        FriendCircle

    }
    
    enum HoursFormat{
        H12,
        H24
    }
    
    String format(long milliseconds);
    
    String format(long milliseconds, HoursFormat hoursFormat);
    
    String formatFirstMessage(long milliseconds, HoursFormat hoursFormat);
    
    public static class Factory {

        public static DateFormatManager create(Model model) {
            if(model == Model.ChatList){
                return new DateFormatChatListImpl();
            }else if(model == Model.ChatMessage){
                return new DateFormatChatMessageImpl();
            }else{
                return new DateFormatFriendCircleImpl();
            }
        }
        
        private static String getTimeQuantumName(DateFormatUtil.TimeQuantum timeQuantum){
            String name = "";
            if(timeQuantum == DateFormatUtil.TimeQuantum.wee_hours){
                name = "凌晨";
            }else if(timeQuantum == DateFormatUtil.TimeQuantum.forenoon){
                name = "上午";
            }else if(timeQuantum == DateFormatUtil.TimeQuantum.nooning){
                name = "中午";
            }else if(timeQuantum == DateFormatUtil.TimeQuantum.afternoon){
                name = "下午";
            }else{
                name = "晚上";
            }
            return name;
        }
        private static class DateFormatChatListImpl implements DateFormatManager {
            
            private HoursFormat defHoursFormat = HoursFormat.H24;
            
            public String format(long milliseconds) {
                return format(milliseconds,defHoursFormat);
            }

            public String format(long milliseconds, HoursFormat hoursFormat) {
                DateFormatUtil. TimeName tName = DateFormatUtil.compareTime(milliseconds);
                String timeQuantumName = getTimeQuantumName(DateFormatUtil.getTimeQuantum(milliseconds));
                if(tName ==DateFormatUtil. TimeName.today){
                    if(hoursFormat == HoursFormat.H12){
                        return timeQuantumName + DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                    }
                }else if(tName == DateFormatUtil.TimeName.yesterday){
                    return "昨天";
                }else if(tName == DateFormatUtil.TimeName.the_day_before_yesterday){
                    return "前天";
                }else if(tName == DateFormatUtil.TimeName.the_day_before_yesterday_more || tName ==DateFormatUtil. TimeName.last_month || tName == DateFormatUtil.TimeName.last_month_more){
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.MM_DD);
                }else if(tName == DateFormatUtil.TimeName.last_year || tName == DateFormatUtil.TimeName.last_year_more){
                    //去年或去年以前
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD);
                }else{
                    //如果是未来的时间
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD_HH_MM);
                }
            }

            public String formatFirstMessage(long milliseconds, HoursFormat hoursFormat) {
                return format(milliseconds,hoursFormat);
            }
        }
        
        private static class DateFormatChatMessageImpl implements DateFormatManager {

            private HoursFormat defHoursFormat = HoursFormat.H24;
            
            public String format(long milliseconds) {
                return format(milliseconds,defHoursFormat);
            }
            
            public String format(long milliseconds, HoursFormat hoursFormat) {
                DateFormatUtil. TimeName tName = DateFormatUtil.compareTime(milliseconds);
                String timeQuantumName = getTimeQuantumName(DateFormatUtil.getTimeQuantum(milliseconds));
                if(tName == DateFormatUtil.TimeName.today){
                    if(hoursFormat == HoursFormat.H12){
                        return timeQuantumName + DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                    }
                }else if(tName == DateFormatUtil.TimeName.yesterday){
                    if(hoursFormat == HoursFormat.H12){
                        return "昨天"+timeQuantumName+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return "昨天"+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                    }
                }else if(tName == DateFormatUtil.TimeName.the_day_before_yesterday){
                    if(hoursFormat == HoursFormat.H12){
                        return "前天"+timeQuantumName+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return "前天"+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                    }
                }else if(tName == DateFormatUtil.TimeName.the_day_before_yesterday_more || tName == DateFormatUtil.TimeName.last_month || tName ==DateFormatUtil. TimeName.last_month_more){
                    if(hoursFormat == HoursFormat.H12){
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.MM_DD2)+timeQuantumName+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.MM_DD2_HH_MM);
                    }
                }else if(tName == DateFormatUtil.TimeName.last_year || tName ==DateFormatUtil. TimeName.last_year_more){
                    //去年或去年以前
                    if(hoursFormat == HoursFormat.H12){
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2)+timeQuantumName+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                    }else{
                        return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2_HH_MM);
                    }
                }else{
                    //如果是未来的时间
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD_HH_MM);
                }
            }

            public String formatFirstMessage(long milliseconds, HoursFormat hoursFormat) {
                //去年或去年以前
                String timeQuantumName = getTimeQuantumName(DateFormatUtil.getTimeQuantum(milliseconds));
                if(hoursFormat == HoursFormat.H12){
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2)+timeQuantumName+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_12);
                }else{
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2_HH_MM);
                }
            
            }
        }
        
        private static class DateFormatFriendCircleImpl implements DateFormatManager {
            
            private HoursFormat defHoursFormat = HoursFormat.H24;
            
            public String format(long milliseconds) {
                return format(milliseconds,defHoursFormat);
            }

            public String format(long milliseconds, HoursFormat hoursFormat) {
                DateFormatUtil. TimeName tName = DateFormatUtil.compareTime(milliseconds);
                if(tName == DateFormatUtil.TimeName.today){
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                }else if(tName == DateFormatUtil.TimeName.yesterday){
                    return "昨天"+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                }else if(tName == DateFormatUtil.TimeName.the_day_before_yesterday){
                    return "前天"+DateFormatUtil.format(milliseconds, DateFormatUtil.HH_MM_24);
                }else if(tName ==DateFormatUtil. TimeName.the_day_before_yesterday_more || tName == DateFormatUtil.TimeName.last_month || tName ==DateFormatUtil. TimeName.last_month_more){
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.MM_DD2);
                }else if(tName == DateFormatUtil.TimeName.last_year || tName ==DateFormatUtil. TimeName.last_year_more){
                    //去年或去年以前
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2);
                }else{
                    //如果是未来的时间
                    return DateFormatUtil.format(milliseconds, DateFormatUtil.YYYY_MM_DD2);
                }
            }

            public String formatFirstMessage(long milliseconds, HoursFormat hoursFormat) {
                return format(milliseconds,hoursFormat);
            }
        }
    }
}
