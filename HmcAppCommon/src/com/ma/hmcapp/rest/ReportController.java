package com.ma.hmcapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.hmcapp.entity.rfid.Report;

@RestController
public class ReportController extends BaseController {

	@Autowired
	private ReportRepository reportRepository;

	@GetMapping(value = "/report2")
	public Report xxx() {
		Report hmc = reportRepository.findById(1l).get();
		System.out.println(hmc.getCompany().getName());
		return hmc;
	}
	
	
}
