CREATE TABLE public.produto (
	id bigserial NOT NULL,
	nome varchar(500) NOT NULL,
	valor numeric(19,2) NOT NULL,
    CONSTRAINT produto_pk PRIMARY KEY (id)
);
