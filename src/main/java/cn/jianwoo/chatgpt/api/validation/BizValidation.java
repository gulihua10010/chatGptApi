package cn.jianwoo.chatgpt.api.validation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import cn.jianwoo.chatgpt.api.exception.ValidationException;
/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 16:03
 */
public class BizValidation {
    private static final Logger logger = LoggerFactory.getLogger(BizValidation.class);

    /**
     * 验证字符串是否为空
     *
     * @param paramValue 字符串值
     * @param paramName  字段名
     * @return
     * @author gulihua
     */
    public static void paramValidate(String paramValue, String paramName) throws ValidationException {
        if (StringUtils.isBlank(paramValue)) {
            throw ValidationException.VALIDATOR_PARAM_IS_NULL
                    .formatMsg("Parameter verified failed, the value is empty in the parameter: %s", paramName)
                    .print();
        }

    }

    /**
     * 验证List是否为空
     *
     * @param paramValue List值
     * @param paramName  字段名
     * @return
     * @author gulihua
     */
    public static void paramValidate(List paramValue, String paramName) throws ValidationException {
        if (CollectionUtils.isEmpty(paramValue)) {
            throw ValidationException.VALIDATOR_PARAM_IS_NULL
                    .formatMsg("Parameter verified failed, the list is empty in the parameter: %s", paramName)
                    .print();
        }

    }

    /**
     * 验证数组是否为空
     *
     * @param paramValue 数组值
     * @param paramName  字段名
     * @return
     * @author gulihua
     */
    public static void paramValidate(Object[] paramValue, String paramName) throws ValidationException {
        if (null == paramValue || paramName.length() == 0) {
            throw ValidationException.VALIDATOR_ARRAY_PARAM_IS_EMPTY
                    .formatMsg(ValidationException.DEFAULT_VALIDATION_MSG + " in the parameter: %s", paramName)
                    .print();
        }

    }

    /**
     * 验证对象是否为空
     *
     * @param paramValue 对象值
     * @param paramName  字段名
     * @return
     * @author gulihua
     */
    public static void paramValidate(Object paramValue, String paramName) throws ValidationException {
        if (null == paramValue) {
            throw ValidationException.VALIDATOR_PARAM_IS_NULL
                    .getNewInstance(ValidationException.DEFAULT_VALIDATION_MSG + " in the parameter: %s", paramName)
                    .print();
        }

    }

    /**
     * 验证字符串是否为空
     *
     * @param paramValue 字符串值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramValidate(String paramValue, String paramName, String msg) throws ValidationException {
        if (StringUtils.isBlank(paramValue)) {
            logger.error("Parameter verified failed, the value is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_NULL, msg, paramName);

        }

    }

    /**
     * 验证List是否为空
     *
     * @param paramValue List值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramValidate(List paramValue, String paramName, String msg) throws ValidationException {
        if (CollectionUtils.isEmpty(paramValue)) {
            logger.error("Page parameter verified failed, the list is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_LIST_EMPTY, msg, paramName);
        }

    }

    /**
     * 验证字符串是否为空
     *
     * @param paramValue 字符串值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramValidate(Object[] paramValue, String paramName, String msg) throws ValidationException {
        if (null == paramValue || paramValue.length == 0) {
            logger.error("Parameter verified failed, the array is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_ARRAY_EMPTY, msg, paramName);

        }

    }

    /**
     * (最大长度验证)验证字符串是否小于最大长度
     *
     * @param paramValue 字符串值
     * @param length     最大长度
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramLengthValidate(String paramValue, Integer length, String paramName, String msg) throws ValidationException {
        if (null != paramValue && paramValue.length() > length) {
            logger.error("Parameter verified failed, the length({}) of parameter '{}' is greater than {}. ", paramValue.length(), paramName, length);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_STRING_LENGTH, msg, paramName);

        }

    }

    /**
     * 验证日期是否在指定范围内
     *
     * @param paramValue 字符串值
     * @param format     日期格式
     * @param rangeFrom  日期开始
     * @param rangeTo    日期几位数
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramDateRangeValidate(String paramValue, String format, Date rangeFrom, Date rangeTo, String paramName, String msg) throws ValidationException {
        if (null != paramValue) {
            Date date = DateUtil.parse(paramValue, format);
            if ((null != rangeFrom && date.before(rangeFrom)) || (null != rangeTo && date.after(rangeTo))) {
                logger.error("Parameter verified failed, the value [{}] of parameter '{}' is not in range {} ~ {}. ",
                        paramValue, paramName, DateUtil.format(rangeTo, "yyyy-MM-dd"),
                        DateUtil.format(rangeFrom, "yyyy-MM-dd"));
                throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_DATE, msg, paramName);

            }

        }

    }

    /**
     * (最小长度验证)验证字符串是否大于最小长度
     *
     * @param paramValue 字符串值
     * @param length     最大长度
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramMinLengthValidate(String paramValue, Integer length, String paramName, String msg) throws ValidationException {
        if (null != paramValue && paramValue.length() < length) {
            logger.error("Parameter verified failed, the length({}) of parameter '{}' is letter than {}. ", paramValue.length(), paramName, length);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_STRING_LENGTH, msg, paramName);

        }

    }

    /**
     * (最小数字验证)验证数字是否大于最小数字
     *
     * @param paramValue 字符串值
     * @param min        最小数字
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramNumberMinValidate(String paramValue, String min, String paramName, String msg) throws ValidationException {
        BigDecimal v;
        try {
            v = new BigDecimal(paramValue);
        } catch (Exception e) {
            String errMsg = String.format("Parameter verified failed, the value[%s] of parameter '%s' is not a number. ", paramValue, paramName);
            logger.error(errMsg);
            throw new ValidationException(ExceptionConstants.VALIDATOR_NUMBER, String.format(ExceptionConstants.VALIDATOR_NUMBER_DESC, paramName), paramName);
        }
        BigDecimal minV;
        try {
            minV = new BigDecimal(min);
        } catch (Exception e) {
            String errMsg = String.format("The value of min '%s' is not a number. ", min);
            logger.error(errMsg);
            minV = new BigDecimal("0");
        }
        if (v.compareTo(minV) < 0) {
            logger.error("Parameter verified failed, the value[{}] of parameter '{}' is letter than {}. ", paramValue, paramName, min);
            throw new ValidationException(ExceptionConstants.VALIDATOR_NUMBER_MIN, msg, paramName);

        }

    }

    /**
     * (最大数字验证)验证数字是否小于最大数字
     *
     * @param paramValue 字符串值
     * @param max        最大数字
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramNumberMaxValidate(String paramValue, String max, String paramName, String msg) throws ValidationException {
        BigDecimal v;
        try {
            v = new BigDecimal(paramValue);
        } catch (Exception e) {
            String errMsg = String.format("Parameter verified failed, the value[%s] of parameter '%s' is not a number. ", paramValue, paramName);
            logger.error(errMsg);
            throw new ValidationException(ExceptionConstants.VALIDATOR_NUMBER, String.format(ExceptionConstants.VALIDATOR_NUMBER_DESC, paramName), paramName);
        }
        BigDecimal maxV;
        try {
            maxV = new BigDecimal(max);
        } catch (Exception e) {
            String errMsg = String.format("The value of max '%s' is not a number. ", max);
            logger.error(errMsg);
            maxV = new BigDecimal("0");
        }
        if (v.compareTo(maxV) > 0) {
            logger.error("Parameter verified failed, the value[{}] of parameter '{}' is greater than {}. ", paramValue, paramName, max);
            throw new ValidationException(ExceptionConstants.VALIDATOR_NUMBER_MAX, msg, paramName);

        }

    }

    /**
     * 字符串正则验证
     *
     * @param paramValue 字符串值
     * @param regex      正则表达式
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramRegexValidate(String paramValue, String regex, String paramName, String msg) throws ValidationException {
        if (StringUtils.isNotBlank(paramValue) && !Pattern.matches(regex, paramValue)) {
            logger.error("Parameter verified failed, the regular expression is {}, but field[{}] value is {}. ", regex, paramName, paramValue);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_STRING_REGEX, msg, paramName);

        }

    }

    /**
     * 数字格式验证(验证字符串是否为数字)
     *
     * @param paramValue 数字的字符串值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramNumberValidate(String paramValue, String paramName, String msg) throws ValidationException {
        if (StringUtils.isNotBlank(paramValue)) {
            try {
                new BigDecimal(paramValue);
            } catch (Exception e) {
                logger.error("Parameter verified failed, the value[{}] of parameter '{}' is not a number", paramValue, paramName);
                throw new ValidationException(ExceptionConstants.VALIDATOR_NUMBER, msg, paramName);
            }
        }

    }


    /**
     * 验证对象是否为空
     *
     * @param paramValue 对象值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramValidate(Object paramValue, String paramName, String msg) throws ValidationException {
        if (null == paramValue) {
            logger.error("Parameter verified failed, the value is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_NULL, msg, paramName);

        }

    }


    /**
     * 验证日期格式
     *
     * @param paramValue 对象值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramDateValidate(String paramValue, String paramName, String msg) throws ValidationException {
        if (StringUtils.isNotBlank(paramValue) && !ReUtil.isMatch(Constants.DATE_REGEX, StringUtils.trim(paramValue))) {
            logger.error("Parameter verified failed, the date format [yyyy-MM-dd] does not match with param({}) value: {}", paramName, paramValue);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_DATE, msg, paramName);

        }

    }


    /**
     * 验证日期时间格式
     *
     * @param paramValue 对象值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramDateTimeValidate(String paramValue, String paramName, String msg) throws ValidationException {
        if (StringUtils.isNotBlank(paramValue) && !ReUtil.isMatch(Constants.DATETIME_REGEX, StringUtils.trim(paramValue))) {
            logger.error("Parameter verified failed, the date format [yyyy-MM-dd HH:mm:ss] does not match with param({}) value: {}", paramName, paramValue);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_DATE, msg, paramName);

        }

    }

    /**
     * 验证对象是否在数组范围内
     *
     * @param paramValue 对象值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @param values     数组范围
     * @return
     * @author gulihua
     */
    public static void paramRangeValidate(Object paramValue, String paramName, String msg, Object... values)
            throws ValidationException {
        boolean valid = false;
        if (null != paramValue) {
            for (Object o : values) {
                if (paramValue.equals(o)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                logger.error("Parameter {} verified failed, the value {} is not in range: {}", paramName, paramValue,
                        JSON.toJSONString(values));
                throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_NOT_IN_RANGE, msg, paramName);

            }
        }

    }

    /**
     * 验证List内容是否为空
     *
     * @param paramValue 字符串 List值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramValidateListContent(List<String> paramValue, String paramName, String msg) throws ValidationException {
        if (!CollectionUtils.isEmpty(paramValue)) {
            for (String s : paramValue) {
                if (StringUtils.isBlank(s)) {
                    logger.error("Page parameter verified failed, some list content is empty in the parameter: {}", paramName);
                    throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_LIST_CONTENT_EMPTY, msg, paramName);
                }
            }

        }

    }

    /**
     * 验证文件最大的长度
     *
     * @param fileObj 文件对象
     * @param maxSize 最大长度
     * @param msg     错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramFileSizeValidate(MultipartFile fileObj, Long maxSize, String msg) throws ValidationException {
        if (null != fileObj) {
            if (fileObj.getSize() > maxSize) {
                logger.error("Parameter verified failed,the file size exceeds the maximum limit: {}, current size: {}", maxSize, fileObj.getSize());
                throw ValidationException.VALIDATOR_FILE_SIZE_MAX
                        .formatMsg(msg, maxSize)
                        .print();
            }
        }

    }


    /**
     * 验证类别是否为空
     *
     * @param paramValue 字符串值
     * @param paramName  字段名
     * @param msg        错误提示消息
     * @return
     * @author gulihua
     */
    public static void paramCategoryValidate(Integer paramValue, String paramName, String msg) throws ValidationException {
        if (null == paramValue) {
            logger.error("Parameter verified failed, the value is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_NULL, msg, paramName);

        }
        if (Constants.CATEGORY_NULL.equals(paramValue)) {
            logger.error("Parameter verified failed, the value is empty in the parameter: {}", paramName);
            throw new ValidationException(ExceptionConstants.VALIDATION_FAILED_NULL, msg, paramName);

        }
    }



}
