import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class AprendizadoDeMaquina {

	static String[][] dadosOriginais = new String[645][5];
	static int[][] dadosNormalizados = new int[645][5];
	static int[][] dadosTreinamento = new int[350][5];
	static int[][] dadosTeste = new int[295][5];
	static final int TAMANHO_BASE_DE_TESTE = 295;
	static final int TAMANHO_BASE_DE_TREINAMENTO = 350;
	static final int TAMANHO_BASE_ORIGINAL = 645;
	static Integer tabelaConfusao[][] = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
	
	public static void main(String [] args)
	{
		leituraDaBaseDeDados();
		normalizacaoDosDados();
		separaDados();
		 
		Scanner ler = new Scanner(System.in);
		System.out.println("Escolha a dist�ncia: ");
		System.out.println("1 -  Dist�ncia Euclidiana\n2 - Dist�ncia de Manhattan\n3 - Dist�ncia de Minkowski");
		int distancia;
		distancia = ler.nextInt();
		
		for(int k = 1; k <= 11; k = k + 2)
			calcularDistancia(k, distancia);
		
		ler.close();
	}
	
	public static void leituraDaBaseDeDados()
	{	
		String nome = "C:\\Users\\Nat�lia\\Documents\\GitHub\\Inteligencia-Artificial\\AprendizadoDeMaquina\\AprendizadoDeMaquina\\src\\balance.txt"; 
	    int contador = 0;
	    try 
	    {
	    	
	      BufferedReader lerArq = new BufferedReader(new FileReader(nome));
	      String linha = lerArq.readLine(); 
	      
	      while (linha != null) {
	    
	    	dadosOriginais[contador] = linha.split(",");
	    
	        linha = lerArq.readLine(); 
	        ++contador;
	      }
	      lerArq.close();
	    } 
	    catch (IOException e) 
	    {
	        System.err.printf("Erro na abertura do arquivo: %s.\n",
	          e.getMessage());
	    }
	}

	public static void normalizacaoDosDados()
	{
		for(int a = 0; a < TAMANHO_BASE_ORIGINAL; a++)
	    {
			for(int b = 0; b < 5; b++)
			{
				if(dadosOriginais[a][b].equals("L"))
					dadosNormalizados[a][b] = 0 ;
				else if(dadosOriginais[a][b].equals("B"))
					dadosNormalizados[a][b] = 1 ;
				else if(dadosOriginais[a][b].equals("R"))
					dadosNormalizados[a][b] = 2 ;
				else 
				{
					dadosNormalizados[a][b] = Integer.valueOf(dadosOriginais[a][b]);
				}
			}
	    }
	}

	public static void separaDados()
	{
		int linha = 0;
		for(int a = 0; a < TAMANHO_BASE_ORIGINAL; a++)
	    {
			for(int b = 0; b < 5; b++)
			{
				if(a < TAMANHO_BASE_DE_TREINAMENTO)
					dadosTreinamento[a][b] = dadosNormalizados[a][b];
				else
					dadosTeste[linha][b] = dadosNormalizados[a][b];
			}
			if(a >= TAMANHO_BASE_DE_TREINAMENTO)
				++linha;
	    }
	}
	
	public static float verificaQuemMaisSeRepete(float[][] distancias_mais_proximas, int k)
	{
		ArrayList<Integer> contadores = new ArrayList<Integer>();
		ArrayList<Float> valores = new ArrayList<Float>();
		int contador = 0;
		float mais_se_repete = 0;
		int maiorValor = 0;
		
		for(int a = 0; a < distancias_mais_proximas.length; a++)
		{
			for(int b = 0; b < distancias_mais_proximas.length; b++)
			{
				if(distancias_mais_proximas[a][0] == distancias_mais_proximas[b][0])
				++contador;
			}
			
			if(!valores.contains(distancias_mais_proximas[a][0]))
			{
				valores.add(distancias_mais_proximas[a][0]);
				contadores.add(contador);
			}
			contador = 0;
		}
		
		// Pego o maior contador
		for(int valor : contadores) {  
		      if(valor > maiorValor) {
		            maiorValor = valor;
		      }
		}
	
		// Vejo se o maior contador se repete.
		int contadorDoMaiorContador = 0;
		for(int contadorAtual : contadores)
		{
			if(contadorAtual == maiorValor)
				++ contadorDoMaiorContador;
		}
		
		// Pego o menor valor dos contadores.
		float menorValor = valores.get(0);
		if(contadorDoMaiorContador != 1)
		{
			contadorDoMaiorContador = 0;
			for(int a = 0; a < valores.size(); a++)
			{
				float valorAtual = valores.get(a);
				int indexValorAtual = valores.indexOf(valorAtual);
				if( contadores.get(indexValorAtual) == maiorValor && valorAtual < menorValor )
					menorValor = valorAtual;
			}
		}
		return menorValor;
	}
	
	public static void calcularDistancia(int k, int distancia)
	{
		float[][] distancias = new float[350][2];
		float[][] distancias_mais_proximas = new float[k][2];
		float menor_distancia;
		
		for(int linhaTeste = 0; linhaTeste < TAMANHO_BASE_DE_TESTE; linhaTeste++)  
		{
			for(int linhaTreinamento = 0; linhaTreinamento < TAMANHO_BASE_DE_TREINAMENTO; linhaTreinamento++ )
			{
				switch (distancia) {
				case 1: // Dist�ncia de Manhattan 
					distancias[linhaTreinamento][0] = Math.abs(dadosTeste[linhaTeste][1] - dadosTreinamento[linhaTreinamento][1]) + 
					Math.abs(dadosTeste[linhaTeste][2] - dadosTreinamento[linhaTreinamento][2]) + 
					Math.abs(dadosTeste[linhaTeste][3] - dadosTreinamento[linhaTreinamento][3]) + 
					Math.abs(dadosTeste[linhaTeste][4] - dadosTreinamento[linhaTreinamento][4]);
					break;

				case 2: // Dist�ncia Euclidiana
					distancias[linhaTreinamento][0] = (float) Math.sqrt((Math.pow((double)dadosTeste[linhaTeste][1] - dadosTreinamento[linhaTreinamento][1], 2) +
							Math.pow((double)dadosTeste[linhaTeste][2] - dadosTreinamento[linhaTreinamento][2], 2) + 
							Math.pow((double)dadosTeste[linhaTeste][3] - dadosTreinamento[linhaTreinamento][3], 2) + 
							Math.pow((double)dadosTeste[linhaTeste][4] - dadosTreinamento[linhaTreinamento][4], 2)));
					break;
				case 3:
					distancias[linhaTreinamento][0] = (float) Math.cbrt((Math.pow((double)dadosTeste[linhaTeste][1] - dadosTreinamento[linhaTreinamento][1], 3) +
							Math.pow((double)dadosTeste[linhaTeste][2] - dadosTreinamento[linhaTreinamento][2], 3) + 
							Math.pow((double)dadosTeste[linhaTeste][3] - dadosTreinamento[linhaTreinamento][3], 3) + 
							Math.pow((double)dadosTeste[linhaTeste][4] - dadosTreinamento[linhaTreinamento][4], 3)));
					break;
				default:
					break;
				}
				
				distancias[linhaTreinamento][1] = linhaTreinamento;
			}
		
			distancias = ordenaLista(distancias);
			int linhaTreinamentoDaMenorDistancia = 0;
			
			for(int a = 0; a < k ; a++)
			{
				distancias_mais_proximas[a][0] = distancias[a][0];
				distancias_mais_proximas[a][1] = distancias[a][1];
			}
			
			if(k == 1)
			{
				menor_distancia = distancias_mais_proximas[0][0];
				linhaTreinamentoDaMenorDistancia = (int) distancias_mais_proximas[0][1];
			}
			else
			{
				menor_distancia = verificaQuemMaisSeRepete(distancias_mais_proximas, k);
				for(int a = 0; a < distancias_mais_proximas.length; a++)
				{
					if(distancias_mais_proximas[a][0] == menor_distancia){
						linhaTreinamentoDaMenorDistancia = (int) distancias_mais_proximas[a][1];
						break;
					}
				}
			}
		
			int classeObtida = 0, classeEsperada = 0;
			
			if(dadosTeste[linhaTeste][0] == 0 )
				classeEsperada = 0;
			else if(dadosTeste[linhaTeste][0] == 1)
				classeEsperada = 1;
			else if(dadosTeste[linhaTeste][0] == 2)
				classeEsperada = 2;
			
			if(dadosTreinamento[linhaTreinamentoDaMenorDistancia][0] == 0)
				classeObtida = 0;
			else if(dadosTreinamento[linhaTreinamentoDaMenorDistancia][0] == 1)
				classeObtida = 1;
			else if(dadosTreinamento[linhaTreinamentoDaMenorDistancia][0] == 2)
				classeObtida = 2;
		
			++tabelaConfusao[classeObtida][classeEsperada];
			}
		imprimeTabelaConfusao(k);
		zerarTabelaConfusao();
	}
	
	public static void zerarTabelaConfusao()
	{
		for(int a = 0; a < 3; a++)
		{
			for(int b = 0; b < 3 ; b++)
			{
				tabelaConfusao[a][b] = 0;
			}
		}
	}
	
 	public static void imprimeTabelaConfusao(int k)
	{
		System.out.println("\n\nTabela confus�o k = " +  k + "\n");
	
		System.out.println("I - Esquerda");
		System.out.println("II - Balanceado");
		System.out.println("III - Direita");
		
		System.out.println("    I  II  III ");
		
		for(int a = 0; a < 3; a++)
		{
			for(int b = 0; b < 3 ; b++)
			{
				if(a == 0 && b == 0)
					System.out.print(" I  ");
				else if(a == 1 && b == 0)
					System.out.print("II  ");
				else if(a == 2 && b == 0)
					System.out.print("III ");
					
				if(b != 2)
					System.out.print(tabelaConfusao[a][b] + "   ");
				else 
					System.out.println(tabelaConfusao[a][b]);
			}
		}
	}
	
	public static float[][] ordenaLista(float[][] listaParaSerOrdenada)
	{
		float auxLinha, auxDistancia;
		
		for(int a = 0; a < listaParaSerOrdenada.length; a++)
		{
			for(int b = 0; b < listaParaSerOrdenada.length; b++)
			{
				if(listaParaSerOrdenada[a][0] < listaParaSerOrdenada[b][0])
				{
					auxDistancia = listaParaSerOrdenada[a][0];
					listaParaSerOrdenada[a][0] = listaParaSerOrdenada[b][0];
					listaParaSerOrdenada[b][0] = auxDistancia;
					
					auxLinha = listaParaSerOrdenada[a][1];
					listaParaSerOrdenada[a][1] = listaParaSerOrdenada[b][1];
					listaParaSerOrdenada[b][1] = auxLinha;
				}
			}
		}
		return listaParaSerOrdenada;
	} 
}
