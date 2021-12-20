package com.ma.hmcapp.rest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ma.hmcapp.entity.Company;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
	List<Company> findByName(String name);
}
