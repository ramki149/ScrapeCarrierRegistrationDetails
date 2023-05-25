package com.census.cargo.resource;

/*
 * Author: @ram
 * 
 * Rest Controller for API Gateway
 * 
 * Provides REST based real time web scraping for individual DOT numbers
 * 
 * Provides REST based START operation for bulk DOT numbers processing.
 * 
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.census.cargo.csv.ReadFromCsv;
import com.census.cargo.scrape.VehicleTypeBreakdownInfo;

@RestController
public class CensusApiGateway
{
	@Autowired
	private VehicleTypeBreakdownInfo vehicleTypeBreakdownInfo;
	
	@Autowired
	private ReadFromCsv readFromCsv;
	
	@GetMapping("/carrier/{dot_number}")
	public String postCarrierRegistration(@PathVariable String dot_number) {
		vehicleTypeBreakdownInfo.scrape(dot_number);
		return dot_number;
	}
	
	@GetMapping("/executebatch")
	public String executeBulkCarrierRegistration() {
		readFromCsv.processMotorCarrierCensusBulkInformation();
		return "Success!";
	}
}