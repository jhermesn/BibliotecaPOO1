CREATE DATABASE IF NOT EXISTS `biblioteca_poo1`;
USE `biblioteca_poo1`;

CREATE TABLE `editora` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `endereco` VARCHAR(255) NOT NULL,
  `telefone` VARCHAR(20) NOT NULL,
  `nome_gerente` VARCHAR(255) NOT NULL,
  PRIMARY KEY(`id`)
);

CREATE TABLE `cliente` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `endereco` VARCHAR(255) NOT NULL,
  `telefone` VARCHAR(20) NOT NULL,
  `cpf` VARCHAR(14) UNIQUE,
  `cnpj` VARCHAR(18) UNIQUE,
  PRIMARY KEY(`id`),
  CONSTRAINT `check_cpf_ou_cnpj` CHECK (
    (cpf IS NOT NULL AND cnpj IS NULL) OR
    (cpf IS NULL AND cnpj IS NOT NULL)
  )
);

CREATE TABLE `livro` (
  `ISBN` VARCHAR(13) NOT NULL,
  `editora_id` INTEGER UNSIGNED NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  `nome_autor` VARCHAR(255) NOT NULL,
  `assunto` VARCHAR(255) NOT NULL,
  `estoque` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY(`ISBN`),
  CONSTRAINT `fk_livro_editora` FOREIGN KEY(`editora_id`)
    REFERENCES `editora`(`id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `check_estoque_positivo` CHECK (`estoque` >= 0),
  INDEX `idx_editora` (`editora_id`),
  INDEX `idx_assunto` (`assunto`)
);

CREATE TABLE `compra` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `cliente_id` INTEGER UNSIGNED NOT NULL,
  `livro_isbn` VARCHAR(13) NOT NULL,
  `data_compra` DATE NOT NULL,
  `quantidade` INTEGER UNSIGNED NOT NULL DEFAULT 1,
  `preco_unitario` DECIMAL(10,2),
  PRIMARY KEY(`id`),
  CONSTRAINT `fk_compra_cliente` FOREIGN KEY(`cliente_id`)
    REFERENCES `cliente`(`id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_compra_livro` FOREIGN KEY(`livro_isbn`)
    REFERENCES `livro`(`ISBN`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `check_quantidade_positiva` CHECK (`quantidade` > 0),
  INDEX `idx_cliente` (`cliente_id`),
  INDEX `idx_livro` (`livro_isbn`),
  INDEX `idx_data` (`data_compra`)
);

CREATE INDEX `idx_cliente_nome` ON `cliente`(`nome`);
CREATE INDEX `idx_livro_nome` ON `livro`(`nome`);
CREATE INDEX `idx_livro_autor` ON `livro`(`nome_autor`);