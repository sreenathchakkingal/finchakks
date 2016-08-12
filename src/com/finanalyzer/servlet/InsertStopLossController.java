package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.finanalyzer.util.Adapter;
import com.google.gson.JsonObject;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

@Controller  
public class InsertStopLossController
{
	private static final float ZERO = 0.0f;
	
	@RequestMapping("/insertStopLoss") 
	public ModelAndView insertStopLoss(HttpServletRequest request, HttpServletResponse response)
	{
		StopLossDbObject stopLossDbObject = new StopLossDbObject("BATAINDIA",2.4f,ZERO, null);
		List<StopLossDbObject> stopLossDbObjects = FastList.newList();
		stopLossDbObjects.add(stopLossDbObject);
		
		final JdoDbOperations<StopLossDbObject> stopLossOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);
		stopLossOperations.deleteEntries();
		stopLossOperations.insertEntries(stopLossDbObjects);
		
		final List<StopLossDbObject> stocks = stopLossOperations.getEntries("stockName");
		
		return new ModelAndView("insertStopLoss", "stocks", stocks); 
	}
}
