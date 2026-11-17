
CREATE DATABASE IF NOT EXISTS helpdesk CHARACTER SET utf8mb4;
USE helpdesk;

CREATE TABLE IF NOT EXISTS usuarios (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    senha         VARCHAR(100) NOT NULL,
    perfil        VARCHAR(20)  NOT NULL,
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    data_cadastro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categorias (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo     BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS solicitacoes (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    titulo           VARCHAR(150) NOT NULL,
    descricao        TEXT         NOT NULL,
    local_ocorrencia VARCHAR(150),
    prioridade       VARCHAR(20)  NOT NULL DEFAULT 'MEDIA',
    status           VARCHAR(30)  NOT NULL DEFAULT 'ABERTA',
    data_abertura    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fechamento  DATETIME     NULL,
    usuario_id       INT NOT NULL,
    tecnico_id       INT NULL,
    categoria_id     INT NOT NULL,
    FOREIGN KEY (usuario_id)   REFERENCES usuarios(id),
    FOREIGN KEY (tecnico_id)   REFERENCES usuarios(id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS interacoes (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    mensagem        TEXT     NOT NULL,
    data_interacao  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    solicitacao_id  INT NOT NULL,
    usuario_id      INT NOT NULL,
    FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
    FOREIGN KEY (usuario_id)     REFERENCES usuarios(id)
);

INSERT IGNORE INTO usuarios (id, nome, email, senha, perfil) VALUES
    (1, 'Admin Teste',       'admin@helpdesk.local',   'admin123', 'ADMIN'),
    (2, 'Tecnico Teste',     'tecnico@helpdesk.local', 'tec123',   'TECNICO'),
    (3, 'Solicitante Teste', 'aluno@helpdesk.local',   'aluno123', 'SOLICITANTE');

INSERT IGNORE INTO categorias (id, nome, descricao) VALUES
    (1, 'Hardware', 'Computadores, impressoras e perifericos'),
    (2, 'Rede',     'Internet, Wi-Fi e acesso a rede'),
    (3, 'Software', 'Instalacao e erros de programas');

INSERT IGNORE INTO solicitacoes
    (id, titulo, descricao, local_ocorrencia, prioridade, status, data_abertura, usuario_id, tecnico_id, categoria_id) VALUES
    (1, 'Computador do laboratorio nao liga', 'A maquina 12 nao da video.', 'Laboratorio 3', 'ALTA',
     'AGUARDANDO_RESPOSTA', UTC_TIMESTAMP() - INTERVAL 3 HOUR, 3, 2, 1);

INSERT IGNORE INTO interacoes (id, mensagem, data_interacao, solicitacao_id, usuario_id) VALUES
    (1, 'A maquina 12 nao da video desde ontem.',         UTC_TIMESTAMP() - INTERVAL 2 HOUR, 1, 3),
    (2, 'Assumi o chamado. O cabo de video esta firme?', UTC_TIMESTAMP() - INTERVAL 1 HOUR, 1, 2);
