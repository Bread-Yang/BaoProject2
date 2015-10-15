package com.mdground.yizhida.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleItemAdapter2 extends CursorAdapter {

	public ScheduleItemAdapter2(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return null;
	}

}
