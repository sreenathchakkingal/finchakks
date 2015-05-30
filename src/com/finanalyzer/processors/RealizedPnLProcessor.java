package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class RealizedPnLProcessor extends PnLProcessor
{

	public RealizedPnLProcessor(InputStream statusInputStream)
	{
		super(statusInputStream);
	}

	@Override
	public FastList<Stock> execute()
	{
		List<String> rowsWithoutHeader= ReaderUtil.convertToList(this.statusInputStream, true, false);
		FastList<Stock> stocks = FastList.newList();
		for (String eachRow : rowsWithoutHeader)
		{
			stocks.add(this.convertRowToStockObject(eachRow));
		}
		return stocks;
	}


	private Stock convertRowToStockObject(String row)
	{
		Stock stock = null;
		String[] stockAttributes = row.split(",");
		String name = ReaderUtil.parseStockName(stockAttributes[0]);
		int quantity = Integer.valueOf(stockAttributes[3]).intValue();

		float buyPrice = Float.valueOf(stockAttributes[4]);
		String buyDate = DateUtil.convertToStandardFormat("d/M/yyyy",stockAttributes[1]);

		float sellPrice = Float.valueOf(stockAttributes[5]);
		String sellDate = DateUtil.convertToStandardFormat("d/M/yyyy",stockAttributes[2]);

		stock = new StockBuilder().name(name).quantity(quantity).buyPrice(buyPrice).buyDate(buyDate).
				sellDate(sellDate).sellPrice(sellPrice).
				build();
		return stock;
	}

}
