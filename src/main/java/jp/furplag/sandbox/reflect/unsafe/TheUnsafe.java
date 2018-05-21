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
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import jp.furplag.function.Suppressor;
import jp.furplag.function.Trebuchet;
import jp.furplag.function.suppress.SuppressConsumer;
import jp.furplag.function.suppress.SuppressFunction;
import jp.furplag.function.suppress.SuppressPredicate;
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
  }

  /** prefix of handler . */
  private static enum Prefix /* @formatter:off */ { get, put }/* @formatter:on */

  /** {@link AccessibleObject#trySetAccessible()} implicitly . */
  private static final UnaryOperator<Field> conciliation = (field) -> Stream.ofNullable(field).peek(AccessibleObject::trySetAccessible).findAny().orElse(null);
  /** prettifying method name . */
  private static final BiFunction<Class<?>, Prefix, String> methodName = (type, prefix) -> String.join("", Objects.toString(prefix, ""), StringUtils.capitalize(Objects.toString(type, "").replaceAll("^.*\\.", "").toLowerCase(Locale.ROOT)));

  /** {@link MethodType} for getter . */
  private static final Function<Class<?>, MethodType> getMethodType = (type) -> MethodType.methodType(type, Object.class, long.class);
  /** {@link MethodType} for setter . */
  private static final Function<Class<?>, MethodType> putMethodType = (type) -> MethodType.methodType(void.class, Object.class, long.class, type);
  /** failsafe for fieldOffset . */
  private static final long invalidOffset;

  static {
    invalidOffset = -1L;
  }
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
    final Class<?> unsafeClass = SuppressFunction.orNull("sun.misc.Unsafe", (Trebuchet.ThrowableFunction<String, Class<?>>) ((x) -> Class.forName(x)));
    theUnsafe = SuppressFunction.orNull(unsafeClass, (Trebuchet.ThrowableFunction<Class<?>, Object>) ((x) -> conciliation.apply(x.getDeclaredField("theUnsafe")).get(null)));
    final Trebuchet.ThrowableBiFunction<String, MethodType, MethodHandle> finder = (name, methodType) -> MethodHandles.privateLookupIn(unsafeClass, MethodHandles.lookup()).findVirtual(unsafeClass, name, methodType);
    final Trebuchet.ThrowableBiFunction<Class<?>, Prefix, Pair<Class<?>, MethodHandle>> pair = (x, y) -> ImmutablePair.of(x, SuppressFunction.orNull(methodName.apply(x, y), (Prefix.get.equals(y) ? getMethodType : putMethodType).apply(x), finder));

    staticFieldBase = SuppressFunction.orNull("staticFieldBase", MethodType.methodType(Object.class, Field.class), finder);
    staticFieldOffset = SuppressFunction.orNull("staticFieldOffset", MethodType.methodType(long.class, Field.class), finder);
    objectFieldOffset = SuppressFunction.orNull("objectFieldOffset", MethodType.methodType(long.class, Field.class), finder);
    // @formatter:off
    gettings = Collections.unmodifiableMap(
      Stream.of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, Object.class)
        .map((x) -> pair.apply(x, Prefix.get))
        .filter((x) -> Objects.nonNull(x.getValue()))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (first, next) -> first))
    );
    settings = Collections.unmodifiableMap(
      gettings.keySet().stream()
        .map((x) -> pair.apply(x, Prefix.put))
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
    return SuppressFunction.orNull(mysterio, field, (Trebuchet.ThrowableBiFunction<Object, Field, Object>) ((o, f) -> Reflections.isStatic.test(f) ? staticFieldBase.invoke(theUnsafe, f) : o));
  }

  /**
   * {@link sun.misc.Unsafe#objectFieldOffset(Field)} and {@link sun.misc.Unsafe#staticFieldOffset(Field)} .
   *
   * @param field {@link Field}
   * @return offset of the field, or returns {@link invalidOffset} if the field is null
   */
  private long fieldOffset(Field field) {
    return (long) Suppressor.orElse(field, (Trebuchet.ThrowableFunction<Field, Object>) ((f) -> (Reflections.isStatic.test(f) ? staticFieldOffset : objectFieldOffset).invoke(theUnsafe, f)), invalidOffset);
  }

  /**
   * read value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field {@link Field}
   * @return the value of the field of classOrInstance
   */
  private Object getInternal(Object mysterio, Field field) {
    // @formatter:off
    return !(Reflections.isAssignable(mysterio, field) && (Reflections.isStatic.test(field) || !(mysterio instanceof Class))) ? null :
      SuppressFunction.orNull(mysterio, field, (Trebuchet.ThrowableBiFunction<Object, Field, Object>) ((o, f) -> gettings.getOrDefault(f.getType(), gettings.get(Object.class)).invoke(theUnsafe, fieldBase(o, f), fieldOffset(f))));
    // @formatter:on
  }

  /**
   * update value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field the field to update
   * @param value the value for update
   */
  private void setInternal(Object mysterio, Field field, Object value) {
    SuppressConsumer.acceptWith(mysterio, field, (Trebuchet.ThrowableBiConsumer<Object, Field>) ((o, f) -> settings.getOrDefault(f.getType(), settings.get(Object.class)).invoke(theUnsafe, fieldBase(o, f), fieldOffset(f), primivatior(f.getType(), value))));
  }

  /**
   * internal snippet for cast the value type .
   *
   * @param fieldType type of the filed
   * @param value the value
   * @return transformed object
   */
  private static Object primivatior(final Class<?> fieldType, final Object value) {
    // @formatter:off
    return String.class.equals(fieldType) ? Objects.toString(value, null) :
      !fieldType.isPrimitive() ? value :
      SuppressFunction.orNull(fieldType, value, (Trebuchet.ThrowableBiFunction<Class<?>, Object, Object>) ((t, v) -> MethodHandles.lookup().findVirtual(Reflections.getClass(v), methodName.apply(t, null).toLowerCase(Locale.ROOT) + "Value", MethodType.methodType(t)).invoke(v)));
    // @formatter:on
  }

  /**
   * {@link TheUnsafe} .
   *
   * @return {@link TheUnsafe}
   */
  static TheUnsafe theUnsafe() {
    return Origin.theUnsafe;
  };

  /**
   * read value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field {@link Field}
   * @return the value of the field
   */
  public static Object get(final Object mysterio, final Field field) {
    return theUnsafe().getInternal(mysterio, field);
  }

  /**
   * update value using under unsafe access.
   *
   * @param mysterio {@link Class} or the instance .
   * @param field the field to update
   * @param value the value for update
   * @return true if update successful
   */
  public static boolean set(final Object mysterio, final Field field, final Object value) {
    // @formatter:off
    return
      SuppressPredicate.isCorrect(mysterio, field, (BiPredicate<Object, Field>) ((o, f) ->
        Reflections.isAssignable(o, f, value) &&
        SuppressPredicate.isCorrect(value, (Trebuchet.ThrowablePredicate<Object>) ((v) -> {theUnsafe().setInternal(o,f, v); return Objects.equals(primivatior(f.getType(), v), get(o, f));}))
      ));
    // @formatter:on
  }
}
