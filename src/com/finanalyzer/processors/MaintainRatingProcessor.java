package com.finanalyzer.processors;

import java.util.List;

import com.finanalyzer.db.CoreDb;
import com.finanalyzer.db.RatingDb;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class MaintainRatingProcessor implements Processor<List<String>>
{

	private final String rating;
	private final String[] ratingsToBeRemoved;

	public MaintainRatingProcessor(String ratingToBeAdded, String[] ratingsToBeRemoved)
	{
		this.rating = ratingToBeAdded;
		this.ratingsToBeRemoved = ratingsToBeRemoved;
	}

	@Override
	public List<String> execute()
	{
		CoreDb<String, String[]> ratingDb = new RatingDb();
		if (StringUtil.isValidValue(ratingsToBeRemoved)) {
			ratingDb.removeEntries(this.ratingsToBeRemoved);
			
		}
		if (StringUtil.isValidValue(this.rating)) {
			ratingDb.addEntry(FastList.newListWith(this.rating));
		}
		return ratingDb.retrieveAllEntries();
	}

}