package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.TransacaoDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Transacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListarTransacaoCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TransacaoDao dao = new TransacaoDao();
		String pagina = request.getParameter("page");
		String limite = request.getParameter("limit");
		String mes = request.getParameter("month");
		String ano = request.getParameter("year");
		String categoria = request.getParameter("categoria");
		String tipo = request.getParameter("type");
		int page = 0;
		int limit = 0;
		int month = -1;
		int year = -1; 
		int category = -1;
		if(pagina!=null && pagina.matches("^\\d+$")) {
			page = Integer.parseInt(pagina);
		}
		if(limite!=null && limite.matches("^\\d+$")) {
			limit = Integer.parseInt(limite);
		}
		if(mes!=null && mes.matches("^\\d+$")) {
			month = Integer.parseInt(mes);
		}
		if(ano!=null && ano.matches("^\\d+$")) {
			year = Integer.parseInt(ano);
		}
		if(categoria!=null && categoria.matches("^\\d+$")) {
			category = Integer.parseInt(categoria);
		}
		List<Transacao> transacoes = dao.fetch(page,limit,month,year,category, tipo);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(transacoes));
		out.flush();
	}

}
