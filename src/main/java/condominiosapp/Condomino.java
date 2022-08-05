package condominiosapp;

public class Condomino extends Pessoa{
    Condomino(String nome, String telefone, String cpf){
        super(nome,telefone,cpf);
    }

    @Override
    public byte nivelDeAcesso() {
        return 1;
    }
}