package Package_HundirFlota_Servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AppServer implements Runnable {
	public static ArrayList<Socket> listaSocket = new ArrayList<Socket>();
	private static BufferedReader mensajeCliente;
	private static int numeroJugadores = 0;
	private static boolean tiempoFinalizado = false;
	private static boolean juegoComenzado = false;
	public static String mensajeTrama = "";
	// ATRIBUTOS
	private Socket socket;
	//////////

	public AppServer(Socket socket) {
		this.socket = socket;
		listaSocket.add(socket);
	}

	public static ArrayList<Socket> getListaSocket() {
		return listaSocket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Conexion creada para " + socket.getInetAddress());
			mensajeCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String linea;
			while ((linea = mensajeCliente.readLine()) != null) {
				gestionServidor(linea, socket);
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// METODO QUE LEERA EL MENSAJE DEL CLIENTE Y REALIZARA ACCIONES
	private static void gestionServidor(String mensaje, Socket socket) throws IOException, InterruptedException {
		System.out.println("Mensaje entrante> " + mensaje);
		if (mensaje.contains("#REG#") && !tiempoFinalizado) {
			numeroJugadores++;
			System.out.println("Jugadores: " + numeroJugadores);
			System.out.println("Registrando al Usuario...");
			String nombre = mensaje.split("#")[2].trim();
			Jugador.registrarJugador(nombre, socket);
			mensajeTrama = "#REG_OK#" + nombre;
			enviarMensajeCliente(mensajeTrama, socket);

		}
		if (mensaje.contains("#REG#") && tiempoFinalizado) {
			mensajeTrama = "#REG_ERR#Cola_Cerrada";
			enviarMensajeCliente(mensajeTrama, socket);
		}
		if (numeroJugadores == 2 && !tiempoFinalizado) {
			System.out.println("Jugadores minimos conectados!");
			cuentaAtras(20, "Empezando la partida");
			tiempoFinalizado = true;
		}
		if (tiempoFinalizado && !juegoComenzado) {
			System.out.println("Empezando juego...");
			Jugador.crearTablero();
			int numCasillas = Tablero.numCasillas[0].length;

			// SE USA LA ITERACION DE LOS SOCKET PARA IDENTIFICAR A CADA CLIENTE Y ENVIAR SU
			// INFORMACION CORRESPONDIENTE
			for (Socket clienteSocket : listaSocket) {
				mensajeTrama = "#TAB," + numCasillas;
				enviarMensajeCliente(mensajeTrama, clienteSocket);
			}
			Tablero.generarBarcos();

			Thread.sleep(1000);
			// SE ITERA POR LOS SOCKET PARA ENVIAR EL MISMO MENSAJE A TODOS LOS CLIENTES
			if (!juegoComenzado) {
				for (Socket clienteSocket : listaSocket) {
					mensajeTrama = "#INICIO#";
					enviarMensajeCliente(mensajeTrama, clienteSocket);
				}
				juegoComenzado = true;
				Jugador.guardarBarcos();
			}
		}
		if (mensaje.contains("#TIRO")) {
			mensajeTrama = mensaje;
		}
		if (juegoComenzado ) {
			Jugador.turnoUsuario();
		}

	}

	// METODO QUE ENVIA MENSAJES AL CLIENTE
	public static void enviarMensajeCliente(String mensaje, Socket socket) throws IOException {
		BufferedWriter mensajeEnviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		System.out.println("Mensaje enviado> " + mensaje);
		mensajeEnviar.write(mensaje);
		mensajeEnviar.newLine();
		mensajeEnviar.flush();
		System.out.println("====================");

	}

	// METODO QUE HACE UNA CUENTA ATRAS
	public static void cuentaAtras(int tiempo, String mensaje) throws InterruptedException {
		System.out.println("Empezando en " + tiempo + " segundos...");
		AtomicInteger cuentaSegundos = new AtomicInteger(tiempo);
		Thread contadorSegundos = new Thread(() -> {
			while (cuentaSegundos.get() > 1) {
				try {
					Thread.sleep(1000);
					cuentaSegundos.decrementAndGet();
					if (cuentaSegundos.get() % 5 == 0) {
						System.out.println("Empezando en " + cuentaSegundos.get() + " segundos...");
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(mensaje);
		});
		contadorSegundos.start();
		contadorSegundos.join();
	}

}
