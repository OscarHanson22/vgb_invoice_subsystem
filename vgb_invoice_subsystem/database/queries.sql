-- A query to retrieve the main attributes of each person (their UUID, and last/first name)

select uuid, firstName, lastName from Person;

-- A query to retrieve the major fields for every person including their email address(es)

select uuid, firstName, lastName, phoneNumber, email from Person person
join Email email on person.personId = email.personId;

-- A query to get the email addresses of a specific person

select uuid, firstName, lastName, phoneNumber, email from Person person
join Email email on person.personId = email.personId
where person.firstName = 'Michael';

-- A query to change the email address of a specific email record

update Email 
set email = 'newemailforoscar@gmail.com'
where emailId = 6;

-- A query (or series of queries) to remove a specific person record

delete from InvoiceItem where invoiceId = 3;
delete from Invoice where customerId = 3 or salespersonId = 3;
delete from Company where contactId = 3;
delete from Email where personId = 3;
delete from Person where personId = 3;

-- A query to get all the items on a specific invoice record

select * from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId;

-- A query to get all the items purchased by a specific customer

select * from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
where invoice.customerId = 2;

-- A query to find the total number of invoices for each customer even if they do not have any*

select invoice.customerId, count(invoice_item.invoiceItemId) as total_invoices from Invoice invoice 
left join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
group by invoice.customerId;

-- A query to find the total number of sales made by each salesperson; do not include anyone who has zero sales

select invoice.salespersonId, count(invoice_item.invoiceItemId) as total_sales from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
group by invoice.salespersonId;

-- A query to find the subtotal charge of all materials purchased in each invoice (hint: you can take an aggregate of a mathematical expression). Do not include taxes.

select invoice.invoiceId, sum(invoice_item.cost) as subtotal from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
join Item item on invoice_item.itemId = item.itemId
where item.discriminator = 'Material'
group by invoice.invoiceId;

-- A query to detect invalid data in a invoice as follows. When a customer buys a certain material, they buy a certain number of units. It should not be the case that they buy (say) 20 boxes of nails and another 30 boxes of nails. Instead there should be one record of 50 boxes of nails.
-- TODO

select invoice.invoiceId, item.materialQuantity from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
join Item item on invoice_item.itemId = item.itemId
where item.discriminator = 'Material'
group by invoice.invoiceId;

-- Write a query to find and report any invoice that includes multiple records of the same material. If your database design prevents such a situation, you should still write this query (but of course would never expect any results).
-- TODO
select item.name from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
join Item item on invoice_item.itemId = item.itemId;

select invoice.invoiceId, item.uuid as subtotal from Invoice invoice 
join InvoiceItem invoice_item on invoice.invoiceId = invoice_item.invoiceId
join Item item on invoice_item.itemId = item.itemId
where item.discriminator = 'Material'
group by invoice.invoiceId;
having 

-- Write a query to detect a potential instance of fraud where an employee makes a sale to their own company (the same person is the sales person as well as the customer's primary contact).

select person.personId as id, person.lastName as last_name, person.firstName as first_name, person.uuid as uuid from Invoice invoice
join Company company on invoice.customerId = company.companyId
join Person person on invoice.salespersonId = person.personId
where invoice.salespersonId = company.contactId;