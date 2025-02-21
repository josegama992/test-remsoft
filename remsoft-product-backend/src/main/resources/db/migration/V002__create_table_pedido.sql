CREATE TABLE public.pedido (
	id bigserial NOT NULL,
	nome_comprador varchar(500) NOT NULL,
	nome_fornecedor varchar(500) NOT NULL,
	CONSTRAINT pedido_pk PRIMARY KEY (id)
);

CREATE TABLE public.pedido_produto (
	pedido_id int8 NOT NULL,
	produto_id int8 NOT NULL,
	quantidade int8 NOT NULL,
	valor_unitario numeric(19,2) NOT NULL,
	CONSTRAINT pedido_produto_pk PRIMARY KEY (pedido_id, produto_id),
	FOREIGN KEY (produto_id) REFERENCES produto(id),
	FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);