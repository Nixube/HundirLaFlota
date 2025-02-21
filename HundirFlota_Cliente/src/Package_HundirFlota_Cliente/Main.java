package Package_HundirFlota_Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// VARIABLES
	public static ArrayList<JLabel> listaCasillas = new ArrayList<JLabel>();
	private static String titulo = "Hundir la Flota";
	public static int numCeldas = 1;
	private static boolean nombrePuesto = false;
	public static String coodenadaPulsada="";
	///////////
	// UI
	public static JPanel JPanelTablero;
	private JPanel panelInformacion = new JPanel();
	private JLabel textTitulo = new JLabel(titulo.toUpperCase());
	private JLabel textoInformacion = new JLabel("INFORMACIÓN");
	private JLabel textJugadores = new JLabel("JUGADORES:");
	private JLabel textDisparo = new JLabel("DISPARO:");
	private JLabel textId = new JLabel("ID:");
	public static JLabel textNumJugadores = new JLabel("0");
	public static JLabel textDisparoComprobacion = new JLabel("NO REALIZADO");
	public static JLabel textNumID = new JLabel("USUARIO");
	private JLabel textBarcos = new JLabel("BARCOS");
	private JLabel textAyuda = new JLabel("¿NECESITAS AYUDA?");
	public static JLabel loadingText = new JLabel("CARGANDO . . .");
	public static JLabel textoResultado = new JLabel("HAS GANADO!");
	///////////
	// IMAGES
	private static ImageIcon iconoBarcoGrande = new ImageIcon("imagenes/barco-grande.png");
	private static ImageIcon iconoBarcoMediano = new ImageIcon("imagenes/barco-mediano.png");
	private static ImageIcon iconoBarcoPequeno = new ImageIcon("imagenes/barco-pequeño.png");
	private ImageIcon iconoTimon = new ImageIcon("imagenes/timon.png");
	private ImageIcon iconoVentana = new ImageIcon("imagenes/iconoVentana.png");
	public static ImageIcon iconoCorona = new ImageIcon("imagenes/corona.png");
	public static ImageIcon iconoGato = new ImageIcon("imagenes/gato.png");
	private static Image imgBarcoGrande = iconoBarcoGrande.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
	private static Image imgBarcoMediano = iconoBarcoMediano.getImage().getScaledInstance(80, 40, Image.SCALE_SMOOTH);
	private static Image imgBarcoPequeno = iconoBarcoPequeno.getImage().getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	private Image imgTimon = iconoTimon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public static Image imgGato = iconoGato.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public static Image imgCorona = iconoCorona.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private JLabel barcoGrandeDisp = new JLabel(new ImageIcon(imgBarcoGrande));
	private JLabel barcoMedianoDisp = new JLabel(new ImageIcon(imgBarcoMediano));
	private JLabel barcoMedianoDisp2 = new JLabel(new ImageIcon(imgBarcoMediano));
	private JLabel barcoPequenoDisp = new JLabel(new ImageIcon(imgBarcoPequeno));
	private JLabel timon = new JLabel(new ImageIcon(imgTimon));
	private JLabel timon2 = new JLabel(new ImageIcon(imgTimon));
	public static JLabel gato = new JLabel(new ImageIcon(imgGato));
	public static JLabel corona = new JLabel(new ImageIcon(imgCorona));
	///////////

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		// DEFAULT CONFIg
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//TEXTO VENTANA
		setTitle("Hundir la Flota");
		//IMAGEN VENTANA
        setIconImage(iconoVentana.getImage());
		//////////
		
		// UI
		// TABLERO
		JPanelTablero = new JPanel();
		JPanelTablero.setBounds(10, 20, 720, 720);
		contentPane.add(JPanelTablero);
		JPanelTablero.setLayout(null);
		// TEXTO CARGANDO
		loadingText.setForeground(new Color(25, 42, 86));
		loadingText.setFont(new Font("Segoe Script", Font.BOLD, 48));
		loadingText.setHorizontalAlignment(SwingConstants.CENTER);
		loadingText.setBounds(0, 0, 720, 720);
		JPanelTablero.add(loadingText);
		// PANEL DERECHO INFORMACION
		panelInformacion.setBackground(new Color(25, 42, 86));
		panelInformacion.setBounds(756, 20, 292, 720);
		contentPane.add(panelInformacion);
		panelInformacion.setLayout(null);
		panelInformacion.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		// TITULO
		textTitulo.setForeground(Color.WHITE);
		textTitulo.setFont(new Font("Segoe Script", Font.BOLD, 18));
		textTitulo.setBounds(46, 34, 200, 30);
		panelInformacion.add(textTitulo);
		// INFORMACION
		textoInformacion.setForeground(Color.WHITE);
		textoInformacion.setFont(new Font("Segoe Script", Font.BOLD, 16));
		textoInformacion.setBounds(78, 120, 136, 20);
		panelInformacion.add(textoInformacion);
		// JUGADORES
		textJugadores.setForeground(Color.WHITE);
		textJugadores.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textJugadores.setBounds(41, 180, 106, 20);
		panelInformacion.add(textJugadores);
		// PUNTOS
		textDisparo.setForeground(Color.WHITE);
		textDisparo.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textDisparo.setBounds(41, 220, 100, 20);
		panelInformacion.add(textDisparo);
		// UUID
		textId.setForeground(Color.WHITE);
		textId.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textId.setBounds(41, 260, 100, 20);
		panelInformacion.add(textId);
		// TOTAL JUGADORES
		textNumJugadores.setForeground(Color.YELLOW);
		textNumJugadores.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textNumJugadores.setBounds(150, 180, 150, 20);
		panelInformacion.add(textNumJugadores);
		// TOTAL PUNTOS
		textDisparoComprobacion.setForeground(Color.YELLOW);
		textDisparoComprobacion.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textDisparoComprobacion.setBounds(120, 220, 150, 20);
		panelInformacion.add(textDisparoComprobacion);
		// ID
		textNumID.setForeground(Color.YELLOW);
		textNumID.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textNumID.setBounds(80, 260, 166, 20);
		panelInformacion.add(textNumID);
		// BARCOS
		textBarcos.setForeground(Color.WHITE);
		textBarcos.setFont(new Font("Segoe Script", Font.BOLD, 16));
		textBarcos.setBounds(106, 378, 80, 20);
		panelInformacion.add(textBarcos);
		// TEXTO AYUDA
		textAyuda.setForeground(Color.WHITE);
		textAyuda.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textAyuda.setBounds(63, 667, 166, 30);
		textAyuda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panelInformacion.add(textAyuda);
		//////////
		// IMAGENES
		// IMG1
		barcoGrandeDisp.setBounds(96, 429, 100, 50);
		panelInformacion.add(barcoGrandeDisp);
		// IMG2
		barcoMedianoDisp.setBounds(106, 490, 80, 40);
		panelInformacion.add(barcoMedianoDisp);
		// IMG3
		barcoMedianoDisp2.setBounds(106, 540, 80, 40);
		panelInformacion.add(barcoMedianoDisp2);
		// IMG4
		barcoPequenoDisp.setBounds(121, 590, 50, 30);
		panelInformacion.add(barcoPequenoDisp);
		// IMG TIMON
		timon.setBounds(10, 657, 50, 50);
		panelInformacion.add(timon);
		// IMG TIMON2
		timon2.setBounds(232, 657, 50, 50);
		panelInformacion.add(timon2);
		// IMG CORONA
		corona.setBounds(180, 305, 50, 50);
		panelInformacion.add(corona);
		corona.setVisible(false);
		// IMG GATO
		gato.setBounds(180, 305, 50, 50);
		panelInformacion.add(gato);
		gato.setVisible(false);
		
		
		textoResultado.setForeground(new Color(255, 255, 0));
		textoResultado.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textoResultado.setBounds(46, 318, 155, 30);
		panelInformacion.add(textoResultado);
		textoResultado.setVisible(false);
		//////////

		// METODOS
		registrarUsuario();
		//////////

		// EVENTOS
		eventos();
		//////////

		// INICIAR CLIENTE
		Thread iniciarCliente = new Thread(new AppCliente());
		iniciarCliente.start();
		/////////

	}

	// METODO QUE REGISTRA AL USUARIO
	private static void registrarUsuario() {
		do {
			String nombreUsuario = JOptionPane.showInputDialog("Pon tu nombre de usuario");
			if (nombreUsuario == null) {
				nombreUsuario = "Sin Nombre";
				textNumID.setText(nombreUsuario.toUpperCase());
				break;

			}
			if (!nombreUsuario.isEmpty() && nombreUsuario.length() <= 15) {
				textNumID.setText(nombreUsuario.toUpperCase());
				nombrePuesto = true;
			}
		} while (!nombrePuesto);
	}

	// METODO DE EVETOS
	private void eventos() {
		textAyuda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				abrirVentanaEmergente();
			}
		});
	}

	// METODO QUE ABRE LA VENTANA DE INFORMACION
	private void abrirVentanaEmergente() {

		JDialog ventanaEmergente = new JDialog(this, "Ayuda", true);
		ventanaEmergente.setSize(550, 580);
		ventanaEmergente.getContentPane().setLayout(new BorderLayout());
		ventanaEmergente.setLocationRelativeTo(this);
		ventanaEmergente.setResizable(false);
		JPanel panelContenido = new JPanel();
		panelContenido.setBackground(Color.WHITE);
		panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

		JLabel titulo = new JLabel("INFORMACIÓN DE AYUDA");
		titulo.setFont(new Font("Segoe Script", Font.BOLD, 18));
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextArea textoAyuda = new JTextArea(" LAS NORMAS SON MUY SENCILLAS:\n\n"
				+ " USTED TIENE UNA FLOTA COMPUESTA POR:\n" + "  - 1 BARCO DE 4 CASILLAS.\n"
				+ "  - 2 BARCOS DE 3 CASILLAS.\n" + "  - 1 BARCO DE 2 CASILLAS.\n\n"
				+ " LAS POSICIONES DE LOS BARCOS SE ASIGNAN DE FORMA ALEATORIA.\n\n"
				+ " EN SU TURNO, PUEDE INTENTAR HUNDIR LA FLOTA ENEMIGA.\n\n"
				+ " EL RESULTADO DE SU INTENTO SERÁ EL SIGUIENTE:\n" + "  - [AZUL] -> HA FALLADO EL DISPARO.\n"
				+ "  - [NARANJA] -> HA DADO EN UN BARCO.\n" + "  - [VERDE] -> HA HUNDIDO UN BARCO.\n"
				+ "  - [ROJO] -> HAN HUNDIDO TU BARCO.");

		textoAyuda.setFont(new Font("Segoe Script", Font.BOLD, 14));
		textoAyuda.setEditable(false);
		textoAyuda.setLineWrap(true);
		textoAyuda.setWrapStyleWord(true);

		JButton botonCerrar = new JButton("Cerrar");
		botonCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
		botonCerrar.addActionListener(e -> ventanaEmergente.dispose());

		panelContenido.add(Box.createVerticalStrut(20));
		panelContenido.add(titulo);
		panelContenido.add(Box.createVerticalStrut(20));
		panelContenido.add(textoAyuda);
		panelContenido.add(Box.createVerticalStrut(20));
		panelContenido.add(botonCerrar);

		ventanaEmergente.getContentPane().add(panelContenido, BorderLayout.CENTER);
		ventanaEmergente.setVisible(true);

	}

	// METODO QUE LLENA EL TABLERO
	public static void llenarTablero(int numCeldas) {
		JPanelTablero.removeAll();

		JPanelTablero.setLayout(new GridLayout(numCeldas, numCeldas));
		int celdas=0;
		for (int i = 0; i < numCeldas; i++) {
			for (int j = 0; j < numCeldas; j++) {
				JLabel label = new JLabel(i + " " + j);
				label.setBackground(new Color(160, 160, 160));
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				// label.setForeground(new Color(0, 0, 0, 0));
				
				 label.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                coodenadaPulsada=label.getText().toString();
		                Main.textDisparoComprobacion.setText(coodenadaPulsada.split(" ")[0]+","+coodenadaPulsada.split(" ")[1]);
		                }
		            });
				listaCasillas.add(label);
				JPanelTablero.add(label);
				
			}
			celdas=i;
		}
		JPanelTablero.revalidate();
		JPanelTablero.repaint();
		Cliente.contarJugadores(celdas+1);
	}
}
