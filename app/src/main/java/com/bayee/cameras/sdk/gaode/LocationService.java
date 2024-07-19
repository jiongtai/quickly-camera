package com.bayee.cameras.sdk.gaode;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationService implements AMapLocationListener {

    private AMapLocationClient mLocationClient;
    private OnLocationChangeListener mListener;

    public LocationService(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this);
    }

    public void startLocation() {
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(locationOption);
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                double altitude = aMapLocation.getAltitude();//海拔高度
                float bearing = aMapLocation.getBearing();//方位角
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
//                String country = aMapLocation.getCountry();//国家信息
                String province = aMapLocation.getProvince();//省信息
                String city = aMapLocation.getCity();//城市信息
                String district = aMapLocation.getDistrict();//城区信息
                String street = aMapLocation.getStreet();//街道信息
                String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
                String local = province + city + district + street + streetNum;
                mListener.onLocationChange(altitude, bearing, latitude, longitude, local, city, district, street);
            } else {
                mListener.onLocationError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
            }
        }
    }

    public void setOnLocationChangeListener(OnLocationChangeListener listener) {
        mListener = listener;
    }

    public interface OnLocationChangeListener {
        void onLocationChange(double altitude, float bearing ,double latitude, double longitude, String local, String city, String district, String street);

        void onLocationError(int errorCode, String errorMessage);
    }


}














