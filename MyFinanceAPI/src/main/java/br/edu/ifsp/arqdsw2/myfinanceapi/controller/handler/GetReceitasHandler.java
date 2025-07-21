package br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler;

import br.edu.ifsp.arqdsw2.myfinanceapi.controller.command.ListarReceitasCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReceitasHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET")
				&& (request.getPathInfo()!=null && request.getPathInfo().equals("/transacao/receitas"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ListarReceitasCommand().executar(request, response);
	}

}