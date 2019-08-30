package com.example.gastospessoais;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class InformacaoActivity extends AppCompatActivity {


    private TextView textView;
    String texto =  "Este aplicativo foi desenvolvido como projeto final da disciplina de Android" +
                    " do curso de Pós-Graduação de Especialização em tecnologia Java da UTFPR\n" +
                    "Foi desenvolvido pelo aluno Eloir José Almeida da Silva\n" +
                    "Aplicativo de Gastos financeiros pessoal\n" +
                    "Versão 1.0\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textView = findViewById(R.id.textViewTextoSobre);
        textView.setText(texto);

    }


    private void voltar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        voltar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                voltar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
