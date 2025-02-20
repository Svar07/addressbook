package org.example.addressbook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {
 @Autowired
    WebTestClient webTestClient;
 @Test
    void contextLoads(){
     assertThat(webTestClient).isNotNull();
 }
 @Test
    void testFindAll(){
     webTestClient.get()
             .uri("/api/v1/addressbookS")
             .exchange()
             .expectStatus().isOk();
 }
}