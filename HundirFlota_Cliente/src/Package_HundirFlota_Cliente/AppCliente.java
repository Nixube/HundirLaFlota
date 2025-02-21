package Package_HundirFlota_Cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class AppCliente implements Runnable {
	private static String ipServidor = "127.0.0.1";
	private static int puerto = 8500;
	private static BufferedReader mensajeServidor;
	private static BufferedWriter mensajeEnviar;
	private static String usuario = Main.textNumID.getText();
	private static int sizeTablero = 0;

	public void run() {
		try (Socket socket = new Socket(ipServidor, puerto)) {
			mensajeServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mensajeEnviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			enviarMensajeServidor("#REG#" + usuario);

			String linea;
			while ((linea = mensajeServidor.readLine()) != null) {
				gestionCliente(linea, socket);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// METODO QUE LEERA EL MENSAJE DEL SERVIDOR Y REALIZARA ACCIONES
	private static void gestionCliente(String mensaje, Socket socket) throws InterruptedException, IOException {
		System.out.println("Mensaje entrante> " + mensaje);
		if (mensaje.contains("#TAB,")) {
			int numCeldas = Integer.parseInt(mensaje.split(",")[1]);
			sizeTablero = numCeldas;
			Main.llenarTablero(sizeTablero);
		}
		if (mensaje.contains("#POS%")) {
			String[] barcos = new String[4];
			Cliente.obtenerBarcos(mensaje, barcos);
			Cliente.registrarCliente(usuario, 0, barcos);
		}
		if (mensaje.equals("#REG_ERR#Cola_Cerrada")) {
			Main.loadingText.setText("COLA CERRADA!");

		}
		if (mensaje.contains("#TURNO")) {
			int tiempo = Integer.parseInt(mensaje.split("#")[2]);
			Cliente.enviarTiro(tiempo);
		}
		if (mensaje.contains("#AGUA")) {
			Cliente.pintarAgua(mensaje);
		}
		if (mensaje.contains("#TOCADO")) {
			Cliente.pintarTocado(mensaje);
		}
		if (mensaje.contains("#HUNDIDO")) {
			Cliente.pintarHundido(mensaje);
		}
		if (mensaje.contains("#GANADOR")) {
			if (mensaje.split("#")[2].equals(usuario)) {
				Main.textoResultado.setText("Has Ganado");
				Main.corona.setVisible(true);
				Main.textoResultado.setVisible(true);
				System.exit(1);
			}
		}
		if (mensaje.contains("#FIN")) {
			Main.textoResultado.setText("Has Perdido");
			Main.gato.setVisible(true);
			Main.textoResultado.setVisible(true);
			Main.JPanelTablero.setVisible(false);
		}
		System.out.println("====================");
	}

	// METODO QUE ENVIA MENSAJES AL SERVIDOR
	public static void enviarMensajeServidor(String mensaje) throws IOException {
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
