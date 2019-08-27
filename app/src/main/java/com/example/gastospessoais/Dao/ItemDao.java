package com.example.gastospessoais.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gastospessoais.Modelo.Item;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ItemDao {

    public static final String TABELA       = "ITENS";
    public static final String DESCRICAO    = "DESCRICAO";
    public static final String VALOR        = "VALOR";
    public static final String DATA         = "DATA";
    public static final String CATEGORIA    = "CATEGORIA";
    public static final String TIPO         = "TIPO";
    public static final String ID           = "ID";


    private ItensDataBase conexao;
    public List<Item> lista;

    public ItemDao(ItensDataBase itensDatabase){
        conexao = itensDatabase;
        lista   = new ArrayList<>();
    }

    public void criarTabela(SQLiteDatabase database){

        String sql =        "CREATE TABLE " + TABELA + "(" +
                ID    +     " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                VALOR  +    " FLOAT NOT NULL, " +
                DATA +      " INTEGER, " +
                CATEGORIA + " CHAR, " +
                DESCRICAO + " CHAR, " +
                TIPO +      " CHAR) ";

        database.execSQL(sql);
    }

    public void apagarTabela(SQLiteDatabase database){

        String sql = "DROP TABLE IF EXISTS " + TABELA;

        database.execSQL(sql);
    }

    public boolean inserir(Item item){

        ContentValues values = new ContentValues();

        values.put(VALOR, item.getValor());
        values.put(DATA, String.valueOf(item.getData().getTime()));
        values.put(CATEGORIA, item.getCategoria());
        values.put(DESCRICAO, item.getDescricao());
        values.put(TIPO, item.getTipo());


        long id = conexao.getWritableDatabase().insert(TABELA,
                null,
                values);

        item.setId(id);

        lista.add(item);

        return true;
    }

    public boolean alterar(Item item){

        ContentValues values = new ContentValues();
        String tipo = item.getTipo();

        values.put(VALOR,  item.getValor());
        values.put(DATA, String.valueOf(item.getData().getTime()));
        values.put(CATEGORIA,  item.getCategoria());
        values.put(DESCRICAO,  item.getDescricao());
        values.put(TIPO,  tipo);


        String[] args = {String.valueOf(item.getId())};

        conexao.getWritableDatabase().update(TABELA,
                values,
                ID + " = ?",
                args);



        return true;
    }

    public boolean apagar(Item item){

        String[] args = {String.valueOf(item.getId())};

        conexao.getWritableDatabase().delete(TABELA,
                ID + " = ?",
                args);
        lista.remove(item);

        return true;
    }

    public void carregarTudo(){

        lista.clear();
        ArrayList listaItens = new ArrayList();

        String sql = "SELECT * FROM " + TABELA + " ORDER BY " + DESCRICAO;

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int colunaValor  = cursor.getColumnIndex(VALOR);
        int colunaId    = cursor.getColumnIndex(ID);
        int colunaData    = cursor.getColumnIndex(DATA);
        int colunaDescricao    = cursor.getColumnIndex(DESCRICAO);
        int colunaCategoria    = cursor.getColumnIndex(CATEGORIA);
        int colunaTipo    = cursor.getColumnIndex(TIPO);

        while(cursor.moveToNext()){

            Item item = new Item();

            item.setTipo(cursor.getString(colunaTipo));
/*
            String dateString = cursor.getString(colunaData);
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            long stringDate = simpledateformat.parse(dateString, pos).getTime();

            //String dateString = stringDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(stringDate);
            Date dataDate = calendar.getTime();
            item.setData(dataDate);
*/

            String dateString = cursor.getString(colunaData);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(dateString));
            Date data = calendar.getTime();
            Long ano = Long.valueOf(data.getYear());
            int mes = (data.getMonth()+1);
            int dia = data.getDay();
            item.setAno(ano);
            item.setMes(mes);
            item.setDia(dia);
            item.setData(data);

            item.setId(cursor.getLong(colunaId));
            item.setDescricao(cursor.getString(colunaDescricao));
            item.setValor(cursor.getFloat(colunaValor));
            item.setCategoria(cursor.getString(colunaCategoria));

            lista.add(item);
        }


        cursor.close();


    }

    public Item itemPorId(long id){

        for (Item p: lista){

            if (p.getId() == id){
                return p;
            }
        }

        return null;
    }


}
