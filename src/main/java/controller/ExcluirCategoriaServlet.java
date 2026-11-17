package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import util.Parametros;

@WebServlet("/categorias/excluir")
public class ExcluirCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Parametros.idDe(request.getParameter("id"));

        try {
            if (categoriaDAO.localizarCategoria(id) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            categoriaDAO.inativar(id);
            response.sendRedirect(request.getContextPath() + "/categorias");
        } catch (SQLException e) {
            throw new ServletException("Erro ao excluir categoria", e);
        }
    }
}
