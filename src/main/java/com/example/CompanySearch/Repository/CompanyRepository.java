package com.example.CompanySearch.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CompanySearch.Entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

}
