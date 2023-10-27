package Ejercicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GestorBiblioteca extends JFrame {
	public GestorBiblioteca() {
	}

	private static ArrayList<Libro> libros = new ArrayList<>();
	private static ArrayList<RegistroPrestamo> registrosPrestamo = new ArrayList();

	public static void guardarLibros(String archivo) {
		try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
			for (Libro libro : libros) {
				escritor.println(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getCodigo());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void cargarLibros(String archivo) {
		libros.clear();
		try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] partes = linea.split(",");
				if (partes.length == 3) {
					String titulo = partes[0];
					String autor = partes[1];
					int codigo = Integer.parseInt(partes[2]);
					Libro libro = new Libro(titulo, autor, codigo);
					libros.add(libro);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void guardarRegistrosPrestamo(String archivo) {
		try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
			for (RegistroPrestamo registro : registrosPrestamo) {
				escritor.println(registro.getLibro().getCodigo() + "," + registro.getFechaPrestamo().getTime());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void cargarRegistrosPrestamo(String archivo) {
		registrosPrestamo.clear();
		try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] partes = linea.split(",");
				if (partes.length == 2) {
					int codigoLibro = Integer.parseInt(partes[0]);
					long tiempoPrestamo = Long.parseLong(partes[1]);
					for (Libro libro : libros) {
						if (libro.getCodigo() == codigoLibro) {
							RegistroPrestamo registro = new RegistroPrestamo(libro);
							registro.getFechaPrestamo().setTime(tiempoPrestamo);
							registrosPrestamo.add(registro);
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void agregarLibro(Libro libro) {
		libros.add(libro);
		guardarLibros("biblioteca.txt");
	}

	public static void registrarPrestamo(Libro libro) {
		RegistroPrestamo registro = new RegistroPrestamo(libro);
		registrosPrestamo.add(registro);
		guardarRegistrosPrestamo("registros_prestamo.txt");
	}

	public static void registrarDevolucion(Libro libro) {
		for (RegistroPrestamo registro : registrosPrestamo) {
			if (registro.getLibro() == libro) {
				registrosPrestamo.remove(registro);
				break;
			}
		}
		guardarRegistrosPrestamo("registros_prestamo.txt");
	}

	public static ArrayList<Libro> buscarLibros(ArrayList<Libro> libros, String consulta) {
		ArrayList<Libro> resultados = new ArrayList<>();
		for (Libro libro : libros) {
			if (libro.getTitulo().toLowerCase().contains(consulta.toLowerCase())
					|| libro.getAutor().toLowerCase().contains(consulta.toLowerCase())
					|| String.valueOf(libro.getCodigo()).contains(consulta)) {
				resultados.add(libro);
			}
		}
		return resultados;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			crearInterfaz();
		});
	}

	public static void crearInterfaz() {
		JFrame frame = new JFrame("Gestor de Biblioteca");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1500, 500);
		frame.getContentPane().setLayout(new BorderLayout());

		JTextArea textArea = new JTextArea();
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu archivoMenu = new JMenu("Gestor de biblioteca");
		menuBar.add(archivoMenu);

		JMenuItem cargarLibrosItem = new JMenuItem("Cargar Libros");
		archivoMenu.add(cargarLibrosItem);
		JMenuItem guardarLibrosItem = new JMenuItem("Guardar Libros");
		archivoMenu.add(guardarLibrosItem);
		JMenuItem cargarRegistrosItem = new JMenuItem("Cargar Registros de Préstamo");
		archivoMenu.add(cargarRegistrosItem);
		JMenuItem guardarRegistrosItem = new JMenuItem("Guardar Registros de Préstamo");
		archivoMenu.add(guardarRegistrosItem);

		cargarLibrosItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarLibros("biblioteca.txt");
				textArea.append("Libros cargados desde archivo.\n");
			}
		});

		guardarLibrosItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardarLibros("biblioteca.txt");
				textArea.append("Libros guardados en archivo.\n");
			}
		});

		cargarRegistrosItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarRegistrosPrestamo("registros_prestamo.txt");
				textArea.append("Registros de préstamo cargados desde archivo.\n");
			}
		});

		guardarRegistrosItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardarRegistrosPrestamo("registros_prestamo.txt");
				textArea.append("Registros de préstamo guardados en archivo.\n");
			}
		});

		JPanel panelBotones = new JPanel();
		frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);

		JTextField tituloField = new JTextField(20);
		JTextField autorField = new JTextField(20);
		JTextField codigoField = new JTextField(10);
		JTextField codigoLibroPrestamoField = new JTextField(10);
		JTextField codigoLibroDevolucionField = new JTextField(10);

		JButton agregarLibroButton = new JButton("Agregar Libro");
		agregarLibroButton.setBackground(Color.cyan);
		JButton buscarLibroButton = new JButton("Buscar Libro");
		buscarLibroButton.setBackground(Color.cyan);
		JButton prestarLibroButton = new JButton("Prestar Libro");
		prestarLibroButton.setBackground(Color.cyan);
		JButton devolverLibroButton = new JButton("Devolver Libro");
		devolverLibroButton.setBackground(Color.cyan);
		JButton mostrarHistorialButton = new JButton("Mostrar Historial Prestamos existentes");
		mostrarHistorialButton.setBackground(Color.cyan);
		JTextField busquedaField = new JTextField(20);

		agregarLibroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String titulo = tituloField.getText();
				String autor = autorField.getText();
				int codigo = 0;

				try {
					codigo = Integer.parseInt(codigoField.getText());

					if (!titulo.isEmpty() && !autor.isEmpty()) {
						Libro libro = new Libro(titulo, autor, codigo);
						agregarLibro(libro);
						textArea.append("Libro agregado: " + libro + "\n");
						tituloField.setText("");
						autorField.setText("");
						codigoField.setText("");
					} else {
						JOptionPane.showMessageDialog(frame, "Por favor, complete todos los campos", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "El codigo debe ser un numero valido", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buscarLibroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String consulta = busquedaField.getText();
				ArrayList<Libro> resultados = buscarLibros(libros, consulta);

				if (resultados.isEmpty()) {
					textArea.append("No se encontraron resultados para la búsqueda.\n");
				} else {
					textArea.append("Resultados de la busqueda:\n");
					for (Libro libro : resultados) {
						textArea.append(libro + "\n");
					}
				}
			}
		});

		prestarLibroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String codigo = codigoLibroPrestamoField.getText();
				int codigoInt = Integer.parseInt(codigo);

				for (Libro libro : libros) {
					if (libro.getCodigo() == codigoInt) {
						registrarPrestamo(libro);
						textArea.append("Libro prestado: " + libro + "\n");
						return;
					}
				}
				textArea.append("No se encontró el libro con ese codigo.\n");
			}
		});

		devolverLibroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String codigo = codigoLibroDevolucionField.getText();
				int codigoInt = Integer.parseInt(codigo);

				for (Libro libro : libros) {
					if (libro.getCodigo() == codigoInt) {
						registrarDevolucion(libro);
						textArea.append("Libro devuelto: " + libro + "\n");
						return;
					}
				}
				textArea.append("No se encontró el libro con ese codigo.\\n");
			}
		});

		mostrarHistorialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("Historial de préstamos:\n");
				for (RegistroPrestamo registro : registrosPrestamo) {
					textArea.append("Libro: " + registro.getLibro() + ", Fecha de Préstamo: "
							+ registro.getFechaPrestamo() + "\n");
				}
			}
		});

		JPanel panelBotones1 = new JPanel(new GridLayout(3, 2, 10, 10));
		frame.add(panelBotones1, BorderLayout.SOUTH);

		JPanel panelAgregarLibro = new JPanel();
		panelAgregarLibro.setLayout(new BoxLayout(panelAgregarLibro, BoxLayout.X_AXIS));

		panelAgregarLibro.add(new JLabel("Título: "));
		panelAgregarLibro.add(tituloField);
		panelAgregarLibro.add(new JLabel("Autor: "));
		panelAgregarLibro.add(autorField);
		panelAgregarLibro.add(new JLabel("Código: "));
		panelAgregarLibro.add(codigoField);
		panelAgregarLibro.add(Box.createHorizontalStrut(10));
		panelAgregarLibro.add(Box.createHorizontalGlue());
		panelAgregarLibro.add(agregarLibroButton);

		JPanel panelBuscarLibro = new JPanel();
		panelBuscarLibro.add(new JLabel("Buscar: "));
		panelBuscarLibro.add(busquedaField);
		panelBuscarLibro.add(buscarLibroButton);

		JPanel panelPrestarDevolver = new JPanel();
		panelPrestarDevolver.setLayout(new BoxLayout(panelPrestarDevolver, BoxLayout.X_AXIS));
		panelPrestarDevolver.add(new JLabel("Código de libro a prestar: "));
		panelPrestarDevolver.add(codigoLibroPrestamoField);
		panelPrestarDevolver.add(prestarLibroButton);
		panelPrestarDevolver.add(Box.createHorizontalStrut(10));
		panelPrestarDevolver.add(Box.createHorizontalGlue());
		panelPrestarDevolver.add(new JLabel("Código de libro a devolver: "));
		panelPrestarDevolver.add(codigoLibroDevolucionField);
		panelPrestarDevolver.add(devolverLibroButton);

		JPanel panelMostrarHistorial = new JPanel();
		panelMostrarHistorial.add(mostrarHistorialButton);

		JPanel panelSalir = new JPanel();
		JButton salirButton = new JButton("Salir");
		salirButton.setBackground(Color.red);
		salirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		panelSalir.add(salirButton);
		panelBotones1.add(panelAgregarLibro);
		panelBotones1.add(panelBuscarLibro);
		panelBotones1.add(panelPrestarDevolver);
		panelBotones1.add(panelMostrarHistorial);
		panelBotones1.add(panelSalir);

		frame.setVisible(true);
	}
}