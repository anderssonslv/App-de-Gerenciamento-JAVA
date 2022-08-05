package condominiosapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Unidade implements Serializable {
    private short numeroDaUnidade;
    private ArrayList <Condomino> moradores;

    public Unidade(short num){
        this.numeroDaUnidade=num;
        moradores = new ArrayList<>();
    }

    public short getNumeroDaUnidade() {
        return numeroDaUnidade;
    }

    public boolean incluirMorador(Pessoa p){
        if (moradores.add((Condomino)p))
            return true;
        return false;
    }

    public Pessoa excluirMorador(String s){
        Pessoa excluido;
        for (Condomino c:moradores)
            if (c.getCpf().equals(s)){
                excluido=c;
                moradores.remove(c);
                return excluido;
            }
        return null;
    }

    public String listarMoradores() {
        StringBuilder str = new StringBuilder("\n-----------------Moradores-------------------");
        str.append("\nMoradores da unidade: "+numeroDaUnidade);
        if (moradores.isEmpty())
            str.append("\nNão há moradores nesta unidade!");
        else {
            for (Condomino c:moradores)
                str.append("\n"+c.toString());
        }
        return str.toString();
    }
    
    public Condomino pesquisaCpf(String Cpf){
        for (Condomino c: moradores)
            if (Cpf.equals(c.getCpf()))
                return c;
        return null;
    }

    @Override
    public String toString() {
        return "\nNúmero:"+ numeroDaUnidade;
    }

}
