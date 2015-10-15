package com.mdground.yizhida.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class PatientInfoPageAdapter extends PagerAdapter{
	 
	private ArrayList<View> viewContainter = null;
//    private ArrayList<String> titleContainer = null;
    
    public PatientInfoPageAdapter(ArrayList<View> viewContainter){
    	this.viewContainter = viewContainter;
//    	this.titleContainer = titleContainer;
    }
    
    @Override
    public int getCount() {
        return viewContainter.size();
    }
    
    
    @Override
    public void destroyItem(ViewGroup container, int position,
            Object object) {
        ((ViewPager) container).removeView(viewContainter.get(position));
    }
    
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewContainter.get(position),0);
        return viewContainter.get(position);
    }
    

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titleContainer.get(position);
//    }


}
