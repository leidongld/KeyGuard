package com.example.leidong.keyguard.db;

/**
 * Created by leidong on 2017/10/15
 */

public class Category {
    private Long id;
    /** Not-null value. */
    private String name;
    private int type;
    /** Not-null value. */
    private String icon;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String name, int type, String icon) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /** Not-null value. */
    public String getIcon() {
        return icon;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public boolean equals(Object another) {
        return ((Category) another).getId() == this.id;
    }
    // KEEP METHODS END
}
