package com.finanalyzer.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.gs.collections.impl.list.mutable.FastList;

@Controller  
public class InsertStopLossController
{
	@RequestMapping("/insertStopLoss") 
	public ModelAndView insertStopLoss(HttpServletRequest request, HttpServletResponse response)
	{
		final JdoDbOperations<StopLossDbObject> stopLossOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);
		stopLossOperations.deleteEntries();
		
		StopLossDbObject BATAINDIA =new StopLossDbObjectBuilder().
				stockName("BATAINDIA").lowerReturnPercentTarget(3.0f).build();
		
		StopLossDbObject YESBANK =new StopLossDbObjectBuilder().
				stockName("YESBANK").lowerReturnPercentTarget(40.0f).upperReturnPercentTarget(60.0f).build();

		StopLossDbObject SYNDIBANK =new StopLossDbObjectBuilder().
				stockName("SYNDIBANK").upperReturnPercentTarget(6.0f).build();

		StopLossDbObject SBIN =new StopLossDbObjectBuilder().
				stockName("SBIN").upperReturnPercentTarget(11.5f).build();

		StopLossDbObject INDUSINDBK =new StopLossDbObjectBuilder().
				stockName("INDUSINDBK").lowerReturnPercentTarget(30.0f).upperReturnPercentTarget(40.0f)
				.achieveAfterDate("2016-12-31").build();
		
		StopLossDbObject HDFCBANK =new StopLossDbObjectBuilder().
				stockName("HDFCBANK").lowerReturnPercentTarget(20.0f).upperReturnPercentTarget(25.0f)
				.achieveAfterDate("2016-12-31").build();
		
		StopLossDbObject ITC =new StopLossDbObjectBuilder().
				stockName("ITC").lowerReturnPercentTarget(3.0f).upperReturnPercentTarget(10.0f).build();
		
		StopLossDbObject TCS =new StopLossDbObjectBuilder().
				stockName("TCS").lowerReturnPercentTarget(4.0f).upperReturnPercentTarget(15.0f).build();
		
		//based on summary
		StopLossDbObject SUNPHARMA =new StopLossDbObjectBuilder().
				stockName("SUNPHARMA").lowerReturnPercentTarget(5.0f).upperReturnPercentTarget(15.0f).build();

		StopLossDbObject HDFC =new StopLossDbObjectBuilder().
				stockName("HDFC").lowerReturnPercentTarget(20.0f).upperReturnPercentTarget(25.0f).build(); //summary hence not looking at date
		
		StopLossDbObject GODREJCP =new StopLossDbObjectBuilder().
				stockName("GODREJCP").lowerReturnPercentTarget(24.0f).upperReturnPercentTarget(30.0f).build(); 

		StopLossDbObject LUPIN =new StopLossDbObjectBuilder().
				stockName("LUPIN").lowerReturnPercentTarget(5.0f).upperReturnPercentTarget(10.0f).build();
		
		StopLossDbObject BERGEPAINT =new StopLossDbObjectBuilder().
				stockName("BERGEPAINT").lowerReturnPercentTarget(45.0f).upperReturnPercentTarget(50.0f).build();
		
		StopLossDbObject INFY =new StopLossDbObjectBuilder().
				stockName("INFY").lowerReturnPercentTarget(5.0f).upperReturnPercentTarget(15.0f).build();	
		
		StopLossDbObject ASIANPAINT =new StopLossDbObjectBuilder().
				stockName("ASIANPAINT").lowerReturnPercentTarget(30.0f).upperReturnPercentTarget(40.0f).build();	
		
		StopLossDbObject EICHERMOT =new StopLossDbObjectBuilder().
				stockName("EICHERMOT").lowerReturnPercentTarget(60.0f).upperReturnPercentTarget(65.0f).build();	
		
		StopLossDbObject IPCALAB =new StopLossDbObjectBuilder().
				stockName("IPCALAB").upperReturnPercentTarget(0.0f).build();	
		
		StopLossDbObject MARICO =new StopLossDbObjectBuilder().
				stockName("MARICO").lowerReturnPercentTarget(45.0f).upperReturnPercentTarget(55.0f).build();	
		
		StopLossDbObject EMAMILTD =new StopLossDbObjectBuilder().
				stockName("EMAMILTD").lowerReturnPercentTarget(25.0f).upperReturnPercentTarget(35.0f).build();		
		
		StopLossDbObject GLENMARK =new StopLossDbObjectBuilder().
				stockName("GLENMARK").lowerReturnPercentTarget(20.0f).upperReturnPercentTarget(25.0f).build();	
		
		StopLossDbObject KAJARIACER =new StopLossDbObjectBuilder().
				stockName("KAJARIACER").lowerReturnPercentTarget(45.0f).upperReturnPercentTarget(52.0f).build();
		
		StopLossDbObject AMARAJABAT =new StopLossDbObjectBuilder().
				stockName("AMARAJABAT").lowerReturnPercentTarget(20.0f).upperReturnPercentTarget(25.0f).build();	
		
		StopLossDbObject FINCABLES =new StopLossDbObjectBuilder().
				stockName("FINCABLES").lowerReturnPercentTarget(40.0f).upperReturnPercentTarget(45.0f).build();		
		
		StopLossDbObject HAVELLS =new StopLossDbObjectBuilder().
				stockName("HAVELLS").lowerReturnPercentTarget(35.0f).upperReturnPercentTarget(45.0f).build();
		
		StopLossDbObject MOTHERSUMI =new StopLossDbObjectBuilder().
				stockName("MOTHERSUMI").lowerReturnPercentTarget(10.0f).upperReturnPercentTarget(18.0f).build();
		
		StopLossDbObject NESTLEIND =new StopLossDbObjectBuilder().
				stockName("NESTLEIND").lowerReturnPercentTarget(15.0f).upperReturnPercentTarget(20.0f).build();
		
		StopLossDbObject PIDILITIND =new StopLossDbObjectBuilder().
				stockName("PIDILITIND").lowerReturnPercentTarget(25.0f).upperReturnPercentTarget(35.0f).build();
		
		StopLossDbObject TATAGLOBAL =new StopLossDbObjectBuilder().
				stockName("TATAGLOBAL").upperReturnPercentTarget(0.0f).build();
		
		StopLossDbObject HCLTECH =new StopLossDbObjectBuilder().
				stockName("HCLTECH").upperReturnPercentTarget(0.0f).build();
		
		StopLossDbObject ICICIBANK =new StopLossDbObjectBuilder().
				stockName("ICICIBANK").upperReturnPercentTarget(0.0f).build();
		
		StopLossDbObject TITAN =new StopLossDbObjectBuilder().
				stockName("TITAN").lowerReturnPercentTarget(10.0f).upperReturnPercentTarget(20.0f).build();
		
		StopLossDbObject NAUKRI =new StopLossDbObjectBuilder().
				stockName("NAUKRI").lowerReturnPercentTarget(10.0f).upperReturnPercentTarget(15.0f).build();
		
		StopLossDbObject CASTROLIND =new StopLossDbObjectBuilder().
				stockName("CASTROLIND").upperReturnPercentTarget(0.0f).build();
		
		List<StopLossDbObject> stopLossDbObjects = FastList.newList();
		stopLossDbObjects.add(BATAINDIA);
		stopLossDbObjects.add(YESBANK);
		stopLossDbObjects.add(SYNDIBANK);
		stopLossDbObjects.add(SBIN);
		stopLossDbObjects.add(INDUSINDBK);
		stopLossDbObjects.add(HDFCBANK);
		stopLossDbObjects.add(ITC);
		stopLossDbObjects.add(HDFC);
		stopLossDbObjects.add(TCS);
		stopLossDbObjects.add(SUNPHARMA);
		stopLossDbObjects.add(ASIANPAINT);
		stopLossDbObjects.add(INFY);
		stopLossDbObjects.add(GODREJCP);
		stopLossDbObjects.add(LUPIN);
		stopLossDbObjects.add(BERGEPAINT);
		stopLossDbObjects.add(EICHERMOT);
		stopLossDbObjects.add(IPCALAB);
		stopLossDbObjects.add(MARICO);
		stopLossDbObjects.add(EMAMILTD );
		stopLossDbObjects.add(GLENMARK);
		stopLossDbObjects.add(KAJARIACER);
		stopLossDbObjects.add(AMARAJABAT);
		stopLossDbObjects.add(FINCABLES);
		stopLossDbObjects.add(HAVELLS);
		stopLossDbObjects.add(MOTHERSUMI);
		stopLossDbObjects.add(NESTLEIND);
		stopLossDbObjects.add(PIDILITIND);
		stopLossDbObjects.add(TATAGLOBAL);
		stopLossDbObjects.add(HCLTECH);
		stopLossDbObjects.add(ICICIBANK);
		stopLossDbObjects.add(TITAN);
		stopLossDbObjects.add(NAUKRI);
		stopLossDbObjects.add(CASTROLIND);

		stopLossOperations.insertEntries(stopLossDbObjects);
		
		final List<StopLossDbObject> stocks = stopLossOperations.getEntries("stockName");
		
		return new ModelAndView("insertStopLoss", "stocks", stocks); 
	}
}
