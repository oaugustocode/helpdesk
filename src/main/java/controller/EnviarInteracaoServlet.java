package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.InteracaoDAO;
import dao.SolicitacaoDAO;
import model.Interacoes;
import model.Solicitacoes;
import model.Usuario;
import util.Parametros;

@WebServlet("/solicitacoes/mensagem")
public class EnviarInteracaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private InteracaoDAO interacaoDAO = new InteracaoDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");
        int id = Parametros.idDe(request.getParameter("id"));
        String texto = request.getParameter("mensagem");

        try {
            Solicitacoes s = solicitacaoDAO.buscar(id);
            if (s == null || !s.podeEscrever(usu)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (texto != null && !texto.isBlank() && texto.length() <= 2000) {
                Interacoes i = new Interacoes();
                i.setSolicitacao_id(id);
                i.setUsuario_id(usu.getId());
                i.setMensagem(texto.trim());

                String novoStatus = "TECNICO".equals(usu.getPerfil()) ? "AGUARDANDO_RESPOSTA" : "EM_ANDAMENTO";
                if (!interacaoDAO.inserir(i, novoStatus)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/solicitacoes/ver?id=" + id);
        } catch (SQLException e) {
            throw new ServletException("Erro ao enviar mensagem", e);
        }
    }
}
