package br.edu.ifsp.arqdsw2.myfinanceapi.model.entity;

import java.sql.Timestamp;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.util.TipoTransacao;

public class Transacao {
	private int id;
	private String description;
	private TipoTransacao tipo;
	private Timestamp data;
	private int idCategoria;

	public Transacao(int id, String description, TipoTransacao tipo, Timestamp data, int idCategoria) {
		this.id = id;
		this.description = description;
		this.tipo = tipo;
		this.data = data;
		this.idCategoria = idCategoria;
	}

	public Transacao() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TipoTransacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransacao tipo) {
		this.tipo = tipo;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
}
