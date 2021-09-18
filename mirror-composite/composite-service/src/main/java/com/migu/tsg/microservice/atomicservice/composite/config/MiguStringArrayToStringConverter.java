package com.migu.tsg.microservice.atomicservice.composite.config;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 去除参数中的重复对象：
 * eg:
 * 对于URL：http://localhost:8080/test?username=a&name=b&name=b
 * 默认spring mvc会绑定成username=a name=b,b
 * 使用该Converter,可绑定成username=a name=b
 */
@Component
public class MiguStringArrayToStringConverter implements Converter<String[], String> {
    @Override
    public String convert(String[] source) {
        if (source == null) {
            return null;
        }

        if (source.length == 0) {
            return "";
        }

        Set<String> set = Arrays.stream(source)
                .collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String sourceElement : set) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(sourceElement);
            i++;
        }
        return sb.toString();
    }
}
