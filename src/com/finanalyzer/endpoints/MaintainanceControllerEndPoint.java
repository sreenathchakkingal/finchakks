package com.finanalyzer.endpoints;

import java.util.List;
import java.util.Map;
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
		LOG.info("MaintainanceControllerEndPoint.updateStockAttributes arguments: moneycontrolName: "+moneycontrolName
				+" isWatchListed: "+isWatchListed+" lowerReturnPercentTarget: "+lowerReturnPercentTarget+
				" upperReturnPercentTarget: "+ upperReturnPercentTarget+
				" stockRatings: "+stockRatings);
		boolean isValidMoneyControlName = StringUtil.isValidValue(moneycontrolName);
		boolean isValidWatchListEntry = StringUtil.isValidValue(isWatchListed);
		final boolean isValidLowerReturnPercentTarget = StopLossDbObject.isValidTarget(lowerReturnPercentTarget);
		final boolean isValidUpperReturnPercentTarget = StopLossDbObject.isValidTarget(upperReturnPercentTarget);
		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();
		
		if (isValidMoneyControlName ||  isValidWatchListEntry)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
			List<AllScripsDbObject> allScripsDbObjects  = (List<AllScripsDbObject>)q.execute(stockName);
			final AllScripsDbObject matchingScrip = allScripsDbObjects.get(0);
			if(isValidMoneyControlName)
			{
				matchingScrip.setMoneycontrolName(moneycontrolName);	
			}
			
			if(isValidWatchListEntry)
			{
				matchingScrip.setWatchListed(isWatchListed.equalsIgnoreCase("yes"));	
			}
			
			for (int i=0; i<stockRatings.size()-1; i=+2)
			{
				final String ratingName = stockRatings.get(i);
				final String ratingValueAsString = stockRatings.get(i+1);
				
				if(StringUtil.isValidValue(ratingName) && StringUtil.isValidValue(ratingValueAsString))
				{
					int ratingValue = Integer.parseInt (ratingValueAsString);
					final StockRatingValuesEnum enumForRatingValue = StockRatingValuesEnum.getEnumForRatingValue(ratingValue);
					ratingsToValue.put(ratingName, enumForRatingValue);
				}
			}
			
			if(!ratingsToValue.isEmpty())
			{
				matchingScrip.setRatingNameToValue(ratingsToValue);	
			}
			
			pm.close();
		}
		
		if(isValidLowerReturnPercentTarget ||  isValidUpperReturnPercentTarget)
		{
			final JdoDbOperations<StopLossDbObject> stopLossOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);
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
			stopLossOperations.insertEntries(FastList.newListWith(builder.build()));
		}
		
		
		return new EndPointResponse(true, "all is well");

	}

}
