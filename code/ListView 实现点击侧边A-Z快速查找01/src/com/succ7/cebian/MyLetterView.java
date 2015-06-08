package com.succ7.cebian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 点击ListView的侧边A-Z实现快速查找的侧边View
 * 
 * @author zhongcj
 * @time 2015年3月9日 下午11:34:21
 */
public class MyLetterView extends View {
	public MyLetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterView(Context context) {
		super(context);
	}

	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	/**
	 * 右侧需要显示的标签,随需求而变
	 */
	public static String[] letters = { "↑", "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
	int choose = -1;
	// 创建画笔
	Paint paint = new Paint();
	// 是否需要显示背景颜色,默认为无
	private boolean showBkg = false;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor("#40000000"));
		}
		int height = getHeight();
		int width = getWidth();
		// 单个字母的高度
		int singleHeight = height / letters.length;
		for (int i = 0; i < letters.length; i++) {
			// 文字颜色
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);
			// 如果被点击,则变颜色
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(letters[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * letters.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null) {
				// 需要加上等号第一个才能被点击
				if (c >= 0 && c < letters.length) {
					listener.onTouchingLetterChanged(letters[c]);
					choose = c;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				// 需要加上等号第一个才能被滑到
				if (c >= 0 && c < letters.length) {
					listener.onTouchingLetterChanged(letters[c]);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}