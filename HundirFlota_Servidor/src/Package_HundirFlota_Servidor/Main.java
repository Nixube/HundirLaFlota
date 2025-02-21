package Package_HundirFlota_Servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	private static int puerto = 8500;

	public static void main(String[] args) {
		System.out.println("Iniciando Servidor...");
		try (ServerSocket serverSocket = new ServerSocket(puerto)) {
			System.out.println("Esperando peticion...");
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Aceptada la peticion de IP: " + socket.getInetAddress());

				Thread crearHilo = new Thread(new AppServer(socket));
				crearHilo.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
