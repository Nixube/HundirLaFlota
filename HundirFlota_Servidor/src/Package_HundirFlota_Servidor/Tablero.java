package Package_HundirFlota_Servidor;

import java.io.IOException;
import java.net.Socket;

public class Tablero {
	public static int[][] numCasillas;
	private int casillas[][];

	public Tablero(int[][] casillas) {
		this.casillas = casillas;
	}

	public int[][] getCasillas() {
		return casillas;
	}

	public void setCasillas(int[][] casillas) {
		this.casillas = casillas;
	}

	// METODO QUE CREA LOS BARCOS DEL USUARIO Y SE LOS ASIGNA A CADA UNO, LUEGO
	// ENVIA AL CLIENTE CORRESPONDIENTE SUS BARCOS
	public static void generarBarcos() throws InterruptedException, IOException {
		System.out.println("Se han generado los barcos!");
		for (Jugador usuario : Jugador.getListaJugadores()) {
			Barco crearBarco = new Barco();
			usuario.setBarco(crearBarco);
			crearBarco.crearBarcos();
			String coordenadas = crearBarco.coordenadasObtenidas();
			AppServer.mensajeTrama = "#POS%" + coordenadas;
			for (Socket socket : AppServer.listaSocket) {
				if (socket == usuario.getSocket()) {
					AppServer.enviarMensajeCliente(AppServer.mensajeTrama, socket);
					System.out.println(
							"Usuario : " + usuario.getNombre() + " ha generado las coordenadas " + coordenadas);
				}
			}

		}
	}
}
