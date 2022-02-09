package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class EventService{

  private final EventRepo repo;

  public EventService(EventRepo repo) {
    this.repo = repo;
  }

  public Mono<Event> save(Event item) {
    return repo.save(item);
  }

  public Mono<Void> deleteById(String id) {
    return repo.deleteById(id);
  }

  public Mono<Event> findById(String id) {
    return repo.findById(id);
  }

  public Flux<Event> findAll() {
    return repo.findAll();
  }

  public Flux<Event> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAllBy(pageable);
  }

}
