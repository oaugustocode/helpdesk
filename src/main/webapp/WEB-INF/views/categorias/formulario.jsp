<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nova Categoria</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Nova Categoria</h1>

        <c:if test="${not empty erro}">
            <p class="mensagemErro"><c:out value="${erro}"/></p>
        </c:if>

        <form class="formulario" action="${pageContext.request.contextPath}/categorias/salvar" method="post">

            <label>Nome:</label>
            <input type="text" name="nome" required>

            <label>Descrição:</label>
            <input type="text" name="descricao">

            <button class="botao" type="submit">Salvar</button>
        </form>

        <a class="linkVoltar" href="${pageContext.request.contextPath}/categorias">Voltar</a>

    </div>

</body>
</html>