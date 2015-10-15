package com.mdground.yizhida.api.base;

public enum PlatformType {

	Unkown(0), ANDROID_PHONE(1), ANDROID_PAD(2);

    
	private int value;

	private PlatformType(int platform) {
		this.value = platform;
	}

	public int value() {
		return value;
	}

}
