package com.paic.crm.entity;


import java.io.Serializable;
import java.util.List;

/**
 * Created by hanyh on 16/3/23.
 */
public class LoginData  implements Serializable{

    public LoginData() {
        super();
    }

    public String PAD_BIZ_SERIES;

    public List<LoginDataBizSeriesInfoListBean> bizSeriesInfoList;

    public LoginDataLoginUserBean loginUser;

    public String shiroKey;

    public String sjyToken;

    public String ucpPicPrifex;

    public long sjyTokenTime;

}
