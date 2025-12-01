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
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class OrderItemDAO extends DBContext {

    private final OrderDAO orderDAO = new OrderDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();

//    public List<OrderItem> getAll() {
//        List<OrderItem> list = new ArrayList<>();
//        try {
//            String query = "SELECT order_item_id, order_id, menu_item_id, unit_price, quantity\n"
//                    + "FROM     order_item";
//
//            ResultSet rs = this.executeSelectionQuery(query, null);
//
//            while (rs.next()) {
//                int orderItemId = rs.getInt(1);
//                int orderId = rs.getInt(2);
//                int menuItemId = rs.getInt(3);
//                int unitPrice = rs.getInt(4);
//                int quantity = rs.getInt(5);
//
//                OrderItem orderItem = new OrderItem(orderItemId, orderDAO.getElementByID(orderId), menuItemDAO.get, unitPrice, quantity);
//
//                list.add(order);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Can't not load list");
//        }
//
//        return list;
//    }
    public List<OrderItem> getAll(int page, int maxElement) {

        List<OrderItem> list = new ArrayList<>();

        try {
            String query = "SELECT order_item_id, order_id, menu_item_id, unit_price, quantity\n"
                    + "FROM     order_item\n"
                    + "ORDER BY order_item_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int orderItemId = rs.getInt(1);
                int orderId = rs.getInt(2);
                int menuItemId = rs.getInt(3);
                int unitPrice = rs.getInt(4);
                int quantity = rs.getInt(5);

                OrderItem orderItem = new OrderItem(orderItemId, orderDAO.getElementByID(orderId), menuItemDAO.getElementByID(menuItemId), unitPrice, quantity);

                list.add(orderItem);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<OrderItem> getAllByOrderId(int orderId) {

        List<OrderItem> list = new ArrayList<>();

        try {
            String query = "SELECT order_item_id, order_id, menu_item_id, unit_price, quantity\n"
                    + "FROM     order_item\n"
                    + "WHERE  (order_id = ?)\n"
                    + "ORDER BY order_item_id\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{orderId});

            while (rs.next()) {
                int orderItemId = rs.getInt(1);
                int menuItemId = rs.getInt(3);
                int unitPrice = rs.getInt(4);
                int quantity = rs.getInt(5);

                OrderItem orderItem = new OrderItem(orderItemId, orderDAO.getElementByID(orderId), menuItemDAO.getElementByID(menuItemId), unitPrice, quantity);

                list.add(orderItem);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<OrderItem> getAllByOrderId(int orderId, int page, int maxElement) {

        List<OrderItem> list = new ArrayList<>();

        try {
            String query = "SELECT order_item_id, order_id, menu_item_id, unit_price, quantity\n"
                    + "FROM     order_item\n"
                    + "WHERE  (order_id = ?)\n"
                    + "ORDER BY order_item_id\n"
                    + "OFFSET ? ROWS\n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{orderId, (page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int orderItemId = rs.getInt(1);
                int menuItemId = rs.getInt(3);
                int unitPrice = rs.getInt(4);
                int quantity = rs.getInt(5);

                OrderItem orderItem = new OrderItem(orderItemId, orderDAO.getElementByID(orderId), menuItemDAO.getElementByID(menuItemId), unitPrice, quantity);

                list.add(orderItem);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public OrderItem getElementByID(int id) {

        try {
            String query = "SELECT order_item_id, order_id, menu_item_id, unit_price, quantity\n"
                    + "FROM     order_item\n"
                    + "WHERE order_item_id = ?";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                int orderItemId = rs.getInt(1);
                int orderId = rs.getInt(2);
                int menuItemId = rs.getInt(3);
                int unitPrice = rs.getInt(4);
                int quantity = rs.getInt(5);

                OrderItem orderItem = new OrderItem(orderItemId, orderDAO.getElementByID(orderId), menuItemDAO.getElementByID(menuItemId), unitPrice, quantity);

                return orderItem;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }

    public int add(int orderId, int menuItemId, int unitPrice, int quantity) {
        try {
            String query = "INSERT INTO order_item\n"
                    + "(order_id, menu_item_id, unit_price, quantity)\n"
                    + "VALUES (?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{orderId, menuItemId, unitPrice, quantity});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int orderItemId, int orderId, int menuItemId, int unitPrice, int quantity) {
        try {

            String query = "UPDATE order_item\n"
                    + "SET          order_id = ?, menu_item_id = ?, unit_price = ?, quantity = ?\n"
                    + "WHERE  (order_item_id = ?)";

            return this.executeQuery(query, new Object[]{orderId, menuItemId, unitPrice, quantity, orderItemId});

        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "DELETE FROM order_item\n"
                    + "WHERE  (order_item_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(order_item_id) AS numrow\n"
                    + "FROM     [order_item]\n";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int countItembyOrderId(int id) {
        try {
            String query = "SELECT COUNT(order_item_id) AS numrow\n"
                    + "FROM     order_item\n"
                    + "WHERE  (order_id = ?)\n";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public String exportBill(int orderId) {

        Order order = orderDAO.getElementByID(orderId);

        if (order == null) {
            return "";
        }

        String filePath = "../../../export/bill/" + order.getOrderDate().toString().replace('/', '-') + "_" + order.getOrderTime().toString().replace(':', '-') + ".txt";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        String content = "";

        List<OrderItem> orderItems = getAllByOrderId(order.getOrderId());

        content += "Create by: " + order.getEmp().getEmpName() + "\n"
                + "Customer Name: " + order.getReservation().getCustomer().getCustomerName() + "\n"
                + "Item \t| UnitPrice \t| Qty \n"
                + "---------------------------\n";

        for (OrderItem orderItem : orderItems) {
            content += orderItem.getMenuItem().getItemName() + "\t| " + orderItem.getMenuItem().getPriceVND() + "\t| " + orderItem.getQuantity() + "\n";
        }

        long total = orderDAO.getTotalPricebyOrderId(order.getOrderId());
        long vat = total * 10 / 100;
        long remain = total + vat;

        content += "---------------------------\n"
                + "Total: " + getFormatVND(total) + "\n"
                + "VAT(10%): " + getFormatVND(vat) + "\n"
                + "Sum: " + getFormatVND(remain) + "\n"
                + "---------------------------";

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

    public String getFormatVND(long number) {
        String str = "";

        String temp = number + "";

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

    public List<Date> statisticDate() {
        List<Date> list = new ArrayList<>();

        try {
            String query = "SELECT order_date\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) = LOWER('Completed'))\n"
                    + "GROUP BY order_date\n"
                    + "ORDER BY order_date";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                Date date = rs.getDate(1);
                list.add(date);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Long> statisticIncome() {
        List<Long> list = new ArrayList<>();

        Date currentDate = null;
        long sum = 0;

        try {
            String query = "SELECT o.order_id, oi.menu_item_id, v.voucher_id, o.order_date, oi.unit_price, oi.quantity, v.discount_type, v.discount_value\n"
                    + "FROM     [order] AS o LEFT JOIN\n"
                    + "                  order_item AS oi ON o.order_id = oi.order_id LEFT JOIN\n"
                    + "                  voucher AS v ON o.voucher_id = v.voucher_id\n"
                    + "WHERE  (oi.menu_item_id IS NOT NULL) AND (LOWER(o.status) = LOWER('Completed'))\n"
                    + "ORDER BY o.order_date";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
//                int orderId = rs.getInt(1);
//                int orderItemId = rs.getInt(2);
                Integer voucherId = rs.getInt(3);
                Date date = rs.getDate(4);

                if (currentDate == null) {
                    currentDate = date;
                }

                if (!date.toString().equalsIgnoreCase(currentDate.toString())) {
                    currentDate = date;
                    list.add(sum);
                    sum = 0;
                }

//                Order order = orderDAO.getElementByID(orderId);
//                OrderItem orderItem = getElementByID(orderItemId); loi do dung chinh dbcontext cua ban than, gay du lieu bi sai

                long mini_sum = rs.getInt(6) * rs.getInt(5);
//                long mini_sum = orderItem.getQuantity() * orderItem.getUnitPrice();

                if (voucherId.intValue() <= 0) {
                    sum += mini_sum;
                } else {
                    Voucher voucher = voucherDAO.getById(voucherId);

                    if (voucher.getDiscountType().equalsIgnoreCase("Percent")) {
                        sum += mini_sum - mini_sum * voucher.getDiscountValue() / 100;
                    } else {
                        if (mini_sum > voucher.getDiscountValue()) {
                            sum += mini_sum - voucher.getDiscountValue();
                        }
                    }
                }
            }
            if (sum > 0) {
                list.add(sum);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }
}
