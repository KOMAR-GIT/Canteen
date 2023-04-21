package main.model;

import lombok.Data;

import java.util.Date;

@Data
public class Person {

    public Person(String name, String date) {
        this.name = name;
        this.date = date;
    }

    private String name;
    private String date;

}
