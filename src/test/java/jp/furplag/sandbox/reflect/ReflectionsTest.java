/*
 * Copyright (C) 2018+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.furplag.sandbox.reflect;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import jdk.jfr.Experimental;
import jp.furplag.sandbox.outerworld.Duplicate;
import jp.furplag.sandbox.outerworld.Nothing;
import jp.furplag.sandbox.outerworld.TheEntity;
import jp.furplag.sandbox.outerworld.TheObject;
import jp.furplag.sandbox.outerworld.nested.Overriden;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

class ReflectionsTest {

  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(
      value = {
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,
        ElementType.PACKAGE,
        ElementType.MODULE,
        ElementType.PARAMETER,
        ElementType.TYPE
      })
  public @interface ReflectionsTestAnnotation {}

  @Test
  void test() {
    assertTrue(new Reflections() {} instanceof Reflections);
    assertTrue(Reflections.class.isAssignableFrom(new Reflections() {}.getClass()));

    assertNull(Reflections.conciliation((Field) null));
  }

  @Test
  void testIsStatic() throws ReflectiveOperationException, SecurityException {
    assertFalse(Reflections.isStatic(null));
    assertFalse(
        Reflections.isStatic(FieldUtils.getDeclaredField(TheObject.class, "thePrimitive", true)));
    assertTrue(
        Reflections.isStatic(
            FieldUtils.getDeclaredField(TheObject.class, "THE_BOOLEAN_STATIC", true)));
  }

  @Test
  void testGetClass() {
    assertNull(Reflections.getClass(null));
    assertEquals(Class.class, Reflections.getClass(Class.class));
    assertEquals(String.class, Reflections.getClass(String.class));
    assertEquals(String.class, Reflections.getClass(""));
  }

  @Test
  void testFamilyze() {
    assertArrayEquals(new Object[] {}, Reflections.familyze(null).toArray());
    assertArrayEquals(new Object[] {Object.class}, Reflections.familyze(Object.class).toArray());
    assertArrayEquals(
        new Object[] {Class.class, Object.class}, Reflections.familyze(Class.class).toArray());
    assertArrayEquals(new Object[] {void.class}, Reflections.familyze(void.class).toArray());
    assertArrayEquals(
        new Object[] {Float.class, Number.class, Object.class},
        Reflections.familyze(Float.class).toArray());
    assertArrayEquals(
        new Object[] {ConcurrentSkipListMap.class, AbstractMap.class, Object.class},
        Reflections.familyze(ConcurrentSkipListMap.class).toArray());
    assertArrayEquals(new Object[] {List.class}, Reflections.familyze(List.class).toArray());
    assertArrayEquals(
        new Object[] {Overriden.class, Duplicate.class, TheEntity.class, Object.class},
        Reflections.familyze(Overriden.class).toArray());
  }

  @Test
  void testGetFields() {
    assertArrayEquals(new Field[] {}, Reflections.getFields(null));
    assertArrayEquals(new Field[] {}, Reflections.getFields(Object.class));
    assertArrayEquals(Nothing.class.getDeclaredFields(), Reflections.getFields(new Nothing()));
    assertArrayEquals(Nothing.class.getDeclaredFields(), Reflections.getFields(Nothing.class));

    assertArrayEquals(TheEntity.class.getDeclaredFields(), Reflections.getFields(TheEntity.class));
    assertArrayEquals(TheEntity.class.getDeclaredFields(), Reflections.getFields(new TheEntity()));

    List<Field> expect =
        Arrays.stream(TheEntity.class.getDeclaredFields())
            .collect(Collectors.toCollection(ArrayList::new));
    assertArrayEquals(expect.toArray(), Reflections.getFields(new TheEntity()));

    expect =
        Arrays.stream(Duplicate.class.getDeclaredFields())
            .collect(Collectors.toCollection(ArrayList::new));
    expect.addAll(Arrays.asList((TheEntity.class.getDeclaredFields())));
    assertArrayEquals(expect.toArray(), Reflections.getFields(new Duplicate()));

    expect =
        Arrays.stream(Overriden.class.getDeclaredFields())
            .collect(Collectors.toCollection(ArrayList::new));
    expect.addAll(Arrays.asList((Duplicate.class.getDeclaredFields())));
    expect.addAll(Arrays.asList((TheEntity.class.getDeclaredFields())));

    assertArrayEquals(expect.toArray(), Reflections.getFields(new Overriden()));
  }

  @Test
  void testgetField() {
    assertEquals((Field) null, Reflections.getField(null, null));
    assertEquals((Field) null, Reflections.getField(null, Objects.toString(null)));
    assertEquals((Field) null, Reflections.getField(null, Objects.toString(null, "")));
    assertEquals((Field) null, Reflections.getField(int.class, "MAX_VALUE"));
    assertEquals(
        FieldUtils.getDeclaredField(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING", true),
        Reflections.getField(new TheEntity(), "PRIVATE_STATIC_FINAL_STRING"));
    assertEquals(
        FieldUtils.getDeclaredField(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING", true),
        Reflections.getField(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING"));
    assertEquals(
        FieldUtils.getDeclaredField(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING", true),
        Reflections.getField(new Duplicate(), "PRIVATE_STATIC_FINAL_STRING"));
  }

  @Test
  void testIsAssignableOfClass() throws ReflectiveOperationException, SecurityException {
    assertFalse(Reflections.isAssignable((Object) null, (Field) null));
    assertFalse(
        Reflections.isAssignable((Object) null, TheEntity.class.getDeclaredField("thePrimitive")));
    assertFalse(Reflections.isAssignable(TheEntity.class, (Field) null));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class, TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            Duplicate.class, TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            Overriden.class, TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            new TheEntity(), TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            new Duplicate(), TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            new Overriden(), TheEntity.class.getDeclaredField("thePrimitiveOfPackage")));
    assertFalse(
        Reflections.isAssignable(
            TheEntity.class, Overriden.class.getDeclaredField("thePrimitiveOfPackage")));
    assertTrue(
        Reflections.isAssignable(
            Overriden.class, Overriden.class.getDeclaredField("thePrimitiveOfPackage")));
    assertFalse(
        Reflections.isAssignable(
            Object.class, Overriden.class.getDeclaredField("thePrimitiveOfPackage")));
  }

  @SuppressWarnings("unchecked")
  @Test
  void testIsAssignableOfField() throws ReflectiveOperationException, SecurityException {
    assertFalse(Reflections.isAssignable((Field) null, (Object) null));
    assertFalse(Reflections.isAssignable((Field) null, "text"));
    assertFalse(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), (Object) null));
    assertTrue(
        Reflections.isAssignable(TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), true));
    assertTrue(
        Reflections.isAssignable(TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), false));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), Boolean.valueOf("true")));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), Boolean.valueOf("false")));
    Boolean undefined = null;
    assertFalse(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("thePrimitiveOfPackage"), undefined));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("PUBLIC_STATIC_FINAL_STRING"), (Object) null));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("PUBLIC_STATIC_FINAL_STRING"), "enable"));
    assertTrue(
        Reflections.isAssignable(
            TheEntity.class.getDeclaredField("PUBLIC_STATIC_FINAL_STRING"), true));

    Method test = Reflections.class.getDeclaredMethod("isAssignable", Class.class, Class.class);
    test.setAccessible(true);

    assertEquals(test.invoke(null, (Class<?>) null, (Class<?>) null), false);
    Class<?>[] primitives =
        ((Map<Class<?>, Class<?>>)
                FieldUtils.getDeclaredField(ClassUtils.class, "primitiveWrapperMap", true)
                    .get(null))
            .keySet()
            .toArray(new Class<?>[] {});
    Class<?>[] primitiveNumbers =
        Arrays.stream(primitives)
            .map(ClassUtils::primitiveToWrapper)
            .filter(c -> Number.class.isAssignableFrom(c))
            .map(ClassUtils::wrapperToPrimitive)
            .toArray(Class[]::new);
    for (Class<?> primitive : primitives) {
      if (void.class.equals(primitive)) continue;
      assertEquals(test.invoke(null, primitive, (Class<?>) null), false);
      assertEquals(test.invoke(null, primitive, ClassUtils.primitiveToWrapper(primitive)), true);
      assertEquals(test.invoke(null, primitive, (Class<?>) null), false);
      assertEquals(test.invoke(null, ClassUtils.primitiveToWrapper(primitive), null), true);
      assertEquals(test.invoke(null, ClassUtils.primitiveToWrapper(primitive), primitive), true);
      if (Number.class.isAssignableFrom(ClassUtils.primitiveToWrapper(primitive))) {
        Arrays.stream(primitiveNumbers)
            .forEach(
                c -> {
                  try {
                    assertEquals(
                        true, test.invoke(null, ClassUtils.primitiveToWrapper(primitive), c));
                    assertEquals(
                        true, test.invoke(null, c, ClassUtils.primitiveToWrapper(primitive)));
                    assertEquals(
                        true,
                        test.invoke(
                            null, ClassUtils.primitiveToWrapper(primitive), BigDecimal.class));
                    assertEquals(
                        true,
                        test.invoke(
                            null, ClassUtils.primitiveToWrapper(primitive), BigInteger.class));
                    assertEquals(true, test.invoke(null, c, BigDecimal.class));
                    assertEquals(true, test.invoke(null, c, BigInteger.class));
                  } catch (IllegalAccessException
                      | IllegalArgumentException
                      | InvocationTargetException e) {
                    fail(e.getMessage());
                  }
                });
      } else {
        for (Class<?> _primitive : new Class[] {boolean.class, char.class}) {
          assertEquals(
              primitive.equals(_primitive),
              test.invoke(null, ClassUtils.primitiveToWrapper(primitive), _primitive));
          assertEquals(
              primitive.equals(_primitive),
              test.invoke(null, _primitive, ClassUtils.primitiveToWrapper(primitive)));
        }
      }
    }
  }

  @Deprecated
  @ReflectionsTestAnnotation
  public static class Origin {

    public int intField;

    @Experimental public String textField;

    @Deprecated
    @SuppressWarnings("unchecked")
    protected <T> T test(T t) {
      return (T) textField;
    }
  }

  @ReflectionsTestAnnotation
  public static final class Any extends Origin {

    @ReflectionsTestAnnotation public int intField;

    @Experimental @ReflectionsTestAnnotation public String textField;

    @Override
    public <T> T test(T t) {
      return super.test(t);
    }
  }

  @Test
  void testAnnotatedWith() throws ReflectiveOperationException {

    assertEquals(Reflections.isAnnotatedWith(null, (Class<Annotation>[]) null), true);
    assertEquals(Reflections.isAnnotatedWith(Origin.class), true);
    assertEquals(Reflections.isAnnotatedWith(Origin.class, (Class<Annotation>[]) null), true);
    assertEquals(
        Reflections.isAnnotatedWith(
            Origin.class.getDeclaredField("intField"), (Class<Annotation>[]) null),
        true);
    assertEquals(Reflections.isAnnotatedWith(Any.class, Deprecated.class), false);
    assertEquals(
        Reflections.isAnnotatedWith(Origin.class.getDeclaredField("intField"), Experimental.class),
        false);

    assertEquals(Reflections.isAnnotatedWith(Origin.class, Deprecated.class), true);
    assertEquals(
        Reflections.isAnnotatedWith(Origin.class.getDeclaredField("textField"), Experimental.class),
        true);

    assertTrue(Reflections.isAnnotatedWith(Origin.class, ReflectionsTestAnnotation.class));
    assertTrue(Reflections.isAnnotatedWith(Any.class, ReflectionsTestAnnotation.class));
    assertTrue(
        Reflections.isAnnotatedWith(Any.class, ReflectionsTestAnnotation.class, Override.class));

    assertEquals(
        Any.class.isAnnotationPresent(ReflectionsTestAnnotation.class),
        Reflections.isAnnotatedWith(Any.class, ReflectionsTestAnnotation.class));

    assertEquals(Reflections.isAnnotatedWithAllOf(null, (Class<Annotation>[]) null), true);
    assertEquals(Reflections.isAnnotatedWithAllOf(String.class), true);
    assertEquals(Reflections.isAnnotatedWithAllOf(Origin.class), false);
    assertEquals(Reflections.isAnnotatedWithAllOf(Origin.class, (Class<Annotation>[]) null), false);
    assertEquals(
        Reflections.isAnnotatedWithAllOf(
            Origin.class.getDeclaredField("intField"), (Class<Annotation>[]) null),
        true);
    assertEquals(Reflections.isAnnotatedWithAllOf(Any.class, Deprecated.class), false);
    assertEquals(
        Reflections.isAnnotatedWithAllOf(
            Origin.class.getDeclaredField("intField"), Experimental.class),
        false);

    assertFalse(Reflections.isAnnotatedWithAllOf(Origin.class, Deprecated.class));
    assertTrue(
        Reflections.isAnnotatedWithAllOf(
            Origin.class, Deprecated.class, ReflectionsTestAnnotation.class));
  }
}
