package com.ztx.myWhether.JsonTool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ztx.myWhether.bean.ForecastBean;
import com.ztx.myWhether.bean.ResultBean;

public class JsonTool {
	static ResultBean resultBean = new ResultBean();
    static List<ForecastBean> forecastList=new ArrayList<ForecastBean>();
	public static ResultBean analyJson(String jsonSoruce) {
		try {
			if(forecastList!=null){
				forecastList.clear();//一定要清空！！！
			}
			
			JSONObject jsonObject = new JSONObject(jsonSoruce);
			String status = jsonObject.getString("status");// 取出状态码
			JSONObject jsonData = jsonObject.getJSONObject("data");  //data对象
			resultBean.setTemp(jsonData.getString("wendu")); // 今日温度
			resultBean.setTip(jsonData.getString("ganmao"));//今日提示
			/**
			 * 预测数组：存放在list集合中
			 */
			JSONArray  forecastArr=jsonData.getJSONArray("forecast"); 
			ForecastBean forecastBean=null;
			for (int i = 0; i < forecastArr.length(); i++) {
				forecastBean=new ForecastBean();
				JSONObject sonOfFore=forecastArr.getJSONObject(i);
				forecastBean.setWindDir(sonOfFore.getString("fengxiang"));
				forecastBean.setWind(sonOfFore.getString("fengli"));
				forecastBean.setDate(sonOfFore.getString("date"));
				forecastBean.setWeaType(sonOfFore.getString("type"));
				forecastBean.setHighTemp(sonOfFore.getString("high"));
				forecastBean.setLowTemp(sonOfFore.getString("low"));					
				forecastList.add(forecastBean);				
			}
			resultBean.setForecasts(forecastList);	
			if(jsonData.has("aqi")){
				resultBean.setAqi(jsonData.getString("aqi"));
			}else {
				resultBean.setAqi("");
			}
			
			/*System.out.println(resultBean.getAqi());
			System.out.println(resultBean.getTemp());
			System.out.println(resultBean.getTip());*/
		} catch (JSONException e) {
			e.printStackTrace();
		}   
		return resultBean;
	}
}
