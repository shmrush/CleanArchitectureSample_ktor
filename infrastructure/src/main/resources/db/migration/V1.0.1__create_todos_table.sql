create table todos (
    id bigint(20) not null primary key auto_increment,
    user_id bigint(20) not null,
    title varchar(255) not null,
    description text not null,
    deadline_at datetime(6) not null,
    completed boolean not null default(false),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    index idx_todos_title(title),
    constraint fk_todos_user_id
        foreign key (user_id)
        references users(id)
        on delete cascade on update cascade
);
