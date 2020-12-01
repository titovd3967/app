CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	username VARCHAR(20) NOT NULL,
	password VARCHAR(250) NOT NULL,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	address VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS CUSTOMER_ROLES (
    customer_id VARCHAR(20) NOT NULL,
    role_id VARCHAR(20) NOT NULL,
    foreign key (customer_id) references customer(id),
    foreign key (role_id) references role(id)
);

CREATE TABLE IF NOT EXISTS item (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(250) NOT NULL,
	price INT NOT NULL,
	quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	customer_id INT NOT NULL,
	courier_id INT,
	status VARCHAR(20) NOT NULL,
	total_price INT NOT NULL,
	foreign key (customer_id) references customer(id)
);

CREATE TABLE IF NOT EXISTS order_item (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	order_id VARCHAR(250) NOT NULL,
	item_id VARCHAR(250) NOT NULL,
	count INT NOT NULL,
	foreign key (order_id) references orders(id),
	foreign key (item_id) references item(id)
);