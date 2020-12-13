package sql;

import logic.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public boolean signUpUser(Users user) {
        boolean flag = false;
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USER_LOGIN + ","
                + Const.USER_PASSWORD + "," + Const.USER_ROLE + ")" + "VALUES(?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());
            prSt.setString(3, user.getRole());
            prSt.executeUpdate();
            flag = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public ResultSet getUser(Users user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=? AND " + Const.USER_PASSWORD + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());
            resSet = prSt.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public String returnUserRole(Users user) {
        String role = "";
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            ResultSet resSet = prSt.executeQuery();
            if (resSet.next()) {
                role = resSet.getString(4);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return role;
    }

    public List<Users> getUsers() {
        List<Users> usersList = new ArrayList<>();
        String select = "SELECT * FROM " + Const.USER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                Users user = new Users(resSet.getInt(1), resSet.getString(2),
                        resSet.getString(3), resSet.getString(4));
                usersList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public boolean deleteUser(Users user) {
        boolean flag = false;
        String delete = "DELETE FROM " + Const.USER_TABLE + " WHERE " + Const.USER_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setInt(1, user.getIdUser());
            prSt.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

    public Boolean updateUsers(Users user) {
        boolean flag = false;
        String update = "UPDATE " + Const.USER_TABLE + " SET " + Const.USER_LOGIN + "=?" + ", "
                + Const.USER_PASSWORD + "=?" + ", " + Const.USER_ROLE + "=? WHERE " + Const.USER_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());
            prSt.setString(3, user.getRole());
            prSt.setInt(4, user.getIdUser());
            int result = prSt.executeUpdate();
            if (result != 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void setCompanyInfo(Companies company) {
        String insert = "INSERT INTO " + Const.COMPANY_TABLE + "(" + Const.USER_LOGIN + ","
                + Const.COMPANY_NAME + "," + Const.COMPANY_CATEGORY + "," + Const.COMPANY_EMAIL + ","
                + Const.COMPANY_PHONE + "," + Const.COMPANY_STATUS + ")" + "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, company.getLogin());
            prSt.setString(2, company.getName());
            prSt.setString(3, company.getCategory());
            prSt.setString(4, company.getEmail());
            prSt.setString(5, company.getPhoneNumber());
            prSt.setString(6, company.getStatus());
            prSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Companies checkCompanyInfo(Companies company) {
        String select = "SELECT * FROM " + Const.COMPANY_TABLE + " WHERE " + Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, company.getLogin());
            ResultSet resSet = prSt.executeQuery();
            if (resSet.next()) {
                company.setName(resSet.getString(2));
                company.setCategory(resSet.getString(3));
                company.setEmail(resSet.getString(4));
                company.setPhoneNumber(resSet.getString(5));
                company.setStatus(resSet.getString(6));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return company;
    }

    public boolean deleteCompanyInfo(Users user) {
        boolean flag = false;
        String delete = "DELETE FROM " + Const.COMPANY_TABLE + " WHERE " + Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setString(1, user.getLogin());
            prSt.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

    public Boolean editCompanyInfo(Companies company) {
        boolean flag = false;
        String update = "UPDATE " + Const.COMPANY_TABLE + " SET " + Const.COMPANY_NAME + "=?" + ", "
                + Const.COMPANY_CATEGORY + "=?" + ", " + Const.COMPANY_EMAIL + "=?" + ", " + Const.COMPANY_PHONE + "=?" +
                ", " + Const.COMPANY_STATUS + "=? WHERE " + Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, company.getName());
            prSt.setString(2, company.getCategory());
            prSt.setString(3, company.getEmail());
            prSt.setString(4, company.getPhoneNumber());
            prSt.setString(5, company.getStatus());
            prSt.setString(6, company.getLogin());
            int result = prSt.executeUpdate();
            if (result != 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean updateCompanyTable(Companies company) {
        boolean flag = false;
        String update = "UPDATE " + Const.COMPANY_TABLE + " SET " + Const.COMPANY_STATUS + "=? WHERE " + Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, company.getStatus());
            prSt.setString(2, company.getLogin());
            int result = prSt.executeUpdate();
            if (result != 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<Companies> getCompaniesTableData() {
        List<Companies> companiesList = new ArrayList<>();
        String select = "SELECT * FROM " + Const.COMPANY_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                Companies company = new Companies(resSet.getString(1), resSet.getString(2),
                        resSet.getString(3), resSet.getString(4),
                        resSet.getString(5), resSet.getString(6));
                companiesList.add(company);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return companiesList;
    }

    public Boolean addCompanyRequest(ProductRequest productRequest) {
        boolean flag = false;
        String insert = "INSERT INTO " + Const.PRODUCT_REQUEST_TABLE + "(" + Const.USER_LOGIN + ","
                + Const.COMPANY_CATEGORY + "," + Const.PRODUCT_REQUEST_PRODUCT_NAME + ","
                + Const.PRODUCT_REQUEST_QUANTITY + "," + Const.PRODUCT_REQUEST_STATUS + ")" + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, productRequest.getLogin());
            prSt.setString(2, productRequest.getCategory());
            prSt.setString(3, productRequest.getProductName());
            prSt.setInt(4, productRequest.getProductQuantity());
            prSt.setString(5, productRequest.getStatus());
            prSt.executeUpdate();
            flag = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteCompanyRequest(ProductRequest productRequest) {
        boolean flag = false;
        String delete = "DELETE FROM " + Const.PRODUCT_REQUEST_TABLE + " WHERE " + Const.PRODUCT_REQUEST_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setInt(1, productRequest.getIdRequest());
            prSt.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

    public List<ProductRequest> getRequestData(Companies company, boolean bool) {
        if (bool) {
            List<ProductRequest> requests = new ArrayList<>();
            String select = "SELECT * FROM " + Const.PRODUCT_REQUEST_TABLE + " WHERE " + Const.USER_LOGIN + "=?";
            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(select);
                prSt.setString(1, company.getLogin());
                ResultSet resSet = prSt.executeQuery();
                while (resSet.next()) {
                    ProductRequest request = new ProductRequest(resSet.getInt(1), resSet.getString(2), resSet.getString(3),
                            resSet.getString(4), resSet.getInt(5), resSet.getDouble(6), resSet.getString(7));
                    requests.add(request);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return requests;
        } else {
            List<ProductRequest> requests = new ArrayList<>();
            String select = "SELECT * FROM " + Const.PRODUCT_REQUEST_TABLE;
            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(select);
                ResultSet resSet = prSt.executeQuery();
                while (resSet.next()) {
                    ProductRequest request = new ProductRequest(resSet.getInt(1), resSet.getString(2), resSet.getString(3),
                            resSet.getString(4), resSet.getInt(5), resSet.getDouble(6), resSet.getString(7));
                    requests.add(request);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return requests;
        }
    }

    public Boolean updateProductRequestTable(ProductRequest productRequest) {
        boolean flag = false;
        String update = "UPDATE " + Const.PRODUCT_REQUEST_TABLE + " SET " + Const.PRODUCT_DEFECT + "=?, "
                + Const.PRODUCT_REQUEST_STATUS + "=? WHERE " + Const.PRODUCT_REQUEST_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setDouble(1, productRequest.getDefect());
            prSt.setString(2, productRequest.getStatus());
            prSt.setInt(3, productRequest.getIdRequest());
            int result = prSt.executeUpdate();
            if (result != 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean addWarehouse(Warehouses warehouse) {
        boolean flag = false;
        String insert = "INSERT INTO " + Const.WAREHOUSES_TABLE + "(" + Const.WAREHOUSES_CATEGORY + ","
                + Const.WAREHOUSES_CURRENT_AMOUNT + "," + Const.WAREHOUSES_CAPACITY + ")" + "VALUES(?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, warehouse.getWarehouseCategory());
            prSt.setInt(2, warehouse.getCurrentAmount());
            prSt.setString(3, warehouse.getCapacity());
            prSt.executeUpdate();
            flag = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<Warehouses> getWarehouses() {
        List<Warehouses> warehousesList = new ArrayList<>();
        String select = "SELECT * FROM " + Const.WAREHOUSES_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                Warehouses warehouse = new Warehouses(resSet.getInt(1), resSet.getString(2),
                        resSet.getInt(3), resSet.getString(4));
                warehousesList.add(warehouse);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return warehousesList;
    }

    public boolean deleteWarehouse(Warehouses warehouse) {
        boolean flag = false;
        String delete = "DELETE FROM " + Const.WAREHOUSES_TABLE + " WHERE " + Const.WAREHOUSES_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setInt(1, warehouse.getWarehouseID());
            prSt.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }

    public Boolean updateWarehousesTable(Warehouses warehouse) {
        boolean flag = false;
        String update = "UPDATE " + Const.WAREHOUSES_TABLE + " SET " + Const.WAREHOUSES_CATEGORY + "=?" + ", " +
                Const.WAREHOUSES_CURRENT_AMOUNT + "=?" + ", " + Const.WAREHOUSES_CAPACITY + "=? WHERE " + Const.WAREHOUSES_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, warehouse.getWarehouseCategory());
            prSt.setInt(2, warehouse.getCurrentAmount());
            prSt.setString(3, warehouse.getCapacity());
            prSt.setInt(4, warehouse.getWarehouseID());
            int result = prSt.executeUpdate();
            if (result != 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<String> getWarehousesCategory() {
        List<String> stringList = new ArrayList<>();
        String select = "SELECT " + Const.WAREHOUSES_CATEGORY + " FROM " + Const.WAREHOUSES_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                String str = resSet.getString(1);
                stringList.add(str);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public void setWarehouseCurrentAmount(Warehouses warehouses) {
        String update = "UPDATE " + Const.WAREHOUSES_TABLE + " SET " + Const.WAREHOUSES_CURRENT_AMOUNT + "=? WHERE " + Const.WAREHOUSES_ID + "=?";
        Integer result = 0;
        String select = "SELECT " + Const.PRODUCT_REQUEST_QUANTITY + " FROM " + Const.PRODUCTS_IN_STOCK_TABLE + " WHERE " +
                Const.WAREHOUSES_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            PreparedStatement prSt2 = getDbConnection().prepareStatement(select);
            prSt2.setInt(1, warehouses.getWarehouseID());
            ResultSet resSet = prSt2.executeQuery();
            while (resSet.next()) {
                Integer count = resSet.getInt(1);
                result += count;
            }
            prSt.setInt(1, result);
            prSt.setInt(2, warehouses.getWarehouseID());
            prSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setProductInStock(ProductRequest productRequest) {
        List<Warehouses> warehousesList = getWarehouses();
        Warehouses warehouse = new Warehouses();
        for (Warehouses w : warehousesList) {
            if (productRequest.getCategory().equals(w.getWarehouseCategory())) {
                warehouse = new Warehouses(w.getWarehouseID(), w.getWarehouseCategory(), w.getCurrentAmount(), w.getCapacity());
                w.setCurrentAmount(w.getCurrentAmount() + productRequest.getProductQuantity());
                break;
            }
        }
        String insert = "INSERT INTO " + Const.PRODUCTS_IN_STOCK_TABLE + "(" + Const.WAREHOUSES_ID + ","
                + Const.PRODUCT_REQUEST_ID + "," + Const.PRODUCT_REQUEST_PRODUCT_NAME + "," +
                Const.PRODUCT_REQUEST_QUANTITY + ")" + "VALUES(?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setInt(1, warehouse.getWarehouseID());
            prSt.setInt(2, productRequest.getIdRequest());
            prSt.setString(3, productRequest.getProductName());
            prSt.setInt(4, productRequest.getProductQuantity());
            prSt.executeUpdate();
            for (Warehouses w : warehousesList) {
                updateWarehousesTable(w);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<ProductsInStock> getProductInStock() {
        List<ProductsInStock> productsInStockList = new ArrayList<>();
        String select = "SELECT * FROM " + Const.PRODUCTS_IN_STOCK_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            ResultSet resSet = prSt.executeQuery();

            while (resSet.next()) {
                ProductsInStock products = new ProductsInStock(resSet.getInt(1), resSet.getInt(2),
                        resSet.getString(3), resSet.getInt(4));
                productsInStockList.add(products);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productsInStockList;
    }

    public void deleteProductInStock(ProductRequest productRequest) {
        try {
            List<ProductsInStock> productsInStockList = getProductInStock();
            List<Warehouses> warehousesList = getWarehouses();
            Warehouses warehouse = new Warehouses();
            for (Warehouses w : warehousesList) {
                if (productRequest.getCategory().equals(w.getWarehouseCategory())) {
                    warehouse = new Warehouses(w.getWarehouseID(), w.getWarehouseCategory(), w.getCurrentAmount(), w.getCapacity());
                    for (ProductsInStock products : productsInStockList) {
                        if (products.getIdProduct() == productRequest.getIdRequest()) {
                            w.setCurrentAmount(w.getCurrentAmount() - productRequest.getProductQuantity());
                        }
                    }
                    break;
                }
            }
            String delete = "DELETE FROM " + Const.PRODUCTS_IN_STOCK_TABLE + " WHERE " + Const.PRODUCT_REQUEST_ID + "=?";

            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setInt(1, productRequest.getIdRequest());
            prSt.executeUpdate();
            for (Warehouses w : warehousesList) {
                updateWarehousesTable(w);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}