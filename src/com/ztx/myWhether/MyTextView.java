package com.ztx.myWhether;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * 此类用于重写TextView 以在单个activity里实现多个跑马灯效果
 * @author laughing
 *
 */
public class MyTextView extends TextView{

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if(focused) super.onFocusChanged(focused, direction, previouslyFocusedRect); 
	}
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		 if(hasWindowFocus) super.onWindowFocusChanged(hasWindowFocus);  
	}
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
