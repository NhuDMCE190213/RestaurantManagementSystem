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
import model.MenuItem;
import model.Recipe;

public class MenuItemDAO extends DBContext {

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    public static void main(String[] args) {
    }

    /**
     *
     *
     *
     * @return
     *
     */
    public List<MenuItem> getAll() {
        List<MenuItem> list = new ArrayList<>();

        try {
            String query = "SELECT menu_item_id, category_id, recipe_id, item_name, image_url, price, description, status\n"
                    + "FROM     menu_item\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY menu_item_id DESC\n";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{});

            while (rs.next()) {
                int menuItemId = rs.getInt(1);
                int categoryId = rs.getInt(2);
                int recipeId = rs.getInt(3);
                String itemName = rs.getString(4);
                String imageUrl = rs.getString(5);
                int price = rs.getInt(6);
                String description = rs.getString(7);
                String status = rs.getString(8);

                //Chua check tinh kha dung cua recipe
                MenuItem menuItem = new MenuItem(menuItemId,
                        categoryDAO.getElementByID(categoryId), recipeDAO.getElementByID(recipeId),
                        itemName, imageUrl, price, description, status);

                list.add(menuItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<MenuItem> getAll(int page, int maxElement) {
        List<MenuItem> list = new ArrayList<>();

        try {
            String query = "SELECT menu_item_id, category_id, recipe_id, item_name, image_url, price, description, status\n"
                    + "FROM  menu_item\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY menu_item_id DESC\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int menuItemId = rs.getInt(1);
                int categoryId = rs.getInt(2);
                int recipeId = rs.getInt(3);
                String itemName = rs.getString(4);
                String imageUrl = rs.getString(5);
                int price = rs.getInt(6);
                String description = rs.getString(7);
                String status = rs.getString(8);

                //Chua check tinh kha dung cua recipe
                MenuItem menuItem = new MenuItem(menuItemId,
                        categoryDAO.getElementByID(categoryId), recipeDAO.getElementByID(recipeId),
                        itemName, imageUrl, price, description, status);

                list.add(menuItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<String> getAllCategoryNames() {

        List<String> categoryNames = new ArrayList<>();

        try {

            String query = "SELECT category_name FROM category WHERE LOWER(status) <> LOWER('Deleted')  ORDER BY category_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {

                categoryNames.add(rs.getString("category_name"));

            }

        } catch (SQLException ex) {

            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return categoryNames;

    }

    public List<String> getTopCategoryNames() {

        List<String> categoryNames = new ArrayList<>();

        try {

            String query = "SELECT TOP 4 category_name FROM category WHERE LOWER(status) <> LOWER('Deleted') ORDER BY category_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {

                categoryNames.add(rs.getString("category_name"));

            }

        } catch (SQLException ex) {

            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return categoryNames;

    }

    /**
     *
     * @param categoryName
     * @return
     */
    public List<MenuItem> getTopMenuItemsByCategoryName(String categoryName) {

        List<MenuItem> list = new ArrayList<>();

        try {

            String query = "SELECT TOP 6\n"
                    + " mi.menu_item_id, mi.category_id, mi.recipe_id, mi.item_name, \n"
                    + " mi.image_url, mi.price, mi.description, mi.status \n"
                    + "FROM menu_item mi \n"
                    + "JOIN category c ON mi.category_id = c.category_id \n"
                    + "WHERE LOWER(c.category_name) = LOWER(?) \n"
                    + " AND LOWER(mi.status) <> LOWER('Deleted')\n"
                    + "ORDER BY mi.menu_item_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{categoryName});

            while (rs.next()) {

                int menuItemId = rs.getInt("menu_item_id");

                String itemName = rs.getString("item_name");

                String imageUrl = rs.getString("image_url");

                int price = rs.getInt("price");

                String description = rs.getString("description");

                String status = rs.getString("status");

                int categoryId = rs.getInt("category_id");

                int recipeId = rs.getInt("recipe_id");

                Category category = categoryDAO.getElementByID(categoryId);

                Recipe recipe = recipeDAO.getElementByID(recipeId);

                if (category != null && recipe != null) {

                    MenuItem item = new MenuItem(
                            menuItemId,
                            category,
                            recipe,
                            itemName,
                            imageUrl,
                            price,
                            description,
                            status
                    );

                    list.add(item);

                }

            }

        } catch (SQLException ex) {

            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, "Can't not load object", ex);

        }

        return list;

    }

    /**
     *
     *
     *
     * @param categoryName
     *
     * @return *
     */
    public List<MenuItem> getMenuItemsByCategoryName(String categoryName) {

        List<MenuItem> list = new ArrayList<>();

        try {

            String query = "SELECT \n"
                    + " mi.menu_item_id, mi.category_id, mi.recipe_id, mi.item_name, \n"
                    + " mi.image_url, mi.price, mi.description, mi.status \n"
                    + "FROM menu_item mi \n"
                    + "JOIN category c ON mi.category_id = c.category_id \n"
                    + "WHERE LOWER(c.category_name) = LOWER(?) \n"
                    + " AND LOWER(mi.status) = 'active'\n"
                    + "ORDER BY mi.menu_item_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{categoryName});

            while (rs.next()) {

                int menuItemId = rs.getInt("menu_item_id");

                String itemName = rs.getString("item_name");

                String imageUrl = rs.getString("image_url");

                int price = rs.getInt("price");

                String description = rs.getString("description");

                String status = rs.getString("status");

                int categoryId = rs.getInt("category_id");

                int recipeId = rs.getInt("recipe_id");

                Category category = categoryDAO.getElementByID(categoryId);

                Recipe recipe = recipeDAO.getElementByID(recipeId);

                if (category != null && recipe != null) {

                    MenuItem item = new MenuItem(
                            menuItemId,
                            category,
                            recipe,
                            itemName,
                            imageUrl,
                            price,
                            description,
                            status
                    );

                    list.add(item);

                }

            }

        } catch (SQLException ex) {

            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, "Can't not load object", ex);

        }

        return list;

    }

    public MenuItem getElementByID(int id) {

        try {

            String query = "SELECT menu_item_id, category_id, recipe_id, item_name, image_url, price, description, status\n"
                    + "FROM     menu_item\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted')) AND (menu_item_id = ?)";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {

                int menuItemId = rs.getInt(1);

                int categoryId = rs.getInt(2);

                int recipeId = rs.getInt(3);

                String itemName = rs.getString(4);

                String imageUrl = rs.getString(5);

                int price = rs.getInt(6);

                String description = rs.getString(7);

                String status = rs.getString(8);

                MenuItem item = new MenuItem(menuItemId,
                        categoryDAO.getElementByID(categoryId),
                        recipeDAO.getElementByID(recipeId),
                        itemName, imageUrl, price, description, status);

                return item;

            }

        } catch (SQLException ex) {

            System.out.println("Can't not load object");

        }

        return null;

    }

    public List<MenuItem> searchAll(String keyword, int page, int maxElement) {
        List<MenuItem> list = new ArrayList<>();
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";

        String query = "SELECT menu_item_id, category_id, recipe_id, item_name, image_url, price, description, status "
                + "FROM [menu_item] "
                + "WHERE LOWER(status) <> LOWER('Deleted') "
                + "AND (LOWER(item_name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)) "
                + "ORDER BY menu_item_id "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY";

        try ( ResultSet rs = this.executeSelectionQuery(query, new Object[]{searchKeyword, searchKeyword, (page - 1) * maxElement, maxElement})) {
            while (rs.next()) {
                int itemId = rs.getInt("menu_item_id");
                int categoryId = rs.getInt("category_id");
                int recipeId = rs.getInt("recipe_id");
                String itemName = rs.getString("item_name");
                String imageUrl = rs.getString("image_url");
                int price = rs.getInt("price");
                String description = rs.getString("description");
                String status = rs.getString("status");

                Category category = categoryDAO.getElementByID(categoryId);
                Recipe recipe = recipeDAO.getElementByID(recipeId);

                MenuItem item = new MenuItem(itemId, category, recipe, itemName, imageUrl, price, description, status);
                list.add(item);
            }
        } catch (SQLException ex) {
            System.out.println("Can't load menu item list: " + ex.getMessage());
        }

        return list;
    }

    /**
     *
     * @return
     */
    public int countItem() {
        try {
            String query = "SELECT COUNT(mi.menu_item_id) AS numrow "
                    + "FROM menu_item AS mi "
                    + "WHERE LOWER(mi.status) <> 'deleted'";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return 0;
    }
public int countItem(String keyword) {
    String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";
    try {
        String query = "SELECT COUNT(mi.menu_item_id) AS numrow "
                + "FROM menu_item AS mi "
                + "WHERE LOWER(mi.status) <> 'deleted' "
                + "AND (LOWER(mi.item_name) LIKE LOWER(?) OR LOWER(mi.description) LIKE LOWER(?))";
        
        // Pass the keyword twice, for both item_name and description
        ResultSet rs = this.executeSelectionQuery(query, new Object[]{searchKeyword, searchKeyword});
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException ex) {
        System.out.println("Error counting search items: " + ex.getMessage());
    }
    return 0;
}
    /**
     * Adds a new menu item to the database.
     *
     * @param item The MenuItem object to add.
     * @return The number of rows affected (1 on success, -1 on failure).
     */
    public int add(MenuItem item) {
        String query = "INSERT INTO [menu_item] (category_id, recipe_id, item_name, image_url, price, description,status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            return this.executeQuery(query, new Object[]{
                item.getCategory().getCategoryId(),
                item.getRecipe().getRecipeId(),
                item.getItemName(),
                item.getImageUrl(),
                item.getPrice(),
                item.getDescription(),
                "Active"
            });
        } catch (SQLException ex) {
            System.out.println("Can't add menu item: " + ex.getMessage());
        }
        return -1;
    }

    /**
     * Updates an existing menu item in the database.
     *
     * @param item The MenuItem object with updated data.
     * @return The number of rows affected (1 on success, -1 on failure).
     */
    public int edit(MenuItem item) {
        String query = "UPDATE [menu_item] "
                + "SET category_id = ?, recipe_id = ?, item_name = ?, image_url = ?, price = ?, description = ?, status = ? "
                + "WHERE menu_item_id = ?";

        try {
            return this.executeQuery(query, new Object[]{
                item.getCategory().getCategoryId(),
                item.getRecipe().getRecipeId(),
                item.getItemName(),
                item.getImageUrl(),
                item.getPrice(),
                item.getDescription(),
                item.getStatus(),
                item.getMenuItemId()
            });
        } catch (SQLException ex) {
            System.out.println("Can't edit menu item: " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param id
     * @return
     */
    public int delete(int id) {
        String query = "UPDATE [menu_item] "
                + "SET status = 'Deleted' "
                + "WHERE menu_item_id = ?";

        try {
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Can't delete menu item: " + ex.getMessage());
        }
        return -1;
    }

    public boolean checkItemNameExist(String itemName, int excludeId) {
        try {
            String query = "SELECT mi.menu_item_id FROM menu_item AS mi "
                    + "WHERE LOWER(mi.status) <> 'deleted' AND mi.item_name = ? AND mi.menu_item_id <> ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{itemName, excludeId});
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}