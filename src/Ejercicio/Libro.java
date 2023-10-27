package Ejercicio;

class Libro {
	private String titulo;
	private String autor;
	private int codigo;

	public Libro(String titulo, String autor, int codigo) {
		this.titulo = titulo;
		this.autor = autor;
		this.codigo = codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public int getCodigo() {
		return codigo;
	}

	@Override
	public String toString() {
		return "Código: " + codigo + ", Título: " + titulo + ", Autor: " + autor;
	}
}