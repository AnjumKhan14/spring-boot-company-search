package com.example.CompanySearch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest
@AutoConfigureMockMvc
class CompanySearchApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		WireMock.stubFor(WireMock.get(WireMock.urlMatching("/TruProxyAPI/rest/Companies/v1/Search.*"))
				.willReturn(WireMock.aResponse().withStatus(200).withBodyFile("company-search-response.json")));

		WireMock.stubFor(WireMock.get(WireMock.urlMatching("/TruProxyAPI/rest/Companies/v1/Officers.*"))
				.willReturn(WireMock.aResponse().withStatus(200).withBodyFile("company-officers-response.json")));
	}

	@Test
	void testSearchCompany() throws Exception {
		mockMvc.perform(post("/api/companies/search").header("x-api-key", "test-key").param("onlyActive", "true")
				.content("{\"companyName\" : \"BBC LIMITED\"}").contentType("application/json"))
				.andExpect(status().isOk());
	}
}
