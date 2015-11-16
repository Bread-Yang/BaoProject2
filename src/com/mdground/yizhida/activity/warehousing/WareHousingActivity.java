package com.mdground.yizhida.activity.warehousing;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.StringUtil;
import com.mdground.yizhida.view.TitleBar;
import com.vanward.ehheater.view.wheelview.OnWheelScrollListener;
import com.vanward.ehheater.view.wheelview.WheelView;
import com.vanward.ehheater.view.wheelview.adapters.AbstractWheelTextAdapter;
import com.vanward.ehheater.view.wheelview.adapters.ArrayWheelAdapter;

import android.app.Dialog;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WareHousingActivity extends TitleBarActivity implements OnClickListener {

	private TextView tv_drug_name, tv_specification, tv_drug_producer, tv_total_inventory,
			tv_warehousing_quantity_title, tv_expiration_date_title, tv_expiration_date, tv_purchase_price_title,
			tv_ov_price_title, tv_sale_price_title, tv_production_date, tv_batch_number_title;

	private EditText et_warehousing_quantity, et_purchase_price, et_ov_price, et_sale_price, et_batch_number,
			et_suppliers, et_purchase_number;

	private Button btn_warehousing_quantity_unit, btn_purchase_price_unit, btn_ov_price_unit, btn_sale_price_unit;

	private Dialog dialog_wheelView;

	private ImageView iv_close;
	private WheelView wheelView;

	private Drug mDrug;

	private int mWheelview_set_flag;

	@Override
	public int getContentLayout() {
		return R.layout.activity_ware_housing;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);

		titleBar.setTitle(getResources().getString(R.string.warehousing));
		titleBar.setBackgroundResource(R.drawable.top_bg1);

	}

	@Override
	public void findView() {
		tv_drug_name = (TextView) findViewById(R.id.tv_drug_name);
		tv_specification = (TextView) findViewById(R.id.tv_specification);
		tv_drug_producer = (TextView) findViewById(R.id.tv_drug_producer);
		tv_total_inventory = (TextView) findViewById(R.id.tv_total_inventory);
		tv_warehousing_quantity_title = (TextView) findViewById(R.id.tv_warehousing_quantity_title);
		tv_expiration_date_title = (TextView) findViewById(R.id.tv_expiration_date_title);
		tv_expiration_date = (TextView) findViewById(R.id.tv_expiration_date);
		tv_purchase_price_title = (TextView) findViewById(R.id.tv_purchase_price_title);
		tv_ov_price_title = (TextView) findViewById(R.id.tv_ov_price_title);
		tv_sale_price_title = (TextView) findViewById(R.id.tv_sale_price_title);
		tv_production_date = (TextView) findViewById(R.id.tv_production_date);
		tv_batch_number_title = (TextView) findViewById(R.id.tv_batch_number_title);

		et_warehousing_quantity = (EditText) findViewById(R.id.et_warehousing_quantity);
		et_purchase_price = (EditText) findViewById(R.id.et_purchase_price);
		et_ov_price = (EditText) findViewById(R.id.et_ov_price);
		et_sale_price = (EditText) findViewById(R.id.et_sale_price);
		et_batch_number = (EditText) findViewById(R.id.et_batch_number);
		et_suppliers = (EditText) findViewById(R.id.et_suppliers);
		et_purchase_number = (EditText) findViewById(R.id.et_purchase_number);

		btn_warehousing_quantity_unit = (Button) findViewById(R.id.btn_warehousing_quantity_unit);
		btn_purchase_price_unit = (Button) findViewById(R.id.btn_purchase_price_unit);
		btn_ov_price_unit = (Button) findViewById(R.id.btn_ov_price_unit);
		btn_sale_price_unit = (Button) findViewById(R.id.btn_sale_price_unit);
	}

	@Override
	public void initView() {
		// 初始化dialog及dialog里面的控件
		dialog_wheelView = new Dialog(this, R.style.patient_detail_dialog);
		dialog_wheelView.setContentView(R.layout.dialog_wheel_view);

		// 设置dialog弹入弹出的动画
		Window window = dialog_wheelView.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window.setAttributes(wlp);

		iv_close = (ImageView) dialog_wheelView.findViewById(R.id.iv_close);

		wheelView = (WheelView) dialog_wheelView.findViewById(R.id.wheelview1);

		SpannableStringBuilder builder = new SpannableStringBuilder(tv_warehousing_quantity_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_warehousing_quantity_title.setText(builder);

		builder = new SpannableStringBuilder(tv_expiration_date_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_expiration_date_title.setText(builder);

		builder = new SpannableStringBuilder(tv_purchase_price_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_purchase_price_title.setText(builder);

		builder = new SpannableStringBuilder(tv_ov_price_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_ov_price_title.setText(builder);

		builder = new SpannableStringBuilder(tv_sale_price_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_sale_price_title.setText(builder);

		builder = new SpannableStringBuilder(tv_batch_number_title.getText().toString());
		builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_batch_number_title.setText(builder);

	}

	@Override
	public void initMemberData() {
		mDrug = getIntent().getParcelableExtra(MemberConstant.BARCODE_SCAN_DRUG);

		if (mDrug != null) {
			tv_drug_name.setText(mDrug.getDrugName());
			tv_specification.setText(mDrug.getSpecification());
			tv_drug_producer.setText(mDrug.getManufacturer());

			tv_total_inventory.setText(getResources().getString(R.string.total_inventory) + ":    "
					+ mDrug.getInventoryQuantity() + mDrug.getInventoryUnit());

			wheelView.setViewAdapter(new ArrayWheelAdapter(this,
					new String[] { mDrug.getUnitGeneric(), mDrug.getUnitSmall() }, wheelView));
		}
	}

	@Override
	public void setListener() {
		iv_close.setOnClickListener(this);
		
		wheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String text = ((AbstractWheelTextAdapter) wheel.getViewAdapter()).getItemText(wheel.getCurrentItem())
						.toString();

				switch (mWheelview_set_flag) {
				case R.id.btn_warehousing_quantity_unit:
					btn_warehousing_quantity_unit.setText(text);
					break;
				case R.id.btn_purchase_price_unit:
					btn_purchase_price_unit.setText(text);
					break;
				case R.id.btn_ov_price_unit:
					btn_ov_price_unit.setText(text);
					break;
				case R.id.btn_sale_price_unit:
					btn_sale_price_unit.setText(text);
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_warehousing_quantity_unit:
			dialog_wheelView.show();

			mWheelview_set_flag = R.id.btn_warehousing_quantity_unit;
			break;

		case R.id.btn_purchase_price_unit:
			dialog_wheelView.show();

			mWheelview_set_flag = R.id.btn_purchase_price_unit;
			break;

		case R.id.btn_ov_price_unit:
			dialog_wheelView.show();

			mWheelview_set_flag = R.id.btn_ov_price_unit;
			break;

		case R.id.btn_sale_price_unit:
			dialog_wheelView.show();

			mWheelview_set_flag = R.id.btn_sale_price_unit;
			break;
		case R.id.iv_close:
			dialog_wheelView.dismiss();
			break;
		}
	}

}
