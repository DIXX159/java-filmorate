package ru.yandex.practicum.filmorate.model;

public class Genre {

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

    public void setGenre(String genreName) {
        this.name = genreName;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }
}
