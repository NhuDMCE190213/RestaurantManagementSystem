USE RestaurantManagement;
GO
SET NOCOUNT ON;
GO

BEGIN TRANSACTION;

DECLARE
    @type_meat INT,
    @type_sea INT,
    @type_veg INT,
    @type_spice INT,
    @cat_app INT,
    @cat_main INT,
    @cat_dess INT,
    @cat_drink INT,
    @role_admin INT,
    @role_manager INT,
    @role_waiter INT,
    @role_chef INT,
    @role_cashier INT,
    @cust_alice INT,
    @cust_bob INT,
    @table_a1 INT,
    @table_b2 INT,
    @ing_beef INT,
    @ing_shrimp INT,
    @ing_lettuce INT,
    @ing_marinade INT,
    @supplier_fresh INT,
    @supplier_ocean INT,
    @emp_admin INT,
    @emp_chef INT,
    @emp_waiter INT,
    @emp_cashier INT,
    @recipe_grill INT,
    @recipe_roll INT,
    @recipe_coffee INT,
    @item_grill INT,
    @item_roll INT,
    @item_coffee INT,
    @voucher_welcome INT,
    @reservation_today INT,
    @import_completed INT,
    @order_completed INT;

INSERT INTO [type] (type_name, description, status) VALUES
('Meat', 'All kinds of meat products', 'Active'),
('Seafood', 'Seafood selections', 'Active'),
('Vegetable', 'Fresh greens and vegetables', 'Active'),
('Seasoning', 'Sauces and seasonings', 'Active');

SELECT @type_meat = type_id FROM [type] WHERE type_name = 'Meat';
SELECT @type_sea = type_id FROM [type] WHERE type_name = 'Seafood';
SELECT @type_veg = type_id FROM [type] WHERE type_name = 'Vegetable';
SELECT @type_spice = type_id FROM [type] WHERE type_name = 'Seasoning';

INSERT INTO category (category_name, description, status) VALUES
('Appetizer', 'Starter dishes', 'Active'),
('Main Course', 'Main dishes', 'Active'),
('Dessert', 'Sweet dishes', 'Active'),
('Beverage', 'Drinks and coffee', 'Active');

SELECT @cat_app = category_id FROM category WHERE category_name = 'Appetizer';
SELECT @cat_main = category_id FROM category WHERE category_name = 'Main Course';
SELECT @cat_dess = category_id FROM category WHERE category_name = 'Dessert';
SELECT @cat_drink = category_id FROM category WHERE category_name = 'Beverage';

INSERT INTO role (role_name, description, status) VALUES
('Admin', 'System administrator', 'Active'),
('Manager', 'Restaurant manager', 'Active'),
('Waiter', 'Service staff', 'Active'),
('Chef', 'Kitchen staff', 'Active'),
('Cashier', 'Cashier staff', 'Active');

SELECT @role_admin = role_id FROM role WHERE role_name = 'Admin';
SELECT @role_manager = role_id FROM role WHERE role_name = 'Manager';
SELECT @role_waiter = role_id FROM role WHERE role_name = 'Waiter';
SELECT @role_chef = role_id FROM role WHERE role_name = 'Chef';
SELECT @role_cashier = role_id FROM role WHERE role_name = 'Cashier';

INSERT INTO customer (customer_account, password, customer_name, gender, dob, phone_number, email, address, status) VALUES
('alice01', 'alicepass', 'Alice Nguyen', 'Female', '1992-04-10', '0905000001', 'alice@example.com', '12 Garden St', 'Active'),
('bob02', 'bobpass', 'Bob Tran', 'Male', '1989-09-22', '0905000002', 'bob@example.com', '34 River Rd', 'Active');

SELECT @cust_alice = customer_id FROM customer WHERE customer_account = 'alice01';
SELECT @cust_bob = customer_id FROM customer WHERE customer_account = 'bob02';

INSERT INTO [table] (table_number, table_capacity, status) VALUES
('A1', 4, 'Available'),
('B2', 6, 'Available');

SELECT @table_a1 = table_id FROM [table] WHERE table_number = 'A1';
SELECT @table_b2 = table_id FROM [table] WHERE table_number = 'B2';

INSERT INTO ingredient (ingredient_name, unit, type_id, expiration_date, status) VALUES
('Beef Ribeye', 'kg', @type_meat, DATEADD(DAY, 10, GETDATE()), 'Active'),
('Tiger Shrimp', 'kg', @type_sea, DATEADD(DAY, 7, GETDATE()), 'Active'),
('Romaine Lettuce', 'kg', @type_veg, DATEADD(DAY, 5, GETDATE()), 'Active'),
('House Marinade', 'l', @type_spice, NULL, 'Active');

SELECT @ing_beef = ingredient_id FROM ingredient WHERE ingredient_name = 'Beef Ribeye';
SELECT @ing_shrimp = ingredient_id FROM ingredient WHERE ingredient_name = 'Tiger Shrimp';
SELECT @ing_lettuce = ingredient_id FROM ingredient WHERE ingredient_name = 'Romaine Lettuce';
SELECT @ing_marinade = ingredient_id FROM ingredient WHERE ingredient_name = 'House Marinade';

INSERT INTO supplier (supplier_name, phone_number, email, address, contact_person, status) VALUES
('FreshLand', '0908000001', 'hello@freshland.vn', '22 Market Ave', 'Minh Le', 'Active'),
('OceanFoods', '0908000002', 'sales@oceanfoods.vn', '88 Dock St', 'Anh Pham', 'Active');

SELECT @supplier_fresh = supplier_id FROM supplier WHERE supplier_name = 'FreshLand';
SELECT @supplier_ocean = supplier_id FROM supplier WHERE supplier_name = 'OceanFoods';

INSERT INTO employee (emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status) VALUES
('admin', CONVERT(NVARCHAR(255), HASHBYTES('MD5', 'admin123'), 2), 'Lan Manager', 'Female', '1988-01-15', '0909000001', 'lan.manager@rms.com', '101 Central Blvd', @role_admin, 'Active'),
('chef', CONVERT(NVARCHAR(255), HASHBYTES('MD5', 'chef123'), 2), 'Khoa Chef', 'Male', '1990-06-20', '0909000002', 'khoa.chef@rms.com', '55 Kitchen Ln', @role_chef, 'Active'),
('waiter', CONVERT(NVARCHAR(255), HASHBYTES('MD5', 'waiter123'), 2), 'My Waiter', 'Female', '1995-03-05', '0909000003', 'my.waiter@rms.com', '78 Service Rd', @role_waiter, 'Active'),
('cashier', CONVERT(NVARCHAR(255), HASHBYTES('MD5', 'cashier123'), 2), 'Son Cashier', 'Male', '1993-11-12', '0909000004', 'son.cashier@rms.com', '43 Billing Sq', @role_cashier, 'Active');

SELECT @emp_admin = emp_id FROM employee WHERE emp_account = 'admin';
SELECT @emp_chef = emp_id FROM employee WHERE emp_account = 'chef';
SELECT @emp_waiter = emp_id FROM employee WHERE emp_account = 'waiter';
SELECT @emp_cashier = emp_id FROM employee WHERE emp_account = 'cashier';

INSERT INTO recipe (recipe_name, status) VALUES
('Signature Grilled Beef', 'Active'),
('Garden Fresh Roll', 'Active'),
('Classic Black Coffee', 'Active');

SELECT @recipe_grill = recipe_id FROM recipe WHERE recipe_name = 'Signature Grilled Beef';
SELECT @recipe_roll = recipe_id FROM recipe WHERE recipe_name = 'Garden Fresh Roll';
SELECT @recipe_coffee = recipe_id FROM recipe WHERE recipe_name = 'Classic Black Coffee';

INSERT INTO menu_item (category_id, recipe_id, item_name, image_url, price, description, status) VALUES
(@cat_main, @recipe_grill, 'Grilled Beef Plate', '/img/grilled-beef.jpg', 180000, 'Charcoal grilled beef with salad', 'Active'),
(@cat_app, @recipe_roll, 'Fresh Salad Roll', '/img/salad-roll.jpg', 45000, 'Rice paper roll with shrimp and greens', 'Active'),
(@cat_drink, @recipe_coffee, 'Black Coffee', '/img/black-coffee.jpg', 30000, 'Vietnamese drip coffee', 'Active');

SELECT @item_grill = menu_item_id FROM menu_item WHERE item_name = 'Grilled Beef Plate';
SELECT @item_roll = menu_item_id FROM menu_item WHERE item_name = 'Fresh Salad Roll';
SELECT @item_coffee = menu_item_id FROM menu_item WHERE item_name = 'Black Coffee';

INSERT INTO voucher (voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status) VALUES
('WELCOME15', 'Welcome 15 percent', 'Percent', 15, 50, '2025-01-01', '2025-12-31', 'Active');

SELECT @voucher_welcome = voucher_id FROM voucher WHERE voucher_code = 'WELCOME15';

INSERT INTO reservation (customer_id, table_id, reservation_date, reservation_time, status) VALUES
(@cust_alice, @table_a1, CAST(GETDATE() AS DATE), '19:00', 'Confirmed');

SELECT @reservation_today = reservation_id FROM reservation WHERE customer_id = @cust_alice AND table_id = @table_a1;

INSERT INTO [import] (supplier_id, emp_id, import_date, status) VALUES
(@supplier_fresh, @emp_chef, DATEADD(DAY, -1, GETDATE()), 'Completed');

SELECT @import_completed = import_id FROM [import] WHERE supplier_id = @supplier_fresh AND status = 'Completed';

INSERT INTO import_detail (import_id, ingredient_id, quantity, unit_price, total_price) VALUES
(@import_completed, @ing_beef, 25, 175000.00, 4375000.00),
(@import_completed, @ing_lettuce, 40, 12000.00, 480000.00),
(@import_completed, @ing_marinade, 10, 60000.00, 600000.00);

INSERT INTO recipe_item (recipe_id, ingredient_id, quantity, unit, note, status) VALUES
(@recipe_grill, @ing_beef, 0.45, 'kg', 'Per portion', 'Active'),
(@recipe_grill, @ing_marinade, 0.05, 'l', 'Marinade per portion', 'Active'),
(@recipe_roll, @ing_shrimp, 0.12, 'kg', 'Shrimp per roll set', 'Active'),
(@recipe_roll, @ing_lettuce, 0.20, 'kg', 'Vegetables per roll set', 'Active'),
(@recipe_coffee, @ing_marinade, 0.00, 'l', 'No usage, placeholder', 'Active');

INSERT INTO [order] (reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status) VALUES
(@reservation_today, @emp_cashier, @voucher_welcome, CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME), 'Cash', 'Completed');

SELECT @order_completed = order_id FROM [order] WHERE reservation_id = @reservation_today;

INSERT INTO order_item (order_id, menu_item_id, unit_price, quantity) VALUES
(@order_completed, @item_grill, 180000, 2),
(@order_completed, @item_roll, 45000, 3),
(@order_completed, @item_coffee, 30000, 2);

INSERT INTO ingredient_usage (usage_date, ingredient_id, quantity_used, stock_before, stock_after, note, created_by) VALUES
(CAST(GETDATE() AS DATE), @ing_beef, 1.00, 25.00, 24.00, N'Điều chỉnh test', @emp_admin),
(CAST(GETDATE() AS DATE), @ing_lettuce, 0.60, 40.00, 39.40, NULL, @emp_admin);

COMMIT TRANSACTION;
GO

PRINT 'Sample data for DBScriptFixUnique has been inserted successfully.';
GO
