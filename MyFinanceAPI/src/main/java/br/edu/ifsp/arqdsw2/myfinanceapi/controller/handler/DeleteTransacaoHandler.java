package br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler;

import br.edu.ifsp.arqdsw2.myfinanceapi.controller.command.DeletarTransacaoCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransacaoHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("DELETE")
				&& request.getPathInfo() != null
				&& request.getPathInfo().matches("^(/transacao/)\\d+$");
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new DeletarTransacaoCommand().executar(request, response);
	}

}
