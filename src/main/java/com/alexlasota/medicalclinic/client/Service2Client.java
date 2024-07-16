package com.alexlasota.medicalclinic.client;

import com.alexlasota.medicalclinic.client.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "service2", url = "https://todo:8082")
public interface Service2Client {

    @GetMapping("/book")
    List<Book> getBooks();
}
