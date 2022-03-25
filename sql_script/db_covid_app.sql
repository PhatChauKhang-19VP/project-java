create database db_covid_app
go
use db_covid_app
go

--use master; 
--drop database db_covid_app

--- TẠO BẢNG VÀ DỮ LIỆU CHO CÁC ĐƠN VỊ HÀNH CHÍNH Ở VIỆT NAM ---

-- create PROVINCES table
create table PROVINCES (
	code nvarchar(20) NOT NULL,
	name nvarchar(255) NOT NULL,
	name_en nvarchar(255) NULL,
	full_name nvarchar(255) NOT NULL,
	full_name_en nvarchar(255) NULL,
	code_name nvarchar(255) NULL,
	constraint provinces_pkey PRIMARY KEY (code)
);
go
-- create DISTRICTS table
create table DISTRICTS (
	code nvarchar(20) NOT NULL,
	name nvarchar(255) NOT NULL,
	name_en nvarchar(255) NULL,
	full_name nvarchar(255) NULL,
	full_name_en nvarchar(255) NULL,
	code_name nvarchar(255) NULL,
	province_code nvarchar(20) NULL,
	constraint districts_pkey PRIMARY KEY (code)
);
go

-- DISTRICTS foreign keys
alter table DISTRICTS add constraint districts_province_code_fkey FOREIGN KEY (province_code) REFERENCES PROVINCES(code);
go

-- create WARDS table
create table WARDS (
	code nvarchar(20) NOT NULL,
	name nvarchar(255) NOT NULL,
	name_en nvarchar(255) NULL,
	full_name nvarchar(255) NULL,
	full_name_en nvarchar(255) NULL,
	code_name nvarchar(255) NULL,
	district_code nvarchar(20) NULL,
	constraint wards_pkey PRIMARY KEY (code)
);
go

-- WARDS foreign keys
alter table WARDS add constraint wards_district_code_fkey FOREIGN KEY (district_code) REFERENCES DISTRICTS(code);

--- TẠO BẢNG VÀ DATA CHO NGƯỜI DÙNG ---

create table LOGIN_INFOS (
	username varchar(50) not null,
	password varchar(128),
	account_status varchar(20) not null,
	user_type varchar(20) not null,
	constraint login_infos_pkey primary key (username),
	constraint login_infos_chk_valid_account_status check (account_status = 'UNLOGIN' or account_status = 'ACTIVE' or account_status = 'INACTIVE'),
	constraint login_infos_chk_valid_user_type check (user_type = 'ADMIN' or user_type = 'MANAGER' or user_type = 'PATIENT')
);
go

create FUNCTION dbo.fn_chk_valid_address (
    @address_province_code nvarchar(20),
	@address_district_code nvarchar(20),
	@address_ward_code nvarchar(20)
)
RETURNS VARCHAR(10)
AS
BEGIN
    IF EXISTS (SELECT *
		from WARDS as w 
			join DISTRICTS as d on w.district_code = d.code
			join PROVINCES as p on d.province_code = p.code
		where w.code = @address_ward_code 
			and d.code  = @address_district_code
			and p.code = @address_province_code
	)
        return 'True'
    return 'False'
END
go

create table ADMINS (
	username varchar(50) not null,
	name nvarchar(255) not null,
	constraint admins_pkey primary key (username)
)
go

alter table ADMINS with check add constraint admins_foreign_key foreign key (username) references ADMINS(username) 
go

create table MANAGERS (
	username varchar(50) not null,
	name nvarchar(255) not null,
	constraint managers_pkey primary key (username)
)
go

alter table MANAGERS with check add constraint managers_foreign_key foreign key (username) references MANAGERS(username) 
go

create table TREATMENT_LOCATIONS (
	treatment_location_code varchar(20) not null,
	name nvarchar(255) not null,
	capacity int not null,
	current_room int not null,

	constraint treatment_locations_pkey primary key (treatment_location_code),
	constraint treatment_locations_chk_valid_figure check (capacity >= 0 and current_room >= 0 and current_room <= capacity)
)
go

create table PATIENTS (
	username varchar(50) not null,	
	name nvarchar(255) not null,
	f_status int not null,
	date_of_birth date not null,
	address_province_code nvarchar(20) not null,
	address_district_code nvarchar(20) not null,
	address_ward_code nvarchar(20) not null,
	address_line nvarchar(255) not null,
	treatment_location_code varchar(20) not null,
	constraint patients_pkey primary key (username),
	constraint patients_chk_valid_f_status check (f_status >= 0),	
	constraint patients_chk_valid_date_of_birth check (datediff(day, date_of_birth, getdate()) >= 0),
)
go

alter table PATIENTS with check add constraint patients_fkey_to_login_infos foreign key (username) references LOGIN_INFOS(username)
alter table PATIENTS with check add constraint patients_fkey_to_provinces foreign key (address_province_code) references PROVINCES(code)
alter table PATIENTS with check add constraint patients_fkey_to_districts foreign key (address_district_code) references DISTRICTS(code)
alter table PATIENTS with check add constraint patients_fkey_to_wards foreign key (address_ward_code) references WARDS(code)
alter table PATIENTS with check add constraint patients_fkey_to_treatment_locations foreign key (treatment_location_code) references TREATMENT_LOCATIONS(treatment_location_code)
alter table PATIENTS with check add constraint patients_chk_valid_address check (dbo.fn_chk_valid_address(address_province_code, address_district_code, address_ward_code) = 'True')
go

create table CLOSE_CONTACTS(
	f_upper_username varchar(50),
	f_lower_username varchar(50)

	constraint close_contacts_pkey primary key(f_upper_username,f_lower_username),
)
go

alter table CLOSE_CONTACTS with check add constraint close_contacts_upper_fkey_to_patients foreign key (f_upper_username) references PATIENTS(username)
alter table CLOSE_CONTACTS with check add constraint close_contacts_lower_fkey_to_patients foreign key (f_lower_username) references PATIENTS(username)
go

create table PRODUCTS (
	product_id varchar(20) not null,
	name nvarchar(255) not null,
	img_src nvarchar(255) not null,
	unit nvarchar(20) not null,
	price float not null,

	constraint products_pkey primary key (product_id),
	constraint products_chk_valid_unit check (unit = 'X' or unit = 'Y' or unit = 'Z')
)
go

create table PACKAGES (
	package_id varchar(20) not null,
	name nvarchar(255) not null,
	img_src nvarchar(255) not null,
	purchased_amount_limit int not null,
	time_limit int not null, -- week
	price float not null,

	constraint packages_pkey primary key (package_id),
	constraint packages_chk_valid_purchased_amount_limit check (purchased_amount_limit > 0 and purchased_amount_limit <= 10),
	constraint packages_chk_valid_time_limit check (time_limit > 0)
)
go

create table PRODUCTS_IN_PACKAGES (
	product_id varchar(20) not null,
	package_id varchar(20) not null,
	quantity int not null,

	constraint products_in_packages_pkey primary key (product_id, package_id),
	constraint products_in_packages_chk_valid_quantity check (quantity > 0)
)
go

alter table PRODUCTS_IN_PACKAGES with check add constraint products_in_packages_fkey_to_products foreign key (product_id) references PRODUCTS(product_id)
alter table PRODUCTS_IN_PACKAGES with check add constraint products_in_packages_fkey_to_packages foreign key (package_id) references PACKAGES(package_id)

create table HISTORIES (
	history_id int identity(1,1) primary key,
	belong_to_username varchar(50) not null,
	at_time datetime not null,
	history_content nvarchar(255) not null
)
go

alter table HISTORIES with check add constraint histories_fkey_to_login_infos foreign key (belong_to_username) references LOGIN_INFOS(username)

create table ORDERS (
	order_id int identity(1,1) not null,
	belong_to_username varchar(50) not null,
	at_datetime datetime not null,
	total float,

	constraint oders_pkey primary key (order_id),
	constraint oders_chk_valid_total check (total >= 0)
)
go

alter table ORDERS with check add constraint orders_fkey_to_patients foreign key (belong_to_username) references PATIENTS(username)

create table ORDERS_DETAILS(
	order_id int not null,
	package_id varchar(20) not null,
	quantity int not null,
	price float not null,
	sub_total float not null,
	
	constraint orders_details_pkey primary key (order_id, package_id),
	constraint orders_chk_valid_quantity check (quantity > 0),
	constraint orders_chk_valid_price check (price > 0),
	constraint orders_chk_valid_sub_total check (sub_total = quantity * price)
)
go

alter table ORDERS_DETAILS with check add constraint orders_details_fkey_to_orders foreign key (order_id) references ORDERS(order_id)
alter table ORDERS_DETAILS with check add constraint orders_details_fkey_to_packages foreign key (package_id) references PACKAGES(package_id)

create table BANK_ACCOUNTS (
	bank_account int identity(1,1) not null,
	password varchar(128) null,
	belong_to_username varchar(50) not null,
	balance float not null,
	creaeted_at datetime not null,
	minimum_payment float not null,

	constraint bank_accounts_pkey primary key (bank_account),
	constraint bank_accounts_chk_valid_balance check (balance >= 0),
	constraint bank_accounts_chk_valid_minimum_payment check (minimum_payment > 0)
)
go 

alter table BANK_ACCOUNTS with check add constraint bank_accounts_fkey foreign key (belong_to_username) references LOGIN_INFOS(username)
go

create FUNCTION dbo.fn_chk_amount_greater_than_minimum_payment (
    @amout float,
	@bank_account int
)
RETURNS VARCHAR(10)
AS
BEGIN
    IF @amout > (select ba.minimum_payment from BANK_ACCOUNTS as ba where ba.bank_account = @bank_account)
        return 'True'
    return 'False'
END
go

create table PAYMENTS_HISTORIES (
	payment_id int identity(1,1) not null, 
	paid_by_bank_account int not null,
	amount float not null,
	paid_at datetime not null,
	old_debit_balance float not null,
	new_debit_balance float not null,
	old_balance float not null,
	new_balance float not null,

	constraint payments_histories_pkey primary key (payment_id),
	constraint payments_histories_check_valid_balance check (old_debit_balance >= 0 and new_debit_balance >=0 and old_balance >= 0 and new_balance >= 0)
)
go

alter table PAYMENTS_HISTORIES with check add constraint payments_histories_fkey_to_bank_accounts foreign key (paid_by_bank_account) references BANK_ACCOUNTS(bank_account)
alter table PAYMENTS_HISTORIES with check add constraint payments_histories_chk_amount_greater_than_minimum_payment check (dbo.fn_chk_amount_greater_than_minimum_payment(amount, paid_by_bank_account) = 'True')
--SELECT w.code as 'w code', d.code as 'd code', p.code as 'p code'
--		from WARDS as w 
--			join DISTRICTS as d on w.district_code = d.code
--			join PROVINCES as p on d.province_code = p.code
--		where w.name = N'Hòa Thành'

--insert into PATIENTS(username, name, f_status, date_of_birth, address_province_code, address_district_code, address_ward_code, address_line)
--	values 
--	('phat', 'Ngo Minh Phat', 1, '2001-06-01', '54', '564', '22243', N'Căn nhà nhỏ')
--go
