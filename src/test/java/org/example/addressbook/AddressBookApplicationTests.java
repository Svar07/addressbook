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


@SpringBootTest  // Аннотация @SpringBootTest указывает, что это интеграционный тест, который загружает полный контекст Spring Boot.
@AutoConfigureMockMvc // Аннотация @AutoConfigureMockMvc настраивает MockMvc для тестирования веб-слоя.
class AddressBookApplicationTests {
	@Autowired // Внедрение репозитория для работы с базой данных.
	AddressBookRepository repository;

	@Autowired // Внедрение WebTestClient для выполнения HTTP-запросов и проверки ответов.
	WebTestClient webTestClient;

	@Test  // Тест для проверки загрузки контекста Spring Boot.
	void contextLoads(){
		assertThat(webTestClient).isNotNull(); // Проверяем, что WebTestClient был успешно создан и не равен null.
	}
	@Test // Тест для проверки метода получения всех записей из адресной книги.
	void testFindAll(){
		webTestClient.get() // Выполняем GET-запрос к эндпоинту /api/v1/addressbookS.
				.uri("/api/v1/addressbookS")
				.exchange()// Отправляем запрос.
				.expectStatus().isOk()// Ожидаем статус ответа 200 OK.
				.expectBody() // Проверяем тело ответа.      указание "[]" проверяет пустая ли база данных
				// Ожидаем, что тело ответа будет соответствовать указанному JSON.
				.json("""  
[{"id":-2,"firstName":"Petr","lastName":"Petrov","phone":"+79111111111","birthday":"1990-01-01"},
{"id":-1,"firstName":"Aleksey","lastName":"Alekseev","phone":"+79000000000","birthday":"1980-01-01"}]
""");
	}
	@Test // Тест для проверки метода сохранения новой записи в адресной книге.
	void testSave() {
		// Создаем новый объект AddressBook для сохранения.
		AddressBook addressBook = new AddressBook(null, "Ivan", "Ivanov", "+799999999999", LocalDate.parse("2000-01-01"));
		// Получаем текущее количество записей в базе данных.
		long sizeBefore = repository.findAll().count().block();  // block используется чтобы превратить реактивный метод в обычный синхронный

		webTestClient.post() // Выполняем POST-запрос к эндпоинту /api/v1/addresbook для сохранения новой записи.
				.uri("/api/v1/addresbook")
				.bodyValue(addressBook) // Передаем объект AddressBook в теле запроса.
				.header("Content-Type", "application/json")// Указываем тип содержимого.
				.exchange()// Отправляем запрос.
				.expectStatus().isOk()// Ожидаем статус ответа 200 OK.
				.expectBody()// Проверяем тело ответа.
				// Ожидаем, что тело ответа будет соответствовать указанному JSON.
				.json("""
                 {
                     "firstName": "Ivan",
                      "lastName": "Ivanov",
                      "phone": "+799999999999",
                      "birthday": "2000-01-01"
                                              }
                                                  """);
        // Получаем количество записей в базе данных после сохранения.
		long sizeAfter = repository.findAll().count().block();
		assertEquals(sizeBefore+1, sizeAfter); // Проверяем, что количество записей увеличилось на 1.
		// можно добавить assertTrue(sizeBefore < sizeAfter); // Альтернативная проверка: убеждаемся, что количество записей увеличилось.для проверки если вызывать несколько методов добавляющих
		// данные в БД и тогда может не выполнится услове sizeBefore+1, sizeAfter так как не смог выполнится один из этих методов
	}
}

