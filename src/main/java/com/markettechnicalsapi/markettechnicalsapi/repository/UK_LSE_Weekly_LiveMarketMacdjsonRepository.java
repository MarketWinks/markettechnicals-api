package com.markettechnicalsapi.markettechnicalsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.markettechnicalsapi.markettechnicalsapi.model.uk_lse_weekly_livemarketmacdjson;

@Repository
public interface UK_LSE_Weekly_LiveMarketMacdjsonRepository
		extends MongoRepository<uk_lse_weekly_livemarketmacdjson, String> {
}
