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
import java.util.function.Consumer;

/**
 * the {@link Consumer Consumers} against some problems when handling exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface Consumers {

  /**
   * {@link BiConsumer} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @see {@link BiConsumer}
   */
  @FunctionalInterface
  static interface Bi<T, U> extends BiConsumer<T, U> {

    /** {@inheritDoc} */
    @Override
    default void accept(T t, U u) {/* @formatter:off */try {orThrow(t, u);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */}

    /** {@inheritDoc} */
    @Override
    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {/* @formatter:off */return of(BiConsumer.super.andThen(theFallen(after)), null);/* @formatter:on */}

    /**
     * performs this operation on the given arguments .
     *
     * @param t the value of the first argument to the operation
     * @param u the value of the second argument to the operation
     * @throws Throwable anything thrown
     */
    void orThrow(T t, U u) throws Throwable;
  }

  /**
   * represents an operation that accepts three input arguments and returns no result .
   * <p>
   * this is the three-arity specialization of {@link Consumer} .
   * <p>
   * unlike most other functional interfaces, {@link Tri} is expected to operate via side-effects .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @see {@link Consumer}
   * @see {@link BiConsumer}
   */
  @FunctionalInterface
  static interface Tri<T, U, V> {

    /**
     * performs this operation on the given arguments .
     *
     * @param t the value of the first argument to the operation
     * @param u the value of the second argument to the operation
     * @param v the value of the third argument to the operation
     */
    default void accept(T t, U u, V v) {/* @formatter:off */try {orThrow(t, u, v);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */}

    /**
     * returns a composed comsumer that performs, in sequence, this operation followed by the after
     * operation .
     * <p>
     * if performing either operation throws an exception, it is relayed to the caller of the
     * composed operation .
     * <p>
     * if performing this operation throws an exception, the {@code after} operation will not be
     * performed .
     *
     * @param after the operation to perform after this operation
     * @return a composed comsumer that performs in sequence this operation followed by the after
     *         operation
     * @throws NullPointerException if after is null
     *//* @formatter:off */
    default Tri<T, U, V> andThen(Tri<? super T, ? super U, ? super V> after) {
      return (t, u, v) -> {/* @formatter:off */accept(t, u, v);theFallen(after).accept(t, u, v);/* @formatter:on */};
    }/* @formatter:on */

    /**
     * performs this operation on the given arguments .
     *
     * @param t the value of the first argument to the operation
     * @param u the value of the second argument to the operation
     * @param v the value of the third argument to the operation
     * @throws Throwable anything thrown
     */
    void orThrow(T t, U u, V v) throws Throwable;
  }

  /**
   * {@link Consumer} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the operation
   * @see {@link Consumer}
   */
  @FunctionalInterface
  static interface Uni<T> extends Consumer<T> {

    /** {@inheritDoc} */
    @Override
    default void accept(T t) {/* @formatter:off */try {orThrow(t);} catch (Throwable ex) {Trebuchet.sneakyThrow(ex);}/* @formatter:on */}

    /** {@inheritDoc} */
    @Override
    default Consumer<T> andThen(Consumer<? super T> after) {/* @formatter:off */return of(Consumer.super.andThen(theFallen(after)), null);/* @formatter:on */}

    /**
     * performs this operation on the given arguments .
     *
     * @param t the value of the input to the operation
     * @throws Throwable anything thrown
     */
    void orThrow(T t) throws Throwable;
  }

  /**
   * returns another consumer that always do nothing if this is null .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param fallen {@link BiConsumer}
   * @return the consumer that always do nothing if this is null
   *//* @formatter:off */
  static <T, U> BiConsumer<? super T, ? super U> theFallen(final BiConsumer<? super T, ? super U> fallen) {
    return Objects.requireNonNullElse(fallen, (t, ex) -> {});
  }/* @formatter:on */

  /**
   * returns another consumer that always do nothing if this is null .
   *
   * @param <T> the type of the input to the operation
   * @param fallen {@link Consumer}
   * @return the consumer that always do nothing if this is null
   *//* @formatter:off */
  static <T> Consumer<? super T> theFallen(final Consumer<? super T> fallen) {
    return Objects.requireNonNullElse(fallen, (ex) -> {});
  }/* @formatter:on */

  /**
   * returns another consumer that always do nothing if this is null .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param fallen {@link Tri}
   * @return the consumer that always do nothing if this is null
   *//* @formatter:off */
  static <T, U, V> Tri<? super T, ? super U, ? super V> theFallen(final Tri<? super T, ? super U, ? super V> fallen) {
    return Objects.requireNonNullElse(fallen, (t, u, v) -> {});
  }/* @formatter:on */


  /**
   * should never write ugly try-catch block to handle exceptions in lambda expression .
   *
   * @param <T> the type of the input to the operation
   * @param <EX> anything thrown
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   * @return {@link Consumer}
   * @throws NullPointerException if consumer is null
   */
  @SuppressWarnings({"unchecked"})/* @formatter:off */
  static <T, EX extends Throwable> Consumer<T> of(final Consumer<? super T> consumer, final BiConsumer<? super T, ? super EX> fallen) {
    return (t) -> {try {consumer.accept(t);} catch (Throwable e) {theFallen(fallen).accept(t, (EX) e);}};
  }/* @formatter:on */

  /**
   * should never write ugly try-catch block to handle exceptions in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <EX> anything thrown
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link Tri}, do nothing if this is null
   * @return {@link BiConsumer}
   * @throws NullPointerException if {@code consumer} is null
   */
  @SuppressWarnings({"unchecked"})/* @formatter:off */
  static <T, U, EX extends Throwable> BiConsumer<T, U> of(final BiConsumer<? super T, ? super U> consumer, final Tri<? super T, ? super U, ? super EX> fallen) {
    return (t, u) -> {try {consumer.accept(t, u);} catch (Throwable e) {theFallen(fallen).accept(t, u, (EX) e);}};
  }/* @formatter:on */

  /**
   * should never write ugly try-catch block to handle exceptions in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param <EX> anything thrown
   * @param consumer {@link Tri}, may not be null
   * @param fallen {@link Consumer}, do nothing if this is null
   * @return {@link Tri}
   * @throws NullPointerException if consumer is null
   */
  @SuppressWarnings({"unchecked"})/* @formatter:off */
  static <T, U, V, EX extends Throwable> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> consumer, final Consumer<? super EX> fallen) {
    return (t, u, v) -> {try {consumer.accept(t, u, v);} catch (Throwable e) {theFallen(fallen).accept((EX) e);}};
  }/* @formatter:on */

  /**
   * should never write ugly try-catch block to handle exceptions in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param consumer {@link Tri}, may not be null
   * @param fallen {@link Tri}, do nothing if this is null
   * @return {@link Tri}
   * @throws NullPointerException if consumer is null
   *//* @formatter:off */
  static <T, U, V> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> consumer, final Tri<? super T, ? super U, ? super V> fallen) {
    return (t, u, v) -> {try {consumer.accept(t, u, v);} catch (Throwable e) {theFallen(fallen).accept(t, u, v);}};
  }/* @formatter:on */
}
