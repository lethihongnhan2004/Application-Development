package com.example.campusexpensese06205.model;


public class Icon {
    private int iconId;  // ID của icon
    private String name;  // Tên chi tiêu

    // Constructor
    public Icon(int iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }

    // Getter và Setter cho iconId và name
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
