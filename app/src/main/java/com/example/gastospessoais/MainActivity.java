package com.example.gastospessoais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastospessoais.Dao.ItensDataBase;
import com.example.gastospessoais.Modelo.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DESCRICAO = "DESCRICAO";
    public static final String VALOR = "VALOR";
    public static final String DATA = "DATA";
    public static final String CATEGORIA = "CATEGORIA";
    public static final String TIPO_RETORNO = "TIPO_RETORNO";
    private static final int REQUEST_NOVO_ITEM    = 1;
    private static final int REQUEST_ALTERAR_ITEM = 2;



    private TextView textViewDisponivel, textViewGastos, textViewReceita;
    private ListView listViewItens;
    private ArrayList<String> listaItens;
    private ArrayAdapter<Item> adapter;


    public String tipoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItensDataBase database = ItensDataBase.getInstance(this);
        database.itemDao.carregarTudo();

        textViewDisponivel = findViewById(R.id.textViewDisponivel);
        textViewGastos = findViewById(R.id.textViewGastos);
        textViewReceita = findViewById(R.id.textViewReceita);
        listViewItens = findViewById(R.id.listaItens);



 /*       listViewItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = (Item) parent.getItemAtPosition(position);

                ItensActivity.alterar(MainActivity.this,
                                        REQUEST_NOVO_ITEM,
                                        item);
            }
        });
*/

/*
        Intent intentRetorno = getIntent();
        Bundle bundleRetorno = intentRetorno.getExtras();

        if (bundleRetorno != null) {

            String descricao = bundleRetorno.getString(DESCRICAO, DESCRICAO);
            String valor = bundleRetorno.getString(VALOR, VALOR);
            String data = bundleRetorno.getString(DATA, DATA);
            String categoria = bundleRetorno.getString(CATEGORIA, CATEGORIA);
            String tipo = bundleRetorno.getString(TIPO_RETORNO, TIPO_RETORNO);

        }
*/

        popularLista();

        registerForContextMenu(listViewItens);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_NOVO_ITEM || requestCode == REQUEST_ALTERAR_ITEM) &&
                resultCode == Activity.RESULT_OK) {

            adapter.notifyDataSetChanged();
        }
    }

    private void popularLista(){

        ItensDataBase database = ItensDataBase.getInstance(this);

        adapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1
                                        , database.itemDao.lista);

        listViewItens.setAdapter(adapter);

    }
/*
    private void excluirPessoa(final Item item){

        String mensagem = getString(R.string.deseja_apagar) + "\n" + item.getDescricao();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                ItensDataBase database =
                                        ItensDataBase.getInstance(MainActivity.this);

                                database.itemDao.apagar(item);

                                adapter.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        //UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

*/
    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(this,
                                    ItensActivity.class);

        switch(item.getItemId()){
            case R.id.menuGastos:
                tipoItem = getString(R.string.gastos);

                intent.putExtra(ItensActivity.KEY_TIPO, tipoItem);

                startActivity(intent);

                return true;

            case R.id.menuReceitas:
                tipoItem = getString(R.string.receita);

                intent.putExtra(ItensActivity.KEY_TIPO, tipoItem);

                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
