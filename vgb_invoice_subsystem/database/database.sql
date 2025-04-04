use ohanson5;

drop table if exists InvoiceItem;
drop table if exists Invoice;
drop table if exists Item;
drop table if exists Company;
drop table if exists Email;
drop table if exists Person;
drop table if exists Address;

create table Person (
	personId int not null primary key auto_increment,
    uuid varchar(50) not null, 
	firstName varchar(50) not null, 
	lastName varchar(50) not null, 
	phoneNumber varchar(50) not null
);

create table Email (
	emailId int not null primary key auto_increment,
	email varchar(100) not null, 
    personId int not null, 
    foreign key (personId) references Person(personId)
);

create table Company (
	companyId int not null primary key auto_increment, 
	contactId int not null, 
	uuid varchar(50) not null, 
    name varchar(100) not null, 
    street varchar(100) not null, 
    city varchar(50) not null, 
    state varchar(50) not null, 
    zip varchar(50) not null, 
    foreign key (contactId) references Person(personId)
);

create table Item (
	 itemId int not null primary key auto_increment,
     uuid varchar(50) not null, 
     name varchar(200) not null, 
     discriminator varchar(100) not null, 
     equipmentModelNumber varchar(100), 
     equipmentRetailPrice double, 
     materialUnit varchar(100), 
     materialPricePerUnit double, 
     materialQuantity int, 
     contractSubcontractorId int, 
     contractAmount double,
     foreign key (contractSubcontractorId) references Company(companyId)
);

create table Invoice (
	invoiceId int not null primary key auto_increment,
	uuid varchar(50) not null, 
    date varchar(50) not null, 
    customerId int not null, 
	salespersonId int not null, 
    cost double not null, 
    tax double not null, 
    total double not null, 
    foreign key (customerId) references Company(companyId),
    foreign key (salespersonId) references Person(personId)
);

create table InvoiceItem (
	invoiceItemId int not null primary key auto_increment, 
    invoiceId int not null,
    itemId int not null, 
	cost double not null, 
    tax double not null, 
    total double not null, 
    foreign key (invoiceId) references Invoice(invoiceId), 
    foreign key (itemId) references Item(itemId)
);

create table Address (
	addressId not null primary key auto_increment,
	city varchar(50) not null, 
	state varchar(50) not null, 
	zip varchar(50) not null, 
	personId int not null,
	foreign key (personId) references Address(addressId)
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

insert into Company(companyId,contactId,uuid,name,street,city,state,zip) values (1,1,'4c141f07-3e8c-4d77-abc4-3e2058fb7e30','Company A','1111 A Street','A City','NE','68848');
insert into Company(companyId,contactId,uuid,name,street,city,state,zip) values (2,2,'b6677ab8-f204-41ec-a297-406b89dd56b6','Company B','2222 B Ave','B City','VE','77889');
insert into Company(companyId,contactId,uuid,name,street,city,state,zip) values (3,3,'85f7ab31-8118-4100-8d08-2e154da6e734','Company C','3333 C Lane','C City','WI','12345');
insert into Company(companyId,contactId,uuid,name,street,city,state,zip) values (4,4,'aeb3835b-e365-46f1-84cc-a7dffb67bb86','Company D','4444 D Trail','D City','CA','99900');
insert into Company(companyId,contactId,uuid,name,street,city,state,zip) values (5,5,'dc94e6f3-ef25-4ca9-8c42-50a72170d51b','Company E','5555 E Boulevard','E City','TX','09876');

insert into Item(itemId,uuid,discriminator,name,materialUnit,materialPricePerUnit) values (1,'4c141f07-3e8c-4d77-abc4-3e2058fb7e30','Material','concrete','lbs','2.0');
insert into Item(itemId,uuid,discriminator,name,contractSubcontractorId,contractAmount) values (2,'b6677ab8-f204-41ec-a297-406b89dd56b6','Contract','tiling',5,10501.0);
insert into Item(itemId,uuid,discriminator,name,equipmentModelNumber,equipmentRetailPrice) values (3,'85f7ab31-8118-4100-8d08-2e154da6e734','Equipment','shovel','856AD',25.0);
insert into Item(itemId,uuid,discriminator,name,equipmentModelNumber,equipmentRetailPrice) values (4,'aeb3835b-e365-46f1-84cc-a7dffb67bb86','Equipment','jackhammer','XXXC1',650.0);
insert into Item(itemId,uuid,discriminator,name,materialUnit,materialPricePerUnit,materialQuantity) values (5,'dc94e6f3-ef25-4ca9-8c42-50a72170d51b','Material','m&ms','bags','5.0',10);

insert into Invoice(invoiceId,customerId,salespersonId,uuid,date,cost,tax,total) values (1,1,5,'8a9bff38-24af-4a0c-b425-bf3a6fa64746','2025-01-13',0.0,0.0,0.0);
insert into Invoice(invoiceId,customerId,salespersonId,uuid,date,cost,tax,total) values (2,2,5,'4f6adb47-2fdd-43b5-8ad0-c10bb4116e52','2025-02-16',0.0,0.0,0.0);
insert into Invoice(invoiceId,customerId,salespersonId,uuid,date,cost,tax,total) values (3,3,5,'c89f1367-ce43-459e-8988-7e4d8cce83ce','2025-02-12',0.0,0.0,0.0);

insert into InvoiceItem(invoiceItemId,invoiceId,itemId,cost,tax,total) values (1,1,1,0.0,0.0,0.0);
insert into InvoiceItem(invoiceItemId,invoiceId,itemId,cost,tax,total) values (2,2,2,0.0,0.0,0.0);
insert into InvoiceItem(invoiceItemId,invoiceId,itemId,cost,tax,total) values (3,3,3,0.0,0.0,0.0);
