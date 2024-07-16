package com.alexlasota.medicalclinic.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    private String title;
    private String author;
    private String isbn;
    private String category;
    private String borrower;
}
