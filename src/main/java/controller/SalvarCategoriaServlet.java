package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;


@WebServlet("/categorias/salvar")
public class SalvarCategoriaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CategoriaDAO categoriaDAO = new CategoriaDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nome = request.getParameter("nome");
		String descricao = request.getParameter("descricao");

		if (nome == null || nome.isBlank()) {
			request.setAttribute("erro", "Informe o nome da categoria.");
			request.getRequestDispatcher("/WEB-INF/views/categorias/formulario.jsp").forward(request, response);
			return;
		}

		Categorias cat = new Categorias();
		cat.setNome(nome);
		cat.setDescricao(descricao);

		try {
			categoriaDAO.salvar(cat);
			response.sendRedirect(request.getContextPath() + "/categorias");
		} catch (SQLException e) {
			throw new ServletException("Erro ao salvar categoria", e);
		}
	}

}
