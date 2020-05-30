package com.frizo.ucc.server.utils.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 此類暫時停用，因為 Spring Boot 已經有相關功能套件。

public class BeanUtils {
    public static void dump(Object from, Object to){
        List<PairMethod> pairMethodList = new ArrayList<>();
        List<Method> toMethodList = Arrays.asList(to.getClass().getDeclaredMethods());
        toMethodList.forEach(method -> {
            String methodName = method.getName();
            Pattern pattern = Pattern.compile("(get.+)|(is.+)");
            Matcher matcher = pattern.matcher(methodName);
            if (matcher.matches()){
                String getterName = matcher.group();
                String setterName = getterName.replaceAll("(get)|(is)", "set");
                pairMethodList.add(new PairMethod(getterName, setterName, method.getReturnType()));
            }
        });

        transform(from, to, pairMethodList);
    }

    private static void transform(Object from, Object to, List<PairMethod> pairMethodList) {
        pairMethodList.forEach(pm -> {
            try {
                Method getterMethod = from.getClass().getDeclaredMethod(pm.getterName);
                Object data = getterMethod.invoke(from);
                Method setterMethod = to.getClass().getDeclaredMethod(pm.setterName, pm.returnType);
                setterMethod.invoke(to, data);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            }
        });
    }

    private static class PairMethod{
        String getterName;
        String setterName;
        Class returnType;

        PairMethod(String getter, String setter, Class returnType){
            this.getterName = getter;
            this.setterName = setter;
            this.returnType = returnType;
        }
    }
}
