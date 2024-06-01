package com.example.apteka30;

public class Medicine {
    private int id; // Уникальный идентификатор
    private String name;
    private String description;
    private String imagePath; // Путь к изображению

    public Medicine() {
        // Пустой конструктор требуется для SQLite
    }

    public Medicine(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
