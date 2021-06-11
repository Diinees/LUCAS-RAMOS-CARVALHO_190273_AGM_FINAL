package com.example.agenda.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.dao.CadastroDAO;
import com.example.agenda.model.Cadastro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.agenda.ui.activities.ConstantesActivities.CHAVE_CADASTRO;

public class CadastroListaActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "PESSOAS CADASTRADAS";
    private final CadastroDAO dao = new CadastroDAO();
    private ArrayAdapter<Cadastro> adapter;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_lista);

        setTitle(TITULO_APPBAR);
        NovoCadastro();
        configuraLista();
    }

    private void NovoCadastro() {
        FloatingActionButton novo_cadastro = findViewById(R.id.novo_cadastro);
        novo_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AbreFormulario();
            }
        });
    }

    private void AbreFormulario() {
        //Transiciona as telas ao clicar em novo_cadastro
        startActivity(new Intent(CadastroListaActivity.this, CadastroActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizaAdapter();
    }

    private void atualizaAdapter() {
        //Limpa a tela e incorpora novamente
        adapter.clear();
        adapter.addAll(dao.todos());
    }

    //Remove cadastros
    private void remove(Cadastro cadastro){

        dao.remove(cadastro);
        adapter.remove(cadastro);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_lista_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        configuraMenu(item);
        return super.onContextItemSelected(item);
    }

    private void configuraMenu(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.activity_lista_menu_remover) {

            //Confirmação para deletar cadastro
            new AlertDialog.Builder(this)
                    .setTitle("Deletar Cadastro")
                    .setMessage("Tem certeza que deseja deletar este cadastro?")
                    .setNegativeButton("Não", null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                            Cadastro cadastroEscolhida = adapter.getItem(menuInfo.position);
                            //Remove o cadastro selecionado
                            remove(cadastroEscolhida);
                        }
                    })
                    .show();
        }
    }

    private void configuraLista() {
        ListView ListaCadastro = findViewById(R.id.acitivty_main_lista_cadastros);
        configuraAdapter(ListaCadastro);
        configuraItemClick(ListaCadastro);
        registerForContextMenu(ListaCadastro);
    }

    private void configuraItemClick(ListView listaDePessoas) {
        //Recebe o cadastro selecionado
        listaDePessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {

                //Recebe a posição do cadastro selecionado
                Cadastro cadastroEscolhido = (Cadastro) adapterView.getItemAtPosition(posicao);
                EditaFormulário(cadastroEscolhido);
            }
        });
    }

    private void EditaFormulário(Cadastro cadastro) {
        Intent vaiParaFormulario = new Intent(CadastroListaActivity.this, CadastroActivity.class);
        //Faz a transição das informações
        vaiParaFormulario.putExtra(CHAVE_CADASTRO, cadastro);
        startActivity(vaiParaFormulario);
    }

    private void configuraAdapter(ListView listaCadastro) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaCadastro.setAdapter(adapter);                                                                                 
    }
}
