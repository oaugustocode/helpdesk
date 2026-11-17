<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nova solicitação</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Nova solicitação</h1>

        <c:if test="${not empty erro}">
            <p class="mensagemErro"><c:out value="${erro}"/></p>
        </c:if>

        <form class="formulario" action="${pageContext.request.contextPath}/solicitacoes/nova" method="post">

            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo" maxlength="150" value="<c:out value='${param.titulo}'/>" required>

            <label for="categoria">Categoria:</label>
            <select id="categoria" name="categoria" required>
                <c:forEach var="c" items="${categorias}">
                    <option value="${c.id}" ${param.categoria == (c.id += '') ? 'selected' : ''}><c:out value="${c.nome}"/></option>
                </c:forEach>
            </select>

            <label for="prioridade">Prioridade:</label>
            <select id="prioridade" name="prioridade">
                <option value="BAIXA" ${param.prioridade == 'BAIXA' ? 'selected' : ''}>Baixa</option>
                <option value="MEDIA" ${empty param.prioridade or param.prioridade == 'MEDIA' ? 'selected' : ''}>Média</option>
                <option value="ALTA" ${param.prioridade == 'ALTA' ? 'selected' : ''}>Alta</option>
            </select>

            <label for="local">Local da ocorrência:</label>
            <input type="text" id="local" name="local" maxlength="150" value="<c:out value='${param.local}'/>">

            <label for="descricao">Descrição:</label>
            <textarea id="descricao" name="descricao" rows="5" required><c:out value="${param.descricao}"/></textarea>

            <button class="botao" type="submit">Abrir chamado</button>
        </form>

        <a class="linkVoltar" href="${pageContext.request.contextPath}/solicitacoes">Voltar</a>

    </div>

</body>
</html>
