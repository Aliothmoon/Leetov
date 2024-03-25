package cn.hyy.leetov.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Predicate;

/**
 * Helper
 *
 * @author HuYuanYang
 * @version 2024/03/24
 */
@SuppressWarnings("unchecked")
public final class SpecialClassUtils {
    private SpecialClassUtils() {
    }

    private static final Set<String> OBJECT_METHODS = new HashSet<>(Arrays.asList("toString",
            "getClass",
            "equals",
            "hashCode",
            "wait",
            "notify",
            "notifyAll",
            "clone",
            "finalize"));

    //proxy frameworks
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
            // cglib
            , "org.springframework.cglib.proxy.Factory"

            // javassist
            , "javassist.util.proxy.ProxyObject"
            , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    private static void doGetFields(Class<?> clazz, List<Field> fields, Predicate<Field> predicate, boolean firstOnly) {
        if (clazz == null || clazz == Object.class) {
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (predicate == null || predicate.test(declaredField)) {
                fields.add(declaredField);
                if (firstOnly) {
                    break;
                }
            }
        }

        if (firstOnly && !fields.isEmpty()) {
            return;
        }

        doGetFields(clazz.getSuperclass(), fields, predicate, firstOnly);
    }

    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        doGetMethods(clazz, methods, null, false);
        return methods;
    }

    public static List<Method> getAllMethods(Class<?> clazz, Predicate<Method> predicate) {
        List<Method> methods = new ArrayList<>();
        doGetMethods(clazz, methods, predicate, false);
        return methods;
    }


    public static Method getFirstMethod(Class<?> clazz, Predicate<Method> predicate) {
        List<Method> methods = new ArrayList<>();
        doGetMethods(clazz, methods, predicate, true);
        return methods.isEmpty() ? null : methods.get(0);
    }


    private static void doGetMethods(Class<?> clazz, List<Method> methods, Predicate<Method> predicate, boolean firstOnly) {
        if (clazz == null || clazz == Object.class) {
            return;
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (clazz.isInterface()) {
            for (Method method : declaredMethods) {
                // 接口类只需要获取 default 方法
                if (method.isDefault() && (predicate == null || predicate.test(method))) {
                    methods.add(method);
                    if (firstOnly) {
                        break;
                    }
                }
            }
        } else {
            for (Method method : declaredMethods) {
                if (predicate == null || predicate.test(method)) {
                    methods.add(method);
                    if (firstOnly) {
                        break;
                    }
                }
            }
        }


        if (firstOnly && !methods.isEmpty()) {
            return;
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            doGetMethods(anInterface, methods, predicate, firstOnly);
        }

        doGetMethods(clazz.getSuperclass(), methods, predicate, firstOnly);
    }


    private static <T> Class<T> getJdkProxySuperClass(Class<T> clazz) {
        final Class<?> proxyClass = Proxy.getProxyClass(clazz.getClassLoader(), clazz.getInterfaces());
        return (Class<T>) proxyClass.getInterfaces()[0];
    }


    public static boolean isObjectMethod(String methodName) {
        return OBJECT_METHODS.contains(methodName);
    }

}
