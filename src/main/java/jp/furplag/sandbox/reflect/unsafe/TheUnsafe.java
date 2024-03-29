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
package jp.furplag.sandbox.reflect.unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import jp.furplag.sandbox.reflect.Reflections;
import jp.furplag.sandbox.stream.Streamr;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * handle class member whether protected ( or invisible ) using sun.misc.Unsafe .
 *
 * @author furplag
 */
public final class TheUnsafe {

  /** lazy initialization for {@link TheUnsafe#theUnsafe theUnsafe}. */
  private static final class Origin {/* @formatter:off */private static final TheUnsafe theUnsafe = new TheUnsafe();/* @formatter:on */}

  /** generate the pair of Type and MethodHandle . */
  private static final Trebuchet.Functions.Tri<Class<?>, Class<?>, UnsafeWeaver.Prefix, ? extends Map.Entry<Class<?>, MethodHandle>> pairGenerator =
      (x, y, z) -> Map.entry(y, Trebuchet.Functions.orNot(x, UnsafeWeaver.getFormattedMethodName(y, z), UnsafeWeaver.getMethodType(y, z), UnsafeWeaver::getMethodHandle));

  /** internal snippet for cast the value type . */
  private static final Trebuchet.Functions.Bi<Class<?>, Object, Object> primitivatorOrigin =
      (t, v) -> String.class.equals(t) ? Objects.toString(v, null) : !t.isPrimitive() ? v : MethodHandles.lookup().findVirtual(Reflections.getClass(v), UnsafeWeaver.getFormattedMethodName(t, null).toLowerCase(Locale.ROOT) + "Value", MethodType.methodType(t)).invoke(v);

  /** update value using under unsafe access . */
  private static final Trebuchet.Predicates.Tri<Object, Field, Object> setOrigin = ((Trebuchet.Predicates.Tri<Object, Field, Object>) Reflections::isAssignable).and((t, u, v) -> Trebuchet.Functions.orNot(t, u, v, (x, y, z) -> {/* @formatter:off */theUnsafe().setInternal(x, y, z); return Objects.equals(primivatior(y.getType(), z), get(x, y));/* @formatter:on */}));

  /** failsafe for fieldOffset . */
  private static final long invalidOffset;

  static {/* @formatter:off */ invalidOffset = -1L; /* @formatter:on */}

  /** {@link sun.misc.Unsafe#getUnsafe()} . */
  private final Object theUnsafe;

  /** {@link sun.misc.Unsafe#getObject(Object, long)} . */
  private final Map<Class<?>, MethodHandle> gettings;

  /** {@link sun.misc.Unsafe#putObject(Object, long, Object)} . */
  private final Map<Class<?>, MethodHandle> settings;

  /** {@link sun.misc.Unsafe#staticFieldBase(Field)} . */
  private final MethodHandle staticFieldBase;

  /** {@link sun.misc.Unsafe#staticFieldOffset(Field)} . */
  private final MethodHandle staticFieldOffset;

  /** {@link sun.misc.Unsafe#objectFieldOffset(Field)} . */
  private final MethodHandle objectFieldOffset;

  /**
   * internal entity of {@link sun.misc.Unsafe} .
   *
   */
  private TheUnsafe() {
    final Class<?> unsafeClass = theUnsafeClass();

    theUnsafe = theUnsafe(unsafeClass);
    staticFieldBase = UnsafeWeaver.getMethodHandle(unsafeClass, "staticFieldBase");
    staticFieldOffset = UnsafeWeaver.getMethodHandle(unsafeClass, "staticFieldOffset");
    objectFieldOffset = UnsafeWeaver.getMethodHandle(unsafeClass, "objectFieldOffset");

    gettings = getFieldAccessors(unsafeClass);
    settings = setFieldAccessors(unsafeClass, gettings);
  }

  /**
   * construct a container of methods to field access .
   *
   * @param unsafeClass {@code sun.misc.Unsafe}
   * @return a container of methods to field access
   */
  private static Map<Class<?>, MethodHandle> getFieldAccessors(final Class<?> unsafeClass) {
    return Streamr.collect(Streamr.stream(UnsafeWeaver.baseFieldTypes).map((x) -> pairGenerator.apply(unsafeClass, x, UnsafeWeaver.Prefix.get)).filter((x) -> Objects.nonNull(x.getValue())), null, null);
  }

  /**
   * construct a container of methods to field access .
   *
   * @param unsafeClass {@code sun.misc.Unsafe}
   * @param gettings primitives and {@link Object}
   * @return a container of methods to field access
   */
  private static Map<Class<?>, MethodHandle> setFieldAccessors(final Class<?> unsafeClass, final Map<Class<?>, MethodHandle> gettings) {
    return Streamr.collect(Streamr.stream(gettings.keySet()).map((x) -> pairGenerator.apply(unsafeClass, x, UnsafeWeaver.Prefix.put)).filter((x) -> Objects.nonNull(x.getValue())), null, null);
  }

  /**
   * read value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return the value of the field
   */
  public static Object get(final Object mysterio, final Field field) {
    return theUnsafe().getInternal(mysterio, field);
  }

  /**
   * internal snippet for cast the value type .
   *
   * @param fieldType type of the filed
   * @param value the value
   * @return transformed object
   */
  private static Object primivatior(final Class<?> fieldType, final Object value) {
    return Trebuchet.Functions.orNot(fieldType, value, primitivatorOrigin);
  }

  /**
   * update value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field the field to update
   * @param value the value for update
   * @return true if update successful
   */
  public static boolean set(final Object mysterio, final Field field, final Object value) {
    return Trebuchet.Predicates.orNot(mysterio, field, value, setOrigin);
  }

  /**
   * {@link TheUnsafe} .
   *
   * @return {@link TheUnsafe}
   */
  private static TheUnsafe theUnsafe() {
    return Origin.theUnsafe;
  }

  /**
   * {@link TheUnsafe} .
   *
   * @return {@code sun.misc.Unsafe}
   */
  private static Object theUnsafe(final Class<?> sunMiscUnsafe) {
    return Trebuchet.Functions.orNot(sunMiscUnsafe, (x) -> Reflections.conciliation(x.getDeclaredField("theUnsafe")).get(null));
  }

  /**
   * {@link TheUnsafe} .
   *
   * @return {@code sun.misc.Unsafe}
   */
  private static Class<?> theUnsafeClass() {
    return Trebuchet.Functions.orNot("sun.misc.Unsafe", Class::forName);
  }

  /**
   * returns {@link sun.misc.Unsafe#staticFieldBase(Field)} if the field is member of class, or returns {@code classOrInstance} if the field is member of instance .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return the object which declaring the field
   * @throws ReflectiveOperationException an access error
   */
  private Object fieldBase(Object mysterio, Field field) {
    return Trebuchet.Functions.orNot(mysterio, field, (o, f) -> Reflections.isStatic(f) ? staticFieldBase.invoke(theUnsafe, f) : o);
  }

  /**
   * {@link sun.misc.Unsafe#objectFieldOffset(Field)} and {@link sun.misc.Unsafe#staticFieldOffset(Field)} .
   *
   * @param field {@link Field}
   * @return offset of the field, or returns {@link invalidOffset} if the field is null
   */
  private long fieldOffset(Field field) {
    return (long) Trebuchet.Functions.orElse(field, (t) -> (Reflections.isStatic(t) ? staticFieldOffset : objectFieldOffset).invoke(theUnsafe, t), () -> invalidOffset);
  }

  /**
   * read value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return the value of the field of class ( or the instance )
   */
  private Object getInternal(Object mysterio, Field field) {
    return !(Reflections.isAssignable(mysterio, field) && (Reflections.isStatic(field) || !(mysterio instanceof Class))) ? null
        : Trebuchet.Functions.orNot(mysterio, field, (o, f) -> gettings.getOrDefault(f.getType(), gettings.get(Object.class)).invoke(theUnsafe, fieldBase(o, f), fieldOffset(f)));
  }

  /**
   * update value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field the field to update
   * @param value the value for update
   */
  private void setInternal(Object mysterio, Field field, Object value) {
    Trebuchet.Consumers.orNot(mysterio, field, value, (_mysterio, _field, _value) -> settings.getOrDefault(_field.getType(), settings.get(Object.class)).invoke(theUnsafe, fieldBase(_mysterio, _field), fieldOffset(_field), primivatior(_field.getType(), _value)));
  }
}
