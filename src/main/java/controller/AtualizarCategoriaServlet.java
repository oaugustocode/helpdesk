package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;
import util.Parametros;

@WebServlet("/categorias/atualizar")
public class AtualizarCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Categorias categoria = new Categorias();
        categoria.setId(Parametros.idDe(request.getParameter("id")));
        categoria.setNome(request.getParameter("nome"));
        categoria.setDescricao(request.getParameter("descricao"));

        try {
            if (categoriaDAO.localizarCategoria(categoria.getId()) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (categoria.getNome() == null || categoria.getNome().isBlank()) {
                request.setAttribute("erro", "Informe o nome da categoria.");
                request.setAttribute("categoria", categoria);
                request.getRequestDispatcher("/WEB-INF/views/categorias/editar.jsp").forward(request, response);
                return;
            }

            categoriaDAO.alterar(categoria);
            response.sendRedirect(request.getContextPath() + "/categorias");
        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar categoria", e);
        }
    }
}
