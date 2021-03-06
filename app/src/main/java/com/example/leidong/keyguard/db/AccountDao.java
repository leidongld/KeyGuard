package com.example.leidong.keyguard.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by leidong on 2017/10/15
 */

public class AccountDao extends AbstractDao<Account, Long>{
    public static final String TABLENAME = "ACCOUNT";

    /**
     * Properties of entity Account.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Type = new Property(2, long.class, "type", false, "TYPE");
        public final static Property Account = new Property(3, String.class, "account", false, "ACCOUNT");
        public final static Property Masked_account = new Property(4, String.class, "masked_account", false, "MASKED_ACCOUNT");
        public final static Property Hide_name = new Property(5, Boolean.class, "hide_name", false, "HIDE_NAME");
        public final static Property Account_salt = new Property(6, String.class, "account_salt", false, "ACCOUNT_SALT");
        public final static Property Salt = new Property(7, String.class, "salt", false, "SALT");
        public final static Property Hash = new Property(8, String.class, "hash", false, "HASH");
        public final static Property Additional = new Property(9, String.class, "additional", false, "ADDITIONAL");
        public final static Property Additional_salt = new Property(10, String.class, "additional_salt", false, "ADDITIONAL_SALT");
        public final static Property Category = new Property(11, long.class, "category", false, "CATEGORY");
        public final static Property Tag = new Property(12, String.class, "tag", false, "TAG");
        public final static Property Website = new Property(13, String.class, "website", false, "WEBSITE");
        public final static Property Last_access = new Property(14, Long.class, "last_access", false, "LAST_ACCESS");
        public final static Property Icon = new Property(15, String.class, "icon", false, "ICON");
    };


    public AccountDao(DaoConfig config) {
        super(config);
    }

    public AccountDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACCOUNT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'TYPE' INTEGER NOT NULL ," + // 2: type
                "'ACCOUNT' TEXT," + // 3: account
                "'MASKED_ACCOUNT' TEXT," + // 4: masked_account
                "'HIDE_NAME' INTEGER," + // 5: hide_name
                "'ACCOUNT_SALT' TEXT," + // 6: account_salt
                "'SALT' TEXT NOT NULL ," + // 7: salt
                "'HASH' TEXT NOT NULL ," + // 8: hash
                "'ADDITIONAL' TEXT," + // 9: additional
                "'ADDITIONAL_SALT' TEXT," + // 10: additional_salt
                "'CATEGORY' INTEGER NOT NULL ," + // 11: category
                "'TAG' TEXT NOT NULL ," + // 12: tag
                "'WEBSITE' TEXT," + // 13: website
                "'LAST_ACCESS' INTEGER," + // 14: last_access
                "'ICON' TEXT);"); // 15: icon
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACCOUNT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Account entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getType());

        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(4, account);
        }

        String masked_account = entity.getMasked_account();
        if (masked_account != null) {
            stmt.bindString(5, masked_account);
        }

        Boolean hide_name = entity.getHide_name();
        if (hide_name != null) {
            stmt.bindLong(6, hide_name ? 1l: 0l);
        }

        String account_salt = entity.getAccount_salt();
        if (account_salt != null) {
            stmt.bindString(7, account_salt);
        }
        stmt.bindString(8, entity.getSalt());
        stmt.bindString(9, entity.getHash());

        String additional = entity.getAdditional();
        if (additional != null) {
            stmt.bindString(10, additional);
        }

        String additional_salt = entity.getAdditional_salt();
        if (additional_salt != null) {
            stmt.bindString(11, additional_salt);
        }
        stmt.bindLong(12, entity.getCategory());
        stmt.bindString(13, entity.getTag());

        String website = entity.getWebsite();
        if (website != null) {
            stmt.bindString(14, website);
        }

        Long last_access = entity.getLast_access();
        if (last_access != null) {
            stmt.bindLong(15, last_access);
        }

        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(16, icon);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Account readEntity(Cursor cursor, int offset) {
        Account entity = new Account( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // name
                cursor.getLong(offset + 2), // type
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // account
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // masked_account
                cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // hide_name
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // account_salt
                cursor.getString(offset + 7), // salt
                cursor.getString(offset + 8), // hash
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // additional
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // additional_salt
                cursor.getLong(offset + 11), // category
                cursor.getString(offset + 12), // tag
                cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // website
                cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14), // last_access
                cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // icon
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Account entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setType(cursor.getLong(offset + 2));
        entity.setAccount(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMasked_account(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHide_name(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setAccount_salt(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSalt(cursor.getString(offset + 7));
        entity.setHash(cursor.getString(offset + 8));
        entity.setAdditional(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAdditional_salt(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCategory(cursor.getLong(offset + 11));
        entity.setTag(cursor.getString(offset + 12));
        entity.setWebsite(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setLast_access(cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14));
        entity.setIcon(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Account entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(Account entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
