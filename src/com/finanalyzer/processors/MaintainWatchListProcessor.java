package com.finanalyzer.processors;

import java.util.List;

import com.finanalyzer.db.WatchListUtil;

public class MaintainWatchListProcessor implements Processor<List<String>>
{

	private final String stockId;
	private final String[] stocksToBeRemoved;

	public MaintainWatchListProcessor(String stockIdToBeAdded, String[] stockIdsToBeRemoved)
	{
		this.stockId = stockIdToBeAdded;
		this.stocksToBeRemoved = stockIdsToBeRemoved;
	}

	@Override
	public List<String> execute()
	{
		WatchListUtil watchListUtil = new WatchListUtil();
		watchListUtil.removeFromWatchList(this.stocksToBeRemoved);
		watchListUtil.addToWatchList(this.stockId);
		return watchListUtil.retrieveWatchList();

	}

	// public static void doItOnce(InputStream stocksAsInputStream)
	// {
	// List<String> watchListFromFile =
	// ReaderUtil.converInputReaderToList(stocksAsInputStream);
	// WatchListUtil.addAllOneTime(watchListFromFile);
	//
	// }

}
