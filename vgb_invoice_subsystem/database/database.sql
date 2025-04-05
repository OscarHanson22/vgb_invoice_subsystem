-- Author: Ermias Wolde
-- Date: Apr 4, 2025
-- Purpose:

-- use ohanson5;

drop table if exists InvoiceItem;
drop table if exists Invoice;
drop table if exists Item;
drop table if exists Company;
drop table if exists Email;
drop table if exists Person;
drop table if exists Address;
drop table if exists Zipcode;
drop table if exists State;

create table Person (
	personId int not null primary key auto_increment,
	uuid varchar(50) unique not null, 
	firstName varchar(50) not null, 
	lastName varchar(50) not null, 
	phoneNumber varchar(50) not null
);

create table Email (
	emailId int not null primary key auto_increment,
	email varchar(100) unique not null, 
	personId int not null, 
	foreign key (personId) references Person(personId)
);

create table State (
	stateId int not null primary key auto_increment,
	stateName varchar(100) not null
);

create table Zipcode (
	zipcodeId int not null primary key auto_increment,
    stateId int not null,
	zipcode varchar(50),
	foreign key (stateId) references State(stateId)
);

create table Address (
	addressId int not null primary key auto_increment,
	street varchar(100) not null, 
	city varchar(50) not null, 
	zipcodeId int not null,
	foreign key (zipcodeId) references Zipcode(zipcodeId)
);

create table Company (
	companyId int not null primary key auto_increment, 
	contactId int not null, 
	addressId int not null,
	uuid varchar(50) unique not null, 
	name varchar(100) not null, 
	foreign key (contactId) references Person(personId),
	foreign key (addressId) references Address(addressId)
);

create table Item (
	itemId int not null primary key auto_increment,
	uuid varchar(50) not null, 
	name varchar(200) not null, 
	discriminator varchar(100) not null, 
	equipmentModelNumber varchar(100), 
	equipmentRetailPrice double, 
	materialUnit varchar(100), 
	materialPricePerUnit double
);

create table Invoice (
	invoiceId int not null primary key auto_increment,
	uuid varchar(50) unique not null, 
	date varchar(50) not null, 
	customerId int not null, 
	salespersonId int not null,  
	foreign key (customerId) references Company(companyId),
	foreign key (salespersonId) references Person(personId)
);

create table InvoiceItem (
	-- if it changes from sale to sale add to II.
	invoiceItemId int not null primary key auto_increment, 
	invoiceId int not null,
	itemId int not null, 
    equipmentDiscriminator varchar(50), 
    leaseAmount double,
	leaseStartDate varchar(50),
	leaseEndDate varchar(50),
    rentAmount double, 
	rentedHours int,
    contractAmount double,
	contractSubcontractorId int,
	materialQuantity int,
	foreign key (invoiceId) references Invoice(invoiceId), 
	foreign key (itemId) references Item(itemId),
    foreign key (contractSubcontractorId) references Company(companyId)
);

insert into Person(personId,uuid,firstName,lastName,phoneNumber) values (1,'c4147f3a-029c-4c8e-8710-3b29a02019d3','Josh','Terminator','111-111-1111');
insert into Person(personId,uuid,firstName,lastName,phoneNumber) values (2,'17a2a995-6555-46e2-8630-bfd3d8765d78','Eminem','Boseman','222-222-2222');
insert into Person(personId,uuid,firstName,lastName,phoneNumber) values (3,'365ecbd2-ed99-4cd6-8d63-d9da8c9caa89','Michael','Starburst','333-333-3333');
insert into Person(personId,uuid,firstName,lastName,phoneNumber) values (4,'ef1e65c3-38c4-49ba-b25d-d6696e1918b4','Ermias','Wolde','444-444-4444');
insert into Person(personId,uuid,firstName,lastName,phoneNumber) values (5,'ee444afd-39d1-4ed6-b611-7f5a8db842f8','Oscar','Hanson','555-555-5555');

insert into Email(emailId,personId,email) values (1,1,'joshtheterminator@yahoo.com');
insert into Email(emailId,personId,email) values (2,2,'eminemnottherapper@gmail.com');
insert into Email(emailId,personId,email) values (3,3,'michael33@aol.com');
insert into Email(emailId,personId,email) values (4,3,'michael33clone@aol.com');
insert into Email(emailId,personId,email) values (5,4,'ewolde2@huskers.unl.edu');
insert into Email(emailId,personId,email) values (6,5,'ohanson5@huskers.unl.edu');

-- Address
insert into State(stateName) values ('Nebraska');
insert into Zipcode(zipcodeId, zipcode, stateId) values (67463, '67463', 1);  -- assuming stateId for Nebraska is 1
insert into Address(street, city, zipcodeId) values ('1234 Joe Street', "Omaha", 67463);

insert into State(stateName) values ('Colorado');
insert into Zipcode(zipcodeId, zipcode, stateId) values (68943, '68943', 2);  -- assuming stateId for Nebraska is 1
insert into Address(street, city, zipcodeId) values ('543 O Street', "Denver", 68943);

insert into State(stateName) values ('California');
insert into Zipcode(zipcodeId, zipcode, stateId) values (20442, '20442', 2);  -- assuming stateId for Nebraska is 1
insert into Address(street, city, zipcodeId) values ('543 O Street', "San Diego", 20442);

-- STEP 1: Insert States
insert into State (stateName) values 
('NE'), -- stateId = 1
('VE'), -- 2
('WI'), -- 3
('CA'), -- 4
('TX'); -- 5

-- STEP 2: Insert Zipcodes (linked to states)
insert into Zipcode (zipcodeId, zipcode, stateId) values
(68848, '68848', 1),
(77889, '77889', 2),
(12345, '12345', 3),
(99900, '99900', 4),
(9876,  '09876', 5);  -- use 9876 or '09876' depending on type (assume INT for now)

-- STEP 3: Insert Addresses (store city as text)
insert into Address (street, city, zipcodeId) values
('1111 A Street', 'A City', 68848),    -- addressId = 1
('2222 B Ave',    'B City', 77889),    -- addressId = 2
('3333 C Lane',   'C City', 12345),    -- addressId = 3
('4444 D Trail',  'D City', 99900),    -- addressId = 4
('5555 E Boulevard', 'E City', 9876);  -- addressId = 5

-- STEP 4: Insert Companies (link to personId and addressId)
insert into Company (companyId,contactId,uuid,name,addressId) values
(1,1,'4c141f07-3e8c-4d77-abc4-3e2058fb7e30','Company A',1),
(2,2,'b6677ab8-f204-41ec-a297-406b89dd56b6','Company B',2),
(3,3,'85f7ab31-8118-4100-8d08-2e154da6e734','Company C',3),
(4,4,'aeb3835b-e365-46f1-84cc-a7dffb67bb86','Company D',4),
(5,5,'dc94e6f3-ef25-4ca9-8c42-50a72170d51b','Company E',5);

insert into Item(itemId,uuid,discriminator,name,materialUnit,materialPricePerUnit) values (1,'4c141f07-3e8c-4d77-abc4-3e2058fb7e30','Material','concrete','lbs','2.0');
insert into Item(itemId,uuid,discriminator,name) values (2,'b6677ab8-f204-41ec-a297-406b89dd56b6','Contract','tiling');
insert into Item(itemId,uuid,discriminator,name,equipmentModelNumber,equipmentRetailPrice) values (3,'85f7ab31-8118-4100-8d08-2e154da6e734','Equipment','shovel','856AD',25.0);
insert into Item(itemId,uuid,discriminator,name,equipmentModelNumber,equipmentRetailPrice) values (4,'aeb3835b-e365-46f1-84cc-a7dffb67bb86','Equipment','jackhammer','XXXC1',650.0);
insert into Item(itemId,uuid,discriminator,name,materialUnit,materialPricePerUnit) values (5,'dc94e6f3-ef25-4ca9-8c42-50a72170d51b','Material','m&ms','bags','5.0');

insert into Invoice(invoiceId,customerId,salespersonId,uuid,date) values 
(1,1,5,'8a9bff38-24af-4a0c-b425-bf3a6fa64746','2025-01-13'),
(2,2,5,'4f6adb47-2fdd-43b5-8ad0-c10bb4116e52','2025-02-16'),
(3,3,5,'c89f1367-ce43-459e-8988-7e4d8cce83ce','2025-02-12'),
(4,5,5,'781399c6-a2a7-4826-8b51-a63697c40118','2025-04-01'),
(5,5,4,'ffc4c7d7-5ec3-4c48-ad9a-f460f6e3e80a','2025-04-02');

insert into InvoiceItem(invoiceItemId,invoiceId,itemId,materialQuantity) values 
(1,1,1,10), 
(4,1,1,20);
insert into InvoiceItem(invoiceItemId,invoiceId,itemId,contractAmount,contractSubcontractorId) values (2,2,2,10501.0,1);
insert into InvoiceItem(invoiceItemId,invoiceId,itemId,equipmentDiscriminator) values (3,3,3,'Purchase'), (5,3,3,'Purchase');