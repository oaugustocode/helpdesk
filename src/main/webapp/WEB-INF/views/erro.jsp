<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Erro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Ocorreu um erro</h1>

        <p class="textoErro">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">Não encontramos o que você procurou.</c:when>
                <c:when test="${pageContext.errorData.statusCode == 403}">Você não tem permissão para esta ação.</c:when>
                <c:otherwise>Algo deu errado. Tente novamente; se continuar, avise o suporte.</c:otherwise>
            </c:choose>
        </p>

        <a class="botao" href="${pageContext.request.contextPath}/home">Voltar ao início</a>

    </div>

</body>
</html>
