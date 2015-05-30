package com.finanalyzer.domain;
import java.util.Map;

public class NDaysPrice
  implements Comparable<NDaysPrice>
{
  private String stockName;
  private Map<String, String> dateToCloseValue;
  
  public NDaysPrice(String stockName, Map<String, String> dateToCloseValue)
  {
    this.stockName = stockName;
    this.dateToCloseValue = dateToCloseValue;
  }
  
  public String getStockName()
  {
    return this.stockName;
  }
  
  public Map<String, String> getDateToCloseValue()
  {
    return this.dateToCloseValue;
  }
  
  public void setDateToCloseValue(Map<String, String> dateToCloseValue)
  {
    this.dateToCloseValue = dateToCloseValue;
  }
  
  public String toString()
  {
    String str = this.stockName + "\n";
    for (Map.Entry<String, String> entry : this.dateToCloseValue.entrySet()) {
      str = str + (String)entry.getKey() + " : " + (String)entry.getValue() + "\n";
    }
    return str;
  }
  
  public int compareTo(NDaysPrice that)
  {
    double netGainThat = Double.valueOf(((String)that.dateToCloseValue.get("NET GAIN")).replace("%", "")).doubleValue();
    double netGainThis = Double.valueOf(((String)getDateToCloseValue().get("NET GAIN")).replace("%", "")).doubleValue();
    if (netGainThis < netGainThat) {
      return -1;
    }
    if (netGainThis > netGainThat) {
      return 1;
    }
    return 0;
  }
}
