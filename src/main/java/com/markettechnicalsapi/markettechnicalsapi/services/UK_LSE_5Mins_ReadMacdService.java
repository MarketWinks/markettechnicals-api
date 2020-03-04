package com.markettechnicalsapi.markettechnicalsapi.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markettechnicalsapi.markettechnicalsapi.model.uk_lse_5mins_livemarketmacd;
import com.markettechnicalsapi.markettechnicalsapi.repository.UK_LSE_5Mins_LiveMarketMacdRepository;;

@RestController
@RequestMapping("/uk_lse_5mins/macd")
public class UK_LSE_5Mins_ReadMacdService {

	@Autowired
	private UK_LSE_5Mins_LiveMarketMacdRepository UK_LSE_5Mins_LiveMarketMacdRepository;

	@org.springframework.scheduling.annotation.Async
	@RequestMapping(value = "/read/{symbol}", method = RequestMethod.GET)
	public List<JSONObject> UK_LSE_5Mins_ReadMacdParser(@PathVariable String symbol) {

		List<JSONObject> execution_result = null;

		int MarketFeedsSizeForSymbol = 0;

		List<uk_lse_5mins_livemarketmacd> MarketFeeds_full = UK_LSE_5Mins_LiveMarketMacdRepository.findAll();

		try {

			List<uk_lse_5mins_livemarketmacd> MarketFeeds = new ArrayList<uk_lse_5mins_livemarketmacd>();

			for (int i = 0; i < MarketFeeds_full.size(); i++) {

				if (MarketFeeds_full.get(i).getSymbol().equals(symbol)) {
					MarketFeedsSizeForSymbol++;
				}

			}

			for (int j = 0; j < MarketFeeds_full.size(); j++) {

				if (MarketFeeds_full.get(j).getSymbol().equals(symbol)) {
					MarketFeeds.add(MarketFeeds_full.get(j));
				}

			}

			Collections.sort(MarketFeeds, new SortbyLatestTime());

			List<JSONObject> macdData = new ArrayList<>();
			
			//limiting it to 400 records in the output json
			
			int iteratorSize = 400;
			
			if(MarketFeedsSizeForSymbol < 400) {
			iteratorSize = 	MarketFeedsSizeForSymbol;
			}

			for (int index = MarketFeedsSizeForSymbol-1; index > MarketFeedsSizeForSymbol-iteratorSize; index--) {

				// LOGIC
				JSONObject obj_inner = new JSONObject();
				obj_inner.put("MACD_Hist", MarketFeeds.get(index).getHistogram().toString());
				obj_inner.put("MACD", MarketFeeds.get(index).getMacd().toString());
				obj_inner.put("MACD_Signal", MarketFeeds.get(index).getSignal().toString());
				obj_inner.put("Price", MarketFeeds.get(index).getPrice().toString());
				// System.out.println(obj_inner);

				JSONObject obj_outer = new JSONObject();
				obj_outer.put(MarketFeeds.get(index).getTime().toString(), obj_inner);

				macdData.add(obj_outer);

			}

//			System.out.println(macdData);

			execution_result = macdData;
		} catch (Exception e) {

			System.out.println(e);
		}

		return execution_result;

	}

}

class SortbyLatestTime implements Comparator<uk_lse_5mins_livemarketmacd> {
	public int compare(uk_lse_5mins_livemarketmacd a, uk_lse_5mins_livemarketmacd b) {
		return a.getTime().compareTo(b.getTime());
	}
}
