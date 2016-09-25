package com.finanalyzer.endpoints;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.internal.runners.model.EachTestNotifier;

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
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.servlet.CombineNDaysHistoryAndUnrealizedController;
import com.finanalyzer.servlet.NDaysHistoryController;
import com.finanalyzer.servlet.UnRealizedPnLController;
import com.finanalyzer.util.Adapter;
import com.finanalyzer.util.ReaderUtil;
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
		String stockNameUpperCase= stockName.toUpperCase();
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<>(AllScripsDbObject.class);
		final AllScripsDbObject allScripsDbObject = allScripsDbOperations.getOneEntry("nseId", FastList.newListWith(stockNameUpperCase));
		
		JdoDbOperations<StopLossDbObject> stopLossDbOperations = new JdoDbOperations<>(StopLossDbObject.class);
		final StopLossDbObject stopLossDbObject = stopLossDbOperations.getNullOrOneEntry("stockName", FastList.newListWith(stockNameUpperCase));
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
			}

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
		return new EndPointResponse(true, "Updated Attributes!!!");

	}

	@ApiMethod(name = "uploadUnrealized", path="uploadUnrealized", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse uploadUnrealized(@Named("unrealized") String unrealized)
	{
		LOG.info("unrealized: "+unrealized);
		List<String> rowsWithoutHeader = ReaderUtil.convertToList(unrealized, true, 0);
		
		final double inputTotalInvestment = getInputTotalnvestment(rowsWithoutHeader);
		
		final List<String> rowsWithoutHeaderAndTrailer = rowsWithoutHeader.subList(0, rowsWithoutHeader.size()-1);
		final List<UnrealizedDbObject> unrealizedDbObjects = Adapter.
				convertMoneyControlDownloadToUnrealizedDbObjects(rowsWithoutHeaderAndTrailer);
		
		JdoDbOperations<UnrealizedDbObject> unrealizeddbOperations = 
				new JdoDbOperations<UnrealizedDbObject>(UnrealizedDbObject.class);
		unrealizeddbOperations.deleteEntries();
		
		final Collection<UnrealizedDbObject> insertedEntries = unrealizeddbOperations.insertEntries(unrealizedDbObjects);
		final double insertedTotalInvestment = getInsertedTotalInvestment(insertedEntries);
		
		return getResponse(inputTotalInvestment, insertedTotalInvestment);
	}
	

	@ApiMethod(name = "refresh", path="refresh", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse refresh()
	{
		
		String message = "";
		try
		{
			final UnRealizedPnLController unRealizedPnLController = new UnRealizedPnLController();
			unRealizedPnLController.unRealizedPnL(null, null);
			message = message+"unRealizedPnL";
			
			final NDaysHistoryController nDaysHistoryController = new NDaysHistoryController();
			nDaysHistoryController.nDaysHistory(null, null);
			message = message+"nDaysHistory";

			final CombineNDaysHistoryAndUnrealizedController combineController = new CombineNDaysHistoryAndUnrealizedController();
			combineController.combineNDaysHistoryAndUnrealized(null, null);
			message = message+"combineController";
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return new EndPointResponse(false, message);
		}
		
		return new EndPointResponse(true, message);
	}
	

	//helper methods
	private EndPointResponse getResponse(final double inputTotalInvestment,
			final double insertedTotalInvestment) {
		final double differenceBetweenInputAndInserted = Math.abs(insertedTotalInvestment-inputTotalInvestment);
		if(differenceBetweenInputAndInserted<=10)
		{
			return new EndPointResponse(true, "Uploaded!!!");
		}
		return new EndPointResponse(false, "Mismatch: "+differenceBetweenInputAndInserted);
	}

	private double getInputTotalnvestment(List<String> rowsWithoutHeader) {
		final String trailer = rowsWithoutHeader.get(rowsWithoutHeader.size()-1);
		final String[] trailerSplit = trailer.split(Adapter.DELIMITER_IN_UNREALIZED_UPLOAD);
		final double totalValueAsPerInput = Double.valueOf(trailerSplit[8]);
		return totalValueAsPerInput;
	}
	
	private double getInsertedTotalInvestment(Collection<UnrealizedDbObject> insertedEntries)
	{
		double totalInvestment = 0.0d;
		for (UnrealizedDbObject unrealizedDbObject : insertedEntries)
		{
			LOG.info("unrealizedDbObject: "+unrealizedDbObject);
			totalInvestment=totalInvestment+unrealizedDbObject.getBuyQuantity()*unrealizedDbObject.getBuyPrice();
		}
		return totalInvestment;
	}

}
