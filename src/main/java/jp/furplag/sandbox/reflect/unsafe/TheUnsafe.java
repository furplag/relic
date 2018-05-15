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
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import jp.furplag.function.Trebuchet;
import jp.furplag.function.Trebuchet.ThrowableBiFunction;
import jp.furplag.function.Trebuchet.ThrowableOperator;
import jp.furplag.sandbox.reflect.Reflections;

/**
 * handle class member whether protected ( or invisible ) using sun.misc.Unsafe .
 *
 * @author furplag
 */
public final class TheUnsafe {

  /** lazy initialization for {@link TheUnsafe#theUnsafe theUnsafe}. */
  private static final class Origin {
    private static final TheUnsafe theUnsafe = new TheUnsafe();
  };

  /** prefix of handler . */
  private static enum Prefix {
    get, put
  }
  /** {@link AccessibleObject#trySetAccessible()} implicitly . */
  private static final ThrowableOperator<Field> conciliation = (field) -> Stream.ofNullable(field).peek(AccessibleObject::trySetAccessible).findAny().orElse(null);
  /** prettifying method name . */
  private static final BiFunction<Class<?>, Prefix, String> methodName = (type, prefix) -> String.join("", Objects.toString(prefix, ""), StringUtils.capitalize(Objects.toString(type, "").replaceAll("^.*\\.", "").toLowerCase(Locale.ROOT)));
  /** {@link MethodType} for getter . */
  private static final Function<Class<?>, MethodType> getMethodType = (type) -> MethodType.methodType(type, Object.class, long.class);
  /** {@link MethodType} for setter . */
  private static final Function<Class<?>, MethodType> putMethodType = (type) -> MethodType.methodType(void.class, Object.class, long.class, type);

  /** {@link sun.misc.Unsafe#getUnsafe()}. */
  private final Object theUnsafe;
  /** {@link sun.misc.Unsafe#getObject(Object, long)}. */
  private final Map<Class<?>, MethodHandle> gettings;
  /** {@link sun.misc.Unsafe#putObject(Object, long, Object)}. */
  private final Map<Class<?>, MethodHandle> settings;
  /** {@link sun.misc.Unsafe#staticFieldBase(Field)}. */
  private final MethodHandle staticFieldBase;
  /** {@link sun.misc.Unsafe#staticFieldOffset(Field)}. */
  private final MethodHandle staticFieldOffset;
  /** {@link sun.misc.Unsafe#objectFieldOffset(Field)}. */
  private final MethodHandle objectFieldOffset;

  /**
   * {@link jp.furplag.reflect.unsafe.TheUnsafe} .
   */
  private TheUnsafe() {
    final Class<?> sunMiscUnsafe = Trebuchet.orElse((Class<?> x) -> Class.forName("sun.misc.Unsafe"), (ex, x) -> null).apply(null);
    theUnsafe = Trebuchet.orElse((Class<?> x) -> conciliation.apply(x.getDeclaredField("theUnsafe")).get(null), (ex, x) -> null).apply(sunMiscUnsafe);
    final ThrowableBiFunction<String, MethodType, MethodHandle> finder = (name, methodType) -> MethodHandles.privateLookupIn(sunMiscUnsafe, MethodHandles.lookup()).findVirtual(sunMiscUnsafe, name, methodType);

    staticFieldBase = Trebuchet.orElse(finder, (ex, x) -> null).apply("staticFieldBase", MethodType.methodType(Object.class, Field.class));
    staticFieldOffset = Trebuchet.orElse(finder, (ex, x) -> null).apply("staticFieldOffset", MethodType.methodType(long.class, Field.class));
    objectFieldOffset = Trebuchet.orElse(finder, (ex, x) -> null).apply("objectFieldOffset", MethodType.methodType(long.class, Field.class));
    // @formatter:off
    gettings = Collections.unmodifiableMap(
      Stream.of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, Object.class)
        .map(x -> ImmutablePair.of(x, Trebuchet.orElse(finder, (ex, e) -> null).apply(methodName.apply(x, Prefix.get), getMethodType.apply(x))))
        .filter(x -> Objects.nonNull(x.getValue()))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (first, next) -> first))
    );
    settings = Collections.unmodifiableMap(
      gettings.keySet().stream()
        .map(x -> ImmutablePair.of(x, Trebuchet.orElse(finder, (ex, e) -> null).apply(methodName.apply(x, Prefix.put), putMethodType.apply(x))))
        .filter(x -> Objects.nonNull(x.getValue()))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (first, next) -> first))
    );
    // @formatter:on
  }

  /**
   * returns {@link sun.misc.Unsafe#staticFieldBase(Field)} if the field is member of class, or returns {@code classOrInstance} if the field is member of instance .
   *
   * @param mysterio {@link Class} or the instance.
   * @param field {@link Field}
   * @return the object which declaring the field
   * @throws ReflectiveOperationException an access error
   */
  private Object fieldBase(Object mysterio, Field field) {
    return Trebuchet.orElse((Field x) -> Modifier.isStatic(field.getModifiers()) ? staticFieldBase.invoke(theUnsafe, field) : mysterio, (t, x) -> null).apply(field);
  }

  /**
   * {@link sun.misc.Unsafe#objectFieldOffset(Field)} and {@link sun.misc.Unsafe#staticFieldOffset(Field)} .
   *
   * @param field {@link Field}
   * @return offset of the field, or returns {@link invalidOffset} if the field is null
   */
  private long fieldOffset(Field field) {
    return (long) Optional.ofNullable(
    Trebuchet.orElse((Field x) -> (Modifier.isStatic(x.getModifiers()) ? staticFieldOffset : objectFieldOffset).invoke(theUnsafe, x), (t, x) -> null).apply(field)
    ).orElse(Long.valueOf(-1L));
  }

  /**
   * read value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field {@link Field}
   * @return the value of the field of classOrInstance
   */
  public Object get(Object mysterio, Field field) {
    return !readable(mysterio, field) ? null :
      Trebuchet.orElse((Field x) -> gettings.getOrDefault(field.getType(), gettings.get(Object.class)).invoke(theUnsafe, fieldBase(mysterio, field), fieldOffset(field)), (t, x) -> null).apply(field);
  }

  /**
   * update value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field the field to update
   * @param value the value for update
   */
  public void set(Object mysterio, Field field, Object value) {
    if (Reflections.isAssignable(mysterio, field, value)) {
      Trebuchet.orElse((Field x) -> settings.getOrDefault(field.getType(), settings.get(Object.class)).invoke(theUnsafe, fieldBase(mysterio, field), fieldOffset(field), primivatior(field.getType(), value)), (t, x) -> {t.printStackTrace(); return null;}).apply(field);
    }
  }

  /**
   * internal snippet for cast the value type .
   *
   * @param fieldType type of the filed
   * @param value the value
   * @return transformed object
   * @throws Throwable an access error
   */
  private static Object primivatior(final Class<?> fieldType, final Object value) throws Throwable {
    // @formatter:off
    return String.class.equals(fieldType) ? Objects.toString(value, null) :
      !fieldType.isPrimitive() ? value :
      MethodHandles.lookup().findVirtual(Reflections.getClass(value), methodName.apply(fieldType, null).toLowerCase(Locale.ROOT) + "Value", MethodType.methodType(fieldType)).invoke(value);
    // @formatter:on
  }

  private static boolean readable(final Object mysterio, final Field field) {
    return Reflections.isAssignable(mysterio, field) && (Modifier.isStatic(field.getModifiers()) || !(mysterio instanceof Class));
  }

  /**
   * {@link TheUnsafe} .
   *
   * @return {@link TheUnsafe}
   */
  public static final TheUnsafe getInstance() {
    return Origin.theUnsafe;
  }
}
