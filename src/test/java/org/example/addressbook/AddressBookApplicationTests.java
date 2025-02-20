package org.example.addressbook;

import org.example.addressbook.model.AddressBook;
import org.example.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureMockMvc
class AddressBookApplicationTests {
	@Autowired
	AddressBookRepository repository;

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
				.expectStatus().isOk()
				.expectBody() // указание "[]" проверяет пустая ли база данных
				.json("""
[{"id":-2,"firstName":"Petr","lastName":"Petrov","phone":"+79111111111","birthday":"1990-01-01"},
{"id":-1,"firstName":"Aleksey","lastName":"Alekseev","phone":"+79000000000","birthday":"1980-01-01"}]
""");
	}
	@Test
	void testSave() {
		AddressBook addressBook = new AddressBook(null, "Ivan", "Ivanov", "+799999999999", LocalDate.parse("2000-01-01"));

		long sizeBefore = repository.findAll().count().block();  // block используется чтобы превратить реактивный метод в обычный синхронный

		webTestClient.post()
				.uri("/api/v1/addresbook")
				.bodyValue(addressBook)
				.header("Content-Type", "application/json")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.json("""
                 {
                     "firstName": "Ivan",
                      "lastName": "Ivanov",
                      "phone": "+799999999999",
                      "birthday": "2000-01-01"
                                              }
                                                  """);

		long sizeAfter = repository.findAll().count().block();
		assertEquals(sizeBefore+1, sizeAfter);
		// можно добавить assertTrue(sizeBefore < sizeAfter); для проверки если вызывать несколько методов добавляющих
		// данные в БД и тогда может не выполнится услове sizeBefore+1, sizeAfter так как не смог выполнится один из этих методов
	}
}

