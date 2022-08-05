package condominiosapp;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

/*
Sobre a verificação de dados: caso n tenha dados, ou o usuario 
não queira verificar, ele inicia uma sessão do zero sem registros

Na primeira sessão É obrigatório a inserção de um administrador e um nome 
para o condominio para o sistema de login, pois precisa ter no minimo UM cpf 
registrado, que será o primeiro login!
Essas configurações podem ser alteradas depois em "Gerenciar Condominio"

Caso, o usuario tenha dados salvos e tenha iniciado sem querer uma primeira
sessão o programa informa que os dados anteriores podem ser perdidos!
Pois a cada alteração dos dados o sistema salva os dados automaticamente!

Achamos melhor mudar o retorno do método login() para Pessoa, caso a pessoa
esteja registrada no sistema, ele retorna a pessoa que esta tentado logar
e configura a GUI de acordo com o nívelDeAcesso() se for o Administrador ou
Condômino, e se for visitante ele solta uma mensagem, pela função modoInterface()

A parte principal da interface gráfica do programa foi feita no netbeans,
porém houve a necessidade de criar "a mão" uma tela para inputs genérica,
que pudesse ser usada tanto para registro de visitantes, datas, e etc
que é aberta apartir de um JOptionPane personalidado de acordo com o
valor do argumento (b) na função input(b), onde os inputs são passados
para um arraylist que é retornado, e o index de cada input é passado para a 
função que deseja realizar!

Para registrar um visitante, ele precisa estar cadastrado, e quando registrado
ele é salvo no arquivo de relatórios por ordem de nome

Ao fazer a tarefa bônus, vimos q o treemap não aceita keys repetidas, pois 
sobreescreve, então, na key do map de Strings, colocamos um número (i) 
apos o nome do visitante, caso tenha 2 visitantes com o mesmo nome. 
E na ordem da unidade colocamos um caractere de ponto + o número (i), 
pois esse número não poderia ficar na frente do número da unidade, feito isso 
o algoritimo de ordenação de Treemap,consegue ordenar, mesmo q tenha mais de 
uma unidade com o mesmo número!
OBS: Não conseguimos fazer a parte das datas

Desenvolvido por Andersson De Souza Silveira e Ricardo Coutinho Cordeiro
*/

public class AppGestaoDeCondominios extends javax.swing.JFrame {
    
    private Condominio condominio; 
        
    public AppGestaoDeCondominios() {
        super("AppCondominios");
        
        condominio = new Condominio ();
        
        initComponents();//Carrega a GUI
        setLocationRelativeTo(null);//Pra ficar no meio da tela
        
        int op = JOptionPane.showConfirmDialog(null, null,"Deseja verificar dados?", JOptionPane.OK_CANCEL_OPTION);
        if (op == 0){
            if (abreDados())
                JOptionPane.showMessageDialog(null, "Dados carregados");
            else {
                JOptionPane.showMessageDialog(null, "Dados não Encontrados");
                primeiroCad();
            }
        } else if (op==-1){System.exit(0);}//Se clicar no x apenas fecha
        else primeiroCad();
        

        Pessoa p;
        while(true){
            p = login();
            if (p==null){
                JOptionPane.showMessageDialog(null, "Não Encontrado no sistema!");
            }
            if (p instanceof Visitante){
                JOptionPane.showMessageDialog(null,"Visitantes não são permitidos!");
            }
            if(p instanceof Administrador|| p instanceof Condomino)
                break;
        }
        setVisible(true);
        modoInterface(p);
        
        jLabel1.setText("Condominio: "+condominio.getNome()+"    Administrador: "+condominio.getAdministrador().getNome());
    }
    
    public static void main(String[] args) {
        AppGestaoDeCondominios App = new AppGestaoDeCondominios();
    }
    
    private Pessoa login(){
        String cpf = JOptionPane.showInputDialog("Digite CPF de Login:");
        if (cpf==null){System.exit(0);}
        return condominio.pesquisaCpf(cpf);
    }
 
    
    private void primeiroCad(){//Função caso a pessoa inicie uma sessao nova, sem backup, ou seja a primeira sessão
        ArrayList <String> str = new ArrayList<>();
        while (true){
            try {
                str = input((byte)2);
                if (str.contains("-1"))//Se o usuario apertar no x ela retorna um "-1" e o programa fecha
                    System.exit(0);
                gerenciarCondominio(str.get(3),str.get(0),str.get(1),str.get(2));
                if (!(str.get(3).isBlank() || str.get(0).isBlank() || str.get(1).isBlank() || str.get(2).isBlank()))
                    break;//Se n tiver campo em branco ele quebra o laço
            } catch (Exception e) {//str volta null e joga uma exception caso tenha campo em branco
                JOptionPane.showMessageDialog(null,"Preencha todos os campos");
                continue;
            }
        }
        attpainel("Você esta em uma primeira sessão, caso ja tenha dados salvos, apenas feche o programa!");
    }
    
    
    private void incluirVisitante(Visitante v){
        if (condominio.incluirVisitante(v)){
            attpainel("Visitante incluido com sucesso!");
            salvaDados();
        }
        else attpainel("Não foi possivel incluir visitante");
        
    }

    private void excluirVisitante(String s){
        if(condominio.excluirVisitante(s)==null)
            attpainel("Não foi possivel excluir visitante");
        else {
            attpainel("Visitante excluido com sucesso!");
            salvaDados();
        }
    }
    //não registra caso a unidade não exista, ou o visitante n esteja cadastrado no sistema
    private void registrarVisitante(Pessoa autorizador, Pessoa visitante, Unidade unidade, Data entrada, Data saida){
        if(condominio.registrarVisitante(autorizador, visitante, unidade, entrada, saida)){
            attpainel("Visitante registrado no relatório, e Arquivado");
            condominio.arquivaRelatorio((byte)2);//Por padrão arquiva pela ordem de Visitante
            salvaDados();
        }
        else attpainel("Não foi possivel registrar no relatório, Verifique se a unidade existe, ou se o Visitante está incluso!");
    }

    private void incluirMorador(short numUnidade,Pessoa condomino){
        if(condominio.incluirMorador(numUnidade,condomino)){
            attpainel("Morador incluido com sucesso!");
            salvaDados();
        }
        else attpainel("Não foi possivel incluir morador!\nVerifique se a unidade que está tentando cadastrar existe");
    }
    
    private void incluirUnidade(Unidade u){
        if (condominio.incluirUnidade(u)){
            attpainel("Unidade incluida com sucesso!");   
            salvaDados();
        }          
        else attpainel("Não foi possivel incluir unidade, Verifique se a unidade ja existe!");
    }

    private void listarUnidades(){
        attpainel(condominio.listarUnidades());
    }

    private void listarMoradores(){
        attpainel(condominio.listarMoradores());
    }

    private void listarVisitantes(){
        attpainel(".");
        attpainel(condominio.listarVisitantes());
    }

    private void relatorioRegistroDeVisitantes(byte b){//Ordena por: 1 = Unidade, 2 = Visitante, 3 = Data
        attpainel("\n");
        attpainel(".");
        attpainel(condominio.registroOrdem(b));
    }

    private void salvaDados(){
            if(condominio.salvaDados())
                JOptionPane.showMessageDialog(null,"Backup Salvo");
            else JOptionPane.showMessageDialog(null,"Erro ao salvar Dados");
    }
    
    private void attpainel(String str){//Função para mostrar as informações na tela
        if (!informacoesCampoTexto.getText().equals(str))//se a info do painel não for igual a nova ele troca a info
            informacoesCampoTexto.setText(str);
        else informacoesCampoTexto.setText("\n"+str);//se for a mesma coisa ele só mostra na ourta linha
    }

    //input() é uma função genérica para mostrar uma tela de inputs
    //1 Para morador  2 Para Editar Condominio 3 Data entrada 4 Data Saida e -1 para visitante
    //0 e 1 também são para o registro de visitantes onde 0 é autorizador e 1 unidade
    private ArrayList<String> input(byte n){
        ArrayList <String> str = new ArrayList<>();
        JTextField campo1 = new JTextField(15);
        JTextField campo2 = new JTextField(15);
        JTextField campo3 = new JTextField(15);
        JTextField campoExtra = new JTextField(15); 
        JPanel myPanel = new JPanel();  
        
        if (n<3){//Para Pessoa
            myPanel.setLayout(new GridLayout(4,2));
            if (n==2){myPanel.add(new JLabel("Digite Nome Administrador: "));}
            else myPanel.add(new JLabel("Digite Nome: "));
            myPanel.add(campo1);
        
            myPanel.add(new JLabel("Digite Telefone: "));
            myPanel.add(campo2);

            myPanel.add(new JLabel("Digite Cpf:"));
            myPanel.add(campo3);
            
            String tipo;
            if (n==-1)
                tipo = "Preencha dados do Visitante";
            else if (n==0)
                tipo = "Preencha os Dados do Autorizador";
            else tipo ="Preencha os campos";
            
            if (n==1){
                myPanel.add(new JLabel("Digite Unidade:"));
                myPanel.add(campoExtra); 
            } else if (n==2){
                myPanel.add(new JLabel("Digite Nome do Condominio:"));
                myPanel.add(campoExtra);
            }
            int result = JOptionPane.showConfirmDialog(null, myPanel,tipo, JOptionPane.OK_CANCEL_OPTION);
            
            if (result == -1 || result == 2){//Caso clique no x volta -1 e fecha o programa se for a tela inicial
                str.add("-1");
                return str;
            }
            if (result == JOptionPane.OK_OPTION) {
                str.add(campo1.getText());
                str.add(campo2.getText());
                str.add(campo3.getText());        
                if (n==1||n==2)
                    str.add(campoExtra.getText());
            }

        }
        else if (n>=3){//Datas
            myPanel.setLayout(new GridLayout(3,2));
            myPanel.add(new JLabel("Digite Dia: "));
            myPanel.add(campo1);
        
            myPanel.add(new JLabel("Digite Mes:"));
            myPanel.add(campo2);
            
            myPanel.add(new JLabel("Digite Ano: "));
            myPanel.add(campo3);

            int result;
            if (n==3)
                result = JOptionPane.showConfirmDialog(null, myPanel,"Digite Data de entrada", JOptionPane.OK_CANCEL_OPTION);
            else result = JOptionPane.showConfirmDialog(null, myPanel,"Digite Data de saida", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                str.add(campo1.getText());
                str.add(campo2.getText());
                str.add(campo3.getText());
            }
            if (result == -1 || result == 2)//se o usuario clicar em cancelar ou no x, retorna null
                return null;
        }
        if (str.contains(""))//Se tiver campo em branco retorna null
            return null;
        return str;
    }
    
    private void modoInterface(Pessoa p){
        if (p.nivelDeAcesso()==1){
            gerenciarCondominio.setVisible(false);
            removerMorador.setVisible(false);
            cadastrarUnidade.setVisible(false);
            listarMorador.setVisible(false);
            listarUnidade.setVisible(false);
            listarRegistros.setVisible(false);
            listarVisitante.setVisible(false);
            jLabel2.setText("Você logou como: Condômino");
        } else jLabel2.setText("Você logou como: Administrador");
    }

    private boolean abreDados(){
        if (condominio.abreDados())
            return true;
        return false;
    }

    //Funcionado
    private void excluirMorador(String cpf){
        if(condominio.excluirMorador(cpf)!=null)
            attpainel("Morador exluido com sucesso!");
        else attpainel("Não foi possivel excluir morador");
    }
    
    public void gerenciarCondominio(String condominio,String nome,String tel,String cpf){
        this.condominio.setNome(condominio);
        this.condominio.setAdministrador(new Administrador(nome, tel, cpf));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        informacoesCampoTexto = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        gerenciarCondominio = new javax.swing.JMenuItem();
        cadastrarMorador = new javax.swing.JMenuItem();
        cadastrarVisitante = new javax.swing.JMenuItem();
        removerMorador = new javax.swing.JMenuItem();
        removerVisitante = new javax.swing.JMenuItem();
        cadastrarUnidade = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        listarMorador = new javax.swing.JMenuItem();
        listarRegistros = new javax.swing.JMenuItem();
        listarVisitante = new javax.swing.JMenuItem();
        listarUnidade = new javax.swing.JMenuItem();
        registrarVisitante = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 450));

        jDesktopPane1.setLayout(new java.awt.GridBagLayout());

        informacoesCampoTexto.setEditable(false);
        informacoesCampoTexto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jScrollPane1.setViewportView(informacoesCampoTexto);
        informacoesCampoTexto.setText("");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 472;
        gridBagConstraints.ipady = 263;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(35, 6, 6, 6);
        jDesktopPane1.add(jScrollPane1, gridBagConstraints);

        jLabel1.setText("aaaa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 418;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 6);
        jDesktopPane1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 376;
        gridBagConstraints.ipady = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jDesktopPane1.add(jLabel2, gridBagConstraints);

        jMenu1.setText("Cadastro");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        gerenciarCondominio.setText("Gerenciar Condominio");
        gerenciarCondominio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gerenciarCondominioActionPerformed(evt);
            }
        });
        jMenu1.add(gerenciarCondominio);

        cadastrarMorador.setText("Cadastrar Morador");
        cadastrarMorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarMoradorActionPerformed(evt);
            }
        });
        jMenu1.add(cadastrarMorador);

        cadastrarVisitante.setText("Cadastrar Visitante");
        cadastrarVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarVisitanteActionPerformed(evt);
            }
        });
        jMenu1.add(cadastrarVisitante);

        removerMorador.setText("Remover Morador");
        removerMorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerMoradorActionPerformed(evt);
            }
        });
        jMenu1.add(removerMorador);

        removerVisitante.setText("Remover Visitante");
        removerVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerVisitanteActionPerformed(evt);
            }
        });
        jMenu1.add(removerVisitante);

        cadastrarUnidade.setText("Cadastrar Unidade");
        cadastrarUnidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarUnidadeActionPerformed(evt);
            }
        });
        jMenu1.add(cadastrarUnidade);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Registros");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        listarMorador.setText("Listar Morador");
        listarMorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarMoradorActionPerformed(evt);
            }
        });
        jMenu2.add(listarMorador);

        listarRegistros.setText("Listar Registros");
        listarRegistros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarRegistrosActionPerformed(evt);
            }
        });
        jMenu2.add(listarRegistros);

        listarVisitante.setText("Listar Visitante");
        listarVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarVisitanteActionPerformed(evt);
            }
        });
        jMenu2.add(listarVisitante);

        listarUnidade.setText("Listar Unidade");
        listarUnidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarUnidadeActionPerformed(evt);
            }
        });
        jMenu2.add(listarUnidade);

        registrarVisitante.setText("Registrar Visitante");
        registrarVisitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarVisitanteActionPerformed(evt);
            }
        });
        jMenu2.add(registrarVisitante);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cadastrarMoradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarMoradorActionPerformed
        ArrayList <String> str = new ArrayList<>();
        str=input((byte)1);
        try {
            short s = Short.parseShort(str.get(3));
            incluirMorador(s,new Condomino(str.get(0), str.get(1), str.get(2)));
        } catch (NumberFormatException e) {
            attpainel("Parametro invalido!");
        } catch (Exception e){
            attpainel("Ação cancelada!");
        }
    }//GEN-LAST:event_cadastrarMoradorActionPerformed

    private void removerMoradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerMoradorActionPerformed
        String str = JOptionPane.showInputDialog("Digite o CPF:");
        excluirMorador(str);
    }//GEN-LAST:event_removerMoradorActionPerformed

    private void cadastrarUnidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarUnidadeActionPerformed
        try {
            String u = JOptionPane.showInputDialog("Digite o número da Unidade que deseja cadastrar: ");
            Short s = Short.parseShort(u);
            incluirUnidade(new Unidade(s));
        } catch (Exception e) {
            attpainel("Não foi possivel registrar unidade");
        }
        
    }//GEN-LAST:event_cadastrarUnidadeActionPerformed

    private void listarMoradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listarMoradorActionPerformed
        listarMoradores();
    }//GEN-LAST:event_listarMoradorActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void gerenciarCondominioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gerenciarCondominioActionPerformed
        ArrayList <String> str = new ArrayList<>();
        str = input((byte)2);
            try {
                gerenciarCondominio(str.get(3),str.get(0),str.get(1),str.get(2));
                attpainel("Nome e Administrador Atualizados!");
                salvaDados();
                jLabel1.setText("Condominio: "+condominio.getNome()+"    Administrador: "+condominio.getAdministrador().getNome());
            } catch (Exception e) {
                attpainel("Não foi possivel alterar informações do condominio");
            }
    }//GEN-LAST:event_gerenciarCondominioActionPerformed

    private void removerVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerVisitanteActionPerformed
        excluirVisitante(JOptionPane.showInputDialog("Digite CPF:"));
    }//GEN-LAST:event_removerVisitanteActionPerformed

    private void cadastrarVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarVisitanteActionPerformed
        ArrayList <String> str = new ArrayList<>();
        try {
            str.addAll(input((byte)-1));
            incluirVisitante(new Visitante(str.get(0), str.get(1), str.get(2)));
        } catch (Exception e) {
            attpainel("Não foi possivel Cadastrar Visitante!");
        }
    }//GEN-LAST:event_cadastrarVisitanteActionPerformed

   
    private void listarRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listarRegistrosActionPerformed
        try {
            byte b = Byte.parseByte(JOptionPane.showInputDialog("Digite a ordem:\n1=Unidade\n2=Visitante"));
            relatorioRegistroDeVisitantes(b); 
        } catch (NumberFormatException e) {
            attpainel("Parametro Incorreto");
        } catch (Exception e){
            attpainel("Ação cancelada!");
        }
    }//GEN-LAST:event_listarRegistrosActionPerformed

    private void listarVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listarVisitanteActionPerformed
        listarVisitantes();
    }//GEN-LAST:event_listarVisitanteActionPerformed

    private void listarUnidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listarUnidadeActionPerformed
        listarUnidades();
    }//GEN-LAST:event_listarUnidadeActionPerformed

    private void registrarVisitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarVisitanteActionPerformed
        ArrayList <String> str = new ArrayList<>();
        try {
            str.addAll(input((byte)0));//autorizador
            str.addAll(input((byte)3));//entrada
            str.addAll(input((byte)4));//saida
            str.addAll(input((byte)-1));//Visitante
            Pessoa autorizador = new Condomino(str.get(0),str.get(1),str.get(2));
            Pessoa visitante = new Visitante(str.get(9),str.get(10),str.get(11));
            registrarVisitante(
                    autorizador, 
                    visitante, 
                    new Unidade(Short.parseShort(JOptionPane.showInputDialog("Digite o Número da unidade"))), 
                    new Data(Byte.parseByte(str.get(3)),Byte.parseByte(str.get(4)),Short.parseShort(str.get(5))), 
                    new Data(Byte.parseByte(str.get(6)),Byte.parseByte(str.get(7)),Short.parseShort(str.get(8)))
            );
        } catch (Exception e) {
            attpainel("Não foi possivel registrar");
        }
    }//GEN-LAST:event_registrarVisitanteActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem cadastrarMorador;
    private javax.swing.JMenuItem cadastrarUnidade;
    private javax.swing.JMenuItem cadastrarVisitante;
    private javax.swing.JMenuItem gerenciarCondominio;
    private javax.swing.JTextPane informacoesCampoTexto;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem listarMorador;
    private javax.swing.JMenuItem listarRegistros;
    private javax.swing.JMenuItem listarUnidade;
    private javax.swing.JMenuItem listarVisitante;
    private javax.swing.JMenuItem registrarVisitante;
    private javax.swing.JMenuItem removerMorador;
    private javax.swing.JMenuItem removerVisitante;
    // End of variables declaration//GEN-END:variables
}
