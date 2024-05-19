package com.example.CompanySearch.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.CompanySearch.Entity.AdressEntity;
import com.example.CompanySearch.Entity.CompanyEntity;
import com.example.CompanySearch.Entity.OfficeEntity;
import com.example.CompanySearch.Model.Address;
import com.example.CompanySearch.Model.Company;
import com.example.CompanySearch.Model.Officer;
import com.example.CompanySearch.Repository.CompanyRepository;

@Service
public class TruProxyService {

	private static final Logger logger = LoggerFactory.getLogger(TruProxyService.class);

	@Value("${truproxy.api.key}")
	private String apiKey;

	@Value("${truproxy.api.base-url}")
	private String baseUrl;

	private final RestTemplate restTemplate;
	private final CompanyRepository companyRepository;

	public TruProxyService(RestTemplate restTemplate, CompanyRepository companyRepository) {
		this.restTemplate = restTemplate;
		this.companyRepository = companyRepository;
	}

	public Company searchCompany(String companyName, String companyNumber, boolean onlyActive) {
		try {
			CompanyEntity cachedCompany = companyRepository.findById(companyNumber).orElse(null);
			if (cachedCompany != null) {
				logger.info("Returning cached company data for companyNumber: {}", companyNumber);
				return convertToModel(cachedCompany);
			}

			String searchUrl = baseUrl + "/Search?Query=" + (companyNumber != null ? companyNumber : companyName);
			Company[] companies = restTemplate.getForObject(searchUrl, Company[].class);

			if (companies != null && companies.length > 0) {
				Company company = companies[0];
				if (onlyActive && !"active".equalsIgnoreCase(company.getCompanyStatus())) {
					return null;
				}

				company.setOfficers(getOfficers(company.getCompanyNumber()));

				// Save company to the database
				CompanyEntity companyEntity = convertToEntity(company);
				companyRepository.save(companyEntity);

				return company;
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			logger.error("API request error: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage());
			throw new RuntimeException("Unexpected error", e);
		}
		return null;
	}

	private List<Officer> getOfficers(String companyNumber) {
		try {
			String officersUrl = baseUrl + "/Officers?CompanyNumber=" + companyNumber;
			Officer[] officers = restTemplate.getForObject(officersUrl, Officer[].class);
			return officers != null ? List.of(officers).stream().filter(officer -> officer.getAppointedOn() != null)
					.collect(Collectors.toList()) : List.of();
		} catch (Exception e) {
			logger.error("Failed to fetch officers: {}", e.getMessage());
			throw new RuntimeException("Failed to fetch officers", e);
		}
	}

	private Company convertToModel(CompanyEntity companyEntity) {
		Company company = new Company();
		company.setCompanyNumber(companyEntity.getCompanyNumber());
		company.setCompanyType(companyEntity.getCompanyType());
		company.setTitle(companyEntity.getTitle());
		company.setCompanyStatus(companyEntity.getCompanyStatus());
		company.setDateOfCreation(companyEntity.getDateOfCreation());

		Address address = new Address();
		address.setLocality(companyEntity.getAddress().getLocality());
		address.setPostalCode(companyEntity.getAddress().getPostalCode());
		address.setPremises(companyEntity.getAddress().getPremises());
		address.setAddressLine1(companyEntity.getAddress().getAddressLine1());
		address.setCountry(companyEntity.getAddress().getCountry());
		company.setAddress(address);

		List<Officer> officers = companyEntity.getOfficers().stream().map(officerEntity -> {
			Officer officer = new Officer();
			officer.setName(officerEntity.getName());
			officer.setOfficerRole(officerEntity.getOfficerRole());
			officer.setAppointedOn(officerEntity.getAppointedOn());

			Address officerAddress = new Address();
			officerAddress.setLocality(officerEntity.getAddress().getLocality());
			officerAddress.setPostalCode(officerEntity.getAddress().getPostalCode());
			officerAddress.setPremises(officerEntity.getAddress().getPremises());
			officerAddress.setAddressLine1(officerEntity.getAddress().getAddressLine1());
			officerAddress.setCountry(officerEntity.getAddress().getCountry());
			officer.setAddress(officerAddress);

			return officer;
		}).collect(Collectors.toList());
		company.setOfficers(officers);

		return company;
	}

	private CompanyEntity convertToEntity(Company company) {
		CompanyEntity companyEntity = new CompanyEntity();
		companyEntity.setCompanyNumber(company.getCompanyNumber());
		companyEntity.setCompanyType(company.getCompanyType());
		companyEntity.setTitle(company.getTitle());
		companyEntity.setCompanyStatus(company.getCompanyStatus());
		companyEntity.setDateOfCreation(company.getDateOfCreation());

		AdressEntity addressEntity = new AdressEntity();
		addressEntity.setLocality(company.getAddress().getLocality());
		addressEntity.setPostalCode(company.getAddress().getPostalCode());
		addressEntity.setPremises(company.getAddress().getPremises());
		addressEntity.setAddressLine1(company.getAddress().getAddressLine1());
		addressEntity.setCountry(company.getAddress().getCountry());
		companyEntity.setAddress(addressEntity);

		List<OfficeEntity> officerEntities = company.getOfficers().stream().map(officer -> {
			OfficeEntity officerEntity = new OfficeEntity();
			officerEntity.setName(officer.getName());
			officerEntity.setOfficerRole(officer.getOfficerRole());
			officerEntity.setAppointedOn(officer.getAppointedOn());

			AdressEntity officerAddress = new AdressEntity();
			officerAddress.setLocality(officer.getAddress().getLocality());
			officerAddress.setPostalCode(officer.getAddress().getPostalCode());
			officerAddress.setPremises(officer.getAddress().getPremises());
			officerAddress.setAddressLine1(officer.getAddress().getAddressLine1());
			officerAddress.setCountry(officer.getAddress().getCountry());
			officerEntity.setAddress(officerAddress);

			return officerEntity;
		}).collect(Collectors.toList());
		companyEntity.setOfficers(officerEntities);

		return companyEntity;
	}
}
