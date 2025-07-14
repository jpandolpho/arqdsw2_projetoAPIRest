CREATE DATABASE IF NOT EXISTS MyFinanceAPI;

USE MyFinanceAPI;

CREATE TABLE categoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(15) NOT NULL
);

CREATE TABLE transacao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(150) NOT NULL,
    tipo ENUM('RECEITA', 'DESPESA') NOT NULL,
    data_transacao DATETIME NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON DELETE BLOCK
);