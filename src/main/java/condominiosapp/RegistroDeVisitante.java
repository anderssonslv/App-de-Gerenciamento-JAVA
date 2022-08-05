package condominiosapp;

import java.io.Serializable;

public class RegistroDeVisitante implements Serializable{
    private Pessoa quemAutorizou;
    private Pessoa visitante;
    private Unidade unidade;
    private Data dataEntrada;
    private Data dataSaida;

    RegistroDeVisitante(Pessoa p, Pessoa v, Unidade u, Data entrada, Data saida){
        this.quemAutorizou = p;
        this.visitante = v;
        this.unidade = u;
        this.dataEntrada = entrada;
        this.dataSaida = saida;
    }

    public String toString() {

        StringBuilder str = new StringBuilder("\n--------------------------");
        str.append("\nNúmero da Unidade: ").append(unidade.getNumeroDaUnidade());
        str.append("\nAutorizado por: ").append(quemAutorizou.getNome());
        str.append("\nCPF:").append(quemAutorizou.getCpf());
        str.append("\nVisitante: ").append(visitante.getNome());
        str.append("\nCPF:").append(visitante.getCpf());
        str.append("\nEntrada: ").append(dataEntrada.toString());
        str.append("\nSaida: ").append(dataSaida.toString());

        return str.toString();
    }

    //Tarefa Bônus
    public String getUnidade() {
        Short s= unidade.getNumeroDaUnidade();
        return s.toString();
    }
    
    public Pessoa getVisitante() {
        return visitante;
    }
}
