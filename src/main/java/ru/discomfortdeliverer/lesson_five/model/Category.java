package ru.discomfortdeliverer.lesson_five.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
    private int id;
    private final String slug;
    private final String name;

    public Category(int id, String slug, String name) {
        this.id = id;
        this.slug = slug;
        this.name = name;
    }
}
