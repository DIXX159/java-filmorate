package ru.yandex.practicum.filmorate.model;

public class Mpa {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setMpa(String mpaName) {
        this.name = mpaName;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }
}