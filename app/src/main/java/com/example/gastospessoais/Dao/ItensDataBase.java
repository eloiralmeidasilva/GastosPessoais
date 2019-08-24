package com.example.gastospessoais.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItensDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME    = "itens.db";
    private static final int    DB_VERSION = 1;

    private static ItensDataBase instance;

    private Context context;
    public  ItemDao itemDao;

    public static ItensDataBase getInstance(Context contexto){

        if (instance == null){
            instance = new ItensDataBase(contexto);
        }

        return instance;
    }

    private ItensDataBase(Context contexto){
        super(contexto, DB_NAME, null, DB_VERSION);

        context = contexto;

        itemDao = new ItemDao(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        itemDao.criarTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        itemDao.apagarTabela(db);

        onCreate(db);
    }


}
