package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import java.io.BufferedReader;

import com.google.gson.Gson;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.TransacaoDao;
import br.edu.ifsp.arqdsw2.myfinanceapi.model.entity.Transacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CriarTransacaoCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedReader reader = request.getReader();
		Transacao transacao = new Gson().fromJson(reader, Transacao.class);
		TransacaoDao dao = new TransacaoDao();
		dao.insert(transacao);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}

}
