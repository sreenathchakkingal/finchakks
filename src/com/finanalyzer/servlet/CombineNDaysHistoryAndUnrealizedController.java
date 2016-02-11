package com.finanalyzer.servlet;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;


@Controller  
public class CombineNDaysHistoryAndUnrealizedController{

	@RequestMapping("/combineNDaysHistoryAndUnrealized")  
	public ModelAndView combineNDaysHistoryAndUnrealized(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);

		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName");
		final List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = unrealizedDetailDbOperations.getEntries("buyDate asc");
		
//		final List<UnrealizedSummaryDbObject> blackListedStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_BLACKLISTED);
		List<NDaysHistoryDbObject> ndaysHistoryDbObjects =null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(NDaysHistoryDbObject.class);
			ndaysHistoryDbObjects = (List<NDaysHistoryDbObject>)q.execute();
			
			for (NDaysHistoryDbObject ndaysHistoryDbObject :  ndaysHistoryDbObjects)
			{
				for (UnrealizedSummaryDbObject unrealizedSummaryDbObject :  unrealizedSummaryDbObjects)
				{
					if(unrealizedSummaryDbObject.getStockName().equals(ndaysHistoryDbObject.getMoneyControlName()))
					{
						ndaysHistoryDbObject.setReturnTillDate(unrealizedSummaryDbObject.getReturnTillDate());
						ndaysHistoryDbObject.setImpactOnAverageReturn(unrealizedSummaryDbObject.getImpactOnAverageReturn());
					}
				}
				
				for (UnrealizedDetailDbObject unrealizedDetailDbObject :  unrealizedDetailDbObjects)
				{
					if(unrealizedDetailDbObject.getStockName().equals(ndaysHistoryDbObject.getMoneyControlName()))
					{
						ndaysHistoryDbObject.setDuration(unrealizedDetailDbObject.getDuration());
						ndaysHistoryDbObject.setBuyPrice(unrealizedDetailDbObject.getBuyPrice());
					}
				}
			}
		}
		finally
		{
			pm.close();
		}

		ModelAndView modelAndView = new ModelAndView("test");
		
		return modelAndView;
	}

}
