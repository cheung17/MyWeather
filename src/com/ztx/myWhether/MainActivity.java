package com.ztx.myWhether;

import java.util.ArrayList; 
import java.util.List;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ztx.myWhether.JsonTool.JsonTool;
import com.ztx.myWhether.bean.ForecastBean;
import com.ztx.myWhether.bean.ResultBean;
import com.ztx.myWhether.httpUtil.HttpUtil;

public class MainActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	Handler handler = null;
	String result = "";
	private ViewPager viewPager;
	List<View> viewList = new ArrayList<View>();
	private PagerAdapter pagerAdapter = null;
	private TextView vpTab0, vpTab1, vpTab2;
	private TextView tabTx1, tabTx2, tabTx3, tabTx4, tabTx5, curCityTx;
	private TextView curTempTx, temDiffTx, weaTypeTx, tipTx;
	private TextView windInfoTx, aqiDataTx, aqiInfoTx;
	private ImageView aqiImg;
	private ImageView whetherTypeImg,whetherTypeImg1,whetherTypeImg2;
	private TextView curTempTx1, temDiffTx1, weaTypeTx1, tipTx1;
	private TextView windInfoTx1, aqiDataTx1, aqiInfoTx1;
	private ImageView aqiImg1;
	private TextView curTempTx2, temDiffTx2, weaTypeTx2, tipTx2;
	private TextView windInfoTx2, aqiDataTx2, aqiInfoTx2;
	private ImageView aqiImg2;
	static String firCityId, secCityId, thirdCityId;
	String fir_day, sec_day, thi_day, forth_day, fif_day;
	ResultBean resultBean = null;
	private SharedPreferences jsonSf = null;
	static SharedPreferences citySf = null;
	private LinearLayout linearCity;
	private Editor editor = null;
	List<ForecastBean> forecasts = null;
	private String firCityName;
	private String thirdCityName;
	private String secCityName;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		setContentView(R.layout.activity_main);
		initViews();
		initEvents();
	}

	private void initEvents() {
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
		curCityTx.setText(firCityName);
		beginQuery(firCityId, 0);

	}

	private void initViews() {
		handler = new Handler();
		
		
		View view1 = View.inflate(getApplicationContext(), R.layout.center1,
				null);
		View view2 = View.inflate(getApplicationContext(), R.layout.center2,
				null);
		View view3 = View.inflate(getApplicationContext(), R.layout.center3,
				null);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		initPagetAdapter();// 初始化适配器
		viewPager = (ViewPager) findViewById(R.id.vp);
		CloseAllActivities.getInstance().addActivity(this);
		readCity();
		tabTx1 = (TextView) findViewById(R.id.first_tab);
		tabTx2 = (TextView) findViewById(R.id.sec_tab);
		tabTx3 = (TextView) findViewById(R.id.third_tab);
		tabTx4 = (TextView) findViewById(R.id.four_tab);
		tabTx5 = (TextView) findViewById(R.id.fif_tab);
		whetherTypeImg=(ImageView) findViewById(R.id.type_img);
		whetherTypeImg1=(ImageView) findViewById(R.id.type_img1);
		whetherTypeImg2=(ImageView) findViewById(R.id.type_img2);
		curCityTx = (TextView) findViewById(R.id.curret_city);
		linearCity = (LinearLayout) findViewById(R.id.liner_city);
		// linearCity=findViewById(id)
		//
		tabTx1.setOnClickListener(this);
		tabTx2.setOnClickListener(this);
		tabTx3.setOnClickListener(this);
		tabTx4.setOnClickListener(this);
		tabTx5.setOnClickListener(this);
		linearCity.setOnClickListener(this);
		// curCityTx.setOnClickListener(this);
		vpTab0 = (TextView) findViewById(R.id.vp1);
		vpTab1 = (TextView) findViewById(R.id.vp2);
		vpTab2 = (TextView) findViewById(R.id.vp3);

	}

	private void initPagetAdapter() { // 初始化Viewpager适配器
		pagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return viewList.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				container.addView(viewList.get(position));
				initVp1Views();
				initVp2Views();
				initVp3Views();
				/**
				 * 设置page为某一页
				 */
				
				return viewList.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList.get(position));
			}
		};
	}

	private void beginQuery(final String city, final int tabPage) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				jsonSf = getSharedPreferences(city, MODE_PRIVATE);
				if (isNetworkAvailable(MainActivity.this)) {
					result = HttpUtil.getJson(city); // 拿到json数据
				if (result.contains("1000")) {  //返回的json当中status为1000时 ，查询正常，否则无效
						editor = jsonSf.edit();
						editor.putString(city, result);
						editor.commit();
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"暂不支持此城市查询", Toast.LENGTH_LONG).show();
							}
						});

						return;
					}

				} else {
					
					result = jsonSf.getString(city, "1");
					if (result.equals("1")) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"网络不可用", Toast.LENGTH_LONG).show();
							}
						});
						return;
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"网络不可用,无法及时更新数据", Toast.LENGTH_LONG).show();
						}
					});

				}
				resultBean = JsonTool.analyJson(result);// 拿到保存了解析后的json数据
				forecasts = resultBean.getForecasts();
				initDate(forecasts);// 初始化日期
				handler.post(new Runnable() {
					public void run() {
						setTabTitle();// 设置tab内容先
						setResult2View(tabPage, 0);
					}
				});
			}

		}).start();
	}

	private void initDate(List<ForecastBean> forecasts) {
		ForecastBean fBean0 = forecasts.get(0);
		fir_day = fBean0.getDate();
		fir_day = subDate(fir_day);// 截取日期
		ForecastBean fBean1 = forecasts.get(1);
		sec_day = fBean1.getDate();
		sec_day = subDate(sec_day);// 截取日期
		ForecastBean fBean2 = forecasts.get(2);
		thi_day = fBean2.getDate();
		thi_day = subDate(thi_day);// 截取日期
		ForecastBean fBean3 = forecasts.get(3);
		forth_day = fBean3.getDate();
		forth_day = subDate(forth_day);// 截取日期
		ForecastBean fBean4 = forecasts.get(4);
		fif_day = fBean4.getDate();
		fif_day = subDate(fif_day);// 截取日期

	}

	private String subDate(String date) { // 截取日期
		date = date.substring(date.indexOf("星"), date.length());
		return date;
	}
           /**
            * 
            * @param tabPage   viewpager页码
            * @param i         
            */
	private void setResult2View(int tabPage, int i) {
		if (tabPage == 0) {
			if (i == 0) {
				tipTx.setText(resultBean.getTip()); // 当日独有
				curTempTx.setText(resultBean.getTemp() + "°");// 当前温度 //当日独有
				if (!resultBean.getAqi().equals("")) { // 空气质量，可能没有 //当日独有
					aqiDataTx.setText(resultBean.getAqi() + " ");
					int aqi = Integer.parseInt(resultBean.getAqi());
					judgeAqi(aqi);// 判断天气质量
				} else {
					aqiDataTx.setText("暂无");
					aqiInfoTx.setText("");
				}
			} else {
				tipTx.setText("抱歉！由于气象台只提供了次日及以后的风力指数，所以此处暂无数据");
				aqiDataTx.setText("暂无");
				aqiInfoTx.setText("");
				curTempTx.setText("");
			}
			ForecastBean fBean = forecasts.get(i);
			windInfoTx.setText(fBean.getWind() + "-" + fBean.getWindDir()); // 风力
			String weaTypeStr=fBean.getWeaType();
			weaTypeTx.setText(weaTypeStr);
			
			String tempLow = fBean.getLowTemp();
			String tempHigh = fBean.getHighTemp();
			tempHigh = tempHigh.substring(tempHigh.indexOf("温") + 1,
					tempHigh.length() - 1);
			tempLow = tempLow.substring(tempLow.indexOf("温") + 1,
					tempLow.length() - 1);
			temDiffTx.setText(tempLow + "°~" + tempHigh + "°");
		} else if (tabPage == 1) {
			if (i == 0) {
				tipTx1.setText(resultBean.getTip()); // 当日独有
				curTempTx1.setText(resultBean.getTemp() + "°");// 当前温度 //当日独有
				if (!resultBean.getAqi().equals("")) { // 空气质量，可能没有 //当日独有
					aqiDataTx1.setText(resultBean.getAqi() + " ");
					int aqi = Integer.parseInt(resultBean.getAqi());
					judgeAqi1(aqi);// 判断天气质量
				} else {
					aqiDataTx1.setText("暂无");
					aqiInfoTx1.setText("");
				}
			} else {
				tipTx1.setText("抱歉！由于气象台只提供了次日及以后的风力指数，所以此处暂无数据");
				aqiDataTx1.setText("暂无");
				aqiInfoTx1.setText("");
				curTempTx1.setText("");
			}
			ForecastBean fBean = forecasts.get(i);
			windInfoTx1.setText(fBean.getWind() + "-" + fBean.getWindDir()); // 风力
			weaTypeTx1.setText(fBean.getWeaType());
			String tempLow = fBean.getLowTemp();
			String tempHigh = fBean.getHighTemp();
			tempHigh = tempHigh.substring(tempHigh.indexOf("温") + 1,
					tempHigh.length() - 1);
			tempLow = tempLow.substring(tempLow.indexOf("温") + 1,
					tempLow.length() - 1);
			temDiffTx1.setText(tempLow + "°~" + tempHigh + "°");

		} else if (tabPage == 2) {
			if (i == 0) {
				tipTx2.setText(resultBean.getTip()); // 当日独有
				curTempTx2.setText(resultBean.getTemp() + "°");// 当前温度 //当日独有
				if (!resultBean.getAqi().equals("")) { // 空气质量，可能没有 //当日独有
					aqiDataTx2.setText(resultBean.getAqi() + " ");
					int aqi = Integer.parseInt(resultBean.getAqi());
					judgeAqi2(aqi);// 判断天气质量
				} else {
					aqiDataTx2.setText("暂无");
					aqiInfoTx2.setText("");
				}
			} else {
				tipTx2.setText("抱歉！由于气象台只提供了次日及以后的风力指数，所以此处暂无数据");
				aqiDataTx2.setText("暂无");
				aqiInfoTx2.setText("");
				curTempTx2.setText("");
			}
			ForecastBean fBean = forecasts.get(i);
			windInfoTx2.setText(fBean.getWind() + "-" + fBean.getWindDir()); // 风力
			weaTypeTx2.setText(fBean.getWeaType());
			String tempLow = fBean.getLowTemp();
			String tempHigh = fBean.getHighTemp();
			tempHigh = tempHigh.substring(tempHigh.indexOf("温") + 1,
					tempHigh.length() - 1);
			tempLow = tempLow.substring(tempLow.indexOf("温") + 1,
					tempLow.length() - 1);
			temDiffTx2.setText(tempLow + "°~" + tempHigh + "°");
		}
	}

	private void judgeAqi(int aqi) {
		if (aqi <= 50) {
			aqiInfoTx.setText("空气优致");
			aqiImg.setImageResource(R.drawable.air1);
		} else if (aqi <= 100) {
			aqiInfoTx.setText("空气良好");
			aqiImg.setImageResource(R.drawable.air2);
		} else if (aqi <= 150) {
			aqiInfoTx.setText("轻度污染");
			aqiImg.setImageResource(R.drawable.air3);
		} else if (aqi <= 200) {
			aqiInfoTx.setText("中度污染");
			aqiImg.setImageResource(R.drawable.air4);
		} else if (aqi <= 300) {
			aqiInfoTx.setText("重度污染");
			aqiImg.setImageResource(R.drawable.air5);
		} else {
			aqiInfoTx.setText("严重污染");
			aqiImg.setImageResource(R.drawable.air6);
		}

	}

	private void judgeAqi1(int aqi) {
		if (aqi <= 50) {
			aqiInfoTx1.setText("空气优致");
			aqiImg1.setImageResource(R.drawable.air1);
		} else if (aqi <= 100) {
			aqiInfoTx1.setText("空气良好");
			aqiImg1.setImageResource(R.drawable.air2);
		} else if (aqi <= 150) {
			aqiInfoTx1.setText("轻度污染");
			aqiImg1.setImageResource(R.drawable.air3);
		} else if (aqi <= 200) {
			aqiInfoTx1.setText("中度污染");
			aqiImg1.setImageResource(R.drawable.air4);
		} else if (aqi <= 300) {
			aqiInfoTx1.setText("重度污染");
			aqiImg1.setImageResource(R.drawable.air5);
		} else {
			aqiInfoTx1.setText("严重污染");
			aqiImg1.setImageResource(R.drawable.air6);
		}

	}

	private void judgeAqi2(int aqi) {
		if (aqi <= 50) {
			aqiInfoTx2.setText("空气优致");
			aqiImg2.setImageResource(R.drawable.air1);
		} else if (aqi <= 100) {
			aqiInfoTx2.setText("空气良好");
			aqiImg2.setImageResource(R.drawable.air2);
		} else if (aqi <= 150) {
			aqiInfoTx2.setText("轻度污染");
			aqiImg2.setImageResource(R.drawable.air3);
		} else if (aqi <= 200) {
			aqiInfoTx2.setText("中度污染");
			aqiImg2.setImageResource(R.drawable.air4);
		} else if (aqi <= 300) {
			aqiInfoTx2.setText("重度污染");
			aqiImg.setImageResource(R.drawable.air5);
		} else {
			aqiInfoTx2.setText("严重污染");
			aqiImg2.setImageResource(R.drawable.air6);
		}

	}

	private void setTabTitle() { // 设置tab标题

		tabTx1.setText(fir_day);
		tabTx2.setText(sec_day);
		tabTx3.setText(thi_day);
		tabTx4.setText(forth_day);
		tabTx5.setText(fif_day);

	}

	@Override
	public void onClick(View v) {
		int curPage = viewPager.getCurrentItem();
		switch (v.getId()) {
		case R.id.first_tab:
			resetBgColor();

			tabTx1.setBackgroundColor(Color.parseColor("#00000000"));
			setResult2View(curPage, 0);

			break;
		case R.id.sec_tab:
			resetBgColor();
			tabTx2.setBackgroundColor(Color.parseColor("#00000000"));
			setResult2View(curPage, 1);

			break;
		case R.id.third_tab:
			resetBgColor();
			tabTx3.setBackgroundColor(Color.parseColor("#00000000"));
			setResult2View(curPage, 2);

			break;
		case R.id.four_tab:
			resetBgColor();
			tabTx4.setBackgroundColor(Color.parseColor("#00000000"));
			setResult2View(curPage, 3);

			break;
		case R.id.fif_tab:
			resetBgColor();
			tabTx5.setBackgroundColor(Color.parseColor("#00000000"));
			setResult2View(curPage, 4);

			break;
		case R.id.liner_city:
			selectCity();

			break;
		default:
			break;
		}
	}

	private void selectCity() {
		int curPage = viewPager.getCurrentItem();
		Intent intent = new Intent(MainActivity.this, CitySelectActivity.class);
		intent.putExtra("curPage", curPage);
		startActivity(intent);
	}

	private void resetBgColor() {
		// "#20000000""#00000000"
		tabTx1.setBackgroundColor(Color.parseColor("#20000000"));
		tabTx2.setBackgroundColor(Color.parseColor("#20000000"));
		tabTx3.setBackgroundColor(Color.parseColor("#20000000"));
		tabTx4.setBackgroundColor(Color.parseColor("#20000000"));
		tabTx5.setBackgroundColor(Color.parseColor("#20000000"));

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) { // 切换page

		int curPage = viewPager.getCurrentItem();
		readCity();
		resetCircleColor();
		resetBgColor();
		tabTx1.setBackgroundColor(Color.parseColor("#00000000"));
		switch (curPage) {
		case 0:
			vpTab0.setBackgroundResource(R.drawable.circle_selected);
			beginQuery(firCityId, 0);
			curCityTx.setText(firCityName);
			break;
		case 1:

			vpTab1.setBackgroundResource(R.drawable.circle_selected);
			beginQuery(secCityId, 1);
			curCityTx.setText(secCityName);
			break;
		case 2:

			vpTab2.setBackgroundResource(R.drawable.circle_selected);
			beginQuery(thirdCityId, 2);
			curCityTx.setText(thirdCityName);
			break;

		default:
			break;
		}

	}

	private void readCity() { // 查看城市是否有改变
		citySf = getSharedPreferences("cities", MODE_PRIVATE);
		firCityId=citySf.getInt("firCityId", 101010100)+"";
		secCityId = citySf.getInt("secCityId", 101020100)+"";
		thirdCityId = citySf.getInt("thirdCityId", 101280101)+"";
		firCityName = citySf.getString("firCityName", "北京");
		secCityName = citySf.getString("secCityName", "上海");
		thirdCityName = citySf.getString("thirdCityName", "广州");
	}

	private void resetCircleColor() {
		vpTab2.setBackgroundResource(R.drawable.circle_bg);
		vpTab1.setBackgroundResource(R.drawable.circle_bg);
		vpTab0.setBackgroundResource(R.drawable.circle_bg);
	}

	private void initVp1Views() { // 初始化Page1数据
		windInfoTx = (TextView) findViewById(R.id.wind_info);
		aqiImg = (ImageView) findViewById(R.id.aqi_img);
		aqiInfoTx = (TextView) findViewById(R.id.aqi_info);
		aqiDataTx = (TextView) findViewById(R.id.aqi_data);
		curTempTx = (TextView) findViewById(R.id.temp_now);
		temDiffTx = (TextView) findViewById(R.id.temp_diff);
		weaTypeTx = (TextView) findViewById(R.id.wea_type);
		tipTx = (TextView) findViewById(R.id.tip_Tx);
	}

	protected void initVp2Views() {// 初始化Page2数据
		windInfoTx1 = (TextView) findViewById(R.id.wind_info1);
		aqiImg1 = (ImageView) findViewById(R.id.aqi_img1);
		aqiInfoTx1 = (TextView) findViewById(R.id.aqi_info1);
		aqiDataTx1 = (TextView) findViewById(R.id.aqi_data1);
		curTempTx1 = (TextView) findViewById(R.id.temp_now1);
		temDiffTx1 = (TextView) findViewById(R.id.temp_diff1);
		weaTypeTx1 = (TextView) findViewById(R.id.wea_type1);
		tipTx1 = (TextView) findViewById(R.id.tip_Tx1);
	}

	protected void initVp3Views() {// 初始化Page3数据
		windInfoTx2 = (TextView) findViewById(R.id.wind_info2);
		aqiImg2 = (ImageView) findViewById(R.id.aqi_img2);
		aqiInfoTx2 = (TextView) findViewById(R.id.aqi_info2);
		aqiDataTx2 = (TextView) findViewById(R.id.aqi_data2);
		curTempTx2 = (TextView) findViewById(R.id.temp_now2);
		temDiffTx2 = (TextView) findViewById(R.id.temp_diff2);
		weaTypeTx2 = (TextView) findViewById(R.id.wea_type2);
		tipTx2 = (TextView) findViewById(R.id.tip_Tx2);
	}

	/**
	 * 
	 * @param activity
	 * @return
	 */

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private long firstTime = 0;

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
				
				firstTime = secondTime;// 更新firstTime
				return true;
			} else { // 两次按键小于2秒时，退出应用
				// System.exit(0);
				// System.exit(); //退出整个应用
				CloseAllActivities.getInstance().exit();
				// MainActivity.getMainActivity().g
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

}
