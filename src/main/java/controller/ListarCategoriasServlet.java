package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;

@WebServlet("/categorias")
public class ListarCategoriasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("categorias", categoriaDAO.listarCategorias(false));
            request.getRequestDispatcher("/WEB-INF/views/categorias/listar.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar categorias", e);
        }
    }
}
