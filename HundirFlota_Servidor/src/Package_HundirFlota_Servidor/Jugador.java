package Package_HundirFlota_Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Jugador {
	private static boolean haGanado = false;
	public static ArrayList<Jugador> listaJugadores = new ArrayList<Jugador>();
	private static ArrayList<String> listaBarcosJugadores = new ArrayList<String>();
	private static int id = 0;
	private String nombre;
	private int tokenTurno;
	private Tablero tablero;
	private Barco barco;
	private Socket socket;

	public Jugador(String nombre, int tokenTurno, Tablero tablero, Barco barco, Socket socket) {
		this.nombre = nombre;
		this.tokenTurno = tokenTurno;
		this.tablero = tablero;
		this.barco = barco;
		this.socket = socket;
	}

	public String getNombre() {
		return nombre;
	}

	public int getTokenTurno() {
		return tokenTurno;
	}

	public Tablero getTablero() {
		return tablero;
	}

	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}

	public void setBarco(Barco barco) {
		this.barco = barco;
	}

	public static ArrayList<Jugador> getListaJugadores() {
		return listaJugadores;
	}

	public Socket getSocket() {
		return socket;
	}

	public Barco getBarco() {
		return barco;
	}

	// METODO QUE REGISTRA AL USUARIO
	public static void registrarJugador(String usuario, Socket socket) {
		id++;
		Jugador crearJugador = new Jugador(usuario, id, null, null, socket);
		listaJugadores.add(crearJugador);
		System.out.println("Registrado al Jugador " + crearJugador.getNombre() + "!");
	}

	// METODO QUE MODIFICAR A TODOS LOS USUARIOS PARA PONERLES EL NUMERO CORRECTO DE
	// CASILLAS --> [10][10]
	public static void crearTablero() {
		Tablero crearTablero;
		for (Jugador usuario : listaJugadores) {
			if (listaJugadores.size() == 2) {
				Tablero.numCasillas = new int[10][10];
				crearTablero = new Tablero(Tablero.numCasillas);
				usuario.setTablero(crearTablero);
			}
			if (listaJugadores.size() > 2) {
				int numeroJugadores = listaJugadores.size() - 2;
				Tablero.numCasillas = new int[10 + numeroJugadores][10 + numeroJugadores];
				crearTablero = new Tablero(Tablero.numCasillas);
				usuario.setTablero(crearTablero);
			}
		}
		System.out.println("Tableros modificados con " + Tablero.numCasillas[0].length + " casillas");
	}

	// METODO QUE REALIZARA LAS ACCIONES DE LOS TURNOS DE LOS USUARIOS
	public static void turnoUsuario() throws InterruptedException, IOException {
		System.out.println("Comenzando turnos!");
		do {
			for (int i = 0; i < AppServer.getListaSocket().size(); i++) {
				if (Jugador.getListaJugadores().get(i).getSocket().equals(AppServer.getListaSocket().get(i))) {
					int id = Jugador.getListaJugadores().get(i).getTokenTurno();
					System.out.println("Turno del Jugador " + id);
					String mensaje = AppServer.mensajeTrama;
					int tiempo = 10;
					mensaje = "#TURNO#" + Integer.toString(tiempo);
					AppServer.enviarMensajeCliente(mensaje, Jugador.getListaJugadores().get(i).getSocket());
					AppServer.cuentaAtras(tiempo, "El turno del jugador " + id + " ha terminado");

					String coordenadaTiro = recibirCoordenadaTiro(Jugador.getListaJugadores().get(i).getSocket());
					registrarTiro(coordenadaTiro, Jugador.getListaJugadores().get(i).getSocket());
				}
			}
		} while (!haGanado);
	}

	private static String recibirCoordenadaTiro(Socket socket) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		return reader.readLine();
	}

	private static void registrarTiro(String coordenada, Socket socket) throws IOException {
		boolean barcoExiste = false;
		String num1 = coordenada.split("\\(")[1].split(",")[0];
		String num2 = coordenada.split("\\(")[1].split(",")[1].substring(0, 1);
		String letra = convertirCoordenadaAFila(num1);
		String posicion = "(" + num1 + "," + num2 + ")";
		int posX = Integer.parseInt(letra);
		int posY = Integer.parseInt(num2);
		System.out.println("Tiro registrado a casilla " + posicion + "!");
		for (String barcos : listaBarcosJugadores) {
			if (barcos.equals(posicion)) {
				System.out.println("Existe el barco" + barcos);
				barcoExiste = true;
			}
		}
		if (barcoExiste) {
			System.out.println("Barco en " + posX + "," + posY);
			barcoExistente(posX, posY, socket);
		}
		if (!barcoExiste) {
			String barcoEliminar = "(" + posX + "," + posY + ")";
			System.out.println("El tiro ha dado al agua!");
			AppServer.enviarMensajeCliente("#AGUA#" + barcoEliminar, socket);
		}
	}

	private static void barcoExistente(int posX, int posY, Socket socket) throws IOException {
		boolean barcoHundido = false;
		int idBarco = Tablero.numCasillas[posY][posX];
		String barcoEliminar = "(" + posX + "," + posY + ")";
		int numCasillas = 0;
		int contarFalse = 0;

		if (Barco.casillasOcupadas[posY][posX]) {
			Barco.casillasOcupadas[posY][posX] = false;
		}
		for (int i = 0; i < Barco.getCasillasOcupadas().length; i++) {
			for (int j = 0; j < Barco.getCasillasOcupadas()[i].length; j++) {
				if (idBarco == Tablero.numCasillas[j][i] && !Barco.casillasOcupadas[j][i]) {
					contarFalse++;
				}
				if (idBarco == Tablero.numCasillas[j][i]) {
					numCasillas++;
				}
			}
		}
		if (numCasillas == contarFalse) {
			barcoHundido = true;
		}
		if (!barcoHundido) {
			System.out.println("El barco ha sido tocado");
			AppServer.enviarMensajeCliente("#TOCADO#" + barcoEliminar, socket);
		}
		if (barcoHundido) {
			System.out.println("El barco ha sido hundido");
			String casillas = "";
			for (int i = 0; i < Barco.getCasillasOcupadas().length; i++) {
				for (int j = 0; j < Barco.getCasillasOcupadas()[i].length; j++) {
					if (idBarco == Tablero.numCasillas[j][i]) {

						casillas = casillas + "(" + i + "," + j + "),";
					}
				}
			}
			casillas = casillas.substring(0, casillas.length() - 1);
			System.out.println("Casillas enviadas> " + casillas);
			for (Socket socketUsuarios : AppServer.listaSocket) {
				AppServer.enviarMensajeCliente("#HUNDIDO#" + casillas, socketUsuarios);

			}
			obtenerGanadorYPerdedor();
		}
	}

	private static String convertirCoordenadaAFila(String coordenada) {
		String letra;
		int fila = 0;
		for (int i = 0; i < coordenada.length(); i++) {
			fila = fila * 26 + (coordenada.charAt(i) - 'A' + 1);
		}
		letra = Integer.toString(fila - 1);
		return letra;
	}

	private static void obtenerGanadorYPerdedor() throws IOException {
		String ganadorActual = "";
		String perdedorActual = "";
		int jugadoresSinBarcos = 0;
		System.out.println("Buscando jugadores Gandor/Perdedor");
		for (Jugador jugador : listaJugadores) {
			boolean tieneBarcosActivos = false;

			for (String barcos : jugador.getBarco().getPosicionesCoordenadas()) {
				if (!barcos.equals(",")) {
					int posX = Integer.parseInt(convertirCoordenadaAFila(barcos.split(",")[0].replace("(", "")));
					int posY = Integer.parseInt(barcos.split(",")[1].replace(")", ""));
					if (Barco.casillasOcupadas[posY][posX]) {
						tieneBarcosActivos = true;
						break;
					}
				}
			}

			if (!tieneBarcosActivos) {
				perdedorActual = jugador.getNombre();
				AppServer.enviarMensajeCliente("#FIN#" + jugador.getNombre(), jugador.getSocket());
				System.out.println("Desconectado al Jugador " + jugador.getNombre());
				jugadoresSinBarcos++;
			}

		}
		if (jugadoresSinBarcos == listaJugadores.size() - 1) {
			for (Jugador jugador : listaJugadores) {
				if (!jugador.getNombre().equals(perdedorActual)) {
					ganadorActual = jugador.getNombre();
					break;
				}
			}

			System.out.println("Ganador: " + ganadorActual);
			for (Jugador socket : listaJugadores) {
				AppServer.enviarMensajeCliente("#GANADOR#" + ganadorActual, socket.getSocket());
			}
			System.exit(1);
		}
	}

	public static void guardarBarcos() {
		for (Jugador jugador : listaJugadores) {
			for (String barcos : jugador.getBarco().getPosicionesCoordenadas()) {
				listaBarcosJugadores.add(barcos);
			}
		}
	}

}
