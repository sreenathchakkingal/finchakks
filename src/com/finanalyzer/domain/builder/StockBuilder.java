package com.finanalyzer.domain.builder;

import java.util.List;

import com.finanalyzer.domain.DateValueObject;
import com.finanalyzer.domain.InterestReturn;
import com.finanalyzer.domain.Stock;

public class StockBuilder
{
  private final Stock stock = new Stock();
  
  public StockBuilder dividend(List<DateValueObject> dividends)
  {
    this.stock.setDividends(dividends);
    return this;
  }
  
  public StockBuilder name(String stockName)
  {
    this.stock.setStockName(stockName);
    return this;
  }
  
  public StockBuilder interestReturns(List<InterestReturn> interestReturns)
  {
    this.stock.setInterestReturns(interestReturns);
    return this;
  }
  
  public StockBuilder averageInterestReturn(float averageInterestReturn)
  {
    this.stock.setAverageInterestReturn(averageInterestReturn);
    return this;
  }
  
  public StockBuilder ups(int ups)
  {
    this.stock.setUps(ups);
    return this;
  }
  
  public StockBuilder downs(int downs)
  {
    this.stock.setDowns(downs);
    return this;
  }
  
  public StockBuilder buyDate(String buyDate)
  {
    this.stock.setBuyDate(buyDate);
    return this;
  }
  
  public StockBuilder buyPrice(float buyPrice)
  {
    this.stock.setBuyPrice(buyPrice);
    return this;
  }
  
  public StockBuilder sellPrice(float sellPrice)
  {
    this.stock.setSellPrice(sellPrice);
    return this;
  }
  
  public StockBuilder quantity(float quantity)
  {
    this.stock.setQuantity(quantity);
    return this;
  }
  
  public StockBuilder totalInvestment(float totalInvestment)
  {
    this.stock.setTotalInvestment(totalInvestment);
    return this;
  }
  
  public StockBuilder totalReturn(float totalReturn)
  {
    this.stock.setTotalReturn(totalReturn);
    return this;
  }
  
  public StockBuilder totalReturnIfBank(float totalReturnIfBank)
  {
    this.stock.setTotalReturnIfBank(totalReturnIfBank);
    return this;
  }
  
  public StockBuilder sellDate(String sellDate)
  {
    this.stock.setSellDate(sellDate);
    return this;
  }
  
  public StockBuilder graphToEquityOpinion(String graphToEquityOpinion)
  {
    this.stock.setGraphToEquityOpinion(graphToEquityOpinion);
    return this;
  }
  
  public StockBuilder reportedNetProfitOpinion(String reportedNetProfitOpinion)
  {
    this.stock.setReportedNetProfitOpinion(reportedNetProfitOpinion);
    return this;
  }
  
  public StockBuilder debtToEquityOpinion(String debtToEquityOpinion)
  {
    this.stock.setDebtToEquityOpinion(debtToEquityOpinion);
    return this;
  }
  
  public StockBuilder sellableQuantity(float sellableQuantity)
  {
    this.stock.setSellableQuantity(sellableQuantity);
    return this;
  }
  
  public StockBuilder returnTillDate(float returnTillDate)
  {
    this.stock.setReturnTillDate(returnTillDate);
    return this;
  }
  
  public StockBuilder blackListed(boolean isBlackListed)
  {
    this.stock.setBlackListed(isBlackListed);
    return this;
  }
  
  public StockBuilder diff(float diff)
  {
    this.stock.setDiff(diff);
    return this;
  }
  
  public Stock build()
  {
    return this.stock;
  }
}
