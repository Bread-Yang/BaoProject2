package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Symptom;

/**
 * 目前症状Adpater
 * 
 * @author Administrator
 * 
 */
public class CurrentSymptomsAdapter extends SimpleAdapter<Symptom> {
	protected static class ViewHolder extends BaseViewHolder {
		private ImageView selectedIcon;
		private TextView symptomName;
	}

	public CurrentSymptomsAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		Symptom t = (Symptom) getItem(position);
		if (t == null) {
			return;
		}
		if (t.isSelected()) {
			viewHolder.selectedIcon.setVisibility(View.VISIBLE);
		} else {
			viewHolder.selectedIcon.setVisibility(View.GONE);
		}
		viewHolder.symptomName.setText(t.getTemplateName());

	}

	@Override
	protected void initHolder(BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		viewHolder.selectedIcon = (ImageView) convertView.findViewById(R.id.sel_icon);
		viewHolder.symptomName = (TextView) convertView.findViewById(R.id.symptom_name);
	}

	@Override
	protected int getViewResource() {
		return R.layout.current_symptoms_gird_item;
	}

}
