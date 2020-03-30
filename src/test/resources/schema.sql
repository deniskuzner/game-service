create table teams(
    id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null, 
    url varchar2(40) not null, 
    name varchar2(40) not null);

create sequence sq_teams;

create table seasons(
	id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    year int(7) not null,
    league varchar2(10) not null
);

create sequence sq_seasons;

create table players(
    id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    team_id number not null, 
    url varchar2(40) not null,
    number varchar2(2) not null,
    name varchar2(40) not null,
    position varchar(2) not null,
    height varchar2(40) not null,
    weight int(7) not null,
    birth_date varchar2(40) not null,
    experience varchar2(40) not null,
    college varchar2(40) not null);
    
create sequence sq_players;
alter table players add constraint fk_players_team foreign key(team_id) references teams;
alter table players add constraint uq_players unique(team_id, number);

create table matches(
	id number primary key,
	created_on timestamp not null, 
    modified_on timestamp not null,
    
    url varchar2(40) not null,
    host_id number not null,
    guest_id number not null,
    result varchar2(10) not null,
    season_id number not null
);

create sequence sq_matches;
alter table matches add constraint fk_matches_host foreign key(host_id) references teams;
alter table matches add constraint fk_matches_guest foreign key(guest_id) references teams;
alter table matches add constraint fk_matches_season foreign key(season_id) references seasons;

create table play_by_play_actions(
	id number primary key,
    created_on timestamp not null,
    modified_on timestamp not null,
    
    quarter varchar2(10) not null,
    timestamp varchar2(10) not null,
    description varchar2(100) not null,
    sum_score varchar(10) not null,
    team_id number not null,
    match_id number not null
);

create sequence sq_play_by_play_actions;
alter table play_by_play_actions add constraint fk_play_by_play_actions_team foreign key(team_id) references teams;

create table foul_actions(
	id number primary key,
	
	foul_by_player_id number not null
);

alter table foul_actions add constraint fk_foul_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table foul_actions add constraint fk_foul_by_player foreign key(foul_by_player_id) references players;

create table miss_actions(
	id number primary key,
	
	miss_player_id number not null,
	block_player_id number not null
);

alter table miss_actions add constraint fk_miss_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table miss_actions add constraint fk_miss_player_id foreign key(miss_player_id) references players;
alter table miss_actions add constraint fk_block_player_id foreign key(block_player_id) references players;

create table point_actions(
	id number primary key,
	points int(7) not null,
	point_player_id number not null,
	assist_player_id number not null
);

alter table point_actions add constraint fk_point_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table point_actions add constraint fk_point_player_id foreign key(point_player_id) references players;
alter table point_actions add constraint fk_assist_player_id foreign key(assist_player_id) references players;

create table rebound_actions(
	id number primary key,
	player_id number not null
);

alter table rebound_actions add constraint fk_rebound_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table rebound_actions add constraint fk_player_id foreign key(player_id) references players;

create table substitution_actions(
	id number primary key,
	in_player_id number not null,
	out_player_id number not null
);

alter table substitution_actions add constraint fk_substitution_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table substitution_actions add constraint fk_in_player_id foreign key(in_player_id) references players;
alter table substitution_actions add constraint fk_out_player_id foreign key(out_player_id) references players;

create table turnover_actions(
	id number primary key,
	turnover_player_id number not null,
	steal_player_id number not null
);

alter table turnover_actions add constraint fk_turnover_actions foreign key(id) references play_by_play_actions on delete cascade;
alter table turnover_actions add constraint fk_turnover_player_id foreign key(turnover_player_id) references players;
alter table turnover_actions add constraint fk_steal_player_id foreign key(steal_player_id) references players;

create table player_stats(
	id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    match_id number not null,
    player_id number not null,
    field_goals int(7) not null,
    field_goal_attempts int(7) not null,
    field_goal_percentage decimal(4,3) not null,
    three_point_field_goals int(7) not null,
    three_point_field_goal_attempts int(7) not null,
    three_point_field_goal_percentage decimal(4,3) not null,
    free_throws int(7) not null,
    free_throw_attempts int(7) not null,
    free_throw_percentage decimal (4,3) not null,
    offensive_rebounds int(7) not null,
    defensive_rebounds int(7) not null,
    total_rebounds int(7) not null,
    assists int(7) not null,
    steals int(7) not null,
    blocks int(7) not null,
    turnovers int(7) not null,
    personal_fouls int(7) not null,
    points int(7) not null
);

create sequence sq_player_stats;
alter table player_stats add constraint fk_player_stats_player foreign key(player_id) references players;

create table team_stats(
	id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    match_id number not null,
    team_id number not null,
    field_goals int(7) not null,
    field_goal_attempts int(7) not null,
    field_goal_percentage decimal(4,3) not null,
    three_point_field_goals int(7) not null,
    three_point_field_goal_attempts int(7) not null,
    three_point_field_goal_percentage decimal(4,3) not null,
    free_throws int(7) not null,
    free_throw_attempts int(7) not null,
    free_throw_percentage decimal (4,3) not null,
    offensive_rebounds int(7) not null,
    defensive_rebounds int(7) not null,
    total_rebounds int(7) not null,
    assists int(7) not null,
    steals int(7) not null,
    blocks int(7) not null,
    turnovers int(7) not null,
    personal_fouls int(7) not null,
    points int(7) not null
);

create sequence sq_team_stats;
alter table team_stats add constraint fk_team_stats_team foreign key(team_id) references teams;

create table line_score_leaders(
	id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    match_id number not null,
    quarter varchar2(10) not null,
    player_id number not null,
    value int(7) not null,
    leader_type varchar2(20)
);

create sequence sq_line_score_leaders;
alter table line_score_leaders add constraint fk_line_score_leaders_player foreign key(player_id) references players;

create table scoring(
	id number primary key, 
    created_on timestamp not null, 
    modified_on timestamp not null,
    
    match_id number not null,
    quarter varchar2(10) not null,
    host_points int(7) not null,
    guest_points int(7) not null
);

create sequence sq_scoring;
alter table scoring add constraint fk_scoring_match foreign key(match_id) references matches;

