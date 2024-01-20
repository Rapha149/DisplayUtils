package de.rapha149.displayutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {

    // fields
    public static <O, R> R getField(O obj, String field) throws ReflectionException {
        return getField(obj, field, null);
    }

    public static <O, R> R getField(O input, String field, Class<R> returnClass) throws ReflectionException {
        return getField(input, input.getClass(), field, returnClass);
    }

    public static <O, R> R getField(O obj, Class<? extends O> objClass, String field) throws ReflectionException {
        return getField(obj, objClass, field, null);
    }

    public static <O, R> R getField(O obj, Class<? extends O> objClass, String field, Class<R> returnClass) throws ReflectionException {
        try {
            Field f = objClass.getDeclaredField(field);
            f.setAccessible(true);
            return (R) f.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not get value of field \"" + field + "\" from object of class " + objClass.getName(), e);
        }
    }

    public static <O> void setField(O obj, String field, Object value) throws ReflectionException {
        setField(obj, obj.getClass(), field, value);
    }

    public static <O> void setField(O obj, Class<? extends O> objClass, String field, Object value) throws ReflectionException {
        try {
            Field f = objClass.getDeclaredField(field);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not set value of field \"" + field + "\" from object of class " + objClass.getName(), e);
        }
    }

    // methods
    public static <O, R> R invokeMethod(O obj, String method, Object... args) throws ReflectionException {
        return invokeMethod(obj, method, null, args);
    }

    public static <O, R> R invokeMethod(O obj, String method, Class<R> returnClass, Object... args) throws ReflectionException {
        return invokeMethod(obj, obj.getClass(), method, returnClass, args);
    }

    public static <O, R> R invokeMethod(O obj, Class<? extends O> objClass, String method, Class<R> returnClass, Object... args) throws ReflectionException {
        return invokeMethod(obj, objClass, method, returnClass, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new), args);
    }

    public static <O, R> R invokeMethod(O obj, Class<? extends O> objClass, String method, Class<R> returnClass, Class<?>[] argTypes, Object... args) throws ReflectionException {
        try {
            Method m = objClass.getDeclaredMethod(method, argTypes);
            m.setAccessible(true);
            return (R) m.invoke(obj, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException("Could not invoke method \"" + method + "\" from object of class " + objClass.getName(), e);
        }
    }

    // constructors
    public static <T> T invokeConstructor(Class<T> clazz, Object... args) throws ReflectionException {
        return invokeConstructor(clazz, Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new), args);
    }

    // constructors
    public static <T> T invokeConstructor(Class<T> clazz, Class<?>[] argTypes, Object... args) throws ReflectionException {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(argTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new ReflectionException("Could not invoke constructor of class " + clazz.getName(), e);
        }
    }

    public static class ReflectionException extends Exception {

        public ReflectionException() {
            super();
        }

        public ReflectionException(String message) {
            super(message);
        }

        public ReflectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
