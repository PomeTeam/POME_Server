package com.example.pomeserver.global.util.authResolver;

import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.constraints.NotNull;


@RequiredArgsConstructor
@Component
public class UserIdResolver implements HandlerMethodArgumentResolver{

    private final TokenUtils tokenUtils;
    private final Environment env;

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return parameter.hasParameterAnnotation(UserId.class) && String.class.equals(parameter.getParameterType());
    }

    @Nullable
    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception
    {
        Auth auth = parameter.getMethodAnnotation(Auth.class);
        if (auth == null) {
            throw new Exception("토큰을 통해 userId를 추출하는 메서드에는 @Auth 어노테이션을 붙여주세요.");
        }
        String userId = tokenUtils.getUserIdFromFullToken(webRequest.getHeader("ACCESS-TOKEN"));

        if (!auth.optional() && userId == null) {
            throw new Exception(("토큰으로 부터 추출한 userId가 Null입니다."));
        }
        return userId;
    }

}
