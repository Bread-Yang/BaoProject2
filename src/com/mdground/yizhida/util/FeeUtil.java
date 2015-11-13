package com.mdground.yizhida.util;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.bean.OfficeVisitFee;

import android.widget.TextView;

public class FeeUtil {

	public static void calculateAmountAndShowInTextView(TextView tv_amount) {
		float amount = 0;

		// 费用
		for (OfficeVisitFee item : MedicalAppliction.mOfficeVisitFeeMap.values()) {
			amount += item.getTotalFee() / 100f;
		}

		// 药物
		for (DrugUse item : MedicalAppliction.mDrugUseMap.values()) {
			amount += item.getSaleQuantity() * item.getSalePrice() / 100f;
		}

		tv_amount.setText(String.format("%.02f", amount) + "元");
	}
	
	public static float getCalculateAmount() {
		float amount = 0;

		// 费用
		for (OfficeVisitFee item : MedicalAppliction.mOfficeVisitFeeMap.values()) {
			amount += item.getTotalFee() / 100f;
		}

		// 药物
		for (DrugUse item : MedicalAppliction.mDrugUseMap.values()) {
			amount += item.getSaleQuantity() * item.getSalePrice() / 100f;
		}
		return amount;
	}
	
}
