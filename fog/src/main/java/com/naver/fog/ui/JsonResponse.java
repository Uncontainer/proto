package com.naver.fog.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pulsarang on 2015. 4. 21..
 */
public class JsonResponse {
    public static final String HTML_TEMPLATE = "ajax:json_html";
    public static final String DATA_HTML_TEMPLATE = "ajax:json_data_html";
    public static final String SUCCESS_TEMPLATE = "ajax:json_success";
    public static final String FAIL_TEMPLATE = "ajax:json_fail";

    public static final String ATTR_HTML_TEMPLATE = "__template";
    public static final String ATTR_ERROR_CODE = "__errorCode";
    public static final String ATTR_ERROR_MESSAGE = "__errorMessage";
    public static final String ATTR_BINDING_RESULT = "__bindingResult";

    public static final String RESULT_TEMPLATE = "ajax:json_template";
    public static final String RESULT_TWO_TEMPLATE = "ajax:json_two_template";
    public static final String RESULT_VALIDERROR = "ajax:json_validation_error";
    public static final String RESULT = "ajax:json_result";
    public static final String RESULT_FAIL_VALIDATION_ERROR = "ajax:json_result_validation_error";
    public static final String RESULT_FORM_SAVE = "ajax:json_result_form_save";
    public static final String JSONP_RESULT = "ajax:json_result_jsonp";
    public static final String RESULT_SUCCESS = "ajax:json_result_success";
    public static final String RESULT_FORMLESS = "ajax:json_result_formless";

    public static final String ATTR_JSON_RESULT = "__json_result";
    public static final String ATTR_SUCCESS = "__json_success";
    public static final String ATTR_REDIRECT_PAGE = "redirectPage";
    public static final String ATTR_MESSAGE_CODE = "messageCode";
    public static final String ATTR_MESSAGE = "message";

    public static final String ATTR_INTERCEPTOR_FILTERED_MESSAGE_ARGS = "__messageArgs";
    public static final String ATTR_INTERCEPTOR_FILTERED_MESSAGE_CODE = "__messageCode";

    public static String jsonTemplate(String template, ModelMap modelMap) {
        modelMap.put("_template", template);
        return RESULT_TEMPLATE;
    }

    public static String jsonTwoTemplate(String template1, String template2, ModelMap modelMap) {
        modelMap.put("_template1", template1);
        modelMap.put("_template2", template2);
        return RESULT_TWO_TEMPLATE;
    }

    public static String jsonValidationError(BindingResult result, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, result);

        return RESULT_VALIDERROR;
    }

    public static String jsonResult(String returnUrl, String message, ModelMap modelMap) {

        Map<String, Object> resultMessage = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(returnUrl)) {
            resultMessage.put(ATTR_REDIRECT_PAGE, returnUrl);
        }
        resultMessage.put("message", message);

        return jsonResultByMap(resultMessage, modelMap);
    }

    /**
     * json 처리결과를 보인후, 페이지 이동이 필요하지 않은 경우 사용
     */
    public static String jsonResult(String message, ModelMap modelMap) {
        return jsonResult("", message, modelMap);
    }

    /**
     * form 저장 결과에 대한 리턴
     *
     * @param returnUrl
     * @param messageCode
     * @param modelMap
     * @return
     */
    @Deprecated
    public static String returnSaveResult(String returnUrl, String messageCode, ModelMap modelMap) {
        if (StringUtils.isNotEmpty(returnUrl)) {
            modelMap.put(ATTR_REDIRECT_PAGE, returnUrl);
        }
        modelMap.put(ATTR_MESSAGE_CODE, messageCode);

        return RESULT_FORM_SAVE;
    }

    public static String returnSaveResult(String messageCode, ModelMap modelMap) {
        modelMap.put(ATTR_MESSAGE_CODE, messageCode);

        return RESULT_FORM_SAVE;
    }

    public static String jsonResultByMap(Map<String, Object> result, ModelMap modelMap, String callbackFunctionName) {
        modelMap.put("callbackFunctionName", callbackFunctionName);
        modelMap.put(ATTR_JSON_RESULT, result);

        return JSONP_RESULT;
    }

    public static String jsonResultByMap(Map<String, Object> result, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, result);

        return RESULT;
    }

    public static String jsonResultByObject(Object obj, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, obj);

        return RESULT;
    }

    public static String jsonResultSuccess() {
        return RESULT_SUCCESS;
    }

    public static String jsonResultFail(String message, ModelMap modelMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", message);
        modelMap.put(ATTR_JSON_RESULT, result);
        modelMap.put(ATTR_SUCCESS, "false");
        return RESULT;
    }

    public static String jsonResultFailValidationError(BindingResult bindingResult, ModelMap modelMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        modelMap.put("bindingResult", bindingResult);
        modelMap.put(ATTR_SUCCESS, "false");
        return RESULT_FAIL_VALIDATION_ERROR;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> String jsonResultFormless(Object result, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, result);
        return RESULT_FORMLESS;
    }

    /**
     * html, success, fail 메시지에 대한 결과를 JSONP로 작성하고 싶은 경우,
     * html, success, fail 함수를 호출하기 전에 호출한다.
     * @param callbackFunctionName
     * @param modelMap
     */
    public static void jsonp(String callbackFunctionName, ModelMap modelMap) {
        modelMap.put("__callbackFunctionName", callbackFunctionName);
    }

    /**
     * html 응답을 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : true,
     *     "htReturnValue" : {
     *	       "html" : "json 형태의 마크업"
     *     }
     * }
     * @param template template ftl 경로명
     * @param modelMap
     * @return
     */
    public static String html(String template, ModelMap modelMap) {
        modelMap.put(ATTR_HTML_TEMPLATE, template);
        return HTML_TEMPLATE;
    }

    /**
     * data와 html 응답을 반환한다.
     * {
     *     "bSuccess" : true,
     *     "htReturnValue" : {
     *     	    "data" : "json 데이터",
     *	       "html" : "json 형태의 마크업"
     *     }
     * }
     * @param template template ftl 경로명
     * @param modelMap
     * @return
     */
    public static String dataHtml(Object obj, String template, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, obj);
        modelMap.put(ATTR_HTML_TEMPLATE, template);
        return DATA_HTML_TEMPLATE;
    }

    /**
     * success 응답을 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : true
     * }
     * @return
     */
    public static String success() {
        return SUCCESS_TEMPLATE;
    }

    /**
     * redirectUrl, 메시지를 포함한 success 응답을 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : true,
     *     "htReturnValue" : {
     *         "redirectPage" : "리다이렉트 url"
     *         , message : "전달한 메시지"
     *     }
     * }
     * @param redirectUrl
     * @param messageCode
     * @param modelMap
     * @return
     */
    public static String success(String redirectUrl, String messageCode, ModelMap modelMap) {
        return success(redirectUrl, messageCode, null, modelMap);
    }

    /**
     * redirectUrl, 메시지를 포함한 success 응답을 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : true,
     *     "htReturnValue" : {
     *         "redirectPage" : "리다이렉트 url"
     *         , message : "전달한 메시지"
     *     }
     * }
     * @param redirectUrl
     * @param messageCode
     * @param modelMap
     * @return
     */
    public static String success(String redirectUrl, String messageCode, Object[] messageArgs, ModelMap modelMap) {
        Map<String, Object> data = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(redirectUrl)) {
            data.put(ATTR_REDIRECT_PAGE, redirectUrl);
        }

        if (messageArgs != null) {
            data.put(ATTR_INTERCEPTOR_FILTERED_MESSAGE_ARGS, messageArgs);
        }

        data.put(ATTR_INTERCEPTOR_FILTERED_MESSAGE_CODE, messageCode);

        return success(data, modelMap);
    }

    /**
     * 데이터를 포함한 success 응답을 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : true,
     *     "htReturnValue" : "json 형태의 데이터"
     * }
     * @param data
     * @param modelMap
     * @return
     */
    public static String success(Object data, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, data);
        return SUCCESS_TEMPLATE;
    }

    /**
     * fail 메시지를 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : false
     * }
     * @return
     */
    public static String fail() {
        return FAIL_TEMPLATE;
    }

    /**
     * 데이터를 포함한 fail 메시지를 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : false,
     *     "htReturnValue" : "json 형태의 데이터"
     * }
     * @param data
     * @param modelMap
     * @return
     */
    public static String fail(Object data, ModelMap modelMap) {
        modelMap.put(ATTR_JSON_RESULT, data);
        return FAIL_TEMPLATE;
    }

    /**
     * validation 에러를 반환한다.
     * @param bindingResult
     * @param modelMap
     * @return
     */
    public static String fail(BindingResult bindingResult, ModelMap modelMap) {
        modelMap.put(ATTR_BINDING_RESULT, bindingResult);
        modelMap.put(ATTR_ERROR_CODE, "INVALID_FORM_DATA");
        return FAIL_TEMPLATE;
    }

    /**
     * errorCode 및 errorMessage를 포함한 fail 메시지를 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : false,
     *     , "sErrorCode" : "전달한 메시지 코드"
     *     , "sErrorMessage" : "전달한 에러 메시지"
     * }
     * @param errorCode
     * @param errorMessage
     * @param modelMap
     * @return
     */
    public static String fail(String errorCode, String errorMessage, ModelMap modelMap) {
        modelMap.put(ATTR_ERROR_CODE, errorCode);
        modelMap.put(ATTR_ERROR_MESSAGE, errorMessage);
        return FAIL_TEMPLATE;
    }

    /**
     * 데이터, errorCode, errorMessage를 포함한 fail 메시지를 반환한다.
     * 반환되는 응답 json내용은 다음의 포맷을 따른다.
     * {
     *     "bSuccess" : false,
     *     , "sErrorCode" : "전달한 메시지 코드"
     *     , "sErrorMessage" : "전달한 에러 메시지"
     *     , "htReturnValue" : "json 형태로 변환된 데이터"
     * }
     * @param errorCode
     * @param errorMessage
     * @param data
     * @param modelMap
     * @return
     */
    public static String fail(String errorCode, String errorMessage, Object data, ModelMap modelMap) {
        modelMap.put(ATTR_ERROR_CODE, errorCode);
        modelMap.put(ATTR_ERROR_MESSAGE, errorMessage);
        modelMap.put(ATTR_JSON_RESULT, data);
        return FAIL_TEMPLATE;
    }
}
