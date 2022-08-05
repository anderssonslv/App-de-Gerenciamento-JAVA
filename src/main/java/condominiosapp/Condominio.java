package condominiosapp;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Condominio implements Serializable{
    private String nome;
    private Pessoa administrador;
    private ArrayList <Unidade> arrayUnidades;
    private ArrayList <Visitante> arrayVisitantes;
    private ArrayList <RegistroDeVisitante> arrayRegistro;

    public Condominio(){
        arrayUnidades = new ArrayList<>();
        arrayVisitantes = new ArrayList<>();
        arrayRegistro = new ArrayList<>();
    }

    public boolean incluirUnidade(Unidade u){
        if (u.getNumeroDaUnidade()<1)//só existe unidade maior q 0
            return false;
        if (!arrayUnidades.isEmpty()){//Se o array n estiver vazio
            for (Unidade uni: arrayUnidades){//verifica se a unidade ja existe
                if ((uni.getNumeroDaUnidade())==u.getNumeroDaUnidade()){     
                    return false;
                }
            }
            arrayUnidades.add(u);
            return true;
        }
        else if (arrayUnidades.add(u))//se o array tiver vazio só adiciona
            return true;
        return false;
    }

    public Unidade excluirUnidade(short s){
        Unidade excluida;
        for (Unidade u:arrayUnidades)
            if (u.getNumeroDaUnidade()==s){
                excluida = u;
                arrayUnidades.remove(u);
                return excluida;
            }
        return null;
    }

    public boolean incluirVisitante(Visitante p){
        if (visitanteExiste(p.getCpf()))
            return false;
        return arrayVisitantes.add(p);
    }

    public Pessoa excluirVisitante(String cpf){
        Pessoa excluido;
        for(Visitante v:arrayVisitantes)
            if (v.getCpf().equals(cpf)){
                excluido=v;
                arrayVisitantes.remove(v);
                return excluido;
            }
        return null;
    }

    public boolean registrarVisitante(Pessoa morador, Pessoa visitante, Unidade unidade, Data entrada, Data saida){
        if (unidadeExiste(unidade.getNumeroDaUnidade()) && visitanteExiste(visitante.getCpf())){
            return arrayRegistro.add(new RegistroDeVisitante(morador, visitante, unidade, entrada, saida));
        }
        return false;
    }

    //toString, getters e setters
    public String toString() {
        return "\nComdominio: "+nome+"Administrador: "+administrador.toString(); 
    }
    public String getNome() {
        return nome;
    }
    public Pessoa getAdministrador() {
        return administrador;
    }
    
    public void setAdministrador(Pessoa administrador) {
        this.administrador = administrador;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean incluirMorador (short numUnidade,Pessoa condomino){
        if (arrayUnidades.isEmpty()){//Não vai incluir se n tiver a unidade
            return false;
        }
        if (unidadeExiste(numUnidade)){//se tudo tiver certo, faz o cadastro
            for (Unidade u: arrayUnidades){
                if (u.getNumeroDaUnidade()==numUnidade){
                    u.incluirMorador(condomino);
                    return true;
                }
            }
        }
        return false;
    }//O método jaCadastrado() da classe unidade verifica o array de morador da unidade
    
    public boolean unidadeExiste(short s){
        if (arrayUnidades.isEmpty()){//se o array ta vazio, n existe
            return false;
        }
        for (Unidade u:arrayUnidades){//checa se ja esta cadastrado
            if (u.getNumeroDaUnidade()==s){//Verifica se existe a unidade
                return true;
            }
        }
        return false;
    }
    
    public boolean  visitanteExiste(String cpf){
        if (!arrayVisitantes.isEmpty()){
            for (Visitante v:arrayVisitantes){
                if (v.getCpf().equals(cpf)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public Pessoa excluirMorador(String cpf){
        if (arrayUnidades.isEmpty())
            return null;
        else{
            Pessoa excluido;
            for (Unidade u:arrayUnidades){
                excluido = u.excluirMorador(cpf);
                if (excluido!=null)
                    return excluido;
            }
        }
        return null;
    }

    public String listarMoradores(){
        if (!arrayUnidades.isEmpty()){//Se não estiver vazio
            StringBuilder str = new StringBuilder();
            for (Unidade u: arrayUnidades)
                str.append("\n"+u.listarMoradores());
            return str.toString();
            }
        return "\nNão há unidades cadastradas!";
    }


    public String listarUnidades(){
        if (!arrayUnidades.isEmpty()){//se n estiver vazio
            StringBuilder str = new StringBuilder("------Unidades Cadastradas------");
            for (Unidade u:arrayUnidades)
                str.append("\nNúmero da unidade: "+u.getNumeroDaUnidade());
            return str.toString();
        }
        return "\nNão há unidades cadastradas!";
    }

    
    public String listarVisitantes(){
        if (!arrayVisitantes.isEmpty()){
            StringBuilder str = new StringBuilder("-------Lista Visitantes-------");
            for (Visitante v:arrayVisitantes)
                str.append("\n"+v.toString());
            return str.toString();
        }
        return "\nNão há visitantes cadastrados";
    }
    
    public Pessoa pesquisaCpf (String cpf){
        if (cpf.equals(getAdministrador().getCpf()))
            return administrador;
        
        if (arrayUnidades.isEmpty())
            return null;
        
        for (Unidade u:arrayUnidades){
            if (u.pesquisaCpf(cpf)!=null)
                return u.pesquisaCpf(cpf);
        }
        if (arrayVisitantes.isEmpty())
            return null;
        
        for (Visitante v:arrayVisitantes){
            if (v.getCpf().equals(cpf)){
                return v;
            }
        }
        return null;
    }
    
    public boolean salvaDados(){//Serializa os objetos em um arquivo.ser
        try (
        ObjectOutputStream registros = new ObjectOutputStream(new FileOutputStream("Registros.ser"));
        ObjectOutputStream unidades = new ObjectOutputStream(new FileOutputStream("Unidades.ser"));
        ObjectOutputStream visitantes = new ObjectOutputStream(new FileOutputStream("Visitantes.ser"));
        ObjectOutputStream admin = new ObjectOutputStream(new FileOutputStream("Adm.ser"));
        ObjectOutputStream condominio = new ObjectOutputStream(new FileOutputStream("Condominio.ser"));
        ) {
            registros.writeObject(arrayRegistro);
            unidades.writeObject(arrayUnidades);
            visitantes.writeObject(arrayVisitantes);
            condominio.writeObject(nome);
            admin.writeObject(administrador);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean abreDados(){
        try (
            ObjectInputStream registro = new ObjectInputStream(new FileInputStream("Registros.ser"));
            ObjectInputStream unidades = new ObjectInputStream(new FileInputStream("Unidades.ser"));
            ObjectInputStream visitantes = new ObjectInputStream(new FileInputStream("Visitantes.ser"));
            ObjectInputStream admin = new ObjectInputStream(new FileInputStream("Adm.ser"));
            ObjectInputStream condominio = new ObjectInputStream(new FileInputStream("Condominio.ser"));
        ) {
            arrayRegistro = (ArrayList<RegistroDeVisitante>)registro.readObject();
            arrayVisitantes = (ArrayList<Visitante>)visitantes.readObject();
            arrayUnidades = (ArrayList<Unidade>)unidades.readObject();
            nome = (String)condominio.readObject();
            administrador = (Administrador)admin.readObject();
            return true;
        } catch (Exception e) {
            return false;
        }   
    }

    public boolean arquivaRelatorio(byte n) {//Byte de ordem de arquivo
        try (
            PrintWriter escreveArquivo = new PrintWriter(new BufferedWriter(new FileWriter("Relatórios.txt",false)));
        ) {// Não precisa fechar o arquivo pq ja ta dentro do try resources, e ele ja fecha
            escreveArquivo.append(registroOrdem(n));
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public String registroOrdem(byte b){//Ordena por: 1 = Unidade, 2 = Visitante
        StringBuilder str = new StringBuilder();
        Map <String,String> mapa = new TreeMap<>(); 
        int i=0;
        
        if (b==1){
            for (RegistroDeVisitante r: arrayRegistro){
                mapa.put(r.getUnidade()+"."+i,r.toString()); 
                i++;
            }
        }
        else {
            for (RegistroDeVisitante r: arrayRegistro){
                mapa.put(r.getVisitante().getNome()+i,r.toString());
                i++;
            }
        }
        
        //Deleta os "[]" gerados pelo mapa.values().toString()
        str.append(mapa.values().toString());
        str.deleteCharAt(0);
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }
   
}
