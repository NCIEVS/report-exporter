package gov.nih.nci.evs.report.exporter.model;

import java.util.Date;

public class Survey {
	
	Date date;
	int rating;
	String email;
	String recommendatons;
	String features;
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRecommendatons() {
		return recommendatons;
	}
	public void setRecommendatons(String recommendatons) {
		this.recommendatons = recommendatons;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}


}
