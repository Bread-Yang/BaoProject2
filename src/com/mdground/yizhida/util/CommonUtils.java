package com.mdground.yizhida.util;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;

public class CommonUtils {

	public static void showPageTab(Resources resources,TextView showTab, View showView,
			TextView hiddenOneTab, View hiddenOneView, TextView hiddenTwoTab,
			View hiddenTwoView, TextView hiddenThreeTab,
			View hiddenThreeView) {
		
		showTab.setTextColor(resources.getColor(R.color.tab_color));
		showView.setVisibility(View.VISIBLE);
		
		hiddenOneTab.setTextColor(resources.getColor(R.color.basic_information_text_color));
		hiddenOneView.setVisibility(View.INVISIBLE);
		hiddenTwoTab.setTextColor(resources.getColor(R.color.basic_information_text_color));
		hiddenTwoView.setVisibility(View.INVISIBLE);
		hiddenThreeTab.setTextColor(resources.getColor(R.color.basic_information_text_color));
		hiddenThreeView.setVisibility(View.INVISIBLE);
	}

}
