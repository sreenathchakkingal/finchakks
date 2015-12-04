package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class NDaysHistoryDbObject {
	
	@Persistent
	private String nseId;
	
	@Persistent
	private String score;
	
	@Persistent
	private String investmentPercent;
	
	@Persistent
	private String industryInvestment;
	
	@Persistent
	private String decreaseSMV;
	
	@Persistent
	private String netGains;
	
	@Persistent
	private String latestClosePrice;
}

