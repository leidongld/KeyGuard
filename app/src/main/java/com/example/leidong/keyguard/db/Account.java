package com.example.leidong.keyguard.db;

/**
 * Created by leidong on 2017/10/15
 */

public class Account {
    private Long id;

    private String name;
    private long type;
    private String account;
    private String masked_account;
    private Boolean hide_name;
    private String account_salt;

    private String salt;

    private String hash;
    private String additional;
    private String additional_salt;
    private long category;

    private String tag;
    private String website;
    private Long last_access;
    private String icon;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String name, long type, String account, String masked_account, Boolean hide_name, String account_salt, String salt, String hash, String additional, String additional_salt, long category, String tag, String website, Long last_access, String icon) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.account = account;
        this.masked_account = masked_account;
        this.hide_name = hide_name;
        this.account_salt = account_salt;
        this.salt = salt;
        this.hash = hash;
        this.additional = additional;
        this.additional_salt = additional_salt;
        this.category = category;
        this.tag = tag;
        this.website = website;
        this.last_access = last_access;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMasked_account() {
        return masked_account;
    }

    public void setMasked_account(String masked_account) {
        this.masked_account = masked_account;
    }

    public Boolean getHide_name() {
        return hide_name;
    }

    public void setHide_name(Boolean hide_name) {
        this.hide_name = hide_name;
    }

    public String getAccount_salt() {
        return account_salt;
    }

    public void setAccount_salt(String account_salt) {
        this.account_salt = account_salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAdditional_salt() {
        return additional_salt;
    }

    public void setAdditional_salt(String additional_salt) {
        this.additional_salt = additional_salt;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getLast_access() {
        return last_access;
    }

    public void setLast_access(Long last_access) {
        this.last_access = last_access;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
