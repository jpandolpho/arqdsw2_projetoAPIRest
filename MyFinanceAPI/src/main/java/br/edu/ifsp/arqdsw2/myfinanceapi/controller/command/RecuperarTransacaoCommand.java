package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.PrintWriter;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.TransacaoDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Transacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RecuperarTransacaoCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getPathInfo();
		if (path == null || !path.matches("^(/transacao/)\\d+$")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da transação inválido.");
			return;
		}
		int id = Integer.parseInt(path.substring(11));
		TransacaoDao dao = new TransacaoDao();
		Transacao transacao = dao.fetchById(id);
		if(transacao!=null) {
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(transacao));
			out.flush();
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
		}
	}

}
