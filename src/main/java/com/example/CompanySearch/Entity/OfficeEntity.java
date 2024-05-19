package com.example.CompanySearch.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class OfficeEntity {

	private String name;
	private String officerRole;
	private String appointedOn;

	@Embedded
	private AdressEntity address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficerRole() {
		return officerRole;
	}

	public void setOfficerRole(String officerRole) {
		this.officerRole = officerRole;
	}

	public String getAppointedOn() {
		return appointedOn;
	}

	public void setAppointedOn(String appointedOn) {
		this.appointedOn = appointedOn;
	}

	public AdressEntity getAddress() {
		return address;
	}

	public void setAddress(AdressEntity address) {
		this.address = address;
	}

}
