package com.markettechnicalsapi.markettechnicalsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.markettechnicalsapi.markettechnicalsapi.model.uk_lse_hourly_livemarketmacdjson;

@Repository
public interface UK_LSE_Hourly_LiveMarketMacdjsonRepository
		extends MongoRepository<uk_lse_hourly_livemarketmacdjson, String> {
}
