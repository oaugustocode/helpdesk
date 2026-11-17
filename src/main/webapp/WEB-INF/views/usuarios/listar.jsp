<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html>
<head>
    <title>Usuários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>Usuários</h1>

        <div class="barraAcoes">
            <a class="botao botaoSecundario" href="${pageContext.request.contextPath}/home">Início</a>
            <a class="botao" href="${pageContext.request.contextPath}/usuarios/novo">Novo usuário</a>
        </div>

        <div class="tabelaRolagem">
        <table class="tabela">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Perfil</th>
                    <th>Ativo</th>
                    <th>Cadastro</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${usuarios}">
                    <tr>
                        <td><c:out value="${u.nome}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.perfil}"/></td>
                        <td>
                            <span class="etiqueta ${u.ativo ? 'etiquetaAtiva' : 'etiquetaInativa'}">
                                ${u.ativo ? 'Sim' : 'Não'}
                            </span>
                        </td>
                        <td><fmt:formatDate value="${u.data_cadastro}" pattern="dd/MM/yyyy"/></td>
                        <td class="colunaAcoes">
                            <a href="${pageContext.request.contextPath}/usuarios/editar?id=${u.id}">Editar</a>

                            <c:if test="${u.ativo and u.id != sessionScope.usuarioLogado.id}">
                                <form action="${pageContext.request.contextPath}/usuarios/status" method="post"
                                      onsubmit="return confirm('Desativar este usuário? Ele não poderá mais entrar.');">
                                    <input type="hidden" name="id" value="${u.id}">
                                    <input type="hidden" name="acao" value="desativar">
                                    <button class="botao botaoSecundario" type="submit">Desativar</button>
                                </form>
                            </c:if>

                            <c:if test="${not u.ativo}">
                                <form action="${pageContext.request.contextPath}/usuarios/status" method="post">
                                    <input type="hidden" name="id" value="${u.id}">
                                    <input type="hidden" name="acao" value="reativar">
                                    <button class="botao botaoSecundario" type="submit">Reativar</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>

    </div>

</body>
</html>
