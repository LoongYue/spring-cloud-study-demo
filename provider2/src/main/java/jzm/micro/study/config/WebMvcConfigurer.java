package jzm.micro.study.config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import jzm.micro.study.core.Result;
import jzm.micro.study.core.ResultCode;
import jzm.micro.study.exception.ServiceException;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory
            .getLogger(WebMvcConfigurer.class);
//    @Value("${spring.profiles.active}")
//    private String env;// 当前激活的配置文件
    
    private final static String SESSION_KEY = "session:";
    
    

    // 访问接口白名单
    private static final List<String> PASSLIST = new ArrayList<String>();

    static {
        PASSLIST.add("LoginController/login");
        PASSLIST.add("LoginController/checklogin");
        PASSLIST.add("LoginController/loginout");
        PASSLIST.add("NovaWDPayRestController/wdPay");
        PASSLIST.add("NovaWDCheckController/wdCheck");
        PASSLIST.add("NovaWDCheckController/checkErrFelow");
        PASSLIST.add("NovaWDPayController/wdPay");
        PASSLIST.add("ApiResourceController/uiConfiguration");
        PASSLIST.add("ApiResourceController/swaggerResources");
        PASSLIST.add("Swagger2Controller/getDocumentation");

    }

    // 统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request,
                    HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (e instanceof ServiceException) {// 业务失败的异常，如“账号或密码错误”
                	String errorCode = ((ServiceException) e).getErrorCode();
                	if(null!=errorCode&&!"".equals(errorCode)) {
                		result.setCode(errorCode);
                		result.setMessage(e.getMessage());
                	}else {
                		result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                	}
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMessage(
                            "接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage(
                            "接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }

        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // 解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT",
                "DELETE");
    }

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。

        // 授权
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                    HttpServletResponse response, Object handler)
                    throws Exception {
                /*if(request.getMethod().equals("OPTIONS")) {
                    return true;
                }
                if(request.getServletPath().equals("/user/login") || request.getServletPath().contains("swagger") || "v2/api".equals(request.getServletPath())) {
                    return true;
                }
                RbacUser user = rbacUserService.getRbacUser(request);
                if(user == null) {
                    logger.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request),
                            JSON.toJSONString(request.getParameterMap()));
                    Result result = new Result();
                    result.setCode(ResultCode.FAIL)
                            .setMessage("用户没有登录");
                    responseResult(response, result);
                    return false;
                }*/
                return true;
            }
        });
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write("后面再补上内容");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 一个简单的签名认证，规则： 1. 将请求参数按ascii码排序 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");// 获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(
                request.getParameterMap().keySet());
        keys.remove("sign");// 排除sign参数
        Collections.sort(keys);// 排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key))
                    .append("&");// 拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0,
                linkString.length() - 1);// 去除最后一个'&'

        String secret = "Potato";// 密钥，自己修改
        String sign = DigestUtils.md5Hex(linkString + secret);// 混合密钥md5

        return StringUtils.equals(sign, requestSign);// 比较
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }

}