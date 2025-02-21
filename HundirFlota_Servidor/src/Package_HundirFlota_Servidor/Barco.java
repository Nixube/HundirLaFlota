package Package_HundirFlota_Servidor;

import java.util.ArrayList;
import java.util.Random;

public class Barco {
	private static int sizeTablero = Tablero.numCasillas[0].length;
	private static int[] sizeBarco = { 2, 3, 3, 4 };
	public static boolean[][] casillasOcupadas = new boolean[sizeTablero][sizeTablero];
	private static int idBarco = 1;
	private ArrayList<String> posicionesArray = new ArrayList<String>();
	private ArrayList<String> posicionesCoordenadas = new ArrayList<String>();

	public ArrayList<String> getPosicionesCoordenadas() {
		return posicionesCoordenadas;
	}

	public ArrayList<String> getPosicionesArray() {
		return posicionesArray;
	}

	public static boolean[][] getCasillasOcupadas() {
		return casillasOcupadas;
	}

	public static int getIdBarco() {
		return idBarco;
	}

	// METODO QUE CREA LOS BARCOS DEL USUARIO
	public void crearBarcos() throws InterruptedException {
		Random generar = new Random();
		int barcosPuestos = 0;
		while (barcosPuestos < sizeBarco.length) {
			int posX = generar.nextInt(0, sizeTablero - 1);
			int posY = generar.nextInt(0, sizeTablero - 1);
			int sizeActualBarco = sizeBarco[barcosPuestos];
			boolean colocarHorizontal = generar.nextBoolean();
			int inicio = 0;
			int fin = 0;
			if (colocarHorizontal) {
				inicio = posX;
				fin = posX + sizeActualBarco - 1;
				if ((fin < sizeTablero && fin >= 0) && casillasNoOcupadas(colocarHorizontal, inicio, fin)) {
					System.out.println("Empieza casilla " + inicio + " y acaba en " + fin
							+ ". El barco tiene una longitud de " + sizeActualBarco + " es horizontal");
					posicionesArray.add(inicio + "," + fin + "," + colocarHorizontal);
					idBarco++;
					barcosPuestos++;
				}

			} else {
				inicio = posY;
				fin = posY + sizeActualBarco - 1;
				if ((fin < sizeTablero && fin >= 0) && casillasNoOcupadas(colocarHorizontal, inicio, fin)) {
					System.out.println("Empieza casilla " + inicio + " y acaba en " + fin
							+ ". El barco tiene una longitud de " + sizeActualBarco + " es vertical");
					posicionesArray.add(inicio + "," + fin + "," + colocarHorizontal);
					idBarco++;
					barcosPuestos++;
				}
			}
		}
		coordenadaToPosicion(posicionesArray, posicionesCoordenadas);
	}

	// METODO QUE COMPRUEBA SI LA CASILLA DONDE PONDRA EL BARCO ESTA OCUPADA -->
	// TRUE/FALSE
	private boolean casillasNoOcupadas(boolean esHorizontal, int inicio, int fin) {
		for (int iterar = inicio; iterar <= fin; iterar++) {
			if (esHorizontal) {
				if (casillasOcupadas[inicio][iterar]) {
					return false;
				}
			} else {
				if (casillasOcupadas[iterar][inicio]) {
					return false;
				}
			}
		}

		for (int iterar = inicio; iterar <= fin; iterar++) {
			if (esHorizontal) {
				Tablero.numCasillas[inicio][iterar] = idBarco;
				casillasOcupadas[inicio][iterar] = true;
			} else {
				Tablero.numCasillas[iterar][inicio] = idBarco;
				casillasOcupadas[iterar][inicio] = true;
			}
		}

		return true;
	}

	// METODO QUE TRADUCE LAS COORDENADAS A POSICIONES DE ARRAY --> (A,3) TO (0, 3)
	private static void coordenadaToPosicion(ArrayList<String> posiciones, ArrayList<String> coordenadas) {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String letra = "";
		String inicio = "";
		String fin = "";

		for (String coords : posiciones) {
			inicio = coords.split(",")[0].trim();
			fin = coords.split(",")[1].trim();
			boolean esHorizontal = Boolean.parseBoolean(coords.split(",")[2]);
			int iterarInicio = Integer.parseInt(inicio);
			int iterarFinal = Integer.parseInt(fin);
			if (esHorizontal) {
				for (int i = iterarInicio; i <= iterarFinal; i++) {
					StringBuilder resultado = new StringBuilder();
					int posicionConvertir = i;

					while (posicionConvertir >= 0) {
						resultado.insert(0, letras.charAt(posicionConvertir % 26));
						posicionConvertir = (posicionConvertir / 26) - 1;
					}

					letra = resultado.toString();
					coordenadas.add("(" + letra + "," + inicio + ")");
					System.out.println("Agregada la coordenada (H) " + "(" + letra + "," + inicio + ")");
					if (i == iterarFinal) {
						coordenadas.add(",");
					}
				}
				System.out.println("==========");
			} else {
				int posicionConvertir = Integer.parseInt(inicio);
				StringBuilder resultado = new StringBuilder();
				while (posicionConvertir >= 0) {
					resultado.insert(0, letras.charAt(posicionConvertir % 26));
					posicionConvertir = (posicionConvertir / 26) - 1;
				}

				letra = resultado.toString();
				for (int i = iterarInicio; i <= iterarFinal; i++) {
					coordenadas.add("(" + letra + "," + i + ")");
					System.out.println("Agregada la coordenada (V) " + "(" + letra + "," + i + ")");
					if (i == iterarFinal) {
						coordenadas.add(",");
					}
				}
				System.out.println("==========");
			}
		}
	}

	// METODO QUE RETORNA LAS COORDENADAS PARA PASARSELAS AL USUARIO ==>
	// (E,4)(E,5),(F,5)(F,6)(F,7),(A,0)(A,1)(A,2),(I,8)(I,9)(I,10)(I,11)
	public String coordenadasObtenidas() {
		String coords = "";
		for (String coordenadas : posicionesCoordenadas) {
			coords = coords + coordenadas;
		}
		return coords.substring(0, coords.length() - 1);
	}
}
