package com.mdground.yizhida.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.Tools;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DaoConfig;
import android.content.Context;
import android.os.Environment;

public class Basedao {

	Context context;
	private FinalDb db;
	private FinalDb cityDb;

	public FinalDb getDb() {
		if (db == null) {
			DaoConfig daoConfig = new DaoConfig();
			daoConfig.setContext(context);
			daoConfig.setDbName("yideguan");
			daoConfig.setDebug(true);
			db = FinalDb.create(daoConfig);
		}
		return db;
	}

	public FinalDb getCityDb() {
		return FinalDb.create(context, getSavePath() + "/city");
	}

	public String getSavePath() {
		return Tools.getAppPath() + MedicalConstant.APP_DATA;
	}

	public void setCityDb(FinalDb cityDb) {
		this.cityDb = cityDb;
	}

	public Basedao(Context context) {

		this.context = context;
	}

	public void CopyAssets(String fileName) {
		File mWorkingPath = new File(getSavePath() + "/");
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {
			}
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = context.getResources().getAssets().open(fileName);
			System.err.println("");
			File outFile = new File(mWorkingPath, fileName);
			out = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
