package com.devu.backend.entity.post;


public enum PostTags {
    SPRING("Spring"),
    C("C"),
    CPP("Cpp"),
    CSHARP("Csharp"),
    JS("JavaScript"),
    REACT("React"),
    VUE("Vue"),
    NODEJS("NodeJS"),
    PYTHON("Python"),
    DJANGO("Django"),
    FLASK("Flask"),
    GO("Go"),
    SWIFT("Swift"),
    ANGULAR("Angular"),
    RUBY("Ruby"),
    JAVA("Java"),
    FLUTTER("Flutter"),
    MYSQL("MySQL"),
    MONGO("MongoDB"),
    MARIA("MariaDB"),
    Oracle("Oracle"),
    POSTGRE("PostgreSQL"),
    REDIS("Redis"),
    AWS("AWS"),
    GCP("GCP"),
    AZURE("Azure"),
    DOCKER("Docker");

    private String title;

    PostTags(String title) {
        this.title = title;
    }

    PostTags() {}

    public String title() {
        return title;
    }
}
