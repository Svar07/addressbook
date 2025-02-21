package org.example.addressbook.repository;

import org.example.addressbook.model.AddressBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AddressBookRepository extends ReactiveCrudRepository<AddressBook, Long> {
    // Добавляем возможность запрпшивать не все данные а кусками (страницами)
    // пагинация — это процесс разделения большого набора данных на страницы если мы будем обращатся к приложению
    // и запрашивать все записи которые существуют в БД  то если записей очень много мы можем перегрузить наше приложение,
    // поэтому пагинвция явно ограничивает  кол-во записей которые можно единовременно запросить из БД
    Flux<AddressBook> findAllBy(Pageable pageable); // Flux это аналог листа, аналог последовательного массива тольком мы
    // работаем с этим массивом асинхронно у Flux есть укороченная версия Mono - Это один элемент этого массива
    // т.е мы работаем с массивом но там всегда один элемент
}

