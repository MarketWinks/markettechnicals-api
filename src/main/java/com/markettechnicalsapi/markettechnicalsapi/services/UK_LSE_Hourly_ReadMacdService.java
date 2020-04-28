package com.markettechnicalsapi.markettechnicalsapi.services;

import java.util.Comparator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markettechnicalsapi.markettechnicalsapi.model.uk_lse_hourly_livemarketmacd;
import com.markettechnicalsapi.markettechnicalsapi.repository.UK_LSE_Hourly_LiveMarketMacdjsonRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RestController
@RequestMapping("/uk_lse_hourly/macd")
public class UK_LSE_Hourly_ReadMacdService {

	@Autowired
	private UK_LSE_Hourly_LiveMarketMacdjsonRepository UK_LSE_Hourly_LiveMarketMacdjsonRepository;

	@org.springframework.scheduling.annotation.Async
	@RequestMapping(value = "/read/{symbol}", method = RequestMethod.GET)
	public JSONObject UK_LSE_Hourly_ReadMacdParser(@PathVariable String symbol) {

		JSONObject execution_result = null;

		MongoClient mongoClient = MongoClients.create(
				"mongodb+srv://marketwinks:L9sS6oOAk1sHL0yi@aws-eu-west1-cluster-tszuq.mongodb.net/marketwinksdbprod?retryWrites=true");

		try {

			MongoDatabase TestDB = mongoClient.getDatabase("marketwinksdbprod");
			MongoCollection<org.bson.Document> uk_lse_hourly_livemarketmacdjsonCollection = TestDB
					.getCollection("uk_lse_hourly_livemarketmacdjson");

			// find one document with new Document
			org.bson.Document doc = uk_lse_hourly_livemarketmacdjsonCollection
					.find(new org.bson.Document("macdjsonref", "uk_lse_hourly_macdjson_" + symbol)).first();

			String docext = doc.toJson().toString();

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(docext);

			execution_result = json;

		} catch (Exception e) {

			System.out.println(e);
		} finally {
			mongoClient.close();

		}

		return execution_result;

	}

}

class SortbyLatestTime_hourly implements Comparator<uk_lse_hourly_livemarketmacd> {
	public int compare(uk_lse_hourly_livemarketmacd a, uk_lse_hourly_livemarketmacd b) {
		return a.getTime().compareTo(b.getTime());
	}
}