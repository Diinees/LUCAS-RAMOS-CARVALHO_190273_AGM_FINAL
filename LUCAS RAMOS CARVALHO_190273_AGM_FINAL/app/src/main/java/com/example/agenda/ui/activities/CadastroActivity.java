package com.example.agenda.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.dao.CadastroDAO;
import com.example.agenda.model.Cadastro;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import static com.example.agenda.ui.activities.ConstantesActivities.CHAVE_CADASTRO;

public class CadastroActivity extends AppCompatActivity {

    //Contantes
    public static final String TITULO_APPBAR = "Cadastro";
    private static final String TITULO_APPBAR_EDITAr_CADASTRO = "Editar Cadastro";
    private static final String TITULO_APPBAR_NOVO_CADASTRO = "Novo Cadastro";

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoCEP;
    private EditText campoRG;
    private EditText campoTelefone;
    private EditText campoGenero;
    private EditText campoAltura;
    private EditText campoNascimento;

    //Instância de classe para persistir os dados
    private final CadastroDAO dao = new CadastroDAO();
    private Cadastro cadastro;

    //Aparece icone para salvar o cadastro
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cadastro_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Pega o id selecionado
        int itemId = item.getItemId();
        if(itemId == R.id.activity_cadastro_menu_salvar){
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializaCampos();
        configuraBotao();
        carregaPessoa();
    }

    private void carregaPessoa() {
        Intent dados = getIntent();
        if(dados.hasExtra(CHAVE_CADASTRO)) {
            setTitle(TITULO_APPBAR_EDITAr_CADASTRO);
            //Serializa as informações do cadastro
            cadastro = (Cadastro) dados.getSerializableExtra(CHAVE_CADASTRO);
            preencheCampos();
        }else{
            setTitle(TITULO_APPBAR_NOVO_CADASTRO);
            cadastro = new Cadastro();
        }
    }

    private void preencheCampos() {
        //Seta os campos preenchidos
        campoNome.setText(cadastro.getNome());
        campoEndereco.setText(cadastro.getEndereco());
        campoCEP.setText(cadastro.getCep());
        campoRG.setText(cadastro.getRg());
        campoTelefone.setText(cadastro.getTelefone());
        campoGenero.setText(cadastro.getGenero());
        campoAltura.setText(cadastro.getAltura());
        campoNascimento.setText(cadastro.getNascimento());
    }

    private void configuraBotao() {
        //Encontra o botão por id
        Button botaoSalvar = findViewById(R.id.button_salvar);
        //Ao clicar retorna algo
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalizaFormulario();
            }
        });
    }

    private void finalizaFormulario() {
        preenchePessoa();
        if(cadastro.idValido()){
            dao.edita(cadastro);
        } else{
            dao.salva(cadastro);
        }
        finish();
    }

    private void preenchePessoa() {
        //Converção para String
        String nome = campoNome.getText().toString();
        String endereco = campoEndereco.getText().toString();
        String CEP = campoCEP.getText().toString();
        String RG = campoRG.getText().toString();
        String Telefone = campoTelefone.getText().toString();
        String Genero = campoGenero.getText().toString();
        String Altura = campoAltura.getText().toString();
        String Nascimento = campoNascimento.getText().toString();
        //Passa os dados do cadastro para poder salvar e editar
        cadastro.setNome(nome);
        cadastro.setEndereco(endereco);
        cadastro.setCep(CEP);
        cadastro.setRg(RG);
        cadastro.setTelefone(Telefone);
        cadastro.setGenero(Genero);
        cadastro.setAltura(Altura);
        cadastro.setNascimento(Nascimento);
    }

    private void inicializaCampos() {
        campoNome = findViewById(R.id.editText_nome);
        campoNascimento = findViewById(R.id.editText_nascimento);
        campoAltura = findViewById(R.id.editText_altura);
        campoEndereco = findViewById(R.id.editText_endereco);
        campoCEP = findViewById(R.id.editText_cep);
        campoRG = findViewById(R.id.editText_rg);
        campoTelefone = findViewById(R.id.editText_telefone);
        campoGenero = findViewById(R.id.editText_genero);

        //Mascaras para cada tipo de campo
        SimpleMaskFormatter smfCEP = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtwCEP = new MaskTextWatcher(campoCEP, smfCEP);
        campoCEP.addTextChangedListener(mtwCEP);

        SimpleMaskFormatter smfRG = new SimpleMaskFormatter("NN.NNN.NNN-N");
        MaskTextWatcher mtwRG = new MaskTextWatcher(campoRG, smfRG);
        campoRG.addTextChangedListener(mtwRG);

        SimpleMaskFormatter smfTelefone = new SimpleMaskFormatter("(NN)NNNNNNNNN");
        MaskTextWatcher mtwTelefone = new MaskTextWatcher(campoTelefone, smfTelefone);
        campoTelefone.addTextChangedListener(mtwTelefone);

        SimpleMaskFormatter smfAltura = new SimpleMaskFormatter("N,NN");
        MaskTextWatcher mtwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(mtwAltura);

        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(mtwNascimento);
    }
}