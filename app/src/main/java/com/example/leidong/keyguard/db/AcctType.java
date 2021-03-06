package com.example.leidong.keyguard.db;

/**
 * Created by leidong on 2017/10/15
 */

public class AcctType {
    private Long id;
    /** Not-null value. */
    private String name;
    private long category;
    private boolean numbers_only;
    private int max_length;
    private String icon;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AcctType() {
    }

    public AcctType(Long id) {
        this.id = id;
    }

    public AcctType(Long id, String name, long category, boolean numbers_only, int max_length, String icon) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.numbers_only = numbers_only;
        this.max_length = max_length;
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

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public boolean getNumbers_only() {
        return numbers_only;
    }

    public void setNumbers_only(boolean numbers_only) {
        this.numbers_only = numbers_only;
    }

    public int getMax_length() {
        return max_length;
    }

    public void setMax_length(int max_length) {
        this.max_length = max_length;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public boolean equals(Object another) {
        return ((AcctType) another).getId() == this.id;
    }
    // KEEP METHODS END
}
