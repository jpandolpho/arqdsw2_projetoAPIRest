package br.edu.ifsp.arqdsw2.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Categoria;

public class CategoriaDao {
	private DataSource dataSource;
	
	public CategoriaDao() throws NamingException{
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyFinanceAPI");
	}
	
	public void insert(Categoria categoria) throws SQLException{
		String sql = "INSERT INTO categoria (nome) VALUES (?)";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, categoria.getNome());
			stmt.executeUpdate();
		}
	}
	
	public Categoria fetchById(int id) throws SQLException {
		Categoria c = null;
		String sql = "SELECT * FROM categoria WHERE id = ?";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				c = new Categoria(rs.getInt("id"), rs.getString("nome"));
			}
		}
		return c;
	}
	
	public List<Categoria> fetchAll() throws SQLException{
		List<Categoria> categorias = new ArrayList<>();
		String sql = "SELECT * FROM categoria";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Categoria c = new Categoria(rs.getInt("id"), rs.getString("nome"));
				categorias.add(c);
			}
		}
		return categorias;
	}
}
