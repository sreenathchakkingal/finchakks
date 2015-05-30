package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.UrlUtil;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class YahooNDaysPricesProcessor
  extends NDaysPricesProcessor
{
 
  public YahooNDaysPricesProcessor(InputStream stocksInputStream, String numOfDays, String source)
  {
    super(stocksInputStream, numOfDays, source);
  }
  
  @Override
public List<NDaysPrice> execute()
  {
    List<String> stocks = ReaderUtil.convertToList(this.stocksInputStream);
    FastList<NDaysPrice> nDaysPrices = FastList.newList();
    for (String stock : stocks) {
      try
      {
        List<String> historicPrices = UrlUtil.getHistoricPricesFromYahoo(Integer.parseInt(this.numOfDays) + 1, stock);
        NDaysPrice nDaysPrice = createNDaysPriceGain(historicPrices, stock);
        
        nDaysPrices.add(nDaysPrice);
      }
      catch (Throwable localThrowable) {}
    }
    return nDaysPrices.sortThis();
  }
  
  @Override
public NDaysPrice createNDaysPriceGain(List<String> rows, String nseStockId)
  {
    float overallGain = 0.0f;
    
    Map<String, String> dateToCloseValue = new UnifiedMap<String, String>();
    dateToCloseValue.put("NET GAIN", "");
    
    int numberOfRows = rows.size();
    for (int i = 1; i < numberOfRows - 1; i++)
    {
      if (rows.get(i).split(",").length <= 5) {
        break;
      }
      float currentClosePrice = ReaderUtil.parseForClosePrice(rows.get(i)).floatValue();
      float prevClosePrice;
      if (i + 1 < rows.size()) {
        prevClosePrice = ReaderUtil.parseForClosePrice(
          rows.get(i + 1)).floatValue();
      } else {
        prevClosePrice = 0.0f;
      }
      float netGain = CalculatorUtil.getNetGain(currentClosePrice, prevClosePrice);
      overallGain += netGain;
      dateToCloseValue.put(ReaderUtil.parseForDate(rows.get(i)), PERCENTAGE_FORMAT.format(netGain));
    }
    dateToCloseValue.put("NET GAIN", PERCENTAGE_FORMAT.format(overallGain));
    return new NDaysPrice(nseStockId, dateToCloseValue);
  }
  
  @Override
public Set<String> getDistinctDates(List<NDaysPrice> nDaysPrices)
  {
    int sizeOfMap = 0;
    int maxSizeOfMap = -1;
    Set<String> distinctDates = new UnifiedSet<String>();
    distinctDates.add("Stock Name");
    
    Set<String> distinctDates1 = UnifiedSet.newSet();
    for (NDaysPrice nDaysPrice : nDaysPrices)
    {
      sizeOfMap = nDaysPrice.getDateToCloseValue().size();
      if (sizeOfMap > maxSizeOfMap)
      {
        distinctDates1 = nDaysPrice.getDateToCloseValue().keySet();
        maxSizeOfMap = sizeOfMap;
      }
    }
    distinctDates.addAll(distinctDates1);
    

    return distinctDates;
  }
}
