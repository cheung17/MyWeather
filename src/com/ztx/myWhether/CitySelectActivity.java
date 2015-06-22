package com.ztx.myWhether;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

public class CitySelectActivity extends Activity implements
		OnFocusChangeListener, OnItemClickListener, OnClickListener {
	private AutoCompleteTextView autoTx;
	private LinearLayout linear_back;
	AnsyTry anys = null;
	int curPage;
	private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10,
			btn11, btn12;
	private InputStream is = null;
	private int cityCount = 0;
	private static String citykeys[] = new String[2586];
	static String cityvalues[] = new String[2586];

	public CitySelectActivity() {
	}

	/**
	 *  
		
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		setContentView(R.layout.city_select);
		initViews();

	}

	private void initViews() {
		ansyReadCity();// 读取城市名单
		linear_back = (LinearLayout) findViewById(R.id.linear_back);
		linear_back.setOnClickListener(this);
		CloseAllActivities.getInstance().addActivity(this);
		Intent intent = this.getIntent();
		curPage = intent.getIntExtra("curPage", 0);
		autoTx = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoTx.setOnFocusChangeListener(this);
		btn1 = (Button) findViewById(R.id.beijing);
		btn2 = (Button) findViewById(R.id.shanghai);
		btn3 = (Button) findViewById(R.id.guangzhou);
		btn4 = (Button) findViewById(R.id.shenzhen);
		btn5 = (Button) findViewById(R.id.wuhan);
		btn6 = (Button) findViewById(R.id.nanjin);
		btn7 = (Button) findViewById(R.id.hangzhou);
		btn8 = (Button) findViewById(R.id.tianjin);
		btn9 = (Button) findViewById(R.id.suzhou);
		btn10 = (Button) findViewById(R.id.xian);
		btn11 = (Button) findViewById(R.id.chegndu);
		btn12 = (Button) findViewById(R.id.shenyang);
		btn1.setOnClickListener(this);
		btn12.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btn10.setOnClickListener(this);
		btn11.setOnClickListener(this);

	}

	public void ansyReadCity() {
		is = getResources().openRawResource(R.raw.city);
		anys = new AnsyTry(is);
		anys.execute();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.autoCompleteTextView1) {
			if (hasFocus) {
				autoTx.setText("");
				autoTx.setTextColor(Color.parseColor("#5d5d5d"));
				ArrayAdapter<String> adapter = new ArrayAdapter<String>( // autoComplete的适配器
						getApplicationContext(), R.layout.list_item, cityvalues);
				autoTx.setAdapter(adapter);
				autoTx.setOnItemClickListener(this);

			} else {
				if (autoTx.getText().toString().length() <= 0) {
					autoTx.setText("请输入城市名...");
					autoTx.setTextColor(Color.parseColor("#E0DFE0"));
				}

			}
		}
	}

	class AnsyTry extends AsyncTask<Void, Void, Void> { // 异步任务，用于读取city.txt里的城市名单

		InputStream is;

		public AnsyTry(InputStream is) {
			super();
			this.is = is;
		}

		@Override
		protected Void doInBackground(Void... params) {
			getCityList(is);
			return null;
		}

		private void getCityList(InputStream is) { // 以行读取
			InputStreamReader isr = null;
			StringBuffer sBuffer = new StringBuffer("");
			try {
				isr = new InputStreamReader(is, "utf-8");
				BufferedReader reader = new BufferedReader(isr);
				String line = "";
				try {
					while ((line = reader.readLine()) != null) {
						sBuffer.append(line.trim());
						splitt(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		private void splitt(String line) { // 以空格为分隔 分别将城市序号及城市名称存放到数组中
			String strr = line.trim();
			String[] abc = strr.split("[\\p{Space}]+");
			// .out.println(abc[0]);
			citykeys[cityCount] = abc[0];       
			cityvalues[cityCount] = abc[1];
			cityCount++;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		View view1 = getWindow().peekDecorView();
		if (view != null) { // 收起输入法
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view1.getWindowToken(), 0);
		}

		String cityStr = autoTx.getText().toString();
		setCity(cityStr);

		
	}

	private int findCityIdByValue(String cityStr) {
		for (int j = 0; j < 2586; j++) {
			if (cityvalues[j].equals(cityStr)) {
				return j;
			}
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear_back:
			finish();
			break;
		case R.id.beijing:
			setCity(btn1.getText().toString());
			break;
		case R.id.shanghai:
			setCity(btn2.getText().toString());
			break;
		case R.id.guangzhou:
			setCity(btn3.getText().toString());
			break;
		case R.id.shenzhen:
			setCity(btn4.getText().toString());
			break;
		case R.id.wuhan:
			setCity(btn5.getText().toString());
			break;
		case R.id.nanjin:
			setCity(btn6.getText().toString());
			break;
		case R.id.hangzhou:
			setCity(btn7.getText().toString());
			break;
		case R.id.tianjin:
			setCity(btn8.getText().toString());
			break;
		case R.id.suzhou:
			setCity(btn9.getText().toString());
			break;
		case R.id.xian:
			setCity(btn10.getText().toString());
			break;
		case R.id.chegndu:
			setCity(btn11.getText().toString());
			break;
		case R.id.shenyang:
			setCity(btn12.getText().toString());
			break;
		default:
			break;
		}
	}

	private void setCity(String cityStr) {
		int i = findCityIdByValue(cityStr);
		int cityId;
		if (i == 0) {
			cityId = 101010100;
		} else {
			cityId = Integer.parseInt(citykeys[i]);
		}
		if (curPage == 0) {
			Editor editor = getSharedPreferences("cities", MODE_PRIVATE).edit();
			editor.putInt("firCityId", cityId);
			editor.putString("firCityName", cityStr);
			editor.commit();
		} else if (curPage == 1) {
			Editor editor = getSharedPreferences("cities", MODE_PRIVATE).edit();
			editor.putInt("secCityId", cityId);
			editor.putString("secCityName", cityStr);
			editor.commit();
		} else {
			Editor editor = getSharedPreferences("cities", MODE_PRIVATE).edit();
			editor.putInt("thirdCityId", cityId);
			editor.putString("thirdCityName", cityStr);
			editor.commit();

		}
		Intent intent = new Intent(CitySelectActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
