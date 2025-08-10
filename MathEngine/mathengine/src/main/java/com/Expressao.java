package com;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class Expressao {
    public LinkedList<String> operandos;
    public ArrayDeque<String> operadores;
    public ArrayDeque<Double> resultadoAtual;
    
    public Expressao(){
        operandos = new LinkedList<>(); 
        operadores  = new ArrayDeque<>();
        resultadoAtual = new ArrayDeque<>();
    }
    public double defineoperando (ArrayDeque<Double> lista, String operador) {
        String operandostr1;
        Double operando1 = 0.0;

        if(operador == "^^"){
            operandostr1 = operandos.remove();
             if(Character.isLetter(operandostr1.charAt(0)))
            {
                return substituiLetra(lista);
            }
            else if(operandostr1.length() == 1){
                operando1 = arrumanumero(operandostr1);
                return operando1;
            }
            operando1 = Double.parseDouble(operandostr1);
            return operando1;
        }

        if(resultadoAtual.isEmpty()){
            operandostr1 = operandos.remove();
            if(Character.isLetter(operandostr1.charAt(0)))
            {
                return substituiLetra(lista);
            }
            else if(operandostr1.length() == 1){
                operando1 = arrumanumero(operandostr1);
                return operando1;
            }
            operando1 = Double.parseDouble(operandostr1);
            return operando1;
        }
        operando1 = resultadoAtual.remove();
        return operando1;
        
    }
    private double arrumanumero(String operandostr)
    {
        double operandodb = 0; // operando DouBle não data base :p
        String Novooperando = "0".concat(operandostr);
        operandodb = Double.parseDouble(Novooperando); 
        return operandodb;
    }
    private static double substituiLetra(ArrayDeque<Double> lista)
    {
        return lista.remove();
    }
}
