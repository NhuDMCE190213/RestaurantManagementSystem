--Lưu ý: Đặt tên cho database: RestaurantManagement
USE master
GO

-- Xóa DB cũ nếu tồn tại
IF DB_ID('RestaurantManagement') IS NOT NULL
    DROP DATABASE RestaurantManagement;
GO

-- Tạo DB mới
CREATE DATABASE RestaurantManagement;
GO
USE RestaurantManagement;
GO

CREATE TABLE type(
    type_id INT IDENTITY(1,1) NOT NULL,
    type_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_type PRIMARY KEY (type_id)
);

CREATE TABLE category (
    category_id INT IDENTITY(1,1) NOT NULL,
    category_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_category PRIMARY KEY (category_id)
);

CREATE UNIQUE INDEX UX_category_name_not_deleted
ON category(category_name)
WHERE status <> 'deleted';

CREATE TABLE role (
    role_id INT IDENTITY(1,1) NOT NULL,
    role_name NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_role PRIMARY KEY (role_id)
);

CREATE UNIQUE INDEX UX_role_name_not_deleted
ON role(role_name)
WHERE status <> 'deleted';

CREATE TABLE customer (
    customer_id INT IDENTITY(1,1) NOT NULL,
	customer_account NVARCHAR(50) NOT NULL,
    password NVARCHAR(255) NOT NULL,
    customer_name NVARCHAR(100) NOT NULL,
	gender NVARCHAR(10) NULL,
    dob DATE NULL,
    phone_number VARCHAR(19) NULL,
    email NVARCHAR(255) NULL,
    address NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_customer PRIMARY KEY (customer_id)
);

CREATE UNIQUE INDEX UX_customer_account_not_deleted
ON customer(customer_account)
WHERE status <> 'deleted';

CREATE TABLE [table] (
    table_id INT IDENTITY(1,1) NOT NULL,
    table_number VARCHAR(10) NOT NULL,
    table_capacity INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Available',
    CONSTRAINT PK_tables PRIMARY KEY (table_id)
);

CREATE UNIQUE INDEX UX_table_number_not_deleted
ON [table](table_number)
WHERE status <> 'deleted';

CREATE TABLE ingredient (
    ingredient_id INT IDENTITY(1,1) NOT NULL,
    ingredient_name NVARCHAR(100) NOT NULL,
	unit NVARCHAR(20) NOT NULL,
    expiration_date DATE NULL,
    type_id INT NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_ingredient PRIMARY KEY (ingredient_id),
    CONSTRAINT FK_type FOREIGN KEY (type_id) REFERENCES type(type_id)
);

CREATE UNIQUE INDEX UX_ingredient_name_not_deleted
ON ingredient(ingredient_name)
WHERE status <> 'deleted';

CREATE TABLE employee (
    emp_id INT IDENTITY(1,1) NOT NULL,
    emp_account NVARCHAR(50) NOT NULL,
    password NVARCHAR(255) NOT NULL,
    emp_name NVARCHAR(100) NOT NULL,
    gender NVARCHAR(10) NULL,
    dob DATE NULL,
    phone_number VARCHAR(20) NULL,
    email NVARCHAR(100) NULL,
    address NVARCHAR(255) NULL,
    role_id INT NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_employee PRIMARY KEY (emp_id),
    CONSTRAINT FK_employee_role FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE UNIQUE INDEX UX_employee_account_not_deleted
ON employee(emp_account)
WHERE status <> 'deleted';

CREATE TABLE recipe (
    recipe_id INT IDENTITY(1,1) NOT NULL,
    recipe_name NVARCHAR(100) NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_recipe PRIMARY KEY (recipe_id)
);

CREATE TABLE menu_item (
    menu_item_id INT IDENTITY(1,1) NOT NULL,
    category_id INT NOT NULL,
    recipe_id INT NOT NULL,
    item_name NVARCHAR(100) NOT NULL,
    image_url NVARCHAR(255) NULL,
    price INT NOT NULL DEFAULT 0,
    description NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_menu_item PRIMARY KEY (menu_item_id),
    CONSTRAINT FK_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT FK_recipe FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);

CREATE UNIQUE INDEX UX_item_name_not_deleted
ON menu_item(item_name)
WHERE status <> 'deleted';

CREATE TABLE reservation (
    reservation_id INT IDENTITY(1,1) NOT NULL,
    customer_id INT NOT NULL,
    table_id INT NOT NULL,
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Pending',
    CONSTRAINT PK_reservation PRIMARY KEY (reservation_id),
    CONSTRAINT FK_reservation_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT FK_reservation_table FOREIGN KEY (table_id) REFERENCES [table](table_id)
);

CREATE TABLE voucher (
    voucher_id INT IDENTITY(1,1) NOT NULL,
    voucher_code NVARCHAR(50) NOT NULL,
    voucher_name NVARCHAR(100) NOT NULL,
    discount_type NVARCHAR(20) NOT NULL,
    discount_value INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_voucher PRIMARY KEY (voucher_id)
);

CREATE UNIQUE INDEX UX_voucher_code_not_deleted
ON voucher(voucher_code)
WHERE status <> 'deleted';

CREATE TABLE [order] (
    order_id INT IDENTITY(1,1) NOT NULL,
    reservation_id INT NULL,
    emp_id INT NULL,
    voucher_id INT NULL,
    order_date DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    order_time TIME NOT NULL DEFAULT CAST(GETDATE() AS TIME),
    payment_method NVARCHAR(20) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Pending',
    CONSTRAINT PK_order PRIMARY KEY (order_id),
    CONSTRAINT FK_order_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT FK_order_employee FOREIGN KEY (emp_id) REFERENCES employee(emp_id),
	CONSTRAINT FK_order_voucher FOREIGN KEY (voucher_id) REFERENCES voucher(voucher_id)
);

CREATE TABLE order_item (
    order_item_id INT IDENTITY(1,1) NOT NULL,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    unit_price INT NOT NULL DEFAULT 0,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT PK_order_item PRIMARY KEY (order_item_id),
    CONSTRAINT FK_order_item_order FOREIGN KEY (order_id) REFERENCES [order](order_id),
    CONSTRAINT FK_order_item_menu FOREIGN KEY (menu_item_id) REFERENCES menu_item(menu_item_id)
);

CREATE UNIQUE INDEX UX_recipe_name_not_deleted
ON recipe(recipe_name)
WHERE status <> 'deleted';

CREATE TABLE recipe_item (
    recipe_item_id INT IDENTITY(1,1) NOT NULL,
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit NVARCHAR(20) NOT NULL,
    note NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT PK_recipe_item PRIMARY KEY (recipe_item_id),
    CONSTRAINT FK_recipe_item_recipe FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id),
    CONSTRAINT FK_recipe_item_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
);

CREATE TABLE supplier (
    supplier_id INT IDENTITY(1,1) PRIMARY KEY,
    supplier_name NVARCHAR(100) NOT NULL,
    phone_number NVARCHAR(20),
    email NVARCHAR(255),
    address NVARCHAR(255),
    contact_person NVARCHAR(100),
    status NVARCHAR(20)
);

CREATE UNIQUE INDEX UX_supplier_name_not_deleted
ON supplier(supplier_name)
WHERE status <> 'deleted';

CREATE TABLE import (
    import_id INT IDENTITY(1,1) PRIMARY KEY,
    supplier_id INT NOT NULL,
    emp_id INT NOT NULL,
    import_date DATETIME NOT NULL DEFAULT GETDATE(),
    status NVARCHAR(20),
    CONSTRAINT FK_Import_Supplier FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    CONSTRAINT FK_Import_Employee FOREIGN KEY (emp_id) REFERENCES Employee(emp_id)   
);

CREATE TABLE import_detail (
    import_detail_id INT IDENTITY(1,1) PRIMARY KEY,
    import_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT FK_ImportDetail_Import FOREIGN KEY (import_id)
        REFERENCES Import(import_id),
    CONSTRAINT FK_ImportDetail_Ingredient FOREIGN KEY (ingredient_id)
        REFERENCES Ingredient(ingredient_id)
);

CREATE TABLE ingredient_usage (
    usage_id INT IDENTITY(1,1) PRIMARY KEY,
    usage_date DATE NOT NULL,
    ingredient_id INT NOT NULL,
    quantity_used DECIMAL(18,2) NOT NULL,
    stock_before DECIMAL(18,2) NOT NULL,
    stock_after DECIMAL(18,2) NOT NULL,
    note NVARCHAR(255) NULL,
    created_by INT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_IngredientUsage_Ingredient FOREIGN KEY (ingredient_id)
        REFERENCES ingredient(ingredient_id),
    CONSTRAINT FK_IngredientUsage_Employee FOREIGN KEY (created_by)
        REFERENCES employee(emp_id)
);

