package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Location;

public class LocationAdapter extends SimpleAdapter<Location> {

	protected static class ViewHolder extends BaseViewHolder {
		private TextView locationName;
	}

	public LocationAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		Location location = (Location) getItem(position);
		if (location != null) {
			viewHolder.locationName.setText(location.getLocationName());
		}
	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		viewHolder.locationName = (TextView) convertView.findViewById(R.id.location_name);
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_location;
	}

}
