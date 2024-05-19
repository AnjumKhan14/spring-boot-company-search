package com.example.CompanySearch.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.CompanySearch.Model.Company;
import com.example.CompanySearch.Service.TruProxyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	private TruProxyService truProxyService;

	@PostMapping("/search")
	public ResponseEntity<?> searchCompany(@RequestBody SearchRequest searchRequest,
			@RequestHeader("x-api-key") String apiKey, @RequestParam boolean onlyActive) {
		try {
			Company company;
			if (searchRequest.getCompanyNumber() != null) {
				company = truProxyService.searchCompany(null, searchRequest.getCompanyNumber(), onlyActive);
			} else if (searchRequest.getCompanyName() != null) {
				company = truProxyService.searchCompany(searchRequest.getCompanyName(), null, onlyActive);
			} else {
				return ResponseEntity.badRequest().body("Either companyName or companyNumber must be provided.");
			}
			if (company == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(company);
		} catch (Exception e) {
			logger.error("Error during company search", e);
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
}
