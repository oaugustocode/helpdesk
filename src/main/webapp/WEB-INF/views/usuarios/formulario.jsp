<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="novo" value="${usuario.id == 0}"/>
<c:set var="souEu" value="${usuario.id == sessionScope.usuarioLogado.id}"/>
<!DOCTYPE html>
<html>
<head>
    <title>${novo ? 'Novo usuário' : 'Editar usuário'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1>${novo ? 'Novo usuário' : 'Editar usuário'}</h1>

        <c:if test="${not empty erro}">
            <p class="mensagemErro"><c:out value="${erro}"/></p>
        </c:if>

        <form class="formulario" action="${pageContext.request.contextPath}/usuarios/salvar" method="post">

            <input type="hidden" name="id" value="${usuario.id}">

            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" maxlength="100" value="<c:out value='${usuario.nome}'/>" required>

            <label for="email">E-mail:</label>
            <input type="email" id="email" name="email" maxlength="150" value="<c:out value='${usuario.email}'/>" required>

            <label for="senha">Senha:</label>
            <input type="password" id="senha" name="senha" maxlength="100" autocomplete="new-password"
                   ${novo ? 'required' : 'placeholder="Deixe em branco para manter a atual"'}>

            <label for="perfil">Perfil:</label>
            <select id="perfil" name="perfil" ${souEu ? 'disabled' : ''}>
                <option value="SOLICITANTE" ${usuario.perfil == 'SOLICITANTE' ? 'selected' : ''}>Solicitante</option>
                <option value="TECNICO" ${usuario.perfil == 'TECNICO' ? 'selected' : ''}>Técnico</option>
                <option value="ADMIN" ${usuario.perfil == 'ADMIN' ? 'selected' : ''}>Administrador</option>
            </select>
            <c:if test="${souEu}">
                <p class="textoVazio">Você não pode alterar o seu próprio perfil.</p>
            </c:if>

            <button class="botao" type="submit">${novo ? 'Criar usuário' : 'Salvar alterações'}</button>
        </form>

        <a class="linkVoltar" href="${pageContext.request.contextPath}/usuarios">Voltar</a>

    </div>

</body>
</html>
