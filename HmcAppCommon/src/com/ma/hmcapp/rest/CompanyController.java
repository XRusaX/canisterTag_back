package com.ma.hmcapp.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ma.hmcapp.entity.Company;

@RestController
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;

	@GetMapping(value = "/company")
	@ResponseBody
	public List<Company> getAll(@RequestParam Map<String,String> searchParams) {
		List<Company> list = new ArrayList<>();
		int page = Integer.valueOf(searchParams.get("page"));
		int size = Integer.valueOf(searchParams.get("size"));
		
		PageRequest pageRequest = PageRequest.of(page, size);  
		
		companyRepository.findAll(pageRequest).forEach(list::add);
		return list;
	}

}
