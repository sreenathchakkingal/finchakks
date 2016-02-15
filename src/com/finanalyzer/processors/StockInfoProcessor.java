package com.finanalyzer.processors;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.finanalyzer.domain.Adjustment;
import com.finanalyzer.domain.AdjustmentType;
import com.finanalyzer.domain.DateValueObject;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.UrlUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class StockInfoProcessor
implements Processor<Stock>
{
	private final String stockName;
	private final Integer numberOfYears;
	private Adjustment splitAdjustment;
	private Adjustment bonusAdjustment;
	private List<DateValueObject> historicalClosePrices;


	public StockInfoProcessor(String stockName, String numberOfYears, String splitRatios, 
			String latesetSplitYear, String bonusRatios, String latestBonusYear)
	{
		this.stockName = stockName;
		this.numberOfYears= numberOfYears==null ? null : Integer.parseInt(numberOfYears);

		if(splitRatios!=null && latesetSplitYear !=null && !"".equals(splitRatios) && !"".equals(latesetSplitYear))
		{
			this.splitAdjustment= new Adjustment(AdjustmentType.SPLIT, splitRatios, Integer.parseInt(latesetSplitYear));
		}

		if(bonusRatios!=null && latestBonusYear !=null && !"".equals(bonusRatios) && !"".equals(latestBonusYear) )
		{
			this.bonusAdjustment= new Adjustment(AdjustmentType.BONUS, bonusRatios, Integer.parseInt(latestBonusYear));
		}
	}

	public StockInfoProcessor(String stockName, String numberOfYears, String historicalClosePrices)
	{
		this.stockName = stockName;
		this.numberOfYears= numberOfYears==null ? null : Integer.parseInt(numberOfYears);
		this.historicalClosePrices=ReaderUtil.parseHistoricalClosePrices(historicalClosePrices, this.numberOfYears);
	}

	@Override
	public Stock execute()
	{
		Stock stock = new Stock(this.stockName);
		stock.setDateToClosePrice(this.historicalClosePrices);
		this.fetchAndstampDividendInfo(stock);
		return stock;
	}


	private void fetchAndstampDividendInfo(Stock stock)
	{
		List<DateValueObject> dividends = this.fetchDividendInfo();
		stock.setDividends(dividends);
	}

	private List<DateValueObject> fetchDividendInfo()
	{
		String downloadLink = null;
		URL yahooDownloadUrl = null;
		try
		{
			downloadLink = UrlUtil.getYahooUrlForTenYearsDividends(this.stockName);
			yahooDownloadUrl = new URL(downloadLink);
			InputStream dividendInfo = yahooDownloadUrl.openConnection().getInputStream();

			List<String> dividendRows = ReaderUtil.convertToList(dividendInfo);
			List<DateValueObject> dividends = FastList.newList();
			for (int i = 1; i < dividendRows.size(); i++)
			{
				String date = ReaderUtil.parseForDate(dividendRows.get(i));
				float dividendPerShare = ReaderUtil.parseForDividend(dividendRows.get(i)).floatValue();
				dividends.add(new DateValueObject(date, dividendPerShare));
			}
			return dividends;
		}
		catch (Throwable localThrowable) {}
		return null;
	}

}
