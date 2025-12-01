package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;

public class CategoryDAO extends DBContext {

    public static void main(String[] args) {
    }

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();

        try {
            String query = "SELECT category_id, category_name, description, status\n"
                    + "FROM     category\n"
                    + "WHERE  (LOWER(status) = LOWER(N'Active'))\n"
                    + "ORDER BY category_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {

                int categoryId = rs.getInt(1);
                String categoryName = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Category category = new Category(categoryId, categoryName, description, status);

                list.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Category> getAll(int page) {
        List<Category> list = new ArrayList<>();

        try {
            String query = "SELECT category_id, category_name, description, status\n"
                    + "FROM     category\n"
                    + "WHERE  (LOWER(status) = LOWER(N'Active'))\n"
                    + "ORDER BY category_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                int categoryId = rs.getInt(1);
                String categoryName = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Category category = new Category(categoryId, categoryName, description, status);

                list.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Category> getAll(int page, String keyword) {
        List<Category> list = new ArrayList<>();
        try {
            String query = "SELECT c.category_id, c.category_name, c.description, c.status "
                    + "FROM category AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' "
                    + "AND (LOWER(c.category_name) LIKE LOWER(?) OR "
                    + "LOWER(c.description) LIKE LOWER(?) OR "
                    + "LOWER(c.status) LIKE LOWER(?)) "
                    + "ORDER BY c.category_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query,
                    new Object[]{keyword, keyword, keyword,
                        (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                int categoryId = rs.getInt(1);
                String categoryName = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Category category = new Category(categoryId, categoryName, description, status);
                list.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Category getElementByID(int id) {

        try {
            String query = "SELECT category_id, category_name, description, status\n"
                    + "FROM     category\n"
                    + "WHERE  (category_id = ? and LOWER(status) = LOWER(N'Active'))\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                int categoryId = rs.getInt(1);
                String categoryName = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Category category = new Category(categoryId, categoryName, description, status);

                return category;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int add(String categoryName, String description) {
        try {
            String query = "INSERT INTO category (category_name, description, status)\n"
                    + "VALUES (?, ?, ?)";

            return this.executeQuery(query, new Object[]{categoryName, description, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int categoryId, String categoryName, String description) {
        try {

            String query = "UPDATE category\n"
                    + "SET category_name = ?, description = ?\n"
                    + "WHERE  (category_id = ?)";

            return this.executeQuery(query, new Object[]{categoryName, description, categoryId});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE category\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (category_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "select count(category_id) as numrow from [dbo].[category]";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }
}
