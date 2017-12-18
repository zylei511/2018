package com.paic.crm.PADsdk;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.paic.crm.utils.CRMLog;
import com.pingan.core.data.PADataAgent;
import com.pingan.core.data.engine.GPSLocation;


public class Location {
	
    //定位模式：LocationMode.Hight_Accuracy(高精度)、LocationMode.Battery_Saving(低功耗)、LocationMode.Device_Sensors(仅设备)
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    //坐标系：gcj02(国测局加密经纬度坐标)、bd09ll(百度经纬度标准)、bd09(百度墨卡托标准)
    private String tempcoor = "gcj02";
    //采集频率单位毫秒
    private int span = 120000; //>1000
    //是否需要地址信息
    private boolean isNeedAddr = false;
    
    
    //以下信息不需要配置
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private Context mcontext;
    
    
    public Location(Context context){
    	mcontext = context;
    	
    }
    
    public void GPSStart(){
    	
    	 mLocationClient = new LocationClient(mcontext);
         mMyLocationListener = new MyLocationListener();
         mLocationClient.registerLocationListener(mMyLocationListener);
    	
    	 initLocation();
    	 mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    	 
    }
    
    
    public void GPSStop(){
    	
	   
   	     mLocationClient.stop();
	
   }
    
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        
       
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(isNeedAddr);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }
    
    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            List<Poi> list = location.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
           // logMsg(sb.toString());
            CRMLog.LogInfo("BaiduLocationApiDem", sb.toString());
            
            //GPS信息写入数据库
             putGPSTodb(location);
        }
        
      
    }
    
    
    public void putGPSTodb(BDLocation location){
    	
    	GPSLocation gPSLocation = new GPSLocation();
    	gPSLocation.setTime(location.getTime());
    	gPSLocation.setAltitude(location.getAltitude());
    	gPSLocation.setLatitude(location.getLatitude());
    	gPSLocation.setLongitude(location.getLongitude());
    	
    	PADataAgent.onGPSLocation(gPSLocation);
    	
    }
}
