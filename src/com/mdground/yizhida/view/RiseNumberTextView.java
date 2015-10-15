package com.mdground.yizhida.view;

import java.text.NumberFormat;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 数字显示0-X
 * 
 * @author Sanpark
 * 
 */
public class RiseNumberTextView extends TextView {
	private Handler handler;
	Runnable r = null;
	double oldnum;
	double newnum;
	double startnum = 0;

	public RiseNumberTextView(Context context, Handler handler) {
		super(context);
	}

	public RiseNumberTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RiseNumberTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public CharSequence getText() {
		return super.getText();
	}

	public void setText(CharSequence text, BufferType type, Handler myHandler) {
		handler = myHandler;
		oldnum = 0.00;
		newnum = Double.valueOf(text.toString());
		// 转化国际化数字显示
		// 若想在数字前面在上￥符号 ，将getIntegerInstance()方法改为getCurrencyInstance () 方法
		final NumberFormat af = NumberFormat.getIntegerInstance();
		// 设置精确到小数点后两位
		af.setMinimumFractionDigits(2);
		r = new Runnable() {

			@Override
			public void run() {
				// 设置每次添加的度量
				oldnum += newnum / 30;
				if (startnum < newnum) {
					if (oldnum > newnum)
						oldnum = newnum;
					setText(af.format(oldnum));
					handler.postDelayed(r, 20);
				} else if (oldnum == newnum) {
					setText(af.format(oldnum));
				}
			}
		};
		handler.postDelayed(r, 20);
	}

}