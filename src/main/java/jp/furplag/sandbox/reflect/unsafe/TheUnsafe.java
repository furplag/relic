/**
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

import jp.furplag.function.ThrowableBiConsumer;
import jp.furplag.function.ThrowableBiFunction;
import jp.furplag.function.ThrowableFunction;
import jp.furplag.function.ThrowableTriFunction;
import jp.furplag.function.ThrowableTriPredicate;
import jp.furplag.sandbox.reflect.Reflections;
import jp.furplag.sandbox.stream.Streamr;

/**
 * handle class member whether protected ( or invisible ) using sun.misc.Unsafe .
 *
 * @author furplag
 */
public final class TheUnsafe {

  /** generate the pair of Type and MethodHandle . */
  private static final ThrowableTriFunction<Class<?>, Class<?>, UnsafeWeaver.Prefix, ? extends Map.Entry<Class<?>, MethodHandle>> pairGenerator =
    (x, y, z) -> Map.entry(y, ThrowableTriFunction.orNull(x, UnsafeWeaver.getFormattedMethodName(y, z), UnsafeWeaver.getMethodType(y, z), UnsafeWeaver::getMethodHandle));

  /** internal snippet for cast the value type . */
  private static final ThrowableBiFunction<Class<?>, Object, Object> primitivatorOrigin =
    (t, v) -> String.class.equals(t) ? Objects.toString(v, null) :
      !t.isPrimitive() ? v :
      MethodHandles.lookup().findVirtual(Reflections.getClass(v), UnsafeWeaver.getFormattedMethodName(t, null).toLowerCase(Locale.ROOT) + "Value", MethodType.methodType(t)).invoke(v);

  /** update value using under unsafe access . */
  private static final ThrowableTriPredicate<Object, Field, Object> setOrigin =
    ((ThrowableTriPredicate<Object, Field, Object>) Reflections::isAssignable)
      .and((t, u, v) -> ThrowableTriPredicate.orNot(t, u, v, (x, y, z) -> {theUnsafe().setInternal(x, y, z); return Objects.equals(primivatior(y.getType(), z), get(x, y));}));
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
   * {@link jp.furplag.reflect.unsafe.TheUnsafe} .
   */
  private TheUnsafe() {
    final Class<?> unsafeClass = ThrowableFunction.orNull("sun.misc.Unsafe", Class::forName);
    theUnsafe = ThrowableBiFunction.orNull(unsafeClass, "theUnsafe", (x, y) -> Reflections.conciliation(x.getDeclaredField(y)).get(null));

    staticFieldBase = ThrowableTriFunction.orNull(unsafeClass, "staticFieldBase", MethodType.methodType(Object.class, Field.class), UnsafeWeaver::getMethodHandle);
    staticFieldOffset = ThrowableTriFunction.orNull(unsafeClass, "staticFieldOffset", MethodType.methodType(long.class, Field.class), UnsafeWeaver::getMethodHandle);
    objectFieldOffset = ThrowableTriFunction.orNull(unsafeClass, "objectFieldOffset", MethodType.methodType(long.class, Field.class), UnsafeWeaver::getMethodHandle);

    // @formatter:off
    gettings = fieldAccessors(unsafeClass, UnsafeWeaver.Prefix.get, boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, Object.class);
    settings = fieldAccessors(unsafeClass, UnsafeWeaver.Prefix.put, gettings.keySet().toArray(Class<?>[]::new));
    // @formatter:on
  }

  /** lazy initialization for {@link TheUnsafe#theUnsafe theUnsafe}. */
  private static final class Origin {
    private static final TheUnsafe theUnsafe = new TheUnsafe();
  }

  /**
   * construct a container of methods to field access .
   *
   * @param unsafeClass {@code sun.misc.Unsafe}
   * @param prefix {@link UnsafeWeaver.Prefix Prefix}
   * @param classes primitives and {@link Object}
   * @return a container of methods to field access
   */
  private static Map<Class<?>, MethodHandle> fieldAccessors(final Class<?> unsafeClass, final UnsafeWeaver.Prefix prefix, final Class<?>... classes) {
    return Streamr.collect(Streamr.stream(classes).map((x) -> pairGenerator.apply(unsafeClass, x, prefix)).filter((x) -> Objects.nonNull(x.getValue())), null, null);
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
    return ThrowableBiFunction.orNull(fieldType, value, primitivatorOrigin);
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
    return ThrowableTriPredicate.orNot(mysterio, field, value, setOrigin);
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
   * returns {@link sun.misc.Unsafe#staticFieldBase(Field)} if the field is member of class, or returns {@code classOrInstance} if the field is member of instance .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return the object which declaring the field
   * @throws ReflectiveOperationException an access error
   */
  private Object fieldBase(Object mysterio, Field field) {
    return ThrowableBiFunction.orNull(mysterio, field, (o, f) -> Reflections.isStatic(f) ? staticFieldBase.invoke(theUnsafe, f) : o);
  }

  /**
   * {@link sun.misc.Unsafe#objectFieldOffset(Field)} and {@link sun.misc.Unsafe#staticFieldOffset(Field)} .
   *
   * @param field {@link Field}
   * @return offset of the field, or returns {@link invalidOffset} if the field is null
   */
  private long fieldOffset(Field field) {
    return (long) ThrowableFunction.orDefault(field, (t) -> (Reflections.isStatic(field) ? staticFieldOffset : objectFieldOffset).invoke(theUnsafe, t), invalidOffset);
  };

  /**
   * read value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return the value of the field of class ( or the instance )
   */
  private Object getInternal(Object mysterio, Field field) {
    // @formatter:off
    return !(Reflections.isAssignable(mysterio, field) && (Reflections.isStatic(field) || !(mysterio instanceof Class))) ? null :
      ThrowableBiFunction.orNull(mysterio, field, (o, f) -> gettings.getOrDefault(f.getType(), gettings.get(Object.class)).invoke(theUnsafe, fieldBase(o, f), fieldOffset(f)));
    // @formatter:on
  }

  /**
   * update value using under unsafe access .
   *
   * @param mysterio {@link Class} or the instance
   * @param field the field to update
   * @param value the value for update
   */
  private void setInternal(Object mysterio, Field field, Object value) {
    ThrowableBiConsumer.orNot(mysterio, field, (o, f) -> settings.getOrDefault(f.getType(), settings.get(Object.class)).invoke(theUnsafe, fieldBase(o, f), fieldOffset(f), primivatior(f.getType(), value)));
  }
}
