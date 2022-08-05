package condominiosapp;

public class Administrador extends Pessoa{

    Administrador(String nome, String telefone, String cpf){
        super(nome,telefone,cpf);
    }

    @Override
    public byte nivelDeAcesso() {
        return 2;
    }
}