package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SessaoListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        evento.getServletContext().getSessionCookieConfig().setAttribute("SameSite", "Lax");
    }
}
