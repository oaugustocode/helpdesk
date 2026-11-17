# Roteiro de apresentação: HelpDesk

Tempo total: cerca de 4 minutos. O professor pediu: **um CRUD simples**, a **relação 1:N funcionando** e uma **explicação curta do código** dessa regra. Não detalhar login nem telas secundárias.

## Antes de começar (faça com calma, antes da sua vez)

- [ ] MySQL rodando (serviço `MySQL80`).
- [ ] Eclipse: aba **Servers** mostra `Tomcat v11.0 Server at localhost [Started]`.
- [ ] No MySQL Workbench, rode o `demo_reset.sql` (zera chamados e mensagens; mantém usuários e categorias).
- [ ] Abra o navegador em `http://localhost:8080/HelpDesk/` em **três lugares** e já entre em cada um (assim ninguém vê a tela de login):

| Onde | Entra como | E-mail | Senha |
|---|---|---|---|
| Aba 1 (janela normal) | Admin | `admin@helpdesk.local` | `admin123` |
| Aba 2 (janela normal, outra aba) ou outro navegador | Solicitante | `aluno@helpdesk.local` | `aluno123` |
| Janela anônima | Técnico | `tecnico@helpdesk.local` | `tec123` |

Atenção: duas abas do **mesmo** navegador dividem a sessão (a segunda login derruba a primeira). Use **navegador diferente** ou **janela anônima** para o solicitante e o técnico.

- [ ] No Eclipse, deixe abertos em abas os 3 arquivos da parte do código (ordem de uso):
  1. `src/main/java/dao/InteracaoDAO.java`
  2. `src/main/java/controller/EnviarInteracaoServlet.java`
  3. `src/main/java/model/Solicitacoes.java`
- [ ] Ensaie uma vez inteiro e rode o `demo_reset.sql` de novo.

---

## Parte 1: o CRUD (cerca de 30 s), no navegador do Admin

**Fale:** "O CRUD escolhido é o de categorias: criar, editar, excluir e reativar."

**Faça:**
1. Menu **Categorias**.
2. **Nova categoria**, nome `Impressoras`, salvar. Ela aparece na lista.
3. **Editar** e mude a descrição, salvar.
4. **Excluir** (confirme). Ela fica marcada **Não**.
5. **Reativar**. Volta para **Sim**.

**Frase de fechamento:** "As categorias alimentam o formulário de chamado, que é o ponto central do sistema."

---

## Parte 2: abrir o chamado, o lado "1" (cerca de 1 min), no navegador do Solicitante

**Fale:** "Aqui o solicitante abre um chamado. Cada chamado pertence a um usuário e a uma categoria."

**Faça:**
1. **Nova solicitação**.
2. Título: `Computador do laboratório não liga`. Categoria: `Hardware`. Prioridade: `Alta`. Local: `Laboratório 3`. Descrição: `A máquina 12 não dá vídeo.`
3. **Abrir chamado**. Ele aparece na lista com status **ABERTA**.

---

## Parte 3: o chat, o lado "N" (cerca de 1:30)

**Fale:** "Esse chamado vai ter várias interações. Cada mensagem é uma linha na tabela `interacoes` ligada ao chamado."

**Faça, alternando as janelas:**
1. **Técnico:** a lista mostra o chamado na fila. Abra e clique **Assumir chamado**. Status: **EM ANDAMENTO**.
2. **Técnico:** escreva `Pode descrever o erro na tela?` e **Enviar**. Status: **AGUARDANDO RESPOSTA**.
3. **Solicitante:** abra o chamado (recarregue a página), veja a mensagem, responda `A tela fica preta.` e **Enviar**. Status volta para **EM ANDAMENTO**.
4. **Técnico:** recarregue e clique **Finalizar** (confirme). Status: **FINALIZADA**. O campo de mensagem some: o chat ficou somente leitura.

**Frase de fechamento:** "O status muda sozinho conforme quem escreve, e depois de finalizado ninguém mais escreve."

---

## Parte 4: o código (cerca de 1 min)

Mostre só estes pontos, nesta ordem.

### 1. `InteracaoDAO.java`, linhas 16 a 19: o 1:N em SQL
**Fale:** "Para mostrar a conversa, busco todas as interações daquele chamado: `where i.solicitacao_id = ?`. Um chamado, várias interações."

### 2. `InteracaoDAO.java`, linha 43 em diante: gravar a mensagem
**Fale:** "Ao gravar uma mensagem, o status do chamado muda **na mesma transação**: ou grava as duas coisas, ou nenhuma. O `UPDATE` só acerta se o chamado ainda está em atendimento; se alguém finalizou enquanto eu digitava, nada é gravado."
Aponte: `setAutoCommit(false)` (linha 50), `commit()` (linha 69) e `rollback()` (linhas 59 e 72).

### 3. `EnviarInteracaoServlet.java`, linhas 34 a 46: quem pode escrever
**Fale:** "O servlet primeiro confere se esse usuário pode escrever nesse chamado. Depois escolhe o novo status: se o técnico respondeu, passa a aguardar o solicitante; se o solicitante respondeu, volta para o técnico."
Aponte a linha 45 (`String novoStatus = ...`).

### 4. Se sobrar tempo: `Solicitacoes.java`, linha 33, `podeEscrever`
**Fale:** "Só conversa o dono do chamado e o técnico que assumiu, e só durante o atendimento."

---

## Se o professor perguntar

| Pergunta | Resposta curta |
|---|---|
| Por que transação? | Para o status e a mensagem nunca ficarem inconsistentes. |
| O que impede um solicitante de escrever no chamado de outro? | `podeEscrever` compara o id do usuário com o dono e o técnico do chamado, e o servidor devolve 403. |
| E um técnico pegar o mesmo chamado de outro? | O `assumir` é um `UPDATE ... where status = 'ABERTA'`: só um consegue. |
| Onde está o login e a proteção das telas? | `AutenticacaoFilter`: exige login e, para categorias e usuários, o perfil `ADMIN`. |
| O chat atualiza sozinho? | Não: recarrega a página ao enviar. Tempo real ficaria fora do escopo. |
| As senhas são criptografadas? | Não, estão em texto puro. É uma limitação conhecida, fora do escopo. |
| Qual o ponto fraco? | Sem teste automático e sem hash de senha. |

## Se algo der errado ao vivo
- **Página de erro simples:** clique em **Voltar ao início**.
- **Voltou para o login:** a sessão caiu (mesmo navegador usado para dois perfis). Entre de novo no perfil certo.
- **Servidor parado:** aba **Servers** no Eclipse, selecione o servidor e use o botão verde de **Start**.
- **Chamados antigos atrapalhando:** rode o `demo_reset.sql` e recarregue.

## Ao terminar
Pare o servidor no Eclipse (aba **Servers**, botão vermelho de **Stop**) antes de fechar.
