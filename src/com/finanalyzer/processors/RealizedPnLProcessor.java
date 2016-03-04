package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.tuple.Tuples;

public class RealizedPnLProcessor extends PnLProcessor
{

	public RealizedPnLProcessor(InputStream statusInputStream)
	{
		super(statusInputStream);
	}

	@Override
	public Pair<List<Stock>, List<StockExceptionDbObject>> execute()
	{
		List<String> rowsWithoutHeader= ReaderUtil.convertToList(this.statusInputStream, true, 0);
		List<Stock> stocks = FastList.newList();
		List<StockExceptionDbObject> dummyList = FastList.newList();
		for (String eachRow : rowsWithoutHeader)
		{
			stocks.add(this.convertRowToStockObject(eachRow));
		}
		return Tuples.pair(stocks,dummyList);
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
