package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.InteracaoDAO;
import dao.SolicitacaoDAO;
import model.Solicitacoes;
import model.Usuario;
import util.Parametros;

@WebServlet("/solicitacoes/ver")
public class VerSolicitacaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private InteracaoDAO interacaoDAO = new InteracaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");

        try {
            Solicitacoes s = solicitacaoDAO.buscar(Parametros.idDe(request.getParameter("id")));
            if (s == null || !s.podeVer(usu)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("solicitacao", s);
            request.setAttribute("interacoes", interacaoDAO.listarPorSolicitacao(s.getId()));
            request.setAttribute("podeEscrever", s.podeEscrever(usu));
            request.setAttribute("podeAssumir", s.podeAssumir(usu));
            request.setAttribute("podeFinalizar", s.podeFinalizar(usu));
            request.getRequestDispatcher("/WEB-INF/views/solicitacoes/ver.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar solicitacao", e);
        }
    }
}
