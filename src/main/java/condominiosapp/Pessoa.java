package condominiosapp;

import java.io.Serializable;

public abstract class Pessoa implements Serializable{
    private String nome;
    private String telefone;
    private String cpf;

    Pessoa (String nome, String telefone, String cpf){
        this.nome=nome; this.telefone=telefone; this.cpf=cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {//Se quiser acho q da pra criar dps uma função com os setters para trocar algum dado caso o usuario tenha confirmado errado
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public abstract byte nivelDeAcesso();

    public String toString(){
        return "Nome: "+nome+" Telefone: "+telefone+" CPF: "+cpf;
    }
    //Talvez as subclasses não precisem ter o método toString explicito, pq elas tem os mesmos atributos
}
