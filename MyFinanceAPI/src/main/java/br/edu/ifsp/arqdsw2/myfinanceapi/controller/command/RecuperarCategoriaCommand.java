package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.PrintWriter;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.CategoriaDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Categoria;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RecuperarCategoriaCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getPathInfo();
		if (path == null || !path.matches("^(/categoria/)\\d+$")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da categoria inválido.");
			return;
		}
		int id = Integer.parseInt(path.substring(11));
		CategoriaDao dao = new CategoriaDao();
		Categoria categoria = dao.fetchById(id);
		if(categoria!=null) {
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(categoria));
			out.flush();
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Categoria não encontrada.");
		}
	}

}
