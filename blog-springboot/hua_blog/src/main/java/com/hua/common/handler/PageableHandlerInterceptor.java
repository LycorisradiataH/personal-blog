package com.hua.common.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hua.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.hua.common.constant.CommonConst.*;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 17:29
 */
public class PageableHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String current = request.getParameter(CURRENT);
        String size = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);
        if (StringUtils.isNotEmpty(current)) {
            PageUtils.setCurrent(new Page<>(Long.parseLong(current), Long.parseLong(size)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PageUtils.remove();
    }
}
