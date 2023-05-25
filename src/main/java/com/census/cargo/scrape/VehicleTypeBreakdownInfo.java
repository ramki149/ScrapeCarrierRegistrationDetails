package com.census.cargo.scrape;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.census.cargo.csv.ConvertToCsv;

/*
 * Author: @ram
 * 
 * Component to provide basic web page scraping
 * 
 */
@Component
public class VehicleTypeBreakdownInfo
{
	@Autowired
	private ConvertToCsv convertToCsv;

	public void scrape(String carrierId) {
		try {
			Document doc = Jsoup.connect("https://ai.fmcsa.dot.gov/SMS/Carrier/" + carrierId + "/CarrierRegistration.aspx").get();
			Elements elements = doc.getAllElements();
			for(Element element: elements) {
				if(element.id().equalsIgnoreCase("regInfo"))
				{
					Elements el = element.getElementsByClass("cargo").first().getAllElements();
					String[] cargosCarried = new String[el.size() + 1];
					int index = 0;
					for(Element item:el) {
						if(index == 0) {
							cargosCarried[0] = String.valueOf(carrierId);
							index++;
							continue;
						}
						if(item.text().trim().length() > 1) {
							cargosCarried[index] = item.text().trim();
							index++;
						}
					}

					convertToCsv.writeToCsv(cargosCarried, "CargoCarried");

					List<String[]> list = new ArrayList<String[]>();
					Elements rows = element.select("tr");
					for (int i = 1; i < rows.size(); i++) {
						Element row = rows.get(i);
						Elements cols = row.select("td");

						String[] record = new String[cols.size() + 2];
						record[0] = String.valueOf(carrierId);
						record[1] = row.select("th").text().trim();
						for(int j = 0;j<cols.size();j++) {
							record[j + 2] = cols.get(j).text().trim();
						}
						list.add(record);
					}
					convertToCsv.writeToCsv(list, "VehicleTypeBreakdown");
					break;

				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Bulk dot_numbers processing
	 */
	public void scrape(String[] dot_numbers) {
		for(String dot_number:dot_numbers) {
			this.scrape(dot_number);
		}
	}
}