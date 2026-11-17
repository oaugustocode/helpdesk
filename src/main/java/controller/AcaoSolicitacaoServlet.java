package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.SolicitacaoDAO;
import model.Solicitacoes;
import model.Usuario;
import util.Parametros;

@WebServlet("/solicitacoes/acao")
public class AcaoSolicitacaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");
        int id = Parametros.idDe(request.getParameter("id"));
        String acao = request.getParameter("acao");

        try {
            Solicitacoes s = solicitacaoDAO.buscar(id);
            if (s == null || !s.podeVer(usu)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("assumir".equals(acao) && s.podeAssumir(usu)) {
                solicitacaoDAO.assumir(id, usu.getId());
            } else if ("finalizar".equals(acao) && s.podeFinalizar(usu)) {
                solicitacaoDAO.finalizar(id);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/solicitacoes/ver?id=" + id);
        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar solicitacao", e);
        }
    }
}
