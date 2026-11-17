<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>HelpDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Bem-vindo, ${sessionScope.usuarioLogado.nome}</h1>
        <p class="subtitulo">Perfil: ${sessionScope.usuarioLogado.perfil}</p>

        <ul class="menuLista">
            <li><a href="${pageContext.request.contextPath}/solicitacoes">Solicitações</a></li>

            <c:if test="${sessionScope.usuarioLogado.perfil == 'SOLICITANTE'}">
                <li><a href="${pageContext.request.contextPath}/solicitacoes/nova">Nova solicitação</a></li>
            </c:if>

            <c:if test="${sessionScope.usuarioLogado.perfil == 'ADMIN'}">
                <li><a href="${pageContext.request.contextPath}/categorias">Categorias</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios">Usuários</a></li>            </c:if>

            <li><a href="${pageContext.request.contextPath}/logout">Sair</a></li>
        </ul>

    </div>

</body>
</html>