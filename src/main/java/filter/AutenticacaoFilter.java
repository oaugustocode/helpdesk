package filter;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import dao.UsuarioDAO;
import model.Usuario;

@WebFilter("/*")
public class AutenticacaoFilter implements Filter {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String rota = request.getServletPath();

        boolean rotaPublica = rota.equals("/login")
            || rota.equals("/login.jsp")
            || rota.startsWith("/css/")
            || rota.startsWith("/js/");

        if (rotaPublica) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sessao = request.getSession(false);
        Usuario usuarioSessao = sessao == null ? null : (Usuario) sessao.getAttribute("usuarioLogado");
        if (usuarioSessao == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Usuario atual = usuarioDAO.buscar(usuarioSessao.getId());
            if (atual == null || !atual.isAtivo()) {
                sessao.invalidate();
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            usuarioSessao.setNome(atual.getNome());
            usuarioSessao.setPerfil(atual.getPerfil());
        } catch (SQLException e) {
            throw new ServletException("Erro ao validar usuario", e);
        }

        boolean soAdmin = rota.startsWith("/usuarios") || rota.startsWith("/categorias");
        if (soAdmin && !"ADMIN".equals(usuarioSessao.getPerfil())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
