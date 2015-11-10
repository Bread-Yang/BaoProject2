package com.mdground.yizhida.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText中方法setFilters()用到的InputFilter,用来限制小数点后面及前面的位数 用法 :
 * mEditText.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2, 2)
 * }); 表示mEditText这个控件只能输入20.00, 10.01这样的数
 * 
 * @author yrb
 * 
 */
public class DecimalDigitsInputFilter implements InputFilter {

	private final int decimalDigits;

	  /**
	   * Constructor.
	   * 
	   * @param decimalDigits maximum decimal digits
	   */
	  public DecimalDigitsInputFilter(int decimalDigits) {
	    this.decimalDigits = decimalDigits;
	  }

	  @Override
	  public CharSequence filter(CharSequence source,
	      int start,
	      int end,
	      Spanned dest,
	      int dstart,
	      int dend) {


	    int dotPos = -1;
	    int len = dest.length();
	    for (int i = 0; i < len; i++) {
	      char c = dest.charAt(i);
	      if (c == '.' || c == ',') {
	        dotPos = i;
	        break;
	      }
	    }
	    if (dotPos >= 0) {

	      // protects against many dots
	      if (source.equals(".") || source.equals(","))
	      {
	          return "";
	      }
	      // if the text is entered before the dot
	      if (dend <= dotPos) {
	        return null;
	      }
	      if (len - dotPos > decimalDigits) {
	        return "";
	      }
	    }

	    return null;
	  }

}