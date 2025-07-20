package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.BufferedReader;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.CategoriaDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Categoria;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CriarCategoriaCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedReader reader = request.getReader();
		Categoria categoria = new Gson().fromJson(reader, Categoria.class);
		CategoriaDao dao = new CategoriaDao();
		dao.insert(categoria);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}

}
