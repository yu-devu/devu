package com.devu.backend.entity.post;


public enum PostTags {
    SPRING("Spring"),
    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    JS("JavaScript"),
    REACT("React.js"),
    VUE("Vue.js"),
    NODE("Node.js"),
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
}
