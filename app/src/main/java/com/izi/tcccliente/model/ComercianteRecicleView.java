package com.izi.tcccliente.model;

import com.google.firebase.database.DatabaseReference;
import com.izi.tcccliente.config.ConfiguracaoFirebase;

public class ComercianteRecicleView {

    private String uid;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;



    public ComercianteRecicleView() {
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public String getSenha() {
        return senha;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }
}
