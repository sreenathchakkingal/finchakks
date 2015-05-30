package com.finanalyzer.domain;

public class AutoCompleteData
{
	private final String label;
	private final String value;

	public AutoCompleteData(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public final String getLabel() {
		return this.label;
	}

	public final String getValue() {
		return this.value;
	}

}
