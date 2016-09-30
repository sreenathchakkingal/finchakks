package com.finanalyzer.endpoints;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.EndPointResponse;
import com.finanalyzer.domain.ModifiableStockAttributes;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.servlet.CombineNDaysHistoryAndUnrealizedController;
import com.finanalyzer.servlet.NDaysHistoryController;
import com.finanalyzer.servlet.UnRealizedPnLController;
import com.finanalyzer.util.ConverterUtil;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;

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
		final StopLossDbObject stopLossDbObject = stopLossDbOperations.getEntries("stockName", FastList.newListWith(stockNameUpperCase), "businessDate desc").get(0);
		ModifiableStockAttributes modifiableStockAttributes = ConverterUtil.getModifiableStockAttributes(allScripsDbObject, stopLossDbObject);

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
			//if it finds the entry for the same bd 
				//see if the same has entry for that bd
				//delete that entry
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(StopLossDbObject.class, ":p.contains(businessDate)");
			List<StopLossDbObject> stopLossObjects  = (List<StopLossDbObject>)q.execute(stockName);
			for(StopLossDbObject eachDbOject : stopLossObjects)
			{
				if(eachDbOject.getStockName().equalsIgnoreCase(stockName))
				{
					LOG.info("StopLossDbObject with same BD and stockName found. Deleting Entry");
					pm.deletePersistent(eachDbOject);
				}
			}
				
//			final JdoDbOperations<StopLossDbObject> stopLossOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);
//			stopLossOperations.deleteEntries("stockName", FastList.newListWith(stockName));

			StopLossDbObjectBuilder builder = new StopLossDbObjectBuilder().stockName(stockName);
			if(isValidLowerReturnPercentTarget)
			{
				builder.lowerReturnPercentTarget(lowerReturnPercentTarget);
			}
			if(isValidUpperReturnPercentTarget)
			{
				builder.upperReturnPercentTarget(upperReturnPercentTarget);
			}
			LOG.info("inserting StopLossDbObject");
			pm.makePersistent(builder.build());
			pm.close();
//			stopLossOperations.insertEntries(FastList.newListWith(builder.build()));
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
		final List<UnrealizedDbObject> unrealizedDbObjects = ConverterUtil.
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
		
		String message = "Refreshed ";
		try
		{
			final UnRealizedPnLController unRealizedPnLController = new UnRealizedPnLController();
			unRealizedPnLController.unRealizedPnL(null, null);
			message = message+"unRealizedPnL";
			
			final NDaysHistoryController nDaysHistoryController = new NDaysHistoryController();
			nDaysHistoryController.nDaysHistory(null, null);
			message = message+" nDaysHistory";

			final CombineNDaysHistoryAndUnrealizedController combineController = new CombineNDaysHistoryAndUnrealizedController();
			combineController.combineNDaysHistoryAndUnrealized(null, null);
			message = message+" combineController";
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return new EndPointResponse(false, message);
		}
		
		return new EndPointResponse(true, message);
	}
	
	@ApiMethod(name = "refreshTargets", path="refreshTargets", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse refreshTargets()
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(StopLossDbObject.class);
			final List<StopLossDbObject> entries =  (List<StopLossDbObject>)q.execute();
			for(StopLossDbObject eachEntry : entries)
			{
				eachEntry.setBusinessDate("2016-09-09");
			}
			pm.close();
		}catch(Throwable t)
		{
			t.printStackTrace();
			return new EndPointResponse(false, "error");
		}

		return new EndPointResponse(true, "done");
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
		final String[] trailerSplit = trailer.split(ConverterUtil.DELIMITER_IN_UNREALIZED_UPLOAD);
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
