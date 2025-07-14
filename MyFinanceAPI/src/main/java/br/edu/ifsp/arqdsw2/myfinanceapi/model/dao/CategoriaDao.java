package br.edu.ifsp.arqdsw2.myfinanceapi.model.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CategoriaDao {
	private DataSource dataSource;
	
	public CategoriaDao() throws NamingException{
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyFinanceAPI");
	}
}
