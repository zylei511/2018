package com.paic.crm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pingan001 on 16/3/21.
 */
public class H5InformationTypeContent implements Serializable{

    public String contactId;

    public String umid;

    public String weAppNo;

    public List<H5InformationData> data;
}
