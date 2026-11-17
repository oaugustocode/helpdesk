package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;
import util.Parametros;

@WebServlet("/categorias/editar")
public class EditarCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Categorias categoria = categoriaDAO.localizarCategoria(Parametros.idDe(request.getParameter("id")));
            if (categoria == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("categoria", categoria);
            request.getRequestDispatcher("/WEB-INF/views/categorias/editar.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar categoria", e);
        }
    }
}
