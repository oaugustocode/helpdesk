<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Categorias</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Categorias</h1>

        <div class="barraAcoes">
            <a class="botao botaoSecundario" href="${pageContext.request.contextPath}/home">Início</a>
            <a class="botao" href="${pageContext.request.contextPath}/categorias/nova">Nova categoria</a>
        </div>

        <h2>Lista de categorias</h2>

        <div class="tabelaRolagem">
            <table class="tabela">
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Descrição</th>
                        <th>Ativa</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${categorias}">
                        <tr>
                            <td><c:out value="${c.nome}"/></td>
                            <td><c:out value="${c.descricao}"/></td>
                            <td>
                                <span class="etiqueta ${c.ativo ? 'etiquetaAtiva' : 'etiquetaInativa'}">
                                    ${c.ativo ? 'Sim' : 'Não'}
                                </span>
                            </td>
                            <td class="colunaAcoes">
                                <a href="${pageContext.request.contextPath}/categorias/editar?id=${c.id}">Editar</a>

                                <c:if test="${c.ativo}">
                                    <form action="${pageContext.request.contextPath}/categorias/excluir" method="post"
                                          onsubmit="return confirm('Deseja realmente excluir esta categoria?');">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <button class="botao botaoSecundario" type="submit">Excluir</button>
                                    </form>
                                </c:if>

                                <c:if test="${not c.ativo}">
                                    <form action="${pageContext.request.contextPath}/categorias/reativar" method="post">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <button class="botao botaoSecundario" type="submit">Reativar</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${empty categorias}">
            <p class="textoVazio">Nenhuma categoria cadastrada.</p>
        </c:if>

    </div>

</body>
</html>
