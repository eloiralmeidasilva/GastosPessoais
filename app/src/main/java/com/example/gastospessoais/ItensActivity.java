package com.example.gastospessoais;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
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
    public static String tipo_retorno_alterar;
    private int modo;

    public static void nova(Activity activity, String tipoItem, int requestCode){

        Intent intent = new Intent(activity, ItensActivity.class);
        tipo_retorno = tipoItem;
        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, String tipoItem, int requestCode, Item item){

        Intent intent = new Intent(activity, ItensActivity.class);
        tipo_retorno_alterar = tipoItem;
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

            modo = bundle.getInt(MODO);

                if (modo == ALTERAR){

                    setTitle(R.string.alterar_item + tipo_retorno_alterar);

                    long id = bundle.getLong(ID);

                    ItensDataBase database = ItensDataBase.getInstance(this);

                    item = database.itemDao.itemPorId(id);

                    editTextDescricao.setText(item.getDescricao());
                    editTextValor.setText(item.getValor().toString());
                    editTextCategoria.setText(item.getCategoria());
                    editTextData.setText(DateFormat.format("dd/MM/yyyy", item.getData()).toString());
                }
                else{

                    setTitle(R.string.novo_item + tipo_retorno);
                    item = new Item();

                }

            }
        }

    public void salvar(View view){

        if(item == null) {

            item = new Item();
        }

        String descricao = editTextDescricao.getText().toString();
        String valor = editTextValor.getText().toString();
        String data = editTextData.getText().toString();
        String categoria = editTextCategoria.getText().toString();

        if(!descricao.equals("")){

            item.setDescricao(descricao);
        }

        if(!valor.equals("")){

            item.setValor(Float.parseFloat(valor));
        }
        if(!data.equals("")){

            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            long stringDate = simpledateformat.parse(data, pos).getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(stringDate);
            Date dataDate = calendar.getTime();
            item.setData(dataDate);

        }
        if(!categoria.equals("")){

            item.setCategoria(categoria);
        }


        ItensDataBase database = ItensDataBase.getInstance(this);

        if (modo == ALTERAR){

            item.setTipo(tipo_retorno_alterar);
            database.itemDao.alterar(item);

        }else{

            item.setTipo(tipo_retorno);
            database.itemDao.inserir(item);
        }

        setResult(Activity.RESULT_OK);
        finish();

    }





    public void voltar(View view){

        finish();

    }

}
