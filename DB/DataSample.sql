---------------------------------------------------------
-- 1. Insert Ingredient Types
---------------------------------------------------------
INSERT INTO type (type_name, description, status) VALUES
('Meat', 'All kinds of meat products', 'Active'),
('Seafood', 'Seafood selections', 'Active'),
('Vegetable', 'Fresh greens and vegetables', 'Active'),
('Seasoning', 'Sauces and seasonings', 'Active');

---------------------------------------------------------
-- 2. Insert Categories
---------------------------------------------------------
INSERT INTO category (category_name, description, status) VALUES
('Appetizer', 'Starter dishes', 'Active'),
('Main Course', 'Main dishes', 'Active'),
('Dessert', 'Sweet dishes', 'Active'),
('Beverage', 'Drinks and coffee', 'Active');

---------------------------------------------------------
-- 3. Insert Roles
---------------------------------------------------------
INSERT INTO role (role_name, description, status) VALUES
('Admin', 'System administrator', 'Active'),
('Manager', 'Restaurant manager', 'Active'),
('Waiter', 'Service staff', 'Active'),
('Chef', 'Kitchen staff', 'Active'),
('Cashier', 'Cashier staff', 'Active');

---------------------------------------------------------
-- 4. Insert Customers
---------------------------------------------------------
INSERT INTO customer (customer_account, password, customer_name, gender, dob, phone_number, email, address, status)
VALUES
('alice01', 'c20ad4d76fe97759aa27a0c99bff6710', 'Alice Nguyen', 'Female', '1992-04-10', '0905000001', 'alice@example.com', '12 Garden St', 'Active'),
('bob02', 'c20ad4d76fe97759aa27a0c99bff6710', 'Bob Tran', 'Male', '1989-09-22', '0905000002', 'bob@example.com', '34 River Rd', 'Active');

---------------------------------------------------------
-- 5. Insert Tables
---------------------------------------------------------
INSERT INTO [table] (table_number, table_capacity, status) VALUES
('A1', 4, 'Available'),
('B2', 6, 'Available');

---------------------------------------------------------
-- 6. Insert Ingredients
---------------------------------------------------------
INSERT INTO ingredient (ingredient_name, unit, type_id, expiration_date, status) VALUES
('Beef Ribeye', 'kg', 1, DATEADD(DAY, 10, GETDATE()), 'Active'),
('Tiger Shrimp', 'kg', 2, DATEADD(DAY, 7, GETDATE()), 'Active'),
('Romaine Lettuce', 'kg', 3, DATEADD(DAY, 5, GETDATE()), 'Active'),
('House Marinade', 'l', 4, NULL, 'Active');

---------------------------------------------------------
-- 7. Insert Suppliers
---------------------------------------------------------
INSERT INTO supplier (supplier_name, phone_number, email, address, contact_person, status) VALUES
('FreshLand', '0908000001', 'hello@freshland.vn', '22 Market Ave', 'Minh Le', 'Active'),
('OceanFoods', '0908000002', 'sales@oceanfoods.vn', '88 Dock St', 'Anh Pham', 'Active');

---------------------------------------------------------
-- 8. Insert Employees
---------------------------------------------------------
INSERT INTO employee (emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status)
VALUES
('admin', 'c20ad4d76fe97759aa27a0c99bff6710', 'Lan Manager', 'Female', '1988-01-15', '0909000001', 'lan.manager@rms.com', '101 Central Blvd', 1, 'Active'),
('chef', 'c20ad4d76fe97759aa27a0c99bff6710', 'Khoa Chef', 'Male', '1990-06-20', '0909000002', 'khoa.chef@rms.com', '55 Kitchen Ln', 4, 'Active'),
('waiter', 'c20ad4d76fe97759aa27a0c99bff6710', 'My Waiter', 'Female', '1995-03-05', '0909000003', 'my.waiter@rms.com', '78 Service Rd', 3, 'Active'),
('cashier', 'c20ad4d76fe97759aa27a0c99bff6710', 'Son Cashier', 'Male', '1993-11-12', '0909000004', 'son.cashier@rms.com', '43 Billing Sq', 5, 'Active');

---------------------------------------------------------
-- 9. Insert Recipes
---------------------------------------------------------
INSERT INTO recipe (recipe_name, status) VALUES
('Signature Grilled Beef', 'Active'),
('Garden Fresh Roll', 'Active'),
('Classic Black Coffee', 'Active');

---------------------------------------------------------
-- 10. Insert Menu Items
---------------------------------------------------------
INSERT INTO menu_item (category_id, recipe_id, item_name, image_url, price, description, status)
VALUES
(2, 1, 'Grilled Beef Plate', '/img/grilled-beef.jpg', 180000, 'Charcoal grilled beef with salad', 'Active'),
(1, 2, 'Fresh Salad Roll', '/img/salad-roll.jpg', 45000, 'Rice paper roll with shrimp and greens', 'Active'),
(4, 3, 'Black Coffee', '/img/black-coffee.jpg', 30000, 'Vietnamese drip coffee', 'Active');

---------------------------------------------------------
-- 11. Insert Voucher
---------------------------------------------------------
INSERT INTO voucher (voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status)
VALUES
('WELCOME15', 'Welcome 15 percent', 'Percent', 15, 50, '2025-01-01', '2025-12-31', 'Active');

---------------------------------------------------------
-- 12. Insert Reservation
---------------------------------------------------------
INSERT INTO reservation (customer_id, table_id, reservation_date, reservation_time, status)
VALUES
(1, 1, GETDATE(), '19:00', 'Confirmed');

---------------------------------------------------------
-- 13. Insert Import Record
---------------------------------------------------------
INSERT INTO import (supplier_id, emp_id, import_date, status)
VALUES
(1, 2, DATEADD(DAY, -1, GETDATE()), 'Completed');

---------------------------------------------------------
-- 14. Insert Import Details
---------------------------------------------------------
INSERT INTO import_detail (import_id, ingredient_id, quantity, unit_price, total_price)
VALUES
(1, 1, 25, 175000, 4375000),
(1, 3, 40, 12000, 480000),
(1, 4, 10, 60000, 600000);

---------------------------------------------------------
-- 15. Insert Recipe Items
---------------------------------------------------------
INSERT INTO recipe_item (recipe_id, ingredient_id, quantity, unit, note, status) VALUES
(1, 1, 0.45, 'kg', 'Per portion', 'Active'),
(1, 4, 0.05, 'l', 'Marinade per portion', 'Active'),
(2, 2, 0.12, 'kg', 'Shrimp per roll set', 'Active'),
(2, 3, 0.20, 'kg', 'Vegetables per roll set', 'Active'),
(3, 4, 0.50, 'l', 'No usage', 'Active');

---------------------------------------------------------
-- 16. Insert Order
---------------------------------------------------------
INSERT INTO [order] (reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status)
VALUES
(1, 4, 1, GETDATE(), GETDATE(), 'Cash', 'Completed');

---------------------------------------------------------
-- 17. Insert Order Items
---------------------------------------------------------
INSERT INTO order_item (order_id, menu_item_id, unit_price, quantity) VALUES
(1, 1, 180000, 2),
(1, 2, 45000, 3),
(1, 3, 30000, 2);

---------------------------------------------------------
-- 18. Insert Ingredient Usage Log
---------------------------------------------------------
INSERT INTO ingredient_usage (usage_date, ingredient_id, quantity_used, stock_before, stock_after, note, created_by) VALUES
(GETDATE(), 1, 1.00, 25.00, 24.00, N'Mẫu ghi chú tồn kho', 1),
(GETDATE(), 3, 0.60, 40.00, 39.40, NULL, 1);

