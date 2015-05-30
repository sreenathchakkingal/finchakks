package com.finanalyzer.api;

import java.util.List;

import com.finanalyzer.domain.StockExchange;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gs.collections.impl.list.mutable.FastList;

public class QDataset
{
	private final String id;
	private final String sourceCode;
	private final String code;
	private final String name;
	private final String urlize_name;
	private final String description;
	private final String updatedAt;
	private final String frequency;
	private final String fromDate;
	private final String toDate;
	private final List<String> columnNames = FastList.newList();
	private final String errors;
	private final String rawData;
	private final List<QEntry> dataset = FastList.newList();
	JsonParser parser = new JsonParser();

	public QDataset(String input)
	{
		JsonObject json = this.parser.parse(input).getAsJsonObject();
		this.id = this.getValueIfNotNull(json, "id");
		this.sourceCode = this.getValueIfNotNull(json, "source_code");
		this.code = this.getValueIfNotNull(json, "code");
		this.name = this.getValueIfNotNull(json, "name");
		this.urlize_name = this.getValueIfNotNull(json, "urlize_name");		
		this.description = this.getValueIfNotNull(json, "description");

		this.updatedAt = this.getValueIfNotNull(json, "updatedAt");
		this.frequency = this.getValueIfNotNull(json, "frequency");
		this.fromDate = this.getValueIfNotNull(json, "from_date");		
		this.toDate = this.getValueIfNotNull(json, "to_date");

		JsonArray tempColNames = (JsonArray) this.parser.parse(json.get("column_names").toString());

		for (Object eachCol : tempColNames)
		{
			this.columnNames.add(eachCol.toString());
		}

		this.errors = json.get("errors").toString();
		this.rawData = json.get("data").toString();

		JsonArray tempDataset = (JsonArray) this.parser.parse(this.rawData);
		for (Object eachRow : tempDataset)
		{
			this.addJsonRow(eachRow.toString());
		}
	}

	public String getValueIfNotNull(JsonObject json, String property)
	{
		return json.get(property)==null ? null : json.get(property).toString();
	}

	public void addJsonRow(String row)
	{
		JsonArray tmp = (JsonArray) this.parser.parse(row);
		this.dataset.add(new QEntry(tmp));
	}

	public List<QEntry> getDataset() {
		return this.dataset;
	}

	public List<String> getColumnNames() {
		return this.columnNames;
	}

	public List<String> getformattedColumnNames(StockExchange stockExchange) {
		List<String> formattedColumnNames = FastList.newList();
		//unformatted column name : "NSE.SYNDIBANK - Close"
		//unformatted column name : "BSE.BOM532540 - Close"
		for (String eachColumnName : this.columnNames)
		{
			//			formattedColumnNames.add(eachColumnName.replace(" - Close", "").replace("NSE.", "").replace("\"", ""));
			formattedColumnNames.add(eachColumnName.replace(" - Close", "").replace(stockExchange.getQuandlMultisetPrefix(), "").replace("\"", ""));
		}
		return formattedColumnNames;
	}

	@Override
	public String toString()
	{
		return "ID:" + this.id + "\nSOURCE CODE:" + this.sourceCode + "\nCODE:" + this.code + "\nNAME:" + this.name + "\nURLIZE NAME:" + this.urlize_name + "\nDESCRIPTION:"
				+ this.description + "\nUPDATED AT:" + this.updatedAt + "\nFREQUENCY:" + this.frequency + "\nFROM DATE:" + this.fromDate + "\nTO DATE:" + this.toDate
				+ "\nERRORS:" + this.errors + "\nRAW DATA:" + this.rawData;
	}
}
