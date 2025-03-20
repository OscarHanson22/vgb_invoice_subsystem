use ohanson5;

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
	uuid varchar(50) not null, 
    name varchar(100) not null, 
    street varchar(100) not null, 
    city varchar(50) not null, 
    state varchar(50) not null, 
    zip varchar(50) not null, 
    personId int not null, 
    foreign key (personId) references Person(personId)
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

insert into Person(personId,uuid,firstName,lastName,phone) values (1,'c4147f3a-029c-4c8e-8710-3b29a02019d3','Josh','Terminator','111-111-1111');
insert into Person(personId,uuid,firstName,lastName,phone) values (2,'17a2a995-6555-46e2-8630-bfd3d8765d78','Eminem','Boseman','222-222-2222');
insert into Person(personId,uuid,firstName,lastName,phone) values (3,'365ecbd2-ed99-4cd6-8d63-d9da8c9caa89','Michael','Starburst','333-333-3333');
insert into Person(personId,uuid,firstName,lastName,phone) values (4,'ef1e65c3-38c4-49ba-b25d-d6696e1918b4','Ermias','Wolde','444-444-4444');
insert into Person(personId,uuid,firstName,lastName,phone) values (5,'ee444afd-39d1-4ed6-b611-7f5a8db842f8','Oscar','Hanson','555-555-5555');

insert into Email(emailId,personId,email) values (1,1,'joshtheterminator@yahoo.com');
insert into Email(emailId,personId,email) values (2,2,'eminemnottherapper@gmail.com');
insert into Email(emailId,personId,email) values (3,3,'michael33@aol.com');
insert into Email(emailId,personId,email) values (4,3,'michael33clone@aol.com');
insert into Email(emailId,personId,email) values (5,4,'ewolde2@huskers.unl.edu');
insert into Email(emailId,personId,email) values (6,5,'ohanson5@huskers.unl.edu');



