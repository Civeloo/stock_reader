package com.civeloo.stockreader.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ListItemView extends TextView {
	private boolean isHeader = false;
	private Paint linePaint;

	public ListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ListItemView(Context context) {
		super(context);
		init();
	}

	public void init() {
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(Color.parseColor("#000000"));
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isHeader) {
			canvas.drawColor(Color.parseColor("#AAFFFF99"));
		}
		canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
		canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(),
				getMeasuredHeight(), linePaint);
		canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
	}
}
