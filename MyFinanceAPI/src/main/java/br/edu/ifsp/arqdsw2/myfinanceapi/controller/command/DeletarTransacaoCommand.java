package br.edu.ifsp.arqdsw2.myfinanceapi.controller.command;

import br.edu.ifsp.arqdsw2.myfinanceapi.model.dao.TransacaoDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeletarTransacaoCommand implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getPathInfo();
		if (path == null || !path.matches("^(/transacao/)\\d+$")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da transação inválido.");
			return;
		}
		int id = Integer.parseInt(path.substring(11));
		TransacaoDao dao = new TransacaoDao();
		boolean excluiu = dao.delete(id);
		if(excluiu) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
		}
	}

}
