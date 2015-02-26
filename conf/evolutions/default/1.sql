# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "usertable" ("Name" VARCHAR(254) NOT NULL,"Address" VARCHAR(254) NOT NULL,"Company" VARCHAR(254) NOT NULL,"Email" VARCHAR(254) NOT NULL,"Password" VARCHAR(254) NOT NULL,"UserType" INTEGER NOT NULL,"Phone" VARCHAR(254) NOT NULL,"id" SERIAL NOT NULL PRIMARY KEY,"created" DATE NOT NULL,"updated" DATE NOT NULL);
create unique index "IDX_EMAIL" on "usertable" ("Email");

# --- !Downs

drop table "usertable";

