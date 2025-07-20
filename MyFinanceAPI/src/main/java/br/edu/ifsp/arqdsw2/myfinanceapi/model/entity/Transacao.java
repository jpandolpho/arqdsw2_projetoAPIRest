package br.edu.ifsp.arqdsw2.myfinanceapi.model.entity;

import java.sql.Timestamp;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.util.TipoTransacao;

public class Transacao {
	private int id;
	private double valor;
	private String description;
	private TipoTransacao tipo;
	private Timestamp data;
	private int idCategoria;

	public Transacao(int id, double valor, String description, TipoTransacao tipo, Timestamp data, int idCategoria) {
		this.id = id;
		this.valor = valor;
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

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
}
