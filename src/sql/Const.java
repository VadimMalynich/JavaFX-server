package sql;

public class Const {
    public static Integer connectedUsers = 0;

    //    TABLE USERS
    public static final String USER_TABLE = "users";
    public static final String USER_ID = "idusers";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";

    //    TABLE COMPANIES
    public static final String COMPANY_TABLE = "companies";
    public static final String COMPANY_NAME = "name";
    public static final String COMPANY_CATEGORY = "category";
    public static final String COMPANY_EMAIL = "email";
    public static final String COMPANY_PHONE = "phoneNumber";
    public static final String COMPANY_STATUS = "companyStatus";

    //    TABLE PRODUCT_REQUEST
    public static final String PRODUCT_REQUEST_TABLE = "product_request";
    public static final String PRODUCT_REQUEST_ID = "idproduct";
    public static final String PRODUCT_REQUEST_PRODUCT_NAME = "productName";
    public static final String PRODUCT_REQUEST_QUANTITY = "quantity";
    public static final String PRODUCT_DEFECT = "defect";
    public static final String PRODUCT_REQUEST_STATUS = "status";

    //    TABLE WAREHOUSES
    public static final String WAREHOUSES_TABLE = "warehouses";
    public static final String WAREHOUSES_ID = "idwarehouses";
    public static final String WAREHOUSES_CATEGORY = "category";
    public static final String WAREHOUSES_CURRENT_AMOUNT = "currentAmount";
    public static final String WAREHOUSES_CAPACITY = "capacity";

    //    TABLE WAREHOUSES
    public static final String PRODUCTS_IN_STOCK_TABLE = "products_in_stock";
}
