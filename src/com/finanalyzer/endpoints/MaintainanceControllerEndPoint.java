package com.finanalyzer.endpoints;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.api.QuandlConnection;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.EndPointResponse;
import com.finanalyzer.domain.ModifiableStockAttributes;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.Adapter;
import com.finanalyzer.util.StringUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "maintainanceControllerEndPoint", version = "v1")
public class MaintainanceControllerEndPoint {
	
	private static final Logger LOG = Logger.getLogger(MaintainanceControllerEndPoint.class.getName());

	@ApiMethod(name = "getModifiableStockAttributes", path="getModifiableStockAttributes")
	public List<ModifiableStockAttributes> getModifiableStockAttributes(@Named("stockName") String stockName)
	{
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<>(AllScripsDbObject.class);
		final AllScripsDbObject allScripsDbObject = allScripsDbOperations.getOneEntry("nseId", FastList.newListWith(stockName));
		
		JdoDbOperations<StopLossDbObject> stopLossDbOperations = new JdoDbOperations<>(StopLossDbObject.class);
		final StopLossDbObject stopLossDbObject = stopLossDbOperations.getNullOrOneEntry("stockName", FastList.newListWith(stockName));
		ModifiableStockAttributes modifiableStockAttributes = Adapter.getModifiableStockAttributes(allScripsDbObject, stopLossDbObject);

		return FastList.newListWith(modifiableStockAttributes);
	}
	
	@ApiMethod(name = "updateStockAttributes", path="updateStockAttributes", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse updateStockAttributes(
			@Named("stockName") String stockName,
			@Nullable @Named("moneycontrolName") String moneycontrolName, 
			@Nullable @Named("isWatchListed") String isWatchListed,
			@Nullable @Named("lowerReturnPercentTarget") float lowerReturnPercentTarget,
			@Nullable @Named("upperReturnPercentTarget") float upperReturnPercentTarget,
			@Nullable @Named("stockRatings") List<String> stockRatings
			)
	
	{
		LOG.info("arguments: moneycontrolName: "+moneycontrolName
				+" isWatchListed: "+isWatchListed+" lowerReturnPercentTarget: "+lowerReturnPercentTarget+
				" upperReturnPercentTarget: "+ upperReturnPercentTarget+
				" stockRatings: "+stockRatings);
		final boolean isValidMoneyControlName = StringUtil.isValidValue(moneycontrolName);
		final boolean isValidWatchListEntry = StringUtil.isValidValue(isWatchListed);
		final boolean isValidLowerReturnPercentTarget = StopLossDbObject.isValidTarget(lowerReturnPercentTarget);
		final boolean isValidUpperReturnPercentTarget = StopLossDbObject.isValidTarget(upperReturnPercentTarget);
		final boolean isValidStockRatings= stockRatings!=null && !stockRatings.isEmpty();
		
		LOG.info("isValidMoneyControlName: "+isValidMoneyControlName+" isValidWatchListEntry: "+
		" isValidLowerReturnPercentTarget: "+isValidLowerReturnPercentTarget +
				" isValidUpperReturnPercentTarget: "+isValidUpperReturnPercentTarget+" isValidStockRatings: "+isValidStockRatings);
		
		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();
		
		if (isValidMoneyControlName ||  isValidWatchListEntry || isValidStockRatings)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
			List<AllScripsDbObject> allScripsDbObjects  = (List<AllScripsDbObject>)q.execute(stockName);
			final AllScripsDbObject matchingScrip = allScripsDbObjects.get(0);
			if(isValidMoneyControlName)
			{
				LOG.info("setting new MoneyControlname: "+moneycontrolName);
				matchingScrip.setMoneycontrolName(moneycontrolName);	
			}
			
			if(isValidWatchListEntry)
			{
				LOG.info("setting new isWatchListed: "+isWatchListed.equalsIgnoreCase("yes"));
				matchingScrip.setWatchListed(isWatchListed.equalsIgnoreCase("yes"));	
			}
			
			if(isValidStockRatings)
			{
				LOG.info("isValidStockRatings");
				matchingScrip.mergeRatings(stockRatings);
				
				
//				for (int i=0; i<stockRatings.size()-1; i=+2)
//				{
//					final String ratingName = stockRatings.get(i);
//					final String ratingValue = stockRatings.get(i+1);
//					
//					if(StringUtil.isValidValue(ratingName) && StringUtil.isValidValue(ratingValue))
//					{
//						LOG.info("ratingName: "+ratingName+" ratingValue: "+ratingValue);
//						final StockRatingValuesEnum enumForRatingValue = StockRatingValuesEnum.getEnumForRatingDescription(ratingValue);
//						ratingsToValue.put(ratingName, enumForRatingValue);
//					}
//				}
			}
			
//			LOG.info("!ratingsToValue.isEmpty() "+!ratingsToValue.isEmpty());
//			
//			if(!ratingsToValue.isEmpty())
//			{
//				final Map<String, StockRatingValuesEnum> ratingNameToValueBeforeUpdate = matchingScrip.getRatingNameToValue();
//				final Map<String, StockRatingValuesEnum> ratingNameToValueToBeUpdated = UnifiedMap.newMap();
//				for (Map.Entry<String, StockRatingValuesEnum> eachMapEntry : ratingsToValue.entrySet())
//				{
//					ratingNameToValueBeforeUpdate.get(eachMapEntry.getKey());
//				}
//				
//				LOG.info("updating matchingScrip rating");
//				matchingScrip.setRatingNameToValue(ratingsToValue);	
//			}
			
			LOG.info("pm.close");
			pm.close();
		}
		
		if(isValidLowerReturnPercentTarget ||  isValidUpperReturnPercentTarget)
		{
			final JdoDbOperations<StopLossDbObject> stopLossOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);
			LOG.info("stopLossOperations.deleteEntries");
			stopLossOperations.deleteEntries("stockName", FastList.newListWith(stockName));
			
			StopLossDbObjectBuilder builder = new StopLossDbObjectBuilder().stockName(stockName);
			if(isValidLowerReturnPercentTarget)
			{
				builder.lowerReturnPercentTarget(lowerReturnPercentTarget);
			}
			if(isValidUpperReturnPercentTarget)
			{
				builder.upperReturnPercentTarget(upperReturnPercentTarget);
			}
			LOG.info("stopLossOperations.deleteEntries");
			stopLossOperations.insertEntries(FastList.newListWith(builder.build()));
		}
		
		LOG.info("return success message");
		return new EndPointResponse(true, "all is well");

	}

}
