package Ejercicio;

import java.util.Date;

class RegistroPrestamo {
	private Libro libro;
	private Date fechaPrestamo;

	public RegistroPrestamo(Libro libro) {
		this.libro = libro;
		this.fechaPrestamo = new Date();
	}

	public Libro getLibro() {
		return libro;
	}

	public Date getFechaPrestamo() {
		return fechaPrestamo;
	}
}