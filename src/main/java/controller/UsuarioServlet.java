package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.UsuarioDAO;
import model.Usuario;
import util.Parametros;

@WebServlet({"/usuarios", "/usuarios/novo", "/usuarios/editar", "/usuarios/salvar", "/usuarios/status"})
public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<String> PERFIS = List.of("ADMIN", "TECNICO", "SOLICITANTE");
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            switch (request.getServletPath()) {
                case "/usuarios" -> {
                    request.setAttribute("usuarios", usuarioDAO.listar());
                    request.getRequestDispatcher("/WEB-INF/views/usuarios/listar.jsp").forward(request, response);
                }
                case "/usuarios/novo" -> formulario(request, response, new Usuario(), null);
                case "/usuarios/editar" -> {
                    Usuario u = usuarioDAO.buscar(Parametros.idDe(request.getParameter("id")));
                    if (u == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } else {
                        formulario(request, response, u, null);
                    }
                }
                default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar usuarios", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario eu = (Usuario) request.getSession().getAttribute("usuarioLogado");

        try {
            switch (request.getServletPath()) {
                case "/usuarios/salvar" -> salvar(request, response, eu);
                case "/usuarios/status" -> mudarStatus(request, response, eu);
                default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao gravar usuario", e);
        }
    }

    private void salvar(HttpServletRequest request, HttpServletResponse response, Usuario eu)
            throws SQLException, ServletException, IOException {

        int id = Parametros.idDe(request.getParameter("id"));
        if (id < 0 || (id > 0 && usuarioDAO.buscar(id) == null)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Usuario u = new Usuario();
        u.setId(id);
        u.setNome(limpar(request.getParameter("nome")));
        u.setEmail(limpar(request.getParameter("email")));
        u.setSenha(request.getParameter("senha"));
        u.setPerfil(id == eu.getId() ? eu.getPerfil() : request.getParameter("perfil"));

        String erro = validar(u);
        if (erro == null) {
            try {
                if (id == 0) {
                    usuarioDAO.salvar(u);
                } else {
                    usuarioDAO.alterar(u);
                }
                if (id == eu.getId()) {
                    eu.setNome(u.getNome());
                    eu.setEmail(u.getEmail());
                }
                response.sendRedirect(request.getContextPath() + "/usuarios");
                return;
            } catch (SQLException e) {
                if (e.getErrorCode() != 1062) {
                    throw e;
                }
                erro = "Ja existe um usuario com esse e-mail.";
            }
        }
        formulario(request, response, u, erro);
    }

    private void mudarStatus(HttpServletRequest request, HttpServletResponse response, Usuario eu)
            throws SQLException, IOException {

        int id = Parametros.idDe(request.getParameter("id"));
        String acao = request.getParameter("acao");
        if (usuarioDAO.buscar(id) == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        boolean desativar = "desativar".equals(acao);
        if ((!desativar && !"reativar".equals(acao)) || (desativar && id == eu.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        usuarioDAO.definirAtivo(id, !desativar);
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private String validar(Usuario u) {
        if (u.getNome() == null || u.getNome().isBlank() || u.getNome().length() > 100) {
            return "Informe o nome (ate 100 caracteres).";
        }
        if (u.getEmail() == null || !u.getEmail().matches("[^@\\s]+@[^@\\s]+") || u.getEmail().length() > 150) {
            return "Informe um e-mail valido (ate 150 caracteres).";
        }
        boolean semSenha = u.getSenha() == null || u.getSenha().isBlank();
        if (u.getId() == 0 && semSenha) {
            return "Informe a senha.";
        }
        if (!semSenha && u.getSenha().length() > 100) {
            return "A senha pode ter ate 100 caracteres.";
        }
        if (!PERFIS.contains(u.getPerfil())) {
            return "Perfil invalido.";
        }
        return null;
    }

    private String limpar(String texto) {
        return texto == null ? null : texto.trim();
    }

    private void formulario(HttpServletRequest request, HttpServletResponse response, Usuario u, String erro)
            throws ServletException, IOException {
        request.setAttribute("usuario", u);
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/usuarios/formulario.jsp").forward(request, response);
    }
}
