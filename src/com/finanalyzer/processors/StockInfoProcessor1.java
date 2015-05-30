package com.finanalyzer.processors;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import com.finanalyzer.domain.DateValueObject;
import com.finanalyzer.domain.InterestReturn;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.UrlUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class StockInfoProcessor1 implements Processor<Stock>
{
	private final String stockName;
	private InputStream stockInfo;

	public StockInfoProcessor1(String stockName, InputStream stockInfo)
	{
		this.stockName = stockName;
		this.stockInfo = stockInfo;
	}

	public StockInfoProcessor1(String stockName)
	{
		this.stockName = stockName;
	}

	@Override
	public Stock execute()
	{
		List<String> closePriceRows = this.fetchClosePriceInfo();
		List<DateValueObject> dividends = this.fetchDividendInfo();
		List<String> opinions = this.fetchOpinions();
		Stock stock = this.createStockDetails(closePriceRows, dividends, opinions);

		return stock;
	}

	private Stock createStockDetails(List<String> closePriceRows, List<DateValueObject> dividends, List<String> opinions)
	{
		int numberOfRows = closePriceRows.size();

		String prevYear = "N/A";

		float netReturns = 0.0f;
		List<InterestReturn> returns = FastList.newList();
		int ups = 0;
		int downs = 0;
		for (int i = 1; i < numberOfRows - 1; i += 12)
		{
			String year = ReaderUtil.parseForDate(closePriceRows.get(i));
			if (closePriceRows.get(i).split(",").length <= 5) {
				break;
			}
			float currentClosePrice = ReaderUtil.parseForClosePrice(closePriceRows.get(i)).floatValue();
			float prevClosePrice;
			if (i + 12 < closePriceRows.size())
			{
				prevClosePrice = ReaderUtil.parseForClosePrice(closePriceRows.get(i + 12)).floatValue();
				prevYear = ReaderUtil.parseForDate(closePriceRows.get(i + 12));
			}
			else
			{
				prevClosePrice = 0.0f;
			}
			if (!year.equalsIgnoreCase(prevYear))
			{
				float returnForYear = CalculatorUtil.calculateQuaterlyCompoundedInterest(prevClosePrice, currentClosePrice, 365L);
				returns.add(new InterestReturn(year, prevYear,returnForYear));
				netReturns += returnForYear;
			}
			if (currentClosePrice > prevClosePrice) {
				ups++;
			} else {
				downs++;
			}
		}
		netReturns /= returns.size();

		return new StockBuilder().name(this.stockName).interestReturns(returns)
				.averageInterestReturn(netReturns).ups(ups).downs(downs)
				.dividend(dividends).graphToEquityOpinion(opinions.get(0)).reportedNetProfitOpinion(opinions.get(1))
				.debtToEquityOpinion(opinions.get(2)).build();
	}

	private List<String> fetchOpinions()
	{
		String row = this.fetchInterestingRowFromStatusInfoSpreadsheet();
		String graphToEquityOpinion = ReaderUtil.fetchGraphOpinion(row);
		String reportedNetProfitOpinion = ReaderUtil.fetchReportedNetProfitOpinion(row);
		String debtToEquityOpinion = ReaderUtil.fetchDebtToEquityOpinion(row);

		return FastList.newListWith(new String[] { graphToEquityOpinion, reportedNetProfitOpinion, debtToEquityOpinion });
	}

	private List<String> fetchClosePriceInfo()
	{
		String downloadLink = null;
		URL yahooDownloadUrl = null;
		try
		{
			downloadLink = UrlUtil.getYahooUrlForTenYearsClosePrices(this.stockName);
			yahooDownloadUrl = new URL(downloadLink);
			InputStream closePriceInfo = yahooDownloadUrl.openConnection().getInputStream();

			return ReaderUtil.convertToList(closePriceInfo);
		}
		catch (Throwable localThrowable) {}
		return null;
	}

	private String fetchInterestingRowFromStatusInfoSpreadsheet()
	{
		List<String> rows = ReaderUtil.convertToList(this.stockInfo);
		for (String eachRow : rows) {
			if (Pattern.compile(Pattern.quote(this.stockName), 2).matcher(eachRow).find()) {
				return eachRow;
			}
		}
		return null;
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
