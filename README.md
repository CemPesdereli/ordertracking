-- Create the database
DROP DATABASE IF EXISTS order_tracking;
CREATE DATABASE IF NOT EXISTS order_tracking;

-- Switch to the newly created database
USE order_tracking;

DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  `role` VARCHAR(255),
  shipping_address TEXT
);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  category VARCHAR(255),
  price DECIMAL(10,2) NOT NULL,
  stock_quantity INT NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS order_info;
CREATE TABLE order_info (  -- Using your preference
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  order_status VARCHAR(255) NOT NULL,
  estimated_delivery_date DATE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_info_id BIGINT NOT NULL,  -- Foreign key referencing OrderInfo
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  FOREIGN KEY (order_info_id) REFERENCES order_info(id),  -- Update foreign key reference
  FOREIGN KEY (product_id) REFERENCES product(id)
  ON DELETE CASCADE
);




Above is sql for creating the database.

JWT token authorization is used in this project.
After starting the spring boot application first you need to register with /api/v1/auth/register       after registration you will receive a token and you must use this token in authorization tab on Postman. Select Bearer token and paste the token whenever you want to send a request to an endpoint.
Once you are registered next time you wish to log in you can use /api/v1/auth/authenticate   to authenticate yourself and get a token.
As a customer you can order products. First you need to send a purchase request to  /cart  endpoint with desired products and quantities. Once you do this related OrderItem and OrderInfo objects will be created and associated to your Customer object. Later you can view your orderinfo by /orderInfos/my/{orderInfoId} since we are using JWT authentication 
your customer information will be transferred via token. You can get your OrderInfos, OrderItems, create new OrderItem for your OrderInfo, Update your existing OrderItem, Delete your OrderItem, Delete your OrderItem functionalities are available. 
The Customer entity has oneToMany relation with OrderInfo. Order Info has oneToMany relation with OrderItem. Product has one to many relation with OrderItem. So actually Order has many to many relation with Product and i implemented it using intermediate class OrderItem.

Admin can do all the crud operations and access all endpoints.
To create an admin object you need to change something on AuthenticationService class.
When you want to create an admin just change the .role(Role.CUSTOMER) to .role(Role.ADMIN) in register method of AuthenticationService. Afrer creating admin object revert the changes to Role.CUSTOMER .












