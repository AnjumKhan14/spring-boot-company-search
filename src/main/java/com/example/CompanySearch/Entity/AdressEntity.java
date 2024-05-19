package com.example.CompanySearch.Entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class AdressEntity {

	private String locality;
	private String postalCode;
	private String premises;
	private String addressLine1;
	private String country;

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPremises() {
		return premises;
	}

	public void setPremises(String premises) {
		this.premises = premises;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
