CREATE TABLE part (
  id serial PRIMARY KEY,
  name varchar(256),
  "number" varchar(256),
  vendor varchar(256),
  qty integer,
  shipped date,
  received date
);
