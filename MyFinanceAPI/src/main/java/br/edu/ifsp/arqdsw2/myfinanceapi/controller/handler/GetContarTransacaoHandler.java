package br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler;

import br.edu.ifsp.arqdsw2.myfinanceapi.controller.command.ContarTransacoesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetContarTransacaoHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET")
				&& (request.getPathInfo()!=null && request.getPathInfo().equals("/contagem"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ContarTransacoesCommand().executar(request, response);
	}

}
