/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class OrderDAO extends DBContext {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();

    public List<Order> getAll() {

        List<Order> list = new ArrayList<>();

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY order_id DESC";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                list.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Order> getAll(int page, int maxElement) {

        List<Order> list = new ArrayList<>();

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY order_id DESC\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                list.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Order> getAllByCustomerId(int customerId, int page, int maxElement) {

        List<Order> list = new ArrayList<>();

        try {
            String query = "SELECT o.order_id, o.reservation_id, o.emp_id, o.voucher_id, o.order_date, o.order_time, o.payment_method, o.status\n"
                    + "FROM     [order] AS o INNER JOIN\n"
                    + "                  reservation AS r ON o.reservation_id = r.reservation_id\n"
                    + "WHERE  (LOWER(o.status) <> LOWER('Deleted')) AND (r.customer_id = ?)\n"
                    + "ORDER BY o.order_id DESC\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{customerId, (page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                list.add(order);
            }

        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return list;
    }

    public Order getElementByID(int id) {

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted') and order_id = ?)\n"
                    + "ORDER BY order_id\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                return order;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }

    public int add(Integer reservationId, Integer empId, Integer voucherId, String paymentMethod) {
        try {
            String query = "INSERT INTO [order]\n"
                    + "(reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{reservationId, empId, voucherId,
                new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), paymentMethod, "Pending"});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int orderId, int reservationId, int empId, Integer voucherId, String paymentMethod) {
        try {

            String query = "UPDATE [order]\n"
                    + "SET reservation_id = ?, emp_id = ?, voucher_id = ?, payment_method = ?\n"
                    + "WHERE  (order_id = ?)";

            return this.executeQuery(query, new Object[]{reservationId, empId, voucherId, paymentMethod, orderId});

        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (order_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(order_id) AS numrow\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int countItemByCustomer(int customerId) {
        try {
            String query = "SELECT COUNT(o.order_id) AS numrow\n"
                    + "FROM     [order] AS o INNER JOIN\n"
                    + "                  reservation AS r ON o.reservation_id = r.reservation_id INNER JOIN\n"
                    + "                  customer AS c ON r.customer_id = c.customer_id\n"
                    + "WHERE  (LOWER(o.status) <> LOWER(N'Deleted')) AND (c.customer_id = ?)";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{customerId});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }
    
    public boolean validateApprove(int id) {
        Order order = getElementByID(id);
        
        if (order.getVoucher() == null) {
            return true;
        } else {
            Voucher voucher = order.getVoucher();
            
            if (voucherDAO.decrease1Quantity(voucher.getVoucherId()) <= 0) {
                return false;
            }
        }
        
        return true;
    }

    public int approve(int id) {
        
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Approved'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return -1;
    }

    public int reject(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Rejected'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int complete(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Completed'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public long getTotalPricebyOrderId(int id) {
        int sum = 0;
        try {
            String query = "SELECT [unit_price]\n"
                    + "      ,[quantity]\n"
                    + "  FROM [RestaurantManagement].[dbo].[order_item]\n"
                    + "  where order_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            while (rs.next()) {
                int unitPrice = rs.getInt(1);
                int quantity = rs.getInt(2);

                sum += unitPrice * quantity;
            }

            Order order = getElementByID(id);
            long discount = 0;

            if (order.getVoucher() != null) {
                if (order.getVoucher().getDiscountType().equalsIgnoreCase("percent")) {
                    discount = (int)((float)order.getVoucher().getDiscountValue() / (float)100 * (float)sum);
                } else {
                    discount = order.getVoucher().getDiscountValue();
                }
                if (discount > sum) {
                    return 0;
                }
                return sum - discount;
            } else {
                return sum;
            }
        } catch (SQLException ex) {
            return -1;
        }
    }

    public String exportIngredientNeed(int id) {

        Order order = getElementByID(id);

        if (order == null) {
            return "";
        }

        String filePath = "../../../export/ingredientNeed/" + order.getOrderDate().toString().replace('/', '-') + "_" + order.getOrderTime().toString().replace(':', '-') + ".txt";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        String content = "";

        content += "Create by: " + order.getEmp().getEmpName() + "\n"
                + "Customer Name: " + order.getReservation().getCustomer().getCustomerName() + "\n"
                + "Table: " + order.getReservation().getTable().getNumber() + "\n"
                + "Ingredient \t| Quantity \n"
                + "---------------------------\n";

        try {
            String query = "SELECT i.ingredient_id, SUM(oi.quantity * ri.quantity) AS total_ingredient_needed\n"
                    + "FROM     [order] AS o INNER JOIN\n"
                    + "                  order_item AS oi ON o.order_id = oi.order_id INNER JOIN\n"
                    + "                  menu_item AS mi ON oi.menu_item_id = mi.menu_item_id INNER JOIN\n"
                    + "                  recipe AS r ON mi.recipe_id = r.recipe_id INNER JOIN\n"
                    + "                  recipe_item AS ri ON r.recipe_id = ri.recipe_id INNER JOIN\n"
                    + "                  ingredient AS i ON ri.ingredient_id = i.ingredient_id\n"
                    + "WHERE  (o.order_id = ?)\n"
                    + "GROUP BY o.order_id, i.ingredient_id\n"
                    + "ORDER BY o.order_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{order.getOrderId()});

            while (rs.next()) {
                int ingredientId = rs.getInt(1);
                double quantity = rs.getDouble(2);

                content += ingredientDAO.getElementByID(ingredientId).getIngredientName() + "\t| " + quantity + "\n";
            }
        } catch (SQLException ex) {
            return "";
        }

        content += "---------------------------\n";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
            return "";
        }

        return content;
    }

    public String getTotalPricebyOrderIdFormatVND(int id) {
        String str = "";

        String temp = getTotalPricebyOrderId(id) + "";

        while (temp.length() > 0) {
            if (temp.length() > 3) {
                str = temp.substring(temp.length() - 3, temp.length()) + str;
                temp = temp.substring(0, temp.length() - 3);
            } else {
                str = temp + str;
                temp = "";
            }
            if (temp.length() > 0) {
                str = "." + str;
            }
        }

        str += " VND";

        return str;
    }

    public int cancel(int orderId) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Cancel'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{orderId});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }
}
