package br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler;

import br.edu.ifsp.arqdsw2.myfinanceapi.controller.command.RecuperarTransacaoCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransacaoPorIdHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET")
				&& request.getPathInfo() != null
				&& request.getPathInfo().matches("^(/transacao/)\\d+$");
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new RecuperarTransacaoCommand().executar(request, response);
	}

}
