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
package jp.furplag.sandbox.trebuchet;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * code snippets against some problems when handling exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface Trebuchet {

  /**
   * consumer.accept(t, u) if done it normally, or fallen.accept(t, EX) if error occurred .
   *
   * @param <T> the type of the input to the operation
   * @param <EX> anything throwan
   * @param t the value of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   * @see {@link Consumers.Uni#accept(Object)}
   *//* @formatter:off */
  static <T, EX extends Throwable> void operateOrElse(final T t, final Consumer<? super T> consumer, final BiConsumer<? super T, ? super EX> fallen) {
    Consumers.of(consumer, fallen).accept(t);
  }/* @formatter:on */

  /**
   * consumer.accept(t, u) if done it normally, or fallen.accept(t, u, EX) if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <EX> anything throwan
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link Consumers.Tri}, do nothing if this is null
   * @see {@link Consumers.Bi#accept(Object, Object)}
   *//* @formatter:off */
  static <T, U, EX extends Throwable> void operateOrElse(final T t, final U u, final Consumers.Bi<? super T, ? super U> consumer, final Consumers.Tri<? super T, ? super U, ? super EX> fallen) {
    Consumers.of(consumer, fallen).accept(t, u);
  }/* @formatter:on */

  /**
   * consumer.accept(t, u, v) if done it normally, or fallen.accept(EX) if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param <EX> anything throwan
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link Consumers.Tri}, may not be null
   * @param fallen {@link Consumer}, do nothing if this is null
   * @see {@link Consumers.Tri#accept(Object, Object, Object)}
   *//* @formatter:off */
  static <T, U, V, EX extends Throwable> void operateOrElse(final T t, final U u, final V v, final Consumers.Tri<? super T, ? super U, ? super V> consumer, final Consumers.Uni<? super EX> fallen) {
    Consumers.of(consumer, fallen).accept(t, u, v);
  }/* @formatter:on */

  /**
   * consumer.accept(t, u, v) if done it normally, or fallen.accept(t, u, v) if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link Consumers.Tri}, may not be null
   * @param fallen {@link Consumers.Tri}, do nothing if this is null
   * @see {@link Consumers.Tri#accept(Object, Object, Object)}
   *//* @formatter:off */
  static <T, U, V> void operateOrElse(final T t, final U u, final V v, final Consumers.Tri<? super T, ? super U, ? super V> consumer, final Consumers.Tri<? super T, ? super U, ? super V> fallen) {
    Consumers.of(consumer, fallen).accept(t, u, v);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t) if done it normally, or fallen.apply(t, EX) if error
   * occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <R> the type of the result of the function
   * @param <EX> anything throwan
   * @param t the value of the first argument to the function
   * @param function {@link Function}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return null if this is null
   * @return the result of function.apply(t) if done it normally, or fallen.apply(t, EX) if error
   *         occurred
   * @see {@link Functions.Uni#apply(Object)}
   *//* @formatter:off */
  static <T, R, EX extends Throwable> R orElse(final T t, final Functions.Uni<? super T, ? extends R> function, final Functions.Bi<? super T, ? super EX, ? extends R> fallen) {
    return Functions.Uni.of(function, fallen).apply(t);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error
   * occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the value of the input to the function
   * @param function {@link Function}, may not be null
   * @param fallen {@link Supplier}, or the function that always return null if this is null
   * @return the result of function.apply(t) if done it normally, or fallen.get() if error
   *         occurred
   * @see {@link Functions.Uni#apply(Object)}
   *//* @formatter:off */
  static <T, R> R orElse(final T t, final Functions.Uni<? super T, ? extends R> function, final Supplier<R> fallen) {
    return orElse(t, function, (x, ex) -> Objects.requireNonNullElse(fallen, () -> null).get());
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u) if done it normally, or fallen.apply(t, u, EX) if
   * error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <EX> anything throwan
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link Functions.Tri}, or the function that always return null if this is null
   * @return the result of function.apply(t, u) if done it normally, or fallen.apply(t, u, EX) if
   *         error occurred
   * @see {@link Functions.Bi#apply(Object, Object)}
   *//* @formatter:off */
  static <T, U, R, EX extends Throwable> R orElse(final T t, final U u, final Functions.Bi<? super T, ? super U, ? extends R> function, final Functions.Tri<? super T, ? super U, ? super EX, ? extends R> fallen) {
    return Functions.Bi.of(function, fallen).apply(t, u);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error
   * occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link Supplier}, or the function that always return null if this is null
   * @return the result of function.apply(t, u) if done it normally, or fallen.get() if error
   *         occurred
   * @see {@link Functions.Bi#apply(Object, Object)}
   *//* @formatter:off */
  static <T, U, R> R orElse(final T t, final U u, final Functions.Bi<? super T, ? super U, ? extends R> function, final Supplier<R> fallen) {
    return orElse(t, u, function, (x, y, ex) -> Objects.requireNonNullElse(fallen, () -> null).get());
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or fallen.apply(t, u, v) if
   * error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link Functions.Tri}, may not be null
   * @param fallen {@link Functions.Tri}, or the function that always return null if this is null
   * @return the result of function.apply(t, u, v) if done it normally, or fallen.apply(t, u, v) if
   *         error occurred
   * @see {@link Functions.Tri#apply(Object, Object, Object)}
   *//* @formatter:off */
  static <T, U, V, R> R orElse(final T t, final U u, final V v, final Functions.Tri<? super T, ? super U, ? super V, ? extends R> function, final Functions.Tri<? super T, ? super U, ? super V, ? extends R> fallen) {
    return Functions.Tri.of(function, fallen).apply(t, u, v);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error
   * occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link Functions.Tri}, may not be null
   * @param fallen {@link Supplier}, or the function that always return null if this is null
   * @return the result of function.apply(t, u, v) if done it normally, or fallen.get() if error
   *         occurred
   * @see {@link Functions.Tri#apply(Object, Object, Object)}
   *//* @formatter:off */
  static <T, U, V, R> R orElse(final T t, final U u, final V v, final Functions.Tri<? super T, ? super U, ? super V, ? extends R> function, final Supplier<R> fallen) {
    return orElse(t, u, v, function, (x, y, z) -> Objects.requireNonNullElse(fallen, () -> null).get());
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or fallen.apply(EX) if error
   * occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param <EX> anything throwan
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link Functions.Tri}, may not be null
   * @param fallen {@link Function}, or the function that always return null if this is null
   * @return the result of function.apply(t, u, v) if done it normally, or fallen.apply(EX) if error
   *         occurred
   * @see {@link Functions.Tri#apply(Object, Object, Object)}
   *//* @formatter:off */
  static <T, U, V, R, EX extends Throwable> R orElse(final T t, final U u, final V v, final Functions.Tri<? super T, ? super U, ? super V, R> function, final Function<? super EX, ? extends R> fallen) {
    return Functions.Tri.of(function, fallen).apply(t, u, v);
  }/* @formatter:on */

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the input to the operation
   * @param t the value of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   *//* @formatter:off */
  static <T, U, V> void orNone(final T t, final Consumers.Uni<? super T> consumer) {
    operateOrElse(t, consumer, (x, ex) -> {});
  }/* @formatter:on */

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   *//* @formatter:off */
  static <T, U> void orNone(final T t, final U u, final Consumers.Bi<? super T, ? super U> consumer) {
    operateOrElse(t, u, consumer, (x, y, ex) -> {});
  }/* @formatter:on */

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link Consumers.Tri}, may not be null
   *//* @formatter:off */
  static <T, U, V> void orNone(final T t, final U u, final V v, final Consumers.Tri<? super T, ? super U, ? super V> consumer) {
    operateOrElse(t, u, v, consumer, (x, y, z) -> {});
  }/* @formatter:on */

  /**
   * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param t the value of the input to the predicate
   * @param predicate {@link Predicates.Tri}, may not be null
   * @return the result of predicate.test(t, u, v) if done it normally, or false if error occurred
   *//* @formatter:off */
  static <T> boolean orNot(final T t, final Predicate<? super T> predicate) {
    return orElse(t, predicate::test, () -> false);
  }/* @formatter:on */

  /**
   * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link Predicates.Tri}, may not be null
   * @return the result of predicate.test(t, u, v) if done it normally, or false if error occurred
   *//* @formatter:off */
  static <T, U> boolean orNot(final T t, final U u, final BiPredicate<? super T, ? super U> predicate) {
    return orElse(t, u, predicate::test, () -> false);
  }/* @formatter:on */

  /**
   * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param v the value of the third argument to the predicate
   * @param predicate {@link Predicates.Tri}, may not be null
   * @return the result of predicate.test(t, u, v) if done it normally, or false if error occurred
   *//* @formatter:off */
  static <T, U, V> boolean orNot(final T t, final U u, final V v, final Predicates.Tri<? super T, ? super U, ? super V> predicate) {
    return orElse(t, u, v, predicate, () -> false);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t) if done it normally, or null if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param function {@link Function}, may not be null
   * @return the result of function.apply(t) if done it normally, or null if error occurred
   *//* @formatter:off */
  static <T, R> R orNull(final T t, final Functions.Uni<? super T, ? extends R> function) {
    return orElse(t, function, () -> null);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u) if done it normally, or null if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @return the result of function.apply(t, u) if done it normally, or null if error occurred
   *//* @formatter:off */
  static <T, U, R> R orNull(final T t, final U u, final Functions.Bi<? super T, ? super U, ? extends R> function) {
    return orElse(t, u, function, () -> null);
  }/* @formatter:on */

  /**
   * returns the result of function.apply(t, u, v) if done it normally, or null if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link Functions.Tri}, may not be null
   * @return the result of function.apply(t, u, v) if done it normally, or null if error occurred
   *//* @formatter:off */
  static <T, U, V, R> R orNull(final T t, final U u, final V v, final Functions.Tri<? super T, ? super U, ? super V, ? extends R> function) {
    return orElse(t, u, v, function, () -> null);
  }/* @formatter:on */

  /**
   * throws any throwable 'sneakily' .
   *
   * @param <EX> anything thrown
   * @param ex anything thrown
   * @throws EX anything thrown
   * @see <a href="https://projectlombok.org/features/SneakyThrows">lombok.Lombok#sneakyThrow(Throwable)</a>
   */
  @SuppressWarnings({"unchecked"})
  static <EX extends Throwable> void sneakyThrow(final Throwable ex) throws EX {
    throw (EX) Objects.requireNonNullElse(ex, new IllegalArgumentException("hmm, no way call me with null ."));
  }
}
