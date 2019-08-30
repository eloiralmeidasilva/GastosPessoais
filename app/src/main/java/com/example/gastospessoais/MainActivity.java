package com.example.gastospessoais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastospessoais.Dao.ItemDao;
import com.example.gastospessoais.Dao.ItensDataBase;
import com.example.gastospessoais.Modelo.Item;
import com.example.gastospessoais.Modelo.Valores;
import com.example.gastospessoais.Utils.UtilsGUI;

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

        atualizarVal();
        popularLista();

        registerForContextMenu(listViewItens);  // indica que o listView vai ter o menu de contexto se segurar clicado


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
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_NOVO_ITEM || requestCode == REQUEST_ALTERAR_ITEM) &&
                resultCode == Activity.RESULT_OK) {

            adapter.notifyDataSetChanged();
        }
    }

    private void atualizarVal(){

        ItensDataBase database = ItensDataBase.getInstance(this);
        database.itemDao.atualizarValores();

        textViewDisponivel.setText(database.itemDao.disponivel.toString());
        textViewGastos.setText(database.itemDao.gastos.toString());
        textViewReceita.setText(database.itemDao.receita.toString());

    }

    private void popularLista(){

        ItensDataBase database = ItensDataBase.getInstance(this);

        adapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_list_item_1
                                        , database.itemDao.lista);

        listViewItens.setAdapter(adapter);

    }

    private void excluirItem(final Item item){

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
                                atualizarVal();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);


    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.principal_menu_contexto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Item itemClasse = adapter.getItem(info.position);

        switch(item.getItemId()){

            case R.id.alterar:
                ItensActivity.alterar(this,
                        itemClasse.getTipo(),
                        REQUEST_ALTERAR_ITEM,
                        itemClasse);

                adapter.notifyDataSetChanged();

                atualizarVal();

                return true;

            case R.id.excluir:
                excluirItem(itemClasse);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


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
                ItensActivity.nova(this, tipoItem, REQUEST_NOVO_ITEM);

                return true;

            case R.id.menuReceitas:
                tipoItem = getString(R.string.receita);

                intent.putExtra(ItensActivity.KEY_TIPO, tipoItem);
                ItensActivity.nova(this, tipoItem, REQUEST_NOVO_ITEM);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
