package com.pickeat.adapters.out.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface KitchenOrderMongoRepository extends MongoRepository<KitchenOrderDocument, String> {
}
