package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.SolicitacaoDAO;
import model.Usuario;

@WebServlet("/solicitacoes")
public class ListarSolicitacoesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");

        try {
            request.setAttribute("solicitacoes", solicitacaoDAO.listar(usu));
            request.getRequestDispatcher("/WEB-INF/views/solicitacoes/listar.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar solicitacoes", e);
        }
    }
}
