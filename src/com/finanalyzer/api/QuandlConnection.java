package com.finanalyzer.api;

import java.util.Set;
import java.util.logging.Logger;

import com.finanalyzer.util.UrlUtil;
import com.google.appengine.labs.repackaged.com.google.common.base.Joiner;



public class QuandlConnection
{
	private static final Logger LOG = Logger.getLogger(QuandlConnection.class.getName());

	private static final String QUESTION_MARK = "?";
	private static final String SINGLE_QUERY_URL = "https://www.quandl.com/api/v1/datasets/%s%s.json?";
	//	private static final String MULTI_QUERY_URL = "https://quandl.com/api/v1/multisets.json?transformation=rdiff&columns=";
	private static final String MULTI_QUERY_URL = "https://quandl.com/api/v1/multisets.json?columns=";
	private static final Joiner.MapJoiner URL_ARG_JOINER = Joiner.on('&').withKeyValueSeparator("=");
	private static final String TOKEN = "VHyaJFRgExWGKQYtacdP";

	//https://quandl.com/api/v1/multisets.csv?columns=WIKI.AAPL.4,DOE.RWTC.1&trim_start=2012-11-01&trim_end=2012-11-30
	//https://www.quandl.com/api/v1/datasets/PRAGUESE/PX.csv?trim_start=2012-11-01&trim_end=2012-11-30

	public QDataset getDataset(SimpleQuery sq) {
		String args = URL_ARG_JOINER.join(sq.getParameterMap());
		String url = this.withAuthToken(String.format(SINGLE_QUERY_URL,sq.getStockExchange().getSingleDatePrefix(),sq.getQCode())+args);
		System.out.println("url : "+url);
		LOG.info("url : "+url);
		return new QDataset(UrlUtil.getResponseAsString(url));
	}

	public QDataset getDataset(MultisetQuery sq) {
		Set<String> qCodes = sq.getQCodes();
		String transformedQCodes=UrlUtil.prefixIndexAndSuffixClosePriceIndex(qCodes,sq.getStockExchange());

		String args = URL_ARG_JOINER.join(sq.getParameterMap());
		String url = this.withAuthToken(MULTI_QUERY_URL+transformedQCodes+args);
		System.out.println("url : "+url);
		String response = UrlUtil.getResponseAsString(url);
		QDataset qDataset = new QDataset(response);
		return qDataset;
	}

	private String withAuthToken(String url) 
	{
		return url + (url.contains(QUESTION_MARK) ? "&" : QUESTION_MARK) + "auth_token=" + TOKEN;
	}
}

