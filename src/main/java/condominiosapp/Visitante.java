package condominiosapp;

public class Visitante extends Pessoa{

    Visitante(String nome, String telefone, String cpf){
        super(nome,telefone,cpf);
    }

    @Override
    public byte nivelDeAcesso() {
        return 0;
    }
}
