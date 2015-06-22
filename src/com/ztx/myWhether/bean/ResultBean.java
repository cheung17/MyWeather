package com.ztx.myWhether.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultBean {
	/**
	 * temp :实时温度 
	 * tip :今日提示
	 * aqi:空气指数
	 */

	private String temp, tip,aqi;
	private List<ForecastBean> forecasts ;
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public List<ForecastBean> getForecasts() {
		return forecasts;
	}
	public void setForecasts(List<ForecastBean> forecasts) {
		this.forecasts = forecasts;
		
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
}
/*
 * private String todayTemp, todayTip, todayHighTemp, todayLowTemp, todayCond,
 * todayWind, todayWindDir, tomHighTemp, tomLowTemp, tomWind, tomCond,
 * tomWindDir, thirdHighTemp, thirdLowTemp, thirdWind, thirdCond, thirdWindDir,
 * foruthHighTemp, foruthLowTemp, foruthWind, foruthCond, foruthWindDir,
 * fifthHighTemp, fifthLowTemp, fifthWind, fifthCond, fifthWindDir,status;
 */