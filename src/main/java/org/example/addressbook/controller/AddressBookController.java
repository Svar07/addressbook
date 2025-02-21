package org.example.addressbook.controller;

import org.example.addressbook.model.AddressBook;
import org.example.addressbook.repository.AddressBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Optional;

@RestController // Аннотация, делающая контроллер RESTful, чтобы Spring мог обрабатывать HTTP-запросы.
@RequestMapping("/api/v1")  // Указание базового пути для всех методов контроллера.
public class AddressBookController {
    private static final Logger logger = LoggerFactory.getLogger(AddressBookController.class);//slf4j оболочка над логгером LoggerFactory чтобы знать от какого класса выводить логи
    // Поле для хранения репозитория. Финальный тип, потому что значение нельзя изменить после инициализации.
   final AddressBookRepository repository; // вставки зависимости для обрашения к БД через конструктор в котроллере
    //Это пример композиции классов, где один класс (AddressBookController) содержит ссылку на другой
    // класс (AddressBookRepository), который используется для выполнения определённых функций (взаимодействие с базой данных)

    @Autowired // Spring  с помощью этой анотацией добовляет зависимость в этот класс
    public AddressBookController (AddressBookRepository repository){ // Конструктор для внедрения зависимости (репозиторий).//конструктор
        this.repository = repository;// Присваивание поля.
    }

    @GetMapping("/test") // Метод-обработчик GET-запросов на путь "/api/v1/test".
    public String getTest(){

        return "HELLO WORLD";// Возвращение простого текста в ответ на запрос.
    }

    @GetMapping("/reactivTest") // Метод-обработчик GET-запросов на путь "/api/v1/reactiveTest".
    public Mono<String> getReactivTest(){ // Использование реактивной модели программирования с использованием Mono (обертка над одним элементом).

        return Mono.just("Hello REACTIV World"); // Создание Mono с фиксированным значением.
    }

    @GetMapping("addressbooks") // будем возвращать массив типа Addressbook Метод-обработчик GET-запросов на путь "/api/v1/addressbooks".
    public Flux<AddressBook> getAddressbook(@RequestParam("page") Optional<Integer> pageOpt,
                                            @RequestParam("size") Optional<Integer> sizeOpt){ // Реактивная коллекция типа Flux
        // для работы с потоком объектов AddressBook параметры Integer page,Integer size нужны для пагинации size кол-во запрашиваемых записей на странице
        // @RequestParam нужен чтобы подсказать Spring какие именно это параметры и как их считывать
        Integer page = pageOpt.orElse(0); // если в padeOpt нет значения то отработать условие else вернуть значение по  умолчанию 0
        Integer size = sizeOpt.orElse(100); //если sizeOpt не передан запиши 100
        PageRequest pageRequest = PageRequest.of(page, size);

        //logger.trace(""); // 1уровень можно указать низкоприорететную инфу (вошли в метод) очень редко выводится
        //logger.debug(""); // 2 уровень при проблемах указывается отладочная информация почти не выводится
        logger.info("getAddressBook: {}", pageRequest); // 3 уровень редко выводится лог что сработал метод getAddressBook  с параметрами
        //logger.warn(""); // 4уровень логирования выводится почти всегда не критичная ошибка можен не выводится
        //logger.error("");// 5уровень самый приорететный всегда выводится что приложение работает не корректно

        // на самом деле работаем с SELECT * FROM Addresbook, Addresbook связали когда объявляли AddressBookRepository и
        // в аргуметне репозитория ReactiveCrudRepository<Addressbook> сказав тем самым что он является типом этой таблице,
        // этого recorda Addresbook который будет являтся записью в таблице addresbook
        // return repository.findAll(); //findAll() является запросом SELECT обращаемся к таблице Addresbook Запрашиваем все записи из таблицы AddressBook через метод findAll().
        return repository.findAllBy(pageRequest);
    }
    @PostMapping("addresbook") // Метод-обработчик POST-запросов на путь "/api/v1/addressbook".
    public Mono<AddressBook> save(@RequestBody AddressBook addressbook){// Внедрение тела запроса в объект AddressBook.
        System.out.println("Received: " + addressbook); // альтернативное Логирование принятого объекта для отладки.
        logger.info("Save: {}", addressbook); // у slf4j есть удобный вывод параметров через {}, ({} -{} - {}, addressbook, 0, Hello позволяет вывести доп параиетра по мимо объекта, т.е 0 и Hello)
        return repository.save(addressbook);// Сохраняем переданный объект в базу данных через репозиторий
        // print не нужно писать так как в record AddressBook есть метод toString который и выводит лог в консоль
    }
    @PostMapping("/transactionsave")
    @Transactional // тип запроса в форме транзакций когда нужно чтобы набор запросов выполнялся целиком или не выполнялось
    // ничего как с переводом на карту если списались деньги с одного счёта то они должны зачислится на другой выполнение половины запроса не приемлимо
    public Mono<AddressBook> transactionSave(@RequestBody AddressBook addressbook){
        repository.findAll().count();
        return repository.save(addressbook);
    }
}

