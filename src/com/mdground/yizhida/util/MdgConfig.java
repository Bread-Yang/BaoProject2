package com.mdground.yizhida.util;

import java.io.File;
import java.io.IOException;

public class MdgConfig {

	private static String filePath = Tools.getAppPath() + File.separator + "device";
//	private static String filePath = Tools.getAppPath() + File.separator + "device_ua";

	public static int getDeviceId() {
		if (ToolFile.isExsit(filePath)) {
			try {
				String dataStr = ToolFile.readFileByLines(filePath);
				return Integer.parseInt(dataStr.trim());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public static void setDeviceId(int deviceId) {
		try {
			if (!ToolFile.isExsit(filePath)) {
				File file = new File(filePath);
				file.createNewFile();
				ToolFile.write(file, String.valueOf(deviceId), System.getProperty("file.encoding"));
			} else {
				ToolFile.write(filePath, String.valueOf(deviceId).getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
