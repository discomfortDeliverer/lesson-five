package ru.discomfortdeliverer.lesson_five.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Category {
    private Integer id;
    private final String slug;
    private final String name;

    public Category(Integer id, String slug, String name) {
        this.id = id;
        this.slug = slug;
        this.name = name;
    }
}
