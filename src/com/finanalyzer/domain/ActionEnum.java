package com.finanalyzer.domain;

import java.util.Map;

import com.gs.collections.impl.map.mutable.UnifiedMap;

public enum ActionEnum {
	RETRIEVE_ALL_DETAILS("Retrieve All Details"),
	UPDATE_STOCK_RATING("Add/Update"), 
	ADD_TO_WATCHLIST("Add to WatchList"), 
	ADD_TO_BLACKLIST("Add to BlackList");
	
	private String actionDescription;
	private static Map<String, ActionEnum> DESCRIPTION_TO_ENUM_MAP=UnifiedMap.newMap();

	ActionEnum(String actionDescription)
	{
		this.actionDescription=actionDescription;	
	}
	
	public String getActionDescription() {
		return actionDescription;
	}

	public static Map<String, ActionEnum> getDESCRIPTION_TO_ENUM_MAP() {
		return DESCRIPTION_TO_ENUM_MAP;
	}

	public static ActionEnum getAction(String actionDescription)
	{
		return DESCRIPTION_TO_ENUM_MAP.get(actionDescription);
	}
	
	static
	{
		for (ActionEnum eachAction : ActionEnum.values())
		{
			DESCRIPTION_TO_ENUM_MAP.put(eachAction.getActionDescription(), eachAction);
		}
	}
}
