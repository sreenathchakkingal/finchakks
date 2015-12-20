package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class RatingDbObject {

	@Persistent
	private String ratingName;

	public RatingDbObject(String ratingName) {
		this.ratingName = ratingName;
	}

	public String getRatingName() {
		return ratingName;
	}

}
