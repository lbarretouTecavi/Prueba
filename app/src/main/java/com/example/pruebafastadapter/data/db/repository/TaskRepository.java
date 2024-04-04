package com.example.pruebafastadapter.data.db.repository;

import android.content.Context;

import com.example.pruebafastadapter.data.db.DatabaseHelper;
import com.example.pruebafastadapter.data.db.dao.TaskDao;
import com.example.pruebafastadapter.data.models.Task;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository implements ITaskRepository {
    private final DatabaseHelper helper;
    private static TaskRepository repository;

    public TaskRepository(Context context) {
        this.helper = new DatabaseHelper(context);
    }

    public static synchronized TaskRepository getInstance(Context context) {
        if (repository == null) {
            repository = new TaskRepository(context);
        }
        return repository;
    }

    @Override
    public List<Task> getAllTask() {
        return helper.getDaoSession().getTaskDao().loadAll();
    }

    @Override
    public Observable<List<Task>> getAllTaskNotCheckedAsync() {
        return Observable.fromCallable(() ->
                helper.getDaoSession()
                        .getTaskDao()
                        .queryBuilder()
                        .orderAsc(TaskDao.Properties.Created)
                        .where(TaskDao.Properties.IsCheck.eq(0))
                        .list()
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Task>> getAllTaskChecked() {
        return Observable.fromCallable(() ->
                        helper.getDaoSession()
                                .getTaskDao()
                                .queryBuilder()
                                .orderDesc(TaskDao.Properties.Updated)
                                .where(TaskDao.Properties.IsCheck.eq(1))
                                .list()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public List<Task> getAllTaskNotSincronized() {
        return helper.getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.IsSincronized.eq(0)).list();
    }

    @Override
    public Task getTask(String idUnique) {
        return helper.getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.IdUnique.eq(idUnique)).unique();
    }

    @Override
    public void postTask(Task task) {
        helper.getDaoSession().getTaskDao().insert(task);
    }


    @Override
    public void saveListTask(List<Task> tasks) {
        helper.getDaoSession().startAsyncSession().runInTx(() -> helper.getDaoSession().getTaskDao().insertOrReplaceInTx(tasks));
    }

    @Override
    public void putTask(Task task) {
        helper.getDaoSession().startAsyncSession().runInTx(() -> helper.getDaoSession().getTaskDao().updateInTx(task));
    }

    @Override
    public void deleteTask(Task task) {
        helper.getDaoSession().startAsyncSession().runInTx(() -> helper.getDaoSession().getTaskDao().deleteInTx(task));
    }
}
