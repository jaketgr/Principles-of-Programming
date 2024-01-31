public class ServiceInfoModel {
    public static final int PRODUCT_INFO_SERVICE = 1;
    public static final int ORDER_INFO_SERVICE = 2;
    public static final int PRODUCT_PRICE_UPDATE_SERVICE = 3;

    public static final int PRODUCT_QUANTITY_UPDATE_SERVICE = 4;
    public static final int AUTH_LOGIN_SERVICE = 5;

    public static final int USER_PERMS_SERVICE = 6;

    //public static final int DEREGISTER = 99;

    public int serviceCode;
    public String serviceHostAddress;

    public int serviceHostPort;
}
