<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html>
<head>
    <title>Chamado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="caixaPrincipal">

        <h1><c:out value="${solicitacao.titulo}"/></h1>

        <div class="barraAcoes">
            <a class="botao botaoSecundario" href="${pageContext.request.contextPath}/solicitacoes">Voltar</a>

            <c:if test="${podeAssumir}">
                <form action="${pageContext.request.contextPath}/solicitacoes/acao" method="post">
                    <input type="hidden" name="id" value="${solicitacao.id}">
                    <input type="hidden" name="acao" value="assumir">
                    <button class="botao" type="submit">Assumir chamado</button>
                </form>
            </c:if>

            <c:if test="${podeFinalizar}">
                <form action="${pageContext.request.contextPath}/solicitacoes/acao" method="post"
                      onsubmit="return confirm('Finalizar este chamado? A conversa ficará somente leitura.');">
                    <input type="hidden" name="id" value="${solicitacao.id}">
                    <input type="hidden" name="acao" value="finalizar">
                    <button class="botao" type="submit">Finalizar</button>
                </form>
            </c:if>
        </div>

        <div class="dadosChamado">
            <div>
                <span>Status</span>
                <span class="etiqueta etiqueta-${solicitacao.status}">
                    <c:out value="${fn:replace(solicitacao.status, '_', ' ')}"/>
                </span>
            </div>
            <div><span>Prioridade</span><c:out value="${solicitacao.prioridade}"/></div>
            <div><span>Categoria</span><c:out value="${solicitacao.categoria_nome}"/></div>
            <div><span>Local</span><c:out value="${solicitacao.local_ocorrencia}"/></div>
            <div><span>Solicitante</span><c:out value="${solicitacao.usuario_nome}"/></div>
            <div>
                <span>Técnico</span>
                <c:out value="${empty solicitacao.tecnico_nome ? 'Aguardando atendente' : solicitacao.tecnico_nome}"/>
            </div>
            <div><span>Aberto em</span><fmt:formatDate value="${solicitacao.data_abertura}" pattern="dd/MM/yyyy HH:mm"/></div>
            <c:if test="${not empty solicitacao.data_fechamento}">
                <div><span>Finalizado em</span><fmt:formatDate value="${solicitacao.data_fechamento}" pattern="dd/MM/yyyy HH:mm"/></div>
            </c:if>
        </div>

        <h2>Descrição</h2>
        <p class="textoPreservado"><c:out value="${solicitacao.descricao}"/></p>

        <h2>Conversa</h2>

        <div class="chat">
            <c:forEach var="i" items="${interacoes}">
                <div class="mensagem ${i.usuario_id == sessionScope.usuarioLogado.id ? 'mensagemMinha' : 'mensagemOutra'}"><span class="mensagemAutor"><c:out value="${i.usuario_nome}"/> · <fmt:formatDate value="${i.data_interacao}" pattern="dd/MM/yyyy HH:mm"/></span><c:out value="${i.mensagem}"/></div>
            </c:forEach>
        </div>

        <c:if test="${empty interacoes}">
            <p class="textoVazio">Nenhuma mensagem ainda.</p>
        </c:if>

        <c:choose>
            <c:when test="${podeEscrever}">
                <form class="formulario" action="${pageContext.request.contextPath}/solicitacoes/mensagem" method="post">
                    <input type="hidden" name="id" value="${solicitacao.id}">
                    <textarea name="mensagem" aria-label="Mensagem" rows="3" maxlength="2000" placeholder="Escreva sua mensagem..." required></textarea>
                    <button class="botao" type="submit">Enviar</button>
                </form>
            </c:when>
            <c:when test="${solicitacao.status == 'ABERTA'}">
                <p class="textoVazio">Aguardando um técnico assumir o chamado.</p>
            </c:when>
            <c:when test="${solicitacao.status == 'FINALIZADA'}">
                <p class="textoVazio">Chamado finalizado: a conversa está somente leitura.</p>
            </c:when>
            <c:otherwise>
                <p class="textoVazio">Você está acompanhando este chamado (somente leitura).</p>
            </c:otherwise>
        </c:choose>

    </div>

</body>
</html>
