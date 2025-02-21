package Package_HundirFlota_Cliente;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

public class Cliente {

	private String nombre;
	private int puntos;
	private String[] barcos;

	public Cliente(String nombre, int puntos, String[] barcos) {
		this.nombre = nombre;
		this.puntos = puntos;
		this.barcos = barcos;
	}

	public String[] getBarcos() {
		return barcos;
	}

	// METODO QUE REGISTRA AL CLIENTE
	public static void registrarCliente(String usuario, int puntos, String[] barcosObtenidos) {
		System.out.println("Se ha creado el Cliente " + usuario + "!");
		Cliente crearCliente = new Cliente(usuario, puntos, barcosObtenidos);
		crearCliente.dibujarBarcos(crearCliente.getBarcos());
	}

	// METODO QUE DIBUJA LOS BARCOS EN EL MAPA
	public void dibujarBarcos(String[] barcos) {
		System.out.println("Dibujando barcos!");
		ArrayList<JLabel> tablero = Main.listaCasillas;
		Color[] coloresElegidos = { Color.PINK, Color.BLACK, Color.YELLOW, Color.MAGENTA };

		int indexColor = 0;

		for (String posTablero : barcos) {
			String coordenada = traducirCoordenadas(posTablero);
			System.out.println("Se ha traducido la coordenada " + coordenada);

			Color colorBarco = coloresElegidos[indexColor];
			indexColor = (indexColor + 1) % coloresElegidos.length;
			for (int i = 0; i < coordenada.split("\\)").length; i++) {
				String numero1 = coordenada.split("\\)")[i].split(",")[0].substring(1).trim();
				String numero2 = coordenada.split("\\)")[i].split(",")[1].trim();
				String posicionColocar = numero1 + " " + numero2;

				for (JLabel casilla : tablero) {
					if (casilla.getText().equals(posicionColocar)) {
						casilla.setBackground(colorBarco);
					}
				}
			}
		}
	}

	// METODO QUE TRADUCE LAS COORDENADAS DE ARRAY A LETRA --> (B,3) TO (1,3)
	private static String traducirCoordenadas(String coordenada) {
		System.out.println("Traduciendo letra " + coordenada);
		String posicion = "";
		int numero = 0;
		String posicionLetra = "";
		String posicionNumero = "";
		for (int i = 0; i < coordenada.split("\\)\\(").length; i++) {
			if (i == 0) {
				posicionLetra = coordenada.split("\\)\\(")[i].split(",")[0].substring(1, 2);
				posicionNumero = coordenada.split("\\)\\(")[i].split(",")[1] + ")";
			} else {
				posicionLetra = coordenada.split("\\)\\(")[i].split(",")[0];
				posicionNumero = coordenada.split("\\)\\(")[i].split(",")[1] + ")";
			}
			numero = posicionLetra.charAt(0) - 'A';
			posicion = posicion + "(" + numero + "," + posicionNumero;
		}
		return posicion.substring(0, posicion.length() - 1);
	}

	// METODO QUE OBTIENE LOS BARCOS DEL SERVIDOR Y LOS ABREGA AL CLIENTE -->
	// (E,4)(E,5),(F,5)(F,6)(F,7),(A,0)(A,1)(A,2),(I,8)(I,9)(I,10)(I,11)
	public static void obtenerBarcos(String mensaje, String[] barcos) {
		for (int i = 0; i < mensaje.split("\\),").length; i++) {
			String coordenada = mensaje.split("\\),")[i] + ")";
			if (coordenada.contains("#POS%")) {
				barcos[i] = coordenada.substring(5, coordenada.length());
			} else if (i == 3) {
				barcos[i] = coordenada.substring(0, coordenada.length() - 1);

			} else {
				barcos[i] = coordenada;
			}
		}
		System.out.println("Coordenadas agregadas!");
	}

	public static void contarJugadores(int celdas) {
		int jugadores = 0;
		if (celdas >= 10) {
			jugadores = celdas - 8;
			Main.textNumJugadores.setText(Integer.toString(jugadores));
		}
	}

	public static void enviarTiro(int tiempo) throws InterruptedException, IOException {
		AppCliente.cuentaAtras(tiempo - 1, "Tiempo acabado");
		String coordenada = Main.coodenadaPulsada;
		if (!coordenada.isEmpty()) {
			String num1 = coordenada.split(" ")[0];
			String letra = coordenadaToPosicion(Integer.parseInt(num1));
			String num2 = coordenada.split(" ")[1];
			String mensaje = "#TIRO(" + letra + "," + num2 + ")#";
			AppCliente.enviarMensajeServidor(mensaje);
		}
	}

	private static String coordenadaToPosicion(int posicionConvertir) {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder resultado = new StringBuilder();

		while (posicionConvertir >= 0) {
			resultado.insert(0, letras.charAt(posicionConvertir % 26));
			posicionConvertir = (posicionConvertir / 26) - 1;
		}

		return resultado.toString();
	}

	public static void pintarAgua(String posicon) {
		System.out.println("Dibujando agua!");
		ArrayList<JLabel> tablero = Main.listaCasillas;
		Color colorAgua = Color.decode("#10597d");
		posicon = posicon.split("AGUA#")[1];
		String posicionColocar = posicon.split(",")[0].substring(1, 2) + " " + posicon.split(",")[1].substring(0, 1);

		for (JLabel casilla : tablero) {
			if (casilla.getText().equals(posicionColocar)) {
				casilla.setBackground(colorAgua);
			}
		}
	}

	public static void pintarTocado(String posicon) {
		System.out.println("Dibujando Tocado!");
		ArrayList<JLabel> tablero = Main.listaCasillas;
		Color colorAgua = Color.ORANGE;
		posicon = posicon.split("TOCADO#")[1];
		String posicionColocar = posicon.split(",")[0].substring(1, 2) + " " + posicon.split(",")[1].substring(0, 1);

		for (JLabel casilla : tablero) {
			if (casilla.getText().equals(posicionColocar)) {
				casilla.setBackground(colorAgua);
			}
		}
	}

	public static void pintarHundido(String posicon) {
		System.out.println("Dibujando Hundido!");
		ArrayList<JLabel> tablero = Main.listaCasillas;
		Color colorAgua = Color.RED;
		posicon = posicon.split("#HUNDIDO#")[1];
		for (String coordenada : posicon.split("\\),\\(")) {
			coordenada=coordenada.replace("(", "");
			coordenada=coordenada.replace(")", "");
			String posicionColocar = coordenada.split(",")[0] + " " + coordenada.split(",")[1];
			for (JLabel casilla : tablero) {
				if (casilla.getText().equals(posicionColocar)) {
					casilla.setBackground(colorAgua);
				}
			}
		}

	}

}
