package com.mdground.yizhida.util;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.mdground.yizhida.bean.Location;
import com.mdground.yizhida.db.Basedao;

public class LocationUtils {

	private Context context;
	private FinalDb locationDb;

	private static LocationUtils locationUtils;

	public static LocationUtils getInstance(Context context) {
		if (locationUtils == null) {
			locationUtils = new LocationUtils(context);
		}
		return locationUtils;
	}

	public LocationUtils(Context context) {
		this.context = context;
		locationDb = new Basedao(context).getCityDb();
	}

	public String getNameFromId(long id) {
		Location location = locationDb.findById(String.valueOf(id), Location.class);
		if (location != null) {
			return location.getLocationName();
		}
		return "";
	}

	public long getIdFromName(String name) {
		List<Location> list = locationDb.findAllByWhere(Location.class, "LocationName = '" + name + "'");
		long id = -1;
		if (list != null && list.size() > 0) {
			id = list.get(0).getLocationID();
		}
		return id;
	}
	
	public Location getLocationById(long locationId){
		return locationDb.findById(String.valueOf(locationId), Location.class);
	}
	/**
	 * 获取国家
	 * @param locationId
	 * @return
	 */
	public List<Location> getListFromLocationId(long locationId) {
		List<Location> list = locationDb.findAllByWhere(Location.class, "LocationID = '" + locationId + "'");
		Location content = new Location();
		content.setLocationName("请选择");
		content.setLocationID(-1);
		list.add(0, content);
		return list;
	}

	public List<Location> getListFromParentId(long parentId) {
		List<Location> list = locationDb.findAllByWhere(Location.class, "ParentID = '" + parentId + "'");
		Location content = new Location();
		content.setLocationName("请选择");
		content.setLocationID(-1);
		list.add(0, content);
		return list;
	}
}
