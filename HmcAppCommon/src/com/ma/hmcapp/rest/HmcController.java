package com.ma.hmcapp.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.GsonBuilder;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.entity.Hmc;

@RestController
public class HmcController extends BaseController {

	@Autowired
	private HmcRepository hmcRepository;

	@GetMapping(value = "/hmc")
	public List<Hmc> getAll(Pageable pageable, @RequestParam Map<String, String> searchParams) {
		List<Hmc> page = hmcRepository.findAll(getExample(Hmc.class, searchParams), pageable).getContent();
		shortenRefs(page);
		return page;
	}

	@GetMapping(value = "/hmc2")
	public Hmc xxx() {
		Hmc hmc = hmcRepository.findById(115l).get();
		
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(hmc));
		
		
		Company company = hmc.getCompany();
		
//		hmc.company = new Company();
//		hmc.company.id = company.getId();
		
		System.out.println(company.getName());
		
		return hmc;
		//System.out.println(hmc.company.name);
		
	}
	
	
}
