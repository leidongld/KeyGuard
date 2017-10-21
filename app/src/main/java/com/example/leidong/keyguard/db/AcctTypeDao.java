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

public class AcctTypeDao extends AbstractDao<AcctType, Long>{
    public static final String TABLENAME = "ACCT_TYPE";

    /**
     * Properties of entity AcctType.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Category = new Property(2, long.class, "category", false, "CATEGORY");
        public final static Property Numbers_only = new Property(3, boolean.class, "numbers_only", false, "NUMBERS_ONLY");
        public final static Property Max_length = new Property(4, int.class, "max_length", false, "MAX_LENGTH");
        public final static Property Icon = new Property(5, String.class, "icon", false, "ICON");
    };


    public AcctTypeDao(DaoConfig config) {
        super(config);
    }

    public AcctTypeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACCT_TYPE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'CATEGORY' INTEGER NOT NULL ," + // 2: category
                "'NUMBERS_ONLY' INTEGER NOT NULL ," + // 3: numbers_only
                "'MAX_LENGTH' INTEGER NOT NULL ," + // 4: max_length
                "'ICON' TEXT);"); // 5: icon
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACCT_TYPE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AcctType entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getCategory());
        stmt.bindLong(4, entity.getNumbers_only() ? 1l: 0l);
        stmt.bindLong(5, entity.getMax_length());

        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(6, icon);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public AcctType readEntity(Cursor cursor, int offset) {
        AcctType entity = new AcctType( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // name
                cursor.getLong(offset + 2), // category
                cursor.getShort(offset + 3) != 0, // numbers_only
                cursor.getInt(offset + 4), // max_length
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // icon
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AcctType entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setCategory(cursor.getLong(offset + 2));
        entity.setNumbers_only(cursor.getShort(offset + 3) != 0);
        entity.setMax_length(cursor.getInt(offset + 4));
        entity.setIcon(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AcctType entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(AcctType entity) {
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
