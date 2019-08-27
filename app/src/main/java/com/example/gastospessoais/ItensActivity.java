package com.example.gastospessoais;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.gastospessoais.Dao.ItensDataBase;
import com.example.gastospessoais.Modelo.Item;
import com.example.gastospessoais.Utils.MascaraEdicao;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItensActivity extends AppCompatActivity {

    private Item item;
    private EditText editTextValor;
    private EditText editTextData;
    private EditText editTextCategoria;
    private EditText editTextDescricao;

    public static String        KEY_TIPO    = "TIPO";
    private static final String MODO        = "MODO";
    private static final String ID          = "ID";
    public static final int    NOVO        = 1;
    public static final int    ALTERAR     = 2;

    public static String tipo_retorno;
    private int modo;

    public static void nova(Activity activity, int requestCode){

        Intent intent = new Intent(activity, ItensActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Item item){

        Intent intent = new Intent(activity, ItensActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, item.getId());

        activity.startActivityForResult(intent, requestCode);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);

        setTitle(R.string.inserirItens);

        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextValor = findViewById(R.id.editTextValor);
        editTextData = findViewById(R.id.editTextData);
        editTextData.addTextChangedListener(MascaraEdicao.mask(editTextData, MascaraEdicao.FORMAT_DATE));
        editTextCategoria = findViewById(R.id.editTextCategoria);


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            tipo_retorno = bundle.getString(KEY_TIPO, KEY_TIPO);

        }
    }

    public void salvar(View view){

        item = new Item();

        Intent intent = new Intent(this,
                                    MainActivity.class);
        String descricao = editTextDescricao.getText().toString();
        String valor = editTextValor.getText().toString();
        String data = editTextData.getText().toString();
        String categoria = editTextCategoria.getText().toString();

        if(!descricao.equals("")){

            intent.putExtra(MainActivity.DESCRICAO , descricao);
            item.setDescricao(descricao);
        }

        if(!valor.equals("")){

            intent.putExtra(MainActivity.VALOR , valor);
            item.setValor(Float.parseFloat(valor));
        }
        if(!data.equals("")){

            intent.putExtra(MainActivity.DATA , data);
            //Date dateString = MascaraEdicao.getDate(Long.parseLong(data), "dd/MM/yyyy");

            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            long stringDate = simpledateformat.parse(data, pos).getTime();

            //String dateString = stringDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(stringDate);
            Date dataDate = calendar.getTime();
            item.setData(dataDate);

        }
        if(!categoria.equals("")){

            intent.putExtra(MainActivity.CATEGORIA , categoria);
            item.setCategoria(categoria);
        }

        intent.putExtra(MainActivity.TIPO_RETORNO , tipo_retorno);
        item.setTipo(tipo_retorno);


        ItensDataBase database = ItensDataBase.getInstance(this);

        //if (modo == NOVO){

            database.itemDao.inserir(item);

        //}else{


          //  database.itemDao.alterar(item);
        //}

        setResult(Activity.RESULT_OK);
        finish();


        startActivity(intent);

    }





    public void voltar(View view){

        finish();

    }

}
