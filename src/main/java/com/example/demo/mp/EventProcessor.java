package com.example.demo.mp;

import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.function.Function;


@Slf4j
@Component
public class EventProcessor {
  private final ObjectMapper mapper;
  private final EventService service;

  public EventProcessor(EventService service) {
    this.service = service;
    this.mapper = new ObjectMapper();
  }

  @Bean
  public Function<Map<String,Object>, Mono<Event>> processEvent() {
    return map -> {
      log.info("processing: {}", map);
      return service.save(mapper.convertValue(map,Event.class));
    };
  }

}

