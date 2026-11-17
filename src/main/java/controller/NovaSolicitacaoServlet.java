package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import dao.SolicitacaoDAO;
import model.Solicitacoes;
import model.Usuario;
import util.Parametros;

@WebServlet("/solicitacoes/nova")
public class NovaSolicitacaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<String> PRIORIDADES = List.of("BAIXA", "MEDIA", "ALTA");
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (!"SOLICITANTE".equals(usu.getPerfil())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        mostrarFormulario(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usu = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (!"SOLICITANTE".equals(usu.getPerfil())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        String local = request.getParameter("local");
        String prioridade = request.getParameter("prioridade");
        int categoria = Parametros.idDe(request.getParameter("categoria"));

        try {
            boolean categoriaValida = categoriaDAO.listarCategorias(true).stream().anyMatch(c -> c.getId() == categoria);

            String erro = null;
            if (titulo == null || titulo.isBlank() || titulo.length() > 150) {
                erro = "Informe um titulo de ate 150 caracteres.";
            } else if (descricao == null || descricao.isBlank()) {
                erro = "Descreva o problema.";
            } else if (local != null && local.length() > 150) {
                erro = "O local pode ter ate 150 caracteres.";
            } else if (!PRIORIDADES.contains(prioridade)) {
                erro = "Prioridade invalida.";
            } else if (!categoriaValida) {
                erro = "Escolha uma categoria valida.";
            }
            if (erro != null) {
                mostrarFormulario(request, response, erro);
                return;
            }

            Solicitacoes s = new Solicitacoes();
            s.setTitulo(titulo);
            s.setDescricao(descricao);
            s.setLocal_ocorrencia(local);
            s.setPrioridade(prioridade);
            s.setUsuario_id(usu.getId());
            s.setCategoria_id(categoria);

            solicitacaoDAO.abrir(s);
            response.sendRedirect(request.getContextPath() + "/solicitacoes");
        } catch (SQLException e) {
            throw new ServletException("Erro ao abrir solicitacao", e);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            request.setAttribute("categorias", categoriaDAO.listarCategorias(true));
            request.setAttribute("erro", erro);
            request.getRequestDispatcher("/WEB-INF/views/solicitacoes/nova.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar categorias", e);
        }
    }
}
