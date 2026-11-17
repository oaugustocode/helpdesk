<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html>
<head>
    <title>Solicitações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Solicitações</h1>

        <div class="barraAcoes">
            <a class="botao botaoSecundario" href="${pageContext.request.contextPath}/home">Início</a>
            <c:if test="${sessionScope.usuarioLogado.perfil == 'SOLICITANTE'}">
                <a class="botao" href="${pageContext.request.contextPath}/solicitacoes/nova">Nova solicitação</a>
            </c:if>
        </div>

        <div class="tabelaRolagem">
        <table class="tabela">
            <thead>
                <tr>
                    <th>Título</th>
                    <th>Categoria</th>
                    <th>Prioridade</th>
                    <th>Status</th>
                    <c:if test="${sessionScope.usuarioLogado.perfil != 'SOLICITANTE'}">
                        <th>Solicitante</th>
                    </c:if>
                    <th>Aberto em</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${solicitacoes}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/solicitacoes/ver?id=${s.id}">
                                <c:out value="${s.titulo}"/>
                            </a>
                        </td>
                        <td><c:out value="${s.categoria_nome}"/></td>
                        <td><c:out value="${s.prioridade}"/></td>
                        <td>
                            <span class="etiqueta etiqueta-${s.status}">
                                <c:out value="${fn:replace(s.status, '_', ' ')}"/>
                            </span>
                        </td>
                        <c:if test="${sessionScope.usuarioLogado.perfil != 'SOLICITANTE'}">
                            <td><c:out value="${s.usuario_nome}"/></td>
                        </c:if>
                        <td><fmt:formatDate value="${s.data_abertura}" pattern="dd/MM/yyyy HH:mm"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>

        <c:if test="${empty solicitacoes}">
            <p class="textoVazio">Nenhuma solicitação por aqui.</p>
        </c:if>

    </div>

</body>
</html>
