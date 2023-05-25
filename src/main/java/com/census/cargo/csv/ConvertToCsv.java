package com.census.cargo.csv;

import java.io.FileWriter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;

/*
 * Author: @ram
 * 
 * Component to provide WRITE operation for CSV file
 */
@Component
public class ConvertToCsv
{
	//Method to support Collection based write
	public void writeToCsv(List<String[]> list, String name) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(name + ".csv", true))) {
            writer.writeAll(list);
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	//Method to support Array based write
	public void writeToCsv(String[] rows, String name) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(name + ".csv", true))) {
            writer.writeNext(rows);
        }catch(Exception e) {
        	e.printStackTrace();
        }
		
	}
}