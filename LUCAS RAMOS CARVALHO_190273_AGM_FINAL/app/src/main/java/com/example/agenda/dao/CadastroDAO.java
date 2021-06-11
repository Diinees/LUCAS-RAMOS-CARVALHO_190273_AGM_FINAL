package com.example.agenda.dao;

import com.example.agenda.model.Cadastro;

import java.util.ArrayList;
import java.util.List;

public class CadastroDAO {

    private final static List<Cadastro> CADASTROS = new ArrayList<>();                                  //Cria um índice para a lista.
    private static int contadorDeId = 1;

    public void salva(Cadastro cadastroSalvo) {

        cadastroSalvo.setId(contadorDeId);                                                            //Seta o id para o pessoaSalvo id.
        CADASTROS.add(cadastroSalvo);                                                                   //Adiciona a pessoa salva.
        atualizaId();
    }

    private void atualizaId() {
        contadorDeId++;                                                                             //Agrega +1 ao id.
    }

    public void edita(Cadastro cadastro){                                                               //Método para editar informações.

        Cadastro cadastroEscolhida = buscaIdPessoa(cadastro);
        if(cadastroEscolhida != null){                                                                //Edita a informação do item selecionado.
            int posicaoDaPessoa = CADASTROS.indexOf(cadastroEscolhida);
            CADASTROS.set(posicaoDaPessoa, cadastro);
        }
    }

    private Cadastro buscaIdPessoa(Cadastro cadastro){
        for (Cadastro p :
                CADASTROS) {
            if (p.getId() == cadastro.getId()) {
                return p;
            }
        }
        return null;
    }

    public List<Cadastro> todos() {                                                                   //Método que retorna as informações salvas.

        return new ArrayList<>(CADASTROS);
    }

    public void remove(Cadastro cadastro) {                                                             //Método para remover a pessoa se for diferente de nulo.
        Cadastro cadastroDevolvida = buscaIdPessoa(cadastro);
        if(cadastroDevolvida != null){
            CADASTROS.remove(cadastroDevolvida);
        }
    }
}
