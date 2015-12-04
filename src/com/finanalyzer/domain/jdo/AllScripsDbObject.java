package com.finanalyzer.domain.jdo;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.Entity;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;

@PersistenceCapable
public class AllScripsDbObject {

	@Persistent
	private String nseId;
	
	@Persistent
	private String stockName;
	
	@Persistent
	private String isin;
	
	@Persistent
	private String bseId;
	
	@Persistent
	private String fairValue;
	
	@Persistent
	private String industry;

	@Persistent
	private String moneycontrolName;
	
	@Persistent
	private String yahooName;

	@Persistent
	private String isWatchListed;

	@Persistent
	private List<String> ratingNameToValue;
	
	@Persistent
	private String isBlackListed;

	public static final Function<AllScripsDbObject, String> STOCK_NAME_COLLECTOR = new Function<AllScripsDbObject, String>() {
	
		@Override
		public String valueOf(AllScripsDbObject allScripsDbObject) {
			return allScripsDbObject.getNseId();
		}
	};

	public static final Predicate<AllScripsDbObject> IS_WATCHLISTED = new Predicate<AllScripsDbObject>() {
	
		@Override
		public boolean accept(AllScripsDbObject allScripsDbObject) {
			return allScripsDbObject.isWatchListed();
		}
	};


	public static final Predicate<AllScripsDbObject> IS_BLACKLISTED = new Predicate<AllScripsDbObject>() {
	
		@Override
		public boolean accept(AllScripsDbObject allScripsDbObject) {
			return allScripsDbObject.isBlackListed();
		}
	};

	
	public static final Predicate<AllScripsDbObject> MONEYCONTROL_NAME_EXISTS = new Predicate<AllScripsDbObject>() {
	
		@Override
		public boolean accept(AllScripsDbObject scrip) {
			
			return StringUtil.isValidValue(scrip.getMoneycontrolName());
		}
	};

	public static final String ISIN = "isin";

	public static final String BSE_ID = "bseId";

	public static final String FAIR_VALUE = "fairValue";

	public static final String INDUSTRY = "industry";

	public static final String STOCK_NAME = "stockName";

	public static final String NSE_ID = "nseId";
	
	public static final String MONEY_CONTROL_NAME = "moneycontrolName";

	public static final Function<Entity, String> NSE_ID_COLLECTOR = new Function<Entity, String>() {

		@Override
		public String valueOf(Entity entity) {
			
			return (String) entity.getProperty("stockId");
		}
	};

	
	public AllScripsDbObject(String nseId, String stockName, String isin,
			String bseId, String moneycontrolName,String yahooName, String fairValue, String industry) 
	{
		this.nseId = nseId;
		this.stockName = stockName;
		this.isin = isin;
		this.bseId = bseId;
		this.moneycontrolName = moneycontrolName;
		this.yahooName = yahooName;
		this.fairValue = fairValue;
		this.industry = industry;
	}
	
	public AllScripsDbObject(String nseId, String stockName, String isin,
			String bseId, String fairValue, String industry) 
	{
		this(nseId,stockName,isin,bseId,null,null, fairValue,industry);
	}
	
	public AllScripsDbObject(String nseId, String bseId, String moneycontrolName,String yahooName) 
	{
		this(nseId, null, null, bseId, moneycontrolName, yahooName, null, null);
	}
	
	public AllScripsDbObject() {
		
	}

	public String getNseId() {
		return nseId;
	}

	public String getStockName() {
		return stockName;
	}

	public String getIsin() {
		return isin;
	}

	public String getBseId() {
		return bseId;
	}

	public String getFairValue() {
		return fairValue;
	}

	public String getIndustry() {
		return industry;
	}

	public String getMoneycontrolName() {
		return moneycontrolName;
	}

	public String getYahooName() {
		return yahooName;
	}

	public void setNseId(String nseId) {
		this.nseId = nseId;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public void setBseId(String bseId) {
		this.bseId = bseId;
	}

	public void setFairValue(String fairValue) {
		this.fairValue = fairValue;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public void setMoneycontrolName(String moneycontrolName) {
		this.moneycontrolName = moneycontrolName;
	}

	public void setYahooName(String yahooName) {
		this.yahooName = yahooName;
	}
	
	public boolean isWatchListed() {
		return Boolean.valueOf(this.isWatchListed);
	}

	public void setWatchListed(boolean isWatchListed) {
		this.isWatchListed = String.valueOf(isWatchListed);
	}
	
	
	public boolean isBlackListed() {
		return Boolean.valueOf(this.isBlackListed);
	}

	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = String.valueOf(isBlackListed);
	}

	public Map<String, StockRatingValuesEnum> getRatingNameToValue() {
		return transforListToMap(this.ratingNameToValue);
	}

	public void setRatingNameToValue(Map<String, StockRatingValuesEnum> ratingNameToValue) {
		this.ratingNameToValue = transforMapToList(ratingNameToValue);
	}

	private Map<String, StockRatingValuesEnum> transforListToMap(List<String> ratings) {
		final Map<String, StockRatingValuesEnum> ratingNameToValue = UnifiedMap.newMap();
		for (int i = 0; i < ratings.size()-1; i=i+2) 
		{
			final String ratingName = ratings.get(i);
			final StockRatingValuesEnum ratingValueEnum = StockRatingValuesEnum.getEnumForRatingDescription(ratings.get(i+1));
			ratingNameToValue.put(ratingName, ratingValueEnum);
		}
		return ratingNameToValue;
	}
	
	private List<String> transforMapToList(Map<String, StockRatingValuesEnum> ratingNameToValue) {
		final List<String> flattenedList = FastList.newList();
		final UnifiedMap<String, StockRatingValuesEnum> ratingNameValue = UnifiedMap.newMap(ratingNameToValue);
		
		final Procedure2<String, StockRatingValuesEnum> flattenRatingMap = new Procedure2<String, StockRatingValuesEnum>() {

			@Override
			public void value(String ratingName, StockRatingValuesEnum ratingValue) {
				flattenedList.add(ratingName);
				flattenedList.add(ratingValue.getDescription());
			}
		};
		
		ratingNameValue.forEachKeyValue(flattenRatingMap);
		
		return flattenedList;
	}
	
	public List<String> getRatingInferences()
	{
		return new StockRatingValue(this.getRatingNameToValue()).getInferences();
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof AllScripsDbObject)
		{
			AllScripsDbObject that = (AllScripsDbObject)object;
			return this.nseId.equals(that.nseId);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.nseId.hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.nseId+" - "+this.bseId;
	}
}
