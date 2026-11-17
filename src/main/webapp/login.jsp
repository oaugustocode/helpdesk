<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>HelpDesk Acadêmico - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal caixaLogin">

        <h1 class="tituloCentral">HelpDesk</h1>
        <p class="subtitulo">Acesse sua conta para continuar.</p>

        <c:if test="${not empty erro}">
            <p class="mensagemErro">${erro}</p>
        </c:if>

        <form class="formulario" action="${pageContext.request.contextPath}/login" method="post">
            <label>E-mail:</label>
            <input type="email" name="txtEmail" required>

            <label>Senha:</label>
            <input type="password" name="txtSenha" required>

            <button class="botao botaoCheio" type="submit">Entrar</button>
        </form>

    </div>

</body>
</html>