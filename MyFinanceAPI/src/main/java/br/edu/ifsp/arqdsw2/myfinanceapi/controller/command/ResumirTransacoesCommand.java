package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.TransacaoDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.util.TipoTransacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResumirTransacoesCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TransacaoDao dao = new TransacaoDao();
		Map<Integer,Double> despesasPorCategoria = dao.fetchSumByCategoria();
		if(despesasPorCategoria!=null) {
			double receitas = dao.fetchSum(TipoTransacao.RECEITA);
			double despesas = dao.fetchSum(TipoTransacao.DESPESA);
			double saldo = receitas-despesas;
			JsonObject json = new JsonObject();
			json.addProperty("receitas", receitas);
			json.addProperty("despesas", despesas);
			json.addProperty("saldo", saldo);
			json.add("despesasPorCategoria", new Gson().toJsonTree(despesasPorCategoria));
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(json));
			out.flush();
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Não existem transações cadastradas.");
		}
	}

}
