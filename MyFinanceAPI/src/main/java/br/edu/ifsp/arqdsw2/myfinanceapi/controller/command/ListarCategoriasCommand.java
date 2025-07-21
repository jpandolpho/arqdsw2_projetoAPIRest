package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.CategoriaDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Categoria;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListarCategoriasCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CategoriaDao dao = new CategoriaDao();
		List<Categoria> categorias = dao.fetchAll();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(categorias));
		out.flush();
	}

}
