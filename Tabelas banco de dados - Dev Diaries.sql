create database blog_bd;

use blog_bd;

create table usuarios (
id integer auto_increment primary key,
nome varchar(200) not null,
username varchar(50) not null unique,
email varchar(50) not null unique,
senha text not null
);

create table posts (
id integer auto_increment primary key,
titulo varchar(100) not null,
conteudo text not null,
curtidas int default 0,
criado_em timestamp default current_timestamp,
id_usuario integer,
foreign key (id_usuario) references usuarios(id) on delete cascade
);

create table comentarios (
id integer auto_increment primary key,
conteudo text not null,
comentado_em timestamp default current_timestamp,
id_usuario integer,
id_post integer,
parent_comment_id integer,
foreign key (id_usuario) references usuarios(id) on delete cascade,
foreign key (id_post) references posts(id) on delete cascade,
foreign key (parent_comment_id) references comentarios(id) on delete cascade
);

create table tag (
    id integer auto_increment primary key,
    nome varchar(50) not null unique
);

create table postagens_tags (
	id_post integer,
    id_tag integer,
    primary key (id_post, id_tag),
    foreign key (id_post) references posts(id) on delete cascade,
    foreign key (id_tag) references tag(id) on delete cascade
);

create table revoked_token (
id integer auto_increment primary key,
token varchar(500) not null,
revoked_at datetime default current_timestamp,
expires_at datetime
);

insert into tag value(1, "teste");