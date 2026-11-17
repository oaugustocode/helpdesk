<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Editar Categoria</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Editar Categoria</h1>

        <c:if test="${not empty erro}">
            <p class="mensagemErro"><c:out value="${erro}"/></p>
        </c:if>

        <form class="formulario" action="${pageContext.request.contextPath}/categorias/atualizar" method="post">

            <input type="hidden" name="id" value="${categoria.id}">

            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<c:out value='${categoria.nome}'/>" required>

            <label for="descricao">Descrição:</label>
            <input type="text" id="descricao" name="descricao" value="<c:out value='${categoria.descricao}'/>">

            <button class="botao" type="submit">Salvar alterações</button>
        </form>

        <a class="linkVoltar" href="${pageContext.request.contextPath}/categorias">Voltar</a>

    </div>

</body>
</html>
