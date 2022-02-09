package com.example.demo.repository;

import reactor.core.publisher.Flux;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.example.demo.model.Event;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends ReactiveMongoRepository<Event, String> {

	Flux<Event> findAllBy(Pageable pageable);
}