package cn.jianwoo.chatgpt.api.constants;

public final class ExceptionConstants {

    public static final String SYSTEM_EXCEPTION = "999999";
    public static final String UNAUTHORIZED = "900001";
    public static final String DAO_INSERT_RESULT_0 = "100001";
    public static final String DAO_UPDATE_RESULT_0 = "100002";
    public static final String DAO_DELETE_RESULT_0 = "100003";
    public static final String DAO_SELECTONE_IS_NULL = "100004";
    public static final String DAO_LIST_IS_NULL = "100005";
    public static final String DAO_PARAM_IS_NULL = "100006";
    public static final String DAO_PARAM_IS_EMPTY = "100007";
    public static final String BIZ_NOT_EXIST = "200001";
    public static final String BIZ_HAS_EXIST = "200002";
    public static final String BIZ_CREATE_FAIL = "200003";
    public static final String BIZ_MODIFY_FAIL = "200004";
    public static final String BIZ_DELETE_FAIL = "200005";
    public static final String BIZ_STATUS_INCORRECT = "200006";
    public static final String BIZ_PASSWORD_INCORRECT = "200007";
    public static final String BIZ_PAGE_TIMEOUT = "200008";
    public static final String BIZ_OPERATION_FAILED = "200009";
    public static final String BIZ_NOT_MATCH = "200010";

    public static final String BIZ_OPERATION_PASSWORD_VERIFY = "200011";


    public static final String BIZ_ACCESS_REFUSED = "900000";


    public static final String BIZ_ACCESS_FREQUENTLY = "700001";
    public static final String VALIDATION_FAILED_NULL = "300001";
    public static final String VALIDATION_FAILED_EMPTY = "300002";
    public static final String VALIDATION_FAILED_LIST_EMPTY = "300003";
    public static final String VALIDATION_FAILED_ARRAY_EMPTY = "300004";
    public static final String VALIDATION_FAILED_NOT_IN_RANGE = "300005";
    public static final String VALIDATION_FAILED_STRING_LENGTH = "300006";
    public static final String VALIDATION_FAILED_STRING_REGEX = "300007";
    public static final String VALIDATION_FAILED_LIST_CONTENT_EMPTY = "300008";
    public static final String VALIDATOR_FILE_SIZE_MAX = "300009";
    public static final String VALIDATOR_NUMBER_MIN = "300010";
    public static final String VALIDATOR_NUMBER_MAX = "300011";
    public static final String VALIDATOR_NUMBER = "300012";
    public static final String VALIDATOR_NUMBER_DESC = "字段[%s]的值不是数字类型!";
    public static final String VALIDATION_FAILED = "300000";
    public static final String VALIDATION_FAILED_DATE = "300014";

    public static final String ADMIN_PASSWORD_NOT_CORRECT = "400001";
    public static final String ARTICLE_STATUS_NOT_MATCH = "400002";
    public static final String ARTICLE_STATUS_NOT_MATCH_DESC = "Current status (%s) for article (%s) is not valid.";
    public static final String SYSTEM_EXCEPTION_DESC = "Some errors occur.";
    public static final String LOGOUT_FAILED = "410001";

    public static final String FILE_PATH_INVAILD = "400005";
    public static final String FILE_PATH_INVAILD_DESC = "The path is invalid";
    public static final String DIR_CREATE_FAILED = "400003";
    public static final String DIR_CREATE_FAILED_DESC = "Directory creation failed";
    public static final String FILE_TRANSFER_FAILED = "400004";
    public static final String FILE_TRANSFER_FAILED_DESC = "File transfer failure";
    public static final String FILE_COPY_FAILED = "400005";
    public static final String FILE_COPY_FAILED_DESC = "File copy failed";
    public static final String FILE_DELETE_FAILED = "400006";
    public static final String FILE_DELETE_FAILED_DESC = "File delete failed";


    public static final String MENU_DEL_ARTICLE_EXITS = "400007";
    public static final String MENU_DEL_ARTICLE_EXITS_DESC = "该菜单(%s)下存在文章！请先删除文章或移动到别的类别中！";
    public static final String MENU_DEL_SUB_EXITS = "400008";
    public static final String MENU_DEL_SUB_EXITS_DESC = "该菜单(%s)下存在子菜单！请先删除子菜单！";
    public static final String VALIDATION_FAILED_INVALID_DESC_CN = "参数无效!";

    public static final String FORM_DUPLICATE = "400009";
    public static final String FORM_DUPLICATE_DESC = "操作频繁!请勿重复提交!";

    public static final String LOGIN_CAPTCHA_AUTH_INVALID = "400010";
    public static final String LOGIN_CAPTCHA_AUTH_INVALID_DESC = "验证码验证失败，请刷新页面重试！";
    public static final String UNAUTHORIZED_DESC = "接口鉴权失败，未经授权的操作!";
    public static final String ACCESS_REFUSED_DESC = "拒绝访问!";
    public static final String ACCESS_FREQUENTLY_DESC = "访问频繁, 请稍后再试!";
    public static final String LOGOUT_FAILED_MSG_1 = "退出登录失败, 用户ID secret为空!";
    public static final String LOGOUT_FAILED_MSG_2 = "退出登录失败, 用户未登录!";
    public static final String LOGOUT_FAILED_MSG_3 = "退出登录失败, token解析失败!";

    public static final String EMAIL_SEND_FAILED = "400011";
    public static final String EMAIL_SEND_FAILED_DESC = "邮件发送失败，请检查邮件配置!";

    public static final String USER_BLOCK = "账号已被冻结! 请1小时后重试!";

    public static final String FILE_NOT_EXIST = "300017";
    public static final String FILE_NOT_EXIST_DESC = "文件/目录[%s]不存在!";
    private ExceptionConstants() {
    }

}
