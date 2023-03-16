package cn.jianwoo.chatgpt.api.constants;

/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 17:18
 */
public final class Constants {
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String TRUE_1 = "1";
    public static final String FALSE_0 = "0";

    public static final String ONE = "1";
    public static final String ZERO = "0";
    public static final String SYSTEM = "SYSTEM";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    public static final String NEGATIVE_ONE = "-1";

    public static final String URL_SEPARATOR = "/";
    public static final String BLANK = "";
    public static final String SPACE = " ";
    public static final String COMMA_SEPARATOR = ",";
    public static final String SEMICOLON = ";";
    public static final String FILE_END_LINE = "\r\n";
    public static final String ELLIPSIS = "...";
    public static final String URL_PARAM_PREFIX = "?";
    public static final String SYMBOL_AMD = "&";
    public static final String SYMBOL_EQUALS = "=";

    public static final String SYMBOL_PERCENT = "%";
    public static final String SYMBOL_AIT = "@";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String HASH_ALGORITHM = "MD5";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public static final String SEPARATE_HYPHEN = "_";
    public static final String FILE_POINT = ".";
    //asterisk
    public static final String ASTERISK = "*";
    //Protocol
    public static final String FILE_PROTOCOL = "file:";

    public static final String BLOG_AUTHOR = "博主";
    public static final String UNKNOW = "未知";
    public static final String ANAONYMOUS = "匿名";

    //validate length
    public static final Integer TITLE_LENGTH = 100;
    public static final Integer AUTHOR_LENGTH = 10;
    public static final Integer TAGS_LENGTH = 15;

    public static final Integer MENU_TEXT_LENGTH = 10;
    public static final Integer MENU_NAME_LENGTH = 20;
    public static final Integer URL_LENGTH = 100;
    public static final Integer NICK_LENGTH = 20;
    public static final Integer EMAIL_LENGTH = 30;

    public static final Integer COMMENT_MIN_CONTENT_LENGTH = 5;
    public static final Integer COMMENT_MAX_CONTENT_LENGTH = 200;

    public static final Integer MESSAGE_BOARD_MIN_CONTENT_LENGTH = 5;
    public static final Integer MESSAGE_BOARD_MAX_CONTENT_LENGTH = 200;
    //reg
    public static final String MENU_NAME_REGEX = "^[_#$@\\d\\w]*$";
    public static final String CLEAR_HTML_TAGS_WITHOUT_MEDIA_REGEX = "\\<(?!img |/?video|source ).*?>";
    public static final String CLEAR_HTML_TAGS_REGEX = "\\<.*?>";
    public static final String NEW_LINE_REGEX = "\n";
    public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    public static final String PHONE_REGEX = "^1\\d{10}$";
    //密码包含 数字,英文,字符中的两种以上，长度6-20
    public static final String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,20}$";
    //日期正则:yyyy-MM-dd
    public static final String DATE_REGEX = "^(\\d{4})[-\\/](\\d{1}|0\\d{1}|1[0-2])([-\\/](\\d{1}|0\\d{1}|[1-2][0-9]|3[0-1]))*$";
    //日期时间正则:yyyy-MM-dd HH:mm:ss
    public static final String DATETIME_REGEX = "^(\\d{4})[-\\/](\\d{1}|0\\d{1}|1[0-2])([-\\/](\\d{1}|0\\d{1}|[1-2][0-9]|3[0-1]))*\\s(\\d{1}|[0-1]\\d{1}|2[0-3])[:]([0-5]\\d{1})[:]([0-5]\\d{1})$";
    // url正则
    public static final String URL_REGEX="^http(s?):\\/\\/(([\\w-]+\\.)+[\\w-]+|((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3})(:\\d{1,5})?(\\/.*)?$";
    //ipv4正则
    public static final String IPV4_REGEX="^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";

   //昵称 (中文数字英文和$_)
    public static final String NICK_REGEX="^[\u4E00-\u9FFF\\d\\w_$]+$";


    //path
    public static final String ALL_PATH_PATTERNS = "/**";
    public static final String ALL_STATIC_PATTERNS = "/static/**";
    public static final String ALL_RES_PATTERNS = "/res/**";
    public static final String ALL_ADMIN_API = "/api/admin/**";
    public static final String ALL_FILE_API = "/api/file/**";
    public static final String ALL_ADMIN_PAGE= "/admin**";

    //file

    public static final String FILE_EXT_JPG= ".jpg";

    public static final String ALL_ADMIN_PASSPORT= "/admin/passport";

    //admin
    public static final String ADMIN = "admin";
    public static final String ADMIN_CN = "管理员";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String CAPTCHA_TOKEN = "captcha_token";
    public static final String GUID = "guid";
    public static final String LOGIN_IP = "login_ip";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String WRONG_PASSWORD = "用户名或密码错误!";
    public static final String SUCCESS_LOGIN = "登录成功!!";
    public static final String SUCCESS_LOGOUT = "退出成功!!";
    public static final String LOGOUT_DESC1 = "用户退出";
    public static final String LOGOUT_DESC2 = "token失效退出";
    public static final String USER_KEY = "USER_ID";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String REFRESH_ACCESS_TOKEN = "refresh_access_token";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String LOGIN_USER_STATUS = "LOGIN_USER_STATUS";
    public static final String LOGIN_ID_SECRET = "login_id_secret";
    public static final String LOGIN_ID = "login_id";
    public static final String USER_PREFIX = "jw_";

    public static final String LOGIN_SESSION = "JIANWOO.LOGIN.SESSION";
    public static final String IP_SPLIT = "@@";
    public static final String EMAIL_TPL_CODE_EXCEPTION = "-1";
    public static final Integer CATEGORY_NULL = -1;
    public static final String COMMENT_HAS_DELETE = "该评论已经删除!";
    public static final String MESSAGE_HAS_DELETE = "该留言已经删除!";
    public static final String ARTICLE_NEED_PASSWORD = "文章[%s]需要验证密码!";


}
