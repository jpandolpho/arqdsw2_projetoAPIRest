package br.edu.ifsp.arqdsw2.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Transacao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.util.TipoTransacao;

public class TransacaoDao {
	private DataSource dataSource;

	public TransacaoDao() throws NamingException {
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyFinanceAPI");
	}

	public void insert(Transacao transacao) throws SQLException {
		String sql = "INSERT INTO transacao (valor,descricao,tipo,data_transacao,categoria_id) VALUES (?,?,?,?,?)";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setDouble(1, transacao.getValor());
			stmt.setString(2, transacao.getDescricao());
			stmt.setString(3, transacao.getTipo().toString());
			stmt.setDate(4, Date.valueOf(transacao.getData().toLocalDateTime().toLocalDate()));
			stmt.setInt(5, transacao.getIdCategoria());
			stmt.executeUpdate();
		}
	}

	public boolean update(Transacao transacao) throws SQLException {
		String sql = "UPDATE transacao SET valor=?, descricao=?, tipo=?, data_transacao=?, categoria_id=? WHERE id=?";
		int rows = 0;
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setDouble(1, transacao.getValor());
			stmt.setString(2, transacao.getDescricao());
			stmt.setString(3, transacao.getTipo().name());
			stmt.setDate(4, Date.valueOf(transacao.getData().toLocalDateTime().toLocalDate()));
			stmt.setInt(5, transacao.getIdCategoria());
			stmt.setInt(6, transacao.getId());
			rows = stmt.executeUpdate();
		}
		return rows > 0;
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM transacao WHERE id=?";
		int rows = 0;
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			rows = stmt.executeUpdate();
		}
		return rows > 0;
	}

	public List<Transacao> fetch(int page, int limit, int month, int year, int categoria) throws SQLException {
		List<Transacao> transacoes = new ArrayList<>();
		String sql = "SELECT * FROM transacao";
		if (month > -1 || year > -1 || categoria > -1) {
			sql += " WHERE";
			boolean setDate = false;
			if (month > -1 && year > -1) {
				setDate = true;
				sql += " MONTH(data_transacao) = ? AND YEAR(data_transacao) = ?";
			}
			if (categoria > -1) {
				if (setDate) {
					sql += " AND";
				}
				sql += " categoria_id = ?";
			}
		}
		sql += " ORDER BY data_transacao DESC";
		if (limit > 0 && page > 0) {
			sql += " LIMIT ? OFFSET ?";
		}
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			int param = 1;
			if (month > -1 && year > -1) {
				stmt.setInt(param, month);
				param++;
				stmt.setInt(param, year);
				param++;
			}
			if (categoria > -1) {
				stmt.setInt(param, categoria);
				param++;
			}
			if (limit > 0 && page > 0) {
				stmt.setInt(param, limit);
				param++;
				stmt.setInt(param, (page-1)*limit);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transacao t = new Transacao();
				t.setId(rs.getInt("id"));
				t.setData(Timestamp.valueOf(rs.getDate("data_transacao").toLocalDate().atStartOfDay()));
				t.setDescricao(rs.getString("descricao"));
				t.setIdCategoria(rs.getInt("categoria_id"));
				t.setValor(rs.getDouble("valor"));
				if (rs.getString("tipo").equals("RECEITA")) {
					t.setTipo(TipoTransacao.RECEITA);
				} else {
					t.setTipo(TipoTransacao.DESPESA);
				}
				transacoes.add(t);
			}
		}
		return transacoes;
	}

	public List<Transacao> fetchReceita(int page, int limit, int month, int year, int categoria) throws SQLException {
		List<Transacao> transacoes = new ArrayList<>();
		String sql = "SELECT * FROM transacao WHERE tipo='RECEITA'";
		if (month > -1 || year > -1 || categoria > -1) {
			if (month > -1 && year > -1) {
				sql += " AND MONTH(data_transacao) = ? AND YEAR(data_transacao) = ?";
			}
			if (categoria > -1) {
				sql += " AND categoria_id = ?";
			}
		}
		if (limit > 0 && page > 0) {
			sql += " LIMIT ? OFFSET ?";
		}
		sql += " ORDER BY data_transacao DESC";

		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			int param = 1;
			if (month > -1 && year > -1) {
				stmt.setInt(param, month);
				param++;
				stmt.setInt(param, year);
				param++;
			}
			if (categoria > -1) {
				stmt.setInt(param, categoria);
				param++;
			}
			if (limit > 0 && page > 0) {
				stmt.setInt(param, limit);
				param++;
				stmt.setInt(param, page);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transacao t = new Transacao();
				t.setId(rs.getInt("id"));
				t.setData(Timestamp.valueOf(rs.getDate("data_transacao").toLocalDate().atStartOfDay()));
				t.setDescricao(rs.getString("descricao"));
				t.setIdCategoria(rs.getInt("categoria_id"));
				t.setValor(rs.getDouble("valor"));
				t.setTipo(TipoTransacao.RECEITA);
				transacoes.add(t);
			}
		}
		return transacoes;
	}

	public List<Transacao> fetchDespesa(int page, int limit, int month, int year, int categoria) throws SQLException {
		List<Transacao> transacoes = new ArrayList<>();
		String sql = "SELECT * FROM transacao WHERE tipo='DESPESA'";
		if (month > -1 || year > -1 || categoria > -1) {
			if (month > -1 && year > -1) {
				sql += " AND MONTH(data_transacao) = ? AND YEAR(data_transacao) = ?";
			}
			if (categoria > -1) {
				sql += " AND categoria_id = ?";
			}
		}
		if (limit > 0 && page > 0) {
			sql += " LIMIT ? OFFSET ?";
		}
		sql += " ORDER BY data_transacao DESC";

		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			int param = 1;
			if (month > -1 && year > -1) {
				stmt.setInt(param, month);
				param++;
				stmt.setInt(param, year);
				param++;
			}
			if (categoria > -1) {
				stmt.setInt(param, categoria);
				param++;
			}
			if (limit > 0 && page > 0) {
				stmt.setInt(param, limit);
				param++;
				stmt.setInt(param, page);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transacao t = new Transacao();
				t.setId(rs.getInt("id"));
				t.setData(Timestamp.valueOf(rs.getDate("data_transacao").toLocalDate().atStartOfDay()));
				t.setDescricao(rs.getString("descricao"));
				t.setIdCategoria(rs.getInt("categoria_id"));
				t.setValor(rs.getDouble("valor"));
				t.setTipo(TipoTransacao.DESPESA);
				transacoes.add(t);
			}
		}
		return transacoes;
	}

	public Transacao fetchById(int id) throws SQLException {
		Transacao t = null;
		String sql = "SELECT * FROM transacao WHERE id = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				t = new Transacao();
				t.setId(rs.getInt("id"));
				t.setData(Timestamp.valueOf(rs.getDate("data_transacao").toLocalDate().atStartOfDay()));
				t.setDescricao(rs.getString("descricao"));
				t.setIdCategoria(rs.getInt("categoria_id"));
				t.setValor(rs.getDouble("valor"));
				if (rs.getString("valor") == "RECEITA") {
					t.setTipo(TipoTransacao.RECEITA);
				} else {
					t.setTipo(TipoTransacao.DESPESA);
				}
			}
			return t;
		}
	}
	
	public Double fetchSum(TipoTransacao tipo) throws SQLException{
		Double sum = null;
		String sql = "SELECT SUM(valor) AS total FROM transacao WHERE tipo = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, tipo.name());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				sum = rs.getDouble("total");
			}
		}
		return sum;
	}
	
	public Map<Integer,Double> fetchSumByCategoria() throws SQLException{
		Map<Integer,Double> result = new HashMap<>();
		String sql = "SELECT categoria_id, SUM(valor) AS total FROM transacao WHERE tipo='DESPESA' GROUP BY categoria_id";
		try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				result.put(rs.getInt("categoria_id"), rs.getDouble("total"));
			}
		}
		return result;
	}
}