package com.alexlasota.medicalclinic.client;

import com.alexlasota.medicalclinic.client.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service1", url = "https://todo:8081")
public interface Service1Client {

    @GetMapping("/book")
    List<Book> getBooks();

    @PostMapping("/book")
    void addBook(@RequestBody Book book);

    @PutMapping("/book")
    void rentBook(@RequestParam String clientName, @RequestParam String isbn);
}
