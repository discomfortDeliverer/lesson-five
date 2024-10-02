package ru.discomfortdeliverer.lesson_five.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Location {
    private Integer id;
    private String slug;
    private String name;
}
