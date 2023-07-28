package com.seago.http.component;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import static com.seago.http.constant.HeaderConstant.AUTHORIZATION_HEADER;
import static com.seago.http.constant.HeaderConstant.BEARER_PREFIX;

@Provider
public class AuthorizationQueryFilter implements ContainerRequestFilter {

    private static ThreadLocal<String> THREAD_LOCAL_TOKEN = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // 获取请求头中的Authorization字段
        String authorizationHeader = requestContext.getHeaderString(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            // 获取Token
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            THREAD_LOCAL_TOKEN.set(token);
        }
    }

    public static String getToken(){
        String token = THREAD_LOCAL_TOKEN.get();
        THREAD_LOCAL_TOKEN.remove();
        return token;
    }

}

