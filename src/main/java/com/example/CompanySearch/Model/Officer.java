package com.example.CompanySearch.Model;

public class Officer {

	private String name;
	private String officerRole;
	private String appointedOn;
	private Address address;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
