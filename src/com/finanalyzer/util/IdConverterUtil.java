package com.finanalyzer.util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.finanalyzer.domain.Stock;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class IdConverterUtil
{
  public static void stampYahooId(List<Stock> stocks, InputStream idMappingInputStream)
  {
    Map<String, String> moneyControlToYahooBseMap = getMapOf(idMappingInputStream, 0, 1);
    for (Stock stock : stocks) {
      stock.setStockName(moneyControlToYahooBseMap.get(stock.getStockName()));
    }
  }
  
  private static Map<String, String> getMapOf(InputStream idMappingInputStream, int keyIndex, int valueIndex)
  {
    List<String> rows = ReaderUtil.convertToList(idMappingInputStream);
    Map<String, String> map = UnifiedMap.newMap();
    for (String row : rows)
    {
      String key = row.split(",")[keyIndex];
      String value = row.split(",")[valueIndex];
      map.put(key, value);
    }
    return map;
  }
}
