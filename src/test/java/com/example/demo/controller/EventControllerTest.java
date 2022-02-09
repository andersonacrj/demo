package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = EventController.class)
@Import(EventService.class)
@ExtendWith(SpringExtension.class)
@Scope("test")
class EventControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  EventService service;

  @Autowired
  private EventController controller;

  Mono<Event> mono;
  Flux<Event> flux;

  @BeforeEach
  void before() {
  EasyRandomParameters parameters = new EasyRandomParameters();
  parameters.setCollectionSizeRange(new EasyRandomParameters.Range<>(2,10));
  EasyRandom generator = new EasyRandom(parameters);

  mono = Mono.just(generator.nextObject(Event.class));
    flux = Flux.just(generator.nextObject(Event.class), generator.nextObject(Event.class));
    Mockito.when(this.service.findAll()).thenReturn(flux);
    Mockito.when(this.service.save(any())).thenReturn(mono);
    Mockito.when(this.service.findById(any())).thenReturn(mono);
  }

  @Test
  void shouldFindById() throws Exception {
    String jsonBlob = objectMapper.writeValueAsString(mono.block());
    WebTestClient.bindToController(controller).build()
      .get().uri("/rest/event/1")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json(jsonBlob);
  }

  @Test
  void shouldFindAll() throws Exception {
    String jsonBlob = objectMapper.writeValueAsString(flux.collectList().block());
    WebTestClient.bindToController(controller).build()
      .get().uri("/rest/event")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json(jsonBlob);
  }

  @Test
  void shouldDeleteById() {
    WebTestClient
      .bindToController(controller)
      .build()
      .delete().uri("/rest/event/1")
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  void shouldSave() throws Exception {
    String jsonBlob = objectMapper.writeValueAsString(mono.block());
    WebTestClient.bindToController(controller).build()
      .post().uri("/rest/event")
      .body(mono,Event.class)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json(jsonBlob);
  }
}
