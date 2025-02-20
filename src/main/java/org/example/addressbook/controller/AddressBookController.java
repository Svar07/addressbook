package org.example.addressbook.controller;

import org.example.addressbook.model.AddressBook;
import org.example.addressbook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class AddressBookController {
   final AddressBookRepository repository; // вставки зависимости для обрашения к БД через конструктор в котроллере

    @Autowired // Spring  с помощью этой атотации добовляет зависимость в этот класс
    public AddressBookController (AddressBookRepository repository){ //конструктор
        this.repository = repository;
    }

    @GetMapping("/test")
    public String getTest(){

        return "HELLO WORLD";
    }

    @GetMapping("/reactivTest")
    public Mono<String> getReactivTest(){

        return Mono.just("Hello REACTIV World");
    }

    @GetMapping("addressbookS") // будем возвращать массив типа Addressbook
    public Flux<AddressBook> getAddressbook(){

        // на самом деле работаем с SELECT * FROM Addresbook, Addresbook связали когда объявляли AddressBookRepository и
        // в аргуметне репозитория ReactiveCrudRepository<Addressbook> сказав тем самым что он является типом этой таблице,
        // этого recorda Addresbook который будет являтся записью в таблице addresbook
        return repository.findAll(); //findAll() является запросом SELECT обращаемся к таблице Addresbook
    }
    @PostMapping("addresbook")
    public Mono<AddressBook> save(@RequestBody AddressBook addressbook){
        System.out.println("Received: " + addressbook);
        return repository.save(addressbook);
    }
}

