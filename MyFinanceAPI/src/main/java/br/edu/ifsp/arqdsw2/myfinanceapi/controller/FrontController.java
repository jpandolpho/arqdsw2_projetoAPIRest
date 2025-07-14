package br.edu.ifsp.arqdsw2.myfinanceapi.controller;

import java.io.IOException;

import br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler.Handler;
import br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler.HandlerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/transacao/*","/categoria/*","/resumo/*"})
public class FrontController extends HttpServlet {
	private Handler chain;

	@Override
	public void init() {
		try {
			chain = HandlerFactory.criarCadeia();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao inicializar a cadeia de handlers", e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			chain.handle(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}