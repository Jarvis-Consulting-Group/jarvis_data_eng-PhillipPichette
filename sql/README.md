# Introduction

This project is designed to allow a user to manipulate a database and test around with queries; to learn SQL and how to use it. I used bash, git, docker and SQL. I set up a docker container that runs psql and then connect to the psql database through an IDE like DBeaver. I then went through all the SQL query exercises and solved them. 

# SQL Queries

###### Table Setup (DDL)

```sql
CREATE SCHEMA cd

CREATE TABLE cd.members
    (
       memid integer NOT NULL, 
       surname character varying(200) NOT NULL, 
       firstname character varying(200) NOT NULL, 
       address character varying(300) NOT NULL, 
       zipcode integer NOT NULL, 
       telephone character varying(20) NOT NULL, 
       recommendedby integer,
       joindate timestamp NOT NULL,
       CONSTRAINT members_pk PRIMARY KEY (memid),
       CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE SET NULL
    );

CREATE TABLE cd.facilities
    (
       facid integer NOT NULL, 
       name character varying(100) NOT NULL, 
       membercost numeric NOT NULL, 
       guestcost numeric NOT NULL, 
       initialoutlay numeric NOT NULL, 
       monthlymaintenance numeric NOT NULL, 
       CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );

CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL, 
       facid integer NOT NULL, 
       memid integer NOT NULL, 
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid),
       CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );

```
###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members;
```

###### Question 2: Add facility

```sql
INSERT INTO cd.facilities (facid,name,membercost,guestcost,initialoutlay,monthlymaintenance) 
VALUES (9,'Spa',20,30,100000,800);

```

###### Question 3: Add facility, incrementing ID

```sql
INSERT INTO cd.facilities (facid,name,membercost,guestcost,initialoutlay,monthlymaintenance) 
VALUES ((SELECT COUNT(*) FROM cd.facilities),'Spa',20,30,100000,800);

```

###### Question 4: Update row

```sql
UPDATE cd.facilities SET initialoutlay=10000 WHERE facid=1;

```

###### Question 5: Update multiple rows

```sql
UPDATE cd.facilities SET 
membercost=(SELECT membercost FROM cd.facilities WHERE facid = 0)*1.1,
guestcost=(SELECT guestcost FROM cd.facilities WHERE facid = 0)*1.1
WHERE facid = 1;

```

###### Question 6: Delete all rows from table

```sql
DELETE FROM cd.bookings;

```

###### Question 7: Delete 1 row from table

```sql
DELETE FROM cd.members WHERE memid=37;

```

###### Question 8: Control which rows are retrieved - part 2

```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities WHERE membercost > 0
AND membercost < monthlymaintenance/50;

```

###### Question 9: Basic string searches

```sql
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

```

###### Question 10: Matching against multiple possible values

```sql
SELECT * FROM cd.facilities WHERE facid IN (1,5);

```

###### Question 11: Working with dates

```sql
SELECT memid, surname, firstname, joindate FROM cd.members WHERE joindate >= '2012/09/01';

```

###### Question 12: Combining results from multiple queries

```sql
SELECT surname FROM cd.members UNION SELECT name FROM cd.facilities;

```

###### Question 13: Retrieve the start times of members' bookings

```sql
SELECT books.starttime FROM cd.bookings books INNER JOIN 
cd.members members ON members.memid = books.memid
WHERE members.firstname = 'David' AND members.surname = 'Farrell';

```

###### Question 14: Work out the start times of bookings for tennis courts

```sql
SELECT books.starttime AS start, fac.name FROM cd.bookings books
INNER JOIN cd.facilities fac ON fac.facid = books.facid
WHERE fac.name LIKE 'Tennis Court%'
AND books.starttime >= '2012/09/21'
AND books.starttime < '2012-09-22';

```

###### Question 15: Produce a list of all members, along with their recommender

```sql
SELECT 
mem1.firstname AS memfname, mem1.surname AS memsname, 
mem2.firstname AS recfname, mem2.surname AS recsname 
FROM cd.members mem1 
LEFT OUTER JOIN cd.members mem2 ON mem1.recommendedby = mem2.memid
ORDER BY (mem1.surname,mem1.firstname);

```

###### Question 16: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT mem2.firstname AS firstname, mem2.surname AS surname
FROM cd.members mem1 INNER JOIN cd.members mem2 ON mem2.memid = mem1.recommendedby
ORDER BY surname, firstname;

```

###### Question 17: Produce a list of all members, along with their recommender, using no joins.

```sql
SELECT DISTINCT mem1.firstname || ' ' || mem1.surname as member,
(SELECT mem2.firstname || ' ' || mem2.surname as recommender 
FROM cd.members mem2 WHERE mem2.memid = mem1.recommendedby)
FROM cd.members mem1 ORDER BY member;

```

###### Question 18: Count the number of recommendations each member makes.

```sql
SELECT recommendedby, COUNT(*) FROM cd.members
WHERE recommendedby IS NOT NULL GROUP BY recommendedby
ORDER BY recommendedby;

```

###### Question 19: List the total slots booked per facility

```sql
SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings GROUP BY facid ORDER BY facid;

```

###### Question 20: List the total slots booked per facility in a given month

```sql
SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings 
WHERE starttime < '2012/10/01' AND starttime >= '2012/09/01' 
GROUP BY facid ORDER BY SUM(slots);

```

###### Question 21: List the total slots booked per facility per month

```sql
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots) AS "Total Slots" FROM cd.bookings 
WHERE EXTRACT(YEAR FROM starttime) = 2012 
GROUP BY facid, month ORDER BY facid, month;

```

###### Question 22: Find the count of members who have made at least one booking

```sql
SELECT COUNT(DISTINCT memid) FROM cd.bookings;

```

###### Question 23: List each member's first booking after September 1st 2012

```sql
SELECT 
mem.surname AS surname, 
mem.firstname AS firstname, 
mem.memid AS memid, 
MIN(book.starttime) AS starttime
FROM cd.members mem INNER JOIN cd.bookings book ON mem.memid = book.memid
WHERE book.starttime > '2012/09/01'
GROUP BY mem.memid ORDER BY mem.memid;

```

###### Question 24: Produce a list of member names, with each row containing the total member count

```sql
SELECT (select count(*) from cd.members) as count, firstname, surname FROM cd.members 
ORDER BY joindate;

```

###### Question 25: Produce a numbered list of members

```sql
SELECT ROW_NUMBER() OVER(), firstname, surname FROM cd.members ORDER BY joindate;

```

###### Question 26: Output the facility id that has the highest number of slots booked, again

```sql
SELECT facid, total FROM
(SELECT facid, SUM(slots) total, RANK() OVER(ORDER BY sum(slots) DESC) FROM cd.bookings GROUP BY facid) as groupbook
WHERE rank = 1;

```

###### Question 27: Format the names of members

```sql
SELECT surname || ', ' || firstname AS name FROM cd.members;

```

###### Question 28: Find telephone numbers with parentheses

```sql
SELECT memid, telephone FROM cd.members WHERE telephone LIKE '(___) ___-____';

```

###### Question 29: Count the number of members whose surname starts with each letter of the alphabet

```sql
SELECT SUBSTR(surname,1,1) AS initial, count(*) AS count
FROM cd.members GROUP BY initial ORDER BY initial;

```
