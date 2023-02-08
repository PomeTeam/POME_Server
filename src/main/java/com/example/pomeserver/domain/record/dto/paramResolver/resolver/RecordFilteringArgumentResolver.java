package com.example.pomeserver.domain.record.dto.paramResolver.resolver;

import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;
@Component
public class RecordFilteringArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RecordFilteringParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String _firstEmotion = webRequest.getParameter("first_emotion");
        String _secondEmotion = webRequest.getParameter("second_emotion");
        Long firstEmotion = _firstEmotion == null ? null : Long.parseLong(_firstEmotion);
        Long secondEmotion = _secondEmotion == null ? null : Long.parseLong(_secondEmotion);
        return new RecordFilteringParam(firstEmotion, secondEmotion);
    }
}
