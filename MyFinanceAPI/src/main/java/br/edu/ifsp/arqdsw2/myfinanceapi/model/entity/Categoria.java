package br.edu.ifsp.arqdsw2.myfinanceapi.model.entity;

public class Categoria {
	private int id;
	private String nome;
	
	public Categoria(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Categoria() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
