package com.finanalyzer.domain.jdo;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

//creating a dummy persitence class because ndayshistory.jsp expects stock.stockRatingValue.score
@PersistenceCapable
public class DummyStockRatingValue implements Serializable{

	@Persistent
	public String score;

	public DummyStockRatingValue(String score) {
		super();
		this.score = score;
	}

	public String getScore() {
		return score;
	}

}
