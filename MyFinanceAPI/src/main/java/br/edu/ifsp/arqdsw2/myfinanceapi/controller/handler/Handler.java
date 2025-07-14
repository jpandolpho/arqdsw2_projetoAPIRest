package br.edu.ifsp.arqdsw2.myfinanceapi.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {
	void setNext(Handler next);

	void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}