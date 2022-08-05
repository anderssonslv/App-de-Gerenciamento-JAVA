package condominiosapp;

import java.io.Serializable;

public class Data implements Serializable{
    private byte dia;
    private byte mes;
    private short ano;

    Data(byte d, byte m, short a){
        this.dia = d; this.mes = m; this.ano = a;
    }

    /*Aqui tentamos, mas não conseguimos, ele até estava ordenando pela data de
    entrada, mas se houvesse uma igual ele sobreescrevia, e não conseguimos o 
    desempate, então resolvemos não colocar
    */
    
    public String toString() {
        return (dia < 10 ? "0"+dia : dia) + "/" + (mes < 10 ? "0"+mes : mes) + "/" + ano;
    }
}
