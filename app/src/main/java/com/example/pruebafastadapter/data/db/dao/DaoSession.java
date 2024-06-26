package com.example.pruebafastadapter.data.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.pruebafastadapter.data.models.Task;

import com.example.pruebafastadapter.data.db.dao.TaskDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig taskDaoConfig;

    private final TaskDao taskDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        taskDao = new TaskDao(taskDaoConfig, this);

        registerDao(Task.class, taskDao);
    }
    
    public void clear() {
        taskDaoConfig.clearIdentityScope();
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

}
