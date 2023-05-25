package com.census.cargo.csv;

import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.census.cargo.scrape.VehicleTypeBreakdownInfo;
import com.opencsv.CSVReader;

/*
 * Author: @ram
 * 
 * Component to provide READ operation from CSV file.
 * 
 * Support bulk read from .csv file for dot_numbers
 * 
 * Executes batch job in a thread to support async execution
 * 
 */
@Component
public class ReadFromCsv {

	@Autowired
	private VehicleTypeBreakdownInfo vehicleTypeBreakdownInfo;

	public void processMotorCarrierCensusBulkInformation() {
		//Run batch on a thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					CSVReader reader = new CSVReader(new FileReader("FMCSA_CENSUS1_2023Apr.csv"));
					List<String[]> allRows = reader.readAll();
					String[] dot_number_arr = new String[allRows.size()];
					int index = 0;
					for (String[] row : allRows) {
						dot_number_arr[index++] = row[0];
					}
					vehicleTypeBreakdownInfo.scrape(dot_number_arr);

				}catch(Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}