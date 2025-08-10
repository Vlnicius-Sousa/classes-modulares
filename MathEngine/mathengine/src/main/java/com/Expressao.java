package com;

import java.util.ArrayDeque;
import java.util.LinkedList;
/**
 * classe Expressao armazena operandos e operadores, como string a principio, e resultadoAtual, array que armazena o resultado das operações
 */
public class Expressao {
    public LinkedList<String> operandos;
    public ArrayDeque<String> operadores;
    public ArrayDeque<Double> resultadoAtual;
    
    public Expressao(){
        operandos = new LinkedList<>(); 
        operadores  = new ArrayDeque<>();
        resultadoAtual = new ArrayDeque<>();
    }
    /**
     * defineoperando realiza a tarefa de resgatar os operandos do array de operandos, ou do array resultados
     * ou da lista que representa os resultados obtidos de outras expressões internas
     * @param lista lista de precedencia
     * @param operador qual operador é o da vez, pois, operador de pontencia tem um comportamento especial
     * @return
     */
    public double defineoperando (ArrayDeque<Double> lista, String operador) {
        String operandostr1;
        Double operando1 = 0.0;
        // checa se o operador é igual a ^^ pois este sera armazenado como operando novamente na lista de operandos, consultar if de potencia na classe MathEngine
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
        // se o resultado atual está vazio retira string de operando, caso contrário retira um double de retultado atual
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
    /**
     * Está função arruma numeros inteiros de um unico digito, pois, a função parseDouble precisa de um 0 a frente de um digito para retornar
     * um Double de um unico digito 
     * @param operandostr numero para ser arrumado
     * @return retorna um Double de um unico digito
     */
    private double arrumanumero(String operandostr)
    {
        double operandodb = 0; // operando DouBle não data base :p
        String Novooperando = "0".concat(operandostr);
        operandodb = Double.parseDouble(Novooperando); 
        return operandodb;
    }
    /**
     * susbtitui letras por Doubles da lista de precedencia, esta que representa o valor de expressões entre parenteses anteriores
     * @param lista lista de precedencia
     * @return remove um Double a lista
     */
    private static double substituiLetra(ArrayDeque<Double> lista)
    {
        return lista.remove();
    }
}
