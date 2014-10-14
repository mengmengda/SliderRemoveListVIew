package com.alphabet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AlphabetScrollBar extends View {
	private float alphabetSize;
	private float scrollBarWidth;
	private String[] list;
	private String alphabet = "";
	private float[] alphabetScrollY;
	private OnSelectAlphabetListener listener;
	private boolean clicked;

	public AlphabetScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		alphabetScrollY = new float[27];
		list =  AlphabetAdapter.alphabetList;
	}

	public String getAlphabet() {
		return alphabet;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initDate();
		darwAlphabetScrollBar(canvas);
	}

	private void initDate() {
		alphabetSize = (float) getHeight() / 28f;
		scrollBarWidth = (float) getWidth();

	}

	private void darwAlphabetScrollBar(Canvas canvas) {

		Paint paintRect = new Paint();
		paintRect.setAntiAlias(true);
		paintRect.setColor(Color.parseColor("#00000000"));
		Paint paintText = new Paint();
		paintText.setAntiAlias(true);
		paintText.setTextSize(alphabetSize < scrollBarWidth ? alphabetSize : scrollBarWidth);

		if (clicked) {
			paintText.setColor(Color.parseColor("#A8A8A8"));
		} else {
			paintText.setColor(Color.parseColor("#A8A8A8"));
		}

		FontMetrics fm = paintText.getFontMetrics();
		int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2f);
		for (int i = 0; i < list.length; i++) {
			RectF rf = new RectF(0f, i * alphabetSize, scrollBarWidth, (i + 1f) * alphabetSize);
			canvas.drawRect(rf, paintRect);
			canvas.drawText(list[i], scrollBarWidth / 2f - paintText.measureText(list[i]) / 2f, i * alphabetSize
					+ alphabetSize / 2f + textHeight / 2, paintText);
			alphabetScrollY[i] = i * alphabetSize;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = 500;
		int height = 500;

		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			width = MeasureSpec.getSize(widthMeasureSpec);
		} else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			width = MeasureSpec.getSize(widthMeasureSpec);
		}
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			height = MeasureSpec.getSize(heightMeasureSpec);
		} else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
			height = MeasureSpec.getSize(heightMeasureSpec);
		}
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			clicked = true;
			invalidate();
			for (int i = 0; i < alphabetScrollY.length; i++) {
				if (y > alphabetScrollY[i] && y <= alphabetScrollY[i] + alphabetSize) {
					listener.setOnSelectAlphabetListener(list[i], true);
					break;
				}
			}

			break;

		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < alphabetScrollY.length; i++) {
				if (y > alphabetScrollY[i] && y <= alphabetScrollY[i] + alphabetSize) {
					listener.setOnSelectAlphabetListener(list[i], true);
					break;
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			clicked = false;
			invalidate();
			listener.setOnSelectAlphabetListener("", false);
			break;
		}
		return true;
	}

	public void setOnScrollBarListener(OnSelectAlphabetListener listener) {
		this.listener = listener;
	};

	public interface OnSelectAlphabetListener {
		public void setOnSelectAlphabetListener(String alphabet, boolean show);
	}

}
