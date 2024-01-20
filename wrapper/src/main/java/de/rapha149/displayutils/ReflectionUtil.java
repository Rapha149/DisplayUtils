package de.rapha149.displayutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Utility class for performing reflection operations.
 */
public class ReflectionUtil {

    /**
     * Retrieves the value of a field from an object.
     *
     * @param obj The object from which to retrieve the field.
     * @param field The name of the field to retrieve.
     * @return The value of the field.
     * @throws ReflectionException If the field could not be retrieved.
     */
    public static <O, R> R getField(O obj, String field) throws ReflectionException {
        return getField(obj, field, null);
    }

    /**
     * Retrieves the value of a field from an object, casting it to a specified class.
     *
     * @param input The object from which to retrieve the field.
     * @param field The name of the field to retrieve.
     * @param returnClass The class to which to cast the field's value.
     * @return The value of the field, cast to the specified class.
     * @throws ReflectionException If the field could not be retrieved.
     */
    public static <O, R> R getField(O input, String field, Class<R> returnClass) throws ReflectionException {
        return getField(input, input.getClass(), field, returnClass);
    }

    /**
     * Retrieves the value of a field from an object of a specified class.
     *
     * @param obj The object from which to retrieve the field.
     * @param objClass The class of the object.
     * @param field The name of the field to retrieve.
     * @return The value of the field.
     * @throws ReflectionException If the field could not be retrieved.
     */
    public static <O, R> R getField(O obj, Class<? extends O> objClass, String field) throws ReflectionException {
        return getField(obj, objClass, field, null);
    }

    /**
     * Retrieves the value of a field from an object of a specified class, casting it to another specified class.
     *
     * @param obj The object from which to retrieve the field.
     * @param objClass The class of the object.
     * @param field The name of the field to retrieve.
     * @param returnClass The class to which to cast the field's value.
     * @return The value of the field, cast to the specified class.
     * @throws ReflectionException If the field could not be retrieved.
     */
    public static <O, R> R getField(O obj, Class<? extends O> objClass, String field, Class<R> returnClass) throws ReflectionException {
        try {
            Field f = objClass.getDeclaredField(field);
            f.setAccessible(true);
            return (R) f.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not get value of field \"" + field + "\" from object of class " + objClass.getName(), e);
        }
    }

    /**
     * Sets the value of a field in an object.
     *
     * @param obj The object in which to set the field.
     * @param field The name of the field to set.
     * @param value The value to set the field to.
     * @throws ReflectionException If the field could not be set.
     */
    public static <O> void setField(O obj, String field, Object value) throws ReflectionException {
        setField(obj, obj.getClass(), field, value);
    }

    /**
     * Sets the value of a field in an object of a specified class.
     *
     * @param obj The object in which to set the field.
     * @param objClass The class of the object.
     * @param field The name of the field to set.
     * @param value The value to set the field to.
     * @throws ReflectionException If the field could not be set.
     */
    public static <O> void setField(O obj, Class<? extends O> objClass, String field, Object value) throws ReflectionException {
        try {
            Field f = objClass.getDeclaredField(field);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not set value of field \"" + field + "\" from object of class " + objClass.getName(), e);
        }
    }

    /**
     * Invokes a method on an object.
     *
     * @param obj The object on which to invoke the method.
     * @param method The name of the method to invoke.
     * @param args The arguments to pass to the method.
     * @return The result of the method invocation.
     * @throws ReflectionException If the method could not be invoked.
     */
    public static <O, R> R invokeMethod(O obj, String method, Object... args) throws ReflectionException {
        return invokeMethod(obj, method, null, args);
    }

    /**
     * Invokes a method on an object, casting the result to a specified class.
     *
     * @param obj The object on which to invoke the method.
     * @param method The name of the method to invoke.
     * @param returnClass The class to which to cast the result of the method invocation.
     * @param args The arguments to pass to the method.
     * @return The result of the method invocation, cast to the specified class.
     * @throws ReflectionException If the method could not be invoked.
     */
    public static <O, R> R invokeMethod(O obj, String method, Class<R> returnClass, Object... args) throws ReflectionException {
        return invokeMethod(obj, obj.getClass(), method, returnClass, args);
    }

    /**
     * Invokes a method on an object of a specified class, casting the result to another specified class.
     *
     * @param obj The object on which to invoke the method.
     * @param objClass The class of the object.
     * @param method The name of the method to invoke.
     * @param returnClass The class to which to cast the result of the method invocation.
     * @param args The arguments to pass to the method.
     * @return The result of the method invocation, cast to the specified class.
     * @throws ReflectionException If the method could not be invoked.
     */
    public static <O, R> R invokeMethod(O obj, Class<? extends O> objClass, String method, Class<R> returnClass, Object... args) throws ReflectionException {
        return invokeMethod(obj, objClass, method, returnClass, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new), args);
    }

    /**
     * Invokes a method on an object of a specified class, casting the result to another specified class.
     *
     * @param obj The object on which to invoke the method.
     * @param objClass The class of the object.
     * @param method The name of the method to invoke.
     * @param returnClass The class to which to cast the result of the method invocation.
     * @param argTypes The types of the arguments to pass to the method.
     * @param args The arguments to pass to the method.
     * @return The result of the method invocation, cast to the specified class.
     * @throws ReflectionException If the method could not be invoked.
     */
    public static <O, R> R invokeMethod(O obj, Class<? extends O> objClass, String method, Class<R> returnClass, Class<?>[] argTypes, Object... args) throws ReflectionException {
        try {
            Method m = objClass.getDeclaredMethod(method, argTypes);
            m.setAccessible(true);
            return (R) m.invoke(obj, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException("Could not invoke method \"" + method + "\" from object of class " + objClass.getName(), e);
        }
    }

    /**
     * Invokes a constructor of a specified class.
     *
     * @param clazz The class whose constructor to invoke.
     * @param args The arguments to pass to the constructor.
     * @return The result of the constructor invocation.
     * @throws ReflectionException If the constructor could not be invoked.
     */
    public static <T> T invokeConstructor(Class<T> clazz, Object... args) throws ReflectionException {
        return invokeConstructor(clazz, Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new), args);
    }

    /**
     * Invokes a constructor of a specified class.
     *
     * @param clazz The class whose constructor to invoke.
     * @param argTypes The types of the arguments to pass to the constructor.
     * @param args The arguments to pass to the constructor.
     * @return The result of the constructor invocation.
     * @throws ReflectionException If the constructor could not be invoked.
     */
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

    /**
     * Exception class for reflection operations.
     */
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