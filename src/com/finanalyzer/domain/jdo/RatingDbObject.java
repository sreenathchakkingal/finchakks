package com.finanalyzer.domain.jdo;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class RatingDbObject implements Serializable{

	@Persistent
	private String ratingName;

	public RatingDbObject(String ratingName) {
		this.ratingName = ratingName;
	}

	public String getRatingName() {
		return ratingName;
	}

}
