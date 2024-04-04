package com.example.pruebafastadapter.data.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.pruebafastadapter.data.models.Task;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK".
*/
public class TaskDao extends AbstractDao<Task, Long> {

    public static final String TABLENAME = "TASK";

    /**
     * Properties of entity Task.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IdUnique = new Property(1, String.class, "idUnique", false, "ID_UNIQUE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Created = new Property(3, String.class, "created", false, "CREATED");
        public final static Property Updated = new Property(4, String.class, "updated", false, "UPDATED");
        public final static Property IsCheck = new Property(5, boolean.class, "isCheck", false, "IS_CHECK");
        public final static Property IsSincronized = new Property(6, boolean.class, "isSincronized", false, "IS_SINCRONIZED");
    }


    public TaskDao(DaoConfig config) {
        super(config);
    }
    
    public TaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ID_UNIQUE\" TEXT," + // 1: idUnique
                "\"CONTENT\" TEXT NOT NULL ," + // 2: content
                "\"CREATED\" TEXT," + // 3: created
                "\"UPDATED\" TEXT," + // 4: updated
                "\"IS_CHECK\" INTEGER NOT NULL ," + // 5: isCheck
                "\"IS_SINCRONIZED\" INTEGER NOT NULL );"); // 6: isSincronized
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Task entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String idUnique = entity.getIdUnique();
        if (idUnique != null) {
            stmt.bindString(2, idUnique);
        }
        stmt.bindString(3, entity.getContent());
 
        String created = entity.getCreated();
        if (created != null) {
            stmt.bindString(4, created);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(5, updated);
        }
        stmt.bindLong(6, entity.getIsCheck() ? 1L: 0L);
        stmt.bindLong(7, entity.getIsSincronized() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Task entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String idUnique = entity.getIdUnique();
        if (idUnique != null) {
            stmt.bindString(2, idUnique);
        }
        stmt.bindString(3, entity.getContent());
 
        String created = entity.getCreated();
        if (created != null) {
            stmt.bindString(4, created);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(5, updated);
        }
        stmt.bindLong(6, entity.getIsCheck() ? 1L: 0L);
        stmt.bindLong(7, entity.getIsSincronized() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Task readEntity(Cursor cursor, int offset) {
        Task entity = new Task( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // idUnique
            cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // created
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // updated
            cursor.getShort(offset + 5) != 0, // isCheck
            cursor.getShort(offset + 6) != 0 // isSincronized
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Task entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIdUnique(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.getString(offset + 2));
        entity.setCreated(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUpdated(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsCheck(cursor.getShort(offset + 5) != 0);
        entity.setIsSincronized(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Task entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Task entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Task entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}