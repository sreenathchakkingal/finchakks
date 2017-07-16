package com.finanalyzer.api;

import java.util.Map;

import com.finanalyzer.domain.StockExchange;
import com.google.appengine.labs.repackaged.com.google.common.base.Optional;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class SimpleQuery 
{
	protected String qCode;

	private SortOrder sort;
	private Optional<Integer> rows;
	private StockExchange stockExchange;
	private Optional<String> start;
	private Optional<String> end;
	private Optional<Integer> column;
	private Collapse collapse;
	private Transform transform;

	public static enum SortOrder {DEFAULT, ASC, DESC}
	public static enum Collapse {NONE, DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUAL}
	public static enum Transform {NONE, DIFF, RDIFF, CUMUL, NORMALIZE}

	SimpleQuery() 
	{
		this.sort = SortOrder.DEFAULT;
		this.rows = Optional.absent();
		this.start = Optional.absent();
		this.end = Optional.absent();
		this.column = Optional.absent();
		this.collapse = Collapse.NONE;
		this.transform = Transform.NONE;
		this.stockExchange=StockExchange.NSE;
	}

	SimpleQuery(String qCode) 
	{
		this();
		this.qCode=qCode;
	}

	private SimpleQuery sortOrder(SortOrder so) {
		this.sort = so;
		return this;
	}

	public SimpleQuery ascending() {
		return this.sortOrder(SortOrder.ASC);
	}

	public SimpleQuery descending() {
		return this.sortOrder(SortOrder.DESC);
	}

	public SimpleQuery numRows(Optional<Integer> rs) {
		this.rows = rs;
		return this;
	}

	public SimpleQuery stockExchange(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
		return this;
	}

	public SimpleQuery allResults() {
		return this.numRows(Optional.<Integer>absent());
	}

	public SimpleQuery numRows(int rs) {
		return this.numRows(Optional.of(rs));
	}

	public SimpleQuery mostRecentRow() {
		return this.numRows(1);
	}

	public SimpleQuery allDates() {
		return this.dateRange(Optional.<String>absent(), Optional.<String>absent());
	}

	public SimpleQuery startDate(String st) {
		return this.dateRange(Optional.of(st), this.end);
	}

	public SimpleQuery noStartDate() {
		return this.dateRange(Optional.<String>absent(), this.end);
	}

	public SimpleQuery endDate(String en) {
		return this.dateRange(this.start, Optional.of(en));
	}

	public SimpleQuery noEndDate() {
		return this.dateRange(this.start, Optional.<String>absent());
	}

	public SimpleQuery dateRange(String st, String en) {
		return this.dateRange(Optional.of(st), Optional.of(en));
	}

	private SimpleQuery dateRange(Optional<String> st, Optional<String> en) {
		this.start = st;
		this.end = en;
		return this;
	}

	public SimpleQuery column(Optional<Integer> column) {
		this.column = column;
		return this;
	}

//	public SimpleQuery column(int column) {
//		return this.column(Optional.of(column));
//	}

	public SimpleQuery allColumns() {
		return this.column(Optional.<Integer>absent());
	}

	public SimpleQuery collapse(Collapse clp) {
		this.collapse = clp;
		return this;
	}

	public SimpleQuery transform(Transform transform) {
		this.transform =transform;
		return this;
	}

	public final Map<String,String> getParameterMap() 
	{
		Map<String, String> parameters = UnifiedMap.newMap();

		if(!this.sort.equals(SortOrder.DEFAULT)) {
			parameters.put("sort_order", this.sort.toString().toLowerCase());
		}
		if(this.rows.isPresent()) {
			parameters.put("rows", String.valueOf(this.rows.get()));
		}
		if(this.start.isPresent()) {
			parameters.put("trim_start", this.start.get().toString());
		}
		if(this.end.isPresent()) {
			parameters.put("trim_end", this.end.get().toString());
		}
		parameters.put("column", String.valueOf(this.stockExchange.getQuandlClosePriceColumnPosition()));

		if(!this.collapse.equals(Collapse.NONE)) {
			parameters.put("collapse", this.collapse.toString().toLowerCase());
		}
		if(!this.transform.equals(Transform.NONE)) {
			parameters.put("transform", this.transform.toString().toLowerCase());
		}

		return parameters;
	}

	public String getQCode()
	{
		return this.qCode;
	}

	public StockExchange getStockExchange()
	{
		return this.stockExchange;
	}


}
