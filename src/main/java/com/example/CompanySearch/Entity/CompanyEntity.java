package com.example.CompanySearch.Entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class CompanyEntity {

	@Id
	private String companyNumber;
	private String companyType;
	private String title;
	private String companyStatus;
	private String dateOfCreation;
	@Embedded
	private AdressEntity address;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OfficeEntity> officers;

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}

	public String getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public AdressEntity getAddress() {
		return address;
	}

	public void setAddress(AdressEntity address) {
		this.address = address;
	}

	public List<OfficeEntity> getOfficers() {
		return officers;
	}

	public void setOfficers(List<OfficeEntity> officers) {
		this.officers = officers;
	}

}
