package com.sa.storage.storageservice.repository;

import com.sa.storage.storageservice.entity.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, String> {
}
