create table travako_server
(
    uuid          varchar(255) not null
        primary key,
    creation_date datetime(6) not null,
    server_key    varchar(255) not null,
    constraint travako_server_key_unique
        unique (server_key)
);

create table travako_scheduler_runner
(
    uuid                varchar(255) not null
        primary key,
    creation_date       datetime(6) not null,
    instance_state      varchar(255) not null,
    last_heartbeat_time datetime(6) null,
    runner_key          varchar(255) not null,
    server_uuid         varchar(255) not null,
    constraint travako_scheduler_runner_key_server_unique
        unique (runner_key, server_uuid),
    constraint travako_scheduler_runner_server_fk
        foreign key (server_uuid) references travako_server (uuid)
);

create table travako_job_instance
(
    uuid              varchar(255) not null
        primary key,
    creation_date     datetime(6) not null,
    job_key           varchar(255) not null,
    job_status        varchar(255) not null,
    last_running_time datetime(6) null,
    runner_uuid       varchar(255) null,
    server_uuid       varchar(255) not null,
    constraint travako_job_instance_key_server_unique
        unique (job_key, server_uuid),
    constraint travako_job_instance_runner_fk
        foreign key (runner_uuid) references travako_scheduler_runner (uuid),
    constraint travako_job_instance_server_fk
        foreign key (server_uuid) references travako_server (uuid)
);

create table travako_leader
(
    uuid               varchar(255) not null
        primary key,
    creation_date      datetime(6) not null,
    last_modified_date datetime(6) not null,
    runner_uuid        varchar(255) null,
    server_uuid        varchar(255) not null,
    constraint travako_leader_server_unique
        unique (server_uuid),
    constraint travako_leader_runner_fk
        foreign key (runner_uuid) references travako_scheduler_runner (uuid),
    constraint travako_leader_server_fk
        foreign key (server_uuid) references travako_server (uuid)
);

