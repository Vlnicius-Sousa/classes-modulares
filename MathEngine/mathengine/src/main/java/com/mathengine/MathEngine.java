package com.mathengine;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import com.Expressao;

public class MathEngine
{
    
	private static Stack<String> precedencia = new Stack<>();
    // define 
	private static ArrayDeque<String> precedenciadeop1 = new ArrayDeque<>(); // da esquerda para a direita '+' e '-'
    private static ArrayDeque<String> precedenciadeop2 = new ArrayDeque<>(); // da esquerda para a direita '*' e '/'
    private static ArrayDeque<String> precedenciadeop3 = new ArrayDeque<>(); // da esquerda para a direita "_/" e "^^"

    private static ArrayDeque<Integer> abreP = new ArrayDeque<>();
    private static ArrayDeque <Integer> fechaP = new ArrayDeque<Integer>();
    

    /**
     * esta variável possui array de objetos que possui duas outras arrays internamente para representar a precedencia de operações. 
     */
    private static ArrayDeque<Expressao> expressao = new ArrayDeque<>();

    private static void termina()
    {
        precedencia.clear();
        precedenciadeop1.clear();
        precedenciadeop2.clear();
        precedenciadeop3.clear();
        fechaP.clear();
        abreP.clear();
    }
    // trecho abaixo demarcado como trecho 1° relacionado a função isExpression
    /**
     * esta função tem como objetivo verificar se um "abre parenteses" tem um par, ou seja, um parenteses de fechamento.
     * e caso tenha um fechamento a função faz uma substring entre o inicio do parenteses e seu par,
     * pois, isto sigfica uma precedencia de operação. a função é recursiva ou seja
     * se haver um parenteses de abertura a função chama a si mesma para verificar se há um parenteses de fechamento
     * @param expression a expreção a ser avaliada
     * @param Iterator o indice do parenteses
     * @return retorna verdadeiro se e somente se haver par de parenteses
     */
    private static boolean ispresend(String expression, int Iterator)
    {
		int ini = (Iterator+1 < expression.length())? Iterator + 1 : 0; //verifica se o inicio do par de parenteses não é o fim da expresão tipo: "1+1("
		if (!(ini == 0)) //se for o fim da expressão nem entra no loop
		{
            char par; //parenteses
            Iterator++;
			for( ;Iterator < expression.length(); Iterator++)
			{
				par = expression.charAt(Iterator);
                //verifica se é um operador ou digito, está aqui para acelerar o processo, pois, caso contrário faria outras 4 verificações 
                if(Character.isDigit(par) || (par == '.' || par == '+' || par == '-' || par == '*' || par == '/' || par == '^' ))
                continue;
                // se for um abre parenteses chama recursivamente
				if (par == '(')
                {
                    abreP.add(Iterator);
                    ispresend(expression, Iterator);
				}
                // caso a fila de fecha parenteses esteja vazia, primeira iteração do codigo adiciona o indice do fecha parentes a fila e adiciona
                // a substring entre o parenteses de inicio, representado pela variável ini e o iterador.
                else if (par == ')' && fechaP.isEmpty())
                {
                    fechaP.add(Iterator);
					precedencia.push(expression.substring(ini, Iterator));
					return true;
                }// caso ache um fecha parenteses interno exemplo 1+(1+(1+1)) no caso o primerio fecha parenteses (1+1)
                 // verifica se o iterador tem o mesmo indice do ultimo fecha parenteses encontrado
                else if (par == ')' && Iterator == fechaP.peek()){
                    continue;
                }// parece redundancia, porém este deve ser o ultimo else if, pois, deve ser executado caso seja "c" seja um fecha parenteses e os outros dois ifs falharem
                 else if (par == ')'){
                    fechaP.add(Iterator);
					precedencia.push(expression.substring(ini, Iterator));
					return true;
                }
			}
		} 
		return false;
    }

    /**
     * esta função faz a verificação da expressão aceitando apenas numeros, ponto "." para casas decimais, operadores "+", "-", "*", "/" e "^^" e parenteses sem espaços
     * @param expression a expressão a ser avaliada
     * @return retorna True caso a expressão tenha apenas caracteres válidos 
     */
    public static boolean isexpression(String expression)
    {
        Integer parenteses; // é utilizado para a confirmação do fechamento de par de parênteses 
        if(expression.isEmpty()) return false;
        int comprimento = expression.length();
        // percorre a string validando cada caractere e chama ispresend caso ache um "("
        for (int i = 0; i < comprimento; i++)
        {
            char c = expression.charAt(i);
            // Continua o loop se o caractere for um dígito ou ponto para indicar casas decimais
            if ( Character.isDigit(c) || c == '.') continue;
            // verifica se o c é um operador e se este não está no final da expreção
            else if((c == '+' || c == '-' || c == '*' || c == '/') && (i+1 < comprimento )) continue;
            else if((c == '^' && expression.charAt(i+1) == '^') && (i+2 < comprimento ))
            {
                i++;
            }
            // o trecho de código abaixo verifica se o char da vez é '(' e se ele possui um par
            else if (c == '(')
            {
                if(abreP.isEmpty()) ispresend(expression, i);
                else if(i == (parenteses = abreP.remove())) continue;
            }
            else if(c == ')' && i == (parenteses = fechaP.remove()))
            {
                continue;
            }
            else 
            {
                termina();
                return false; //encerra o Método e retorna false em caso encontre um caractere inválido
            }
        }   
        return true;
    }

    //1° isExpression

    // trecho a baixo demarcado com 2° formataExprecao
    private static char novovalor = 'a';
    /**
     * esta função faz a substituição de pares de parenteses internos a um parenteses, por exemplo 1+(1+(1+1)) se torna 1+(1+a)
     * @param PcStack uma Stack que representa a precedencia, os parenteses
     */
    private static void arrumaprecedencia(Stack<String> PcStack)
    {
        String expintern = new String(); // expressão dentro de parenteses
        Stack<String> novaPcStack = new Stack<>();
        int comprimento = PcStack.size();
        /*adiciona os elementos de PcStack em ordem */
        for(int i = 0; i <comprimento; i++)
        {
            novaPcStack.add(PcStack.elementAt(i));
        }
        /*percorre cada elemento de PcStack */
        for (int i = 0; i < PcStack.size(); i++)
        {
            String subexp = PcStack.elementAt(i); // faz a atribuição do elemento no indice i a String subexp = Sub expressão
            // percorre a sub expressão
            for (int j = 0; j < subexp.length(); j++)
            {
                char c = subexp.charAt(j); // caractere no indice j, atribuido a variável c
                if(c != '(') // caso não seja um parenteses continua o loop interno
                {
                    continue;
                }
                // se o caractere c for um '(' chega a esse trecho de código
                boolean continua = true;
                do { // faz um do while até encontrar um fecha parenteses ")"
                    c = subexp.charAt(j);
                    if(c != ')'){
                        expintern = expintern.concat(Character.toString(c));
                        j++;
                        continue;
                    }
                    expintern = expintern.concat(Character.toString(c));
                    subexp = subexp.replace(expintern,Character.toString(novovalor));
                    
                    novovalor = (novovalor == 'z') ? 'a' : ++novovalor;
                    
                    novaPcStack.setElementAt(subexp, i);
                    
                    continua = false;
                    
                }while(continua || j < subexp.length());
            }
        }
        PcStack.clear();
        // faz iteração para recolocar as Strings na ordem correta para serem executadas
        for(int i = 0; i < comprimento; i++)
        {
            PcStack.add(novaPcStack.pop());
        }
   
    }
    // 2° formata exprecao
    /**
     * esta função faz a substituição de pares de parenteses, por exemplo 1+(1+(1+1))+(1+1) se torna 1+a+b
     * @param expression uma String que representa a expressão
     * @return retorna a string com letras no lugar dos parenteses
     */
    private static String arrumaprecedencia(String expression)
    {
        String expintern = new String();
        int comprimento = expression.length();
    
        for (int j = 0; j < comprimento ; j++)
        {
            char c = expression.charAt(j) ;
            if(c == '(')
            {
                abreP.add(j);
                expintern = expintern.concat(Character.toString(c));
                j++;
                boolean continua = true;
                do {
                    c = expression.charAt(j) ;
                    if(c == '(')
                    {
                        expintern = expintern.concat(Character.toString(c));
                        abreP.add(j);
                    } else if (c == ')'){
                        expintern = expintern.concat(Character.toString(c));
                        abreP.remove();
                    } else expintern = expintern.concat(Character.toString(c));
                    
                    if(c == ')' && abreP.isEmpty()){

                        expression = expression.replace(expintern,Character.toString(novovalor));

                        novovalor = (novovalor == 'z')?'a':++novovalor;
                            
                        continua = false;
                    }
                    
                    j++;
                }while(continua);
                expintern = "";
                comprimento = expression.length();
                j = 0;
            }
            
        }
        return expression;
    }

    
    /**
     * esta função apada o operador da string
     * @param operador o operador a ser apagado
     * @param expressao a string que contem o operador
     * @return retorna a string sem a primeira ocorrencia do operador
     */
    private static String apagaoperador(String operador, String expressao){
        // pega a primeira ocorrencia do operador na string
        int index = expressao.indexOf(operador);
        // transforma a string em array
        char[] antingaexprecao = expressao.toCharArray();
        // pega o tamanho do operador 
        int tamanhodoOperador = (index + operador.length());
        // percorre o array deixando um espaço em branco em cada indice onde contem um caractere de operador
        for(int j = index; j < tamanhodoOperador; j++){
            if(antingaexprecao[j] == '+' || antingaexprecao[j] == '-' || antingaexprecao[j] == '*'
            || antingaexprecao[j] == '/' || antingaexprecao[j] == '^')
            {
                antingaexprecao[j] = ' ';
            }else continue;
        }
        expressao = String.valueOf(antingaexprecao); // transforma de char[] para String
        
        return expressao;
    }

    /**
     *  esta função faz a troca do operando por um espaço em branco, exemplo: operando = 1+ e expressao = 2*3/1+ retorna 2*3/ +
     * @param operando a string que possui o operando e seu operador
     * @param expressao a string de referencia para apagar o operando
     * @return retorna a string sem o operando
     */
    private static String trocaoperando(String operando, String expressao)
    {
        // pega a primeira ocorrencia de operando
        int index = expressao.indexOf(operando);
        // transforma expressao em array
        char[] antingaexprecao = expressao.toCharArray();
        // pega o tamanho da substring do operando
        int tamanhodasubstring = (index + operando.length());
        // percorre a expressao e apaga quaisquer digitos, pontos ou letras
        for(int j = index; j < tamanhodasubstring; j++){
            if(Character.isDigit(antingaexprecao[j]) || antingaexprecao[j] == '.' || Character.isLetter(antingaexprecao[j])){
                antingaexprecao[j] = ' ';
            }else continue;
        }
        expressao = String.valueOf(antingaexprecao);
        
        return expressao;
    }
    // 2° formataExprecao
    /**
     * está função faz a captura dos operadores em seguida faz a adição destes a um array de operadores
     * @param exprecao
     */
    private static void SetoperandoEoperador(String exprecao)
    {
        // set dos operadores
        Expressao subExpressao = new Expressao();
        int comprimento = exprecao.length();

        for (int i = 0; i < comprimento; i++ )
        {
            char caractere = exprecao.charAt(i);
            if(caractere == '+' || caractere == '-'){
                precedenciadeop1.add(Character.toString(caractere));
            }
            else if(caractere == '/' || caractere == '*'){
                precedenciadeop2.add(Character.toString(caractere));
            }
            else if(caractere == '^' && exprecao.charAt(i+1) == '^'){
                precedenciadeop3.add("^^");
                i++;
            } 
            
        }
        int operadoresint = precedenciadeop1.size() + precedenciadeop2.size() + precedenciadeop3.size();
        for(int j = 0; j < operadoresint; j++)
        {
            if(!precedenciadeop3.isEmpty()) subExpressao.operadores.add(precedenciadeop3.remove());
            else if(!precedenciadeop2.isEmpty()) subExpressao.operadores.add(precedenciadeop2.remove());
            else if(!precedenciadeop1.isEmpty()) subExpressao.operadores.add(precedenciadeop1.remove());
        }
        // set dos operandos
        int ops = subExpressao.operadores.size();
        String operador;
        String operando;
        String novaexprecao = exprecao;

        char numero;

        Iterator<String> iterador = subExpressao.operadores.iterator();

        while(iterador.hasNext()){
            operador = iterador.next();
            int op = novaexprecao.indexOf(operador);
            comprimento = 0;
            if(!(novaexprecao.charAt(op-1) == ' ')){
                int tamnhodonumero = op;
                operando = "";
                for(int l = op-1; l >= comprimento; l--)
                {
                    numero = novaexprecao.charAt(l);
                    if(Character.isDigit(numero) || numero == '.' || Character.isLetter(numero)){
                        tamnhodonumero--;
                    }else{
                        break;
                    }
                }
                operando = exprecao.substring(tamnhodonumero, op);
                subExpressao.operandos.add(operando);
                String operadorReplace = operando.concat(operador);
                novaexprecao = trocaoperando(operadorReplace, novaexprecao);
            }
            op = novaexprecao.indexOf(operador)+1;
            if(operador.length() > 1)
            {
                    op++;
            }
            comprimento = exprecao.length();
            if(!(novaexprecao.charAt(op) == ' ')){
                

                operando = "";
                for(int h = op; h < comprimento; h++)
                {
                    numero = novaexprecao.charAt(h);
                    if(Character.isDigit(numero) || numero == '.' || Character.isLetter(numero)){
                        operando = operando.concat(Character.toString(numero));
                    }else{
                        break;
                    }
                }
                subExpressao.operandos.add(operando);
                String operadorReplace = operador.concat(operando);
                novaexprecao = trocaoperando(operadorReplace, novaexprecao);
            }
            novaexprecao = apagaoperador(operador, novaexprecao);
        }
        expressao.add(subExpressao);
    }
    
    private static void formataexprecao (String expression)
    {
        

        if(!precedencia.empty())
        {

            String subexprecao = new String();
            arrumaprecedencia(precedencia);

            do {
                subexprecao = precedencia.pop();
                
                SetoperandoEoperador(subexprecao);

            } while (!precedencia.empty());
            abreP.clear();
            SetoperandoEoperador(arrumaprecedencia(expression));

        }else
        SetoperandoEoperador(expression);
        
    }
    // 2° formataExprecao

    // trecho abaixo demarcado com 3° fazCalculo

    private static ArrayDeque<Double> listaDprecedencia = new ArrayDeque<>(); // guarda os valores de expressões entre parenteses

    // 3° fazCalculo

    private static Double Soma (BigDecimal operando1, BigDecimal operando2){
        BigDecimal BigResultado;
        BigResultado = operando1.add(operando2);
        return BigResultado.doubleValue();
    }

    // 3° fazCalculo

    private static Double Subtrai (BigDecimal operando1, BigDecimal operando2){
        BigDecimal BigResultado;
        BigResultado = operando1.subtract(operando2);
        return BigResultado.doubleValue();
    }

    // 3° fazCalculo

    private static Double multiplica (BigDecimal operando1, BigDecimal operando2){
        BigDecimal BigResultado;
        BigResultado = operando1.multiply(operando2);
        return BigResultado.doubleValue();
    }

    // 3° fazCalculo

    private static Double divide (BigDecimal operando1, BigDecimal operando2){
        BigDecimal BigResultado;
        BigResultado = operando1.divide(operando2);
        return BigResultado.doubleValue();
    }

    // 3° fazCalculo

    private static Double Potencializa (double operando1, double operando2){
        
        return Math.pow(operando1, operando2);
    }


    public static String fazCalculo (String expression)
    {  
        String resultado = new String();

        formataexprecao(expression);
        String operador;
        Double operando1;
        Double operando2;
        BigDecimal BigOperando1;
        BigDecimal BigOperando2;
        for(Expressao subExpressao : expressao){

            int ops = subExpressao.operadores.size();
    
            for(int i = 0; i < ops; i++)
            {
                operador =  subExpressao.operadores.remove();
                operando1 = subExpressao.defineoperando(listaDprecedencia, operador);
                operando2 = subExpressao.defineoperando(listaDprecedencia, operador);
                
                BigOperando1 = BigDecimal.valueOf(operando1);
                BigOperando2 = BigDecimal.valueOf(operando2);
    
                if(operador.equals("^^")){
                    subExpressao.operandos.add(String.valueOf(Potencializa(operando1, operando2)));
                }
                else if (operador.equals("*")){
                    subExpressao.resultadoAtual.add(multiplica(BigOperando1, BigOperando2));
                }
                else if(operador.equals("/")){
                    subExpressao.resultadoAtual.add(divide(BigOperando1, BigOperando2));
                }
                else if(operador.equals("+")){
                    subExpressao.resultadoAtual.add(Soma(BigOperando1, BigOperando2));
                }
                else if(operador.equals("-")){
                    subExpressao.resultadoAtual.add(Subtrai(BigOperando1, BigOperando2));
                }
            }
            if(!subExpressao.resultadoAtual.isEmpty()){
                listaDprecedencia.add(subExpressao.resultadoAtual.remove());
            }
            else{
                listaDprecedencia.add(Double.valueOf(subExpressao.operandos.remove()));
            }

        }
        resultado = Double.toString(listaDprecedencia.getFirst());
        termina();
        return resultado;
    }
}