# Roteiro de 5 minutos: HelpDesk

Objetivo: mostrar **1 CRUD simples (categorias)** e a **relação 1:N (chamado → mensagens)**, e explicar o código dessa regra.

## Antes (rode o demo_reset.sql e deixe tudo logado)

| Janela | Perfil | E-mail | Senha |
|---|---|---|---|
| Navegador A | Admin | admin@helpdesk.local | admin123 |
| Navegador B | Solicitante | aluno@helpdesk.local | aluno123 |
| Janela anônima | Técnico | tecnico@helpdesk.local | tec123 |

No Eclipse, abas abertas: `InteracaoDAO.java` e `EnviarInteracaoServlet.java`.

---

## 0:00 – 0:30 | Abertura (fale)

"Fiz um sistema de Help Desk. Um **chamado** pertence a um usuário e tem **várias mensagens**: é a relação 1 para N, entre as tabelas `solicitacoes` e `interacoes`. Vou mostrar um CRUD, depois o chamado com o chat, e por fim o código."

## 0:30 – 1:15 | CRUD de categorias (Admin)

Fale: "O CRUD é o de categorias."
Mostre, rápido: **Categorias → Nova** (`Impressoras`) → **Editar** → **Excluir** → **Reativar**.
Feche: "Essas categorias alimentam o formulário do chamado."

## 1:15 – 2:00 | Lado "1": abrir o chamado (Solicitante)

Fale: "O solicitante abre um chamado com categoria, prioridade e descrição."
Mostre: **Nova solicitação** → título `Computador não liga`, categoria `Hardware`, prioridade `Alta` → **Abrir**. Aparece como **ABERTA**.

## 2:00 – 3:30 | Lado "N": o chat (alterne as janelas)

Fale: "Cada mensagem é uma linha em `interacoes` ligada ao chamado."

1. **Técnico:** abre o chamado → **Assumir** → vira **EM ANDAMENTO**.
2. **Técnico:** escreve `Pode descrever o erro?` → status vira **AGUARDANDO RESPOSTA**.
3. **Solicitante:** recarrega, responde `A tela fica preta.` → volta para **EM ANDAMENTO**.
4. **Técnico:** **Finalizar** → **FINALIZADA**, e o campo de mensagem some.

Feche: "O status muda sozinho conforme quem escreve, e depois de finalizado ninguém escreve mais."

## 3:30 – 4:45 | Código (só 3 pontos)

1. **`InteracaoDAO`, linhas 16–19:** "Para montar a conversa busco as mensagens do chamado com `where i.solicitacao_id = ?`. Um chamado, várias mensagens: é o 1:N."
2. **`InteracaoDAO`, a partir da linha 43:** "Ao gravar a mensagem, o status do chamado muda **na mesma transação**: `setAutoCommit(false)`, `commit` e `rollback`. Ou grava as duas coisas, ou nenhuma."
3. **`EnviarInteracaoServlet`, linhas 34–46:** "Antes de gravar, confere se o usuário pode escrever (dono do chamado ou técnico que assumiu). Se o técnico respondeu, o status vai para aguardando; se o solicitante respondeu, volta para andamento."

## 4:45 – 5:00 | Fechamento

"Resumindo: um CRUD de categorias e um chamado com várias mensagens, com o status controlado na mesma transação da gravação."

---

## Se perguntarem

- **Por que transação?** Para o status e a mensagem nunca ficarem inconsistentes.
- **Dois técnicos pegam o mesmo chamado?** Não: o `assumir` é um `UPDATE ... where status = 'ABERTA'`, só um consegue.
- **Atualiza em tempo real?** Não, recarrega a página ao enviar.
- **Senhas criptografadas?** Não, limitação conhecida.

## Se travar

Página de erro: **Voltar ao início**. Voltou ao login: a sessão caiu (use navegadores diferentes). Servidor parado: aba **Servers** do Eclipse → Start.
