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
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * the {@link Function Functions} against some problems when handling exceptions in lambda
 * expression .
 *
 * @author furplag
 *
 */
public interface Functions {

  /**
   * {@link BiFunction} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @see {@link BiFunction}
   */
  @FunctionalInterface
  static interface Bi<T, U, R> extends BiFunction<T, U, R> {

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     * @param <EX> anything thrown
     * @param function {@link BiFunction}, may not be null
     * @param fallen {@link Tri}, or the function that always return null if this is null
     * @return {@link Function}
     * @throws NullPointerException if function is null
     */
    @SuppressWarnings({"unchecked"})/* @formatter:off */
    static <T, U, R, EX extends Throwable> BiFunction<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function, final Tri<? super T, ? super U, ? super EX, ? extends R> fallen) {
      return (t, u) -> {try {return function.apply(t, u);} catch (Throwable e) {return theFallen(fallen).apply(t, u, (EX) e);}};
    }/* @formatter:on */

    /** {@inheritDoc} */
    @Override
    default R apply(T t, U u) {/* @formatter:off */try {return orThrow(t, u);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

    /**
     * applies this function to the given argument .
     *
     * @param t the value of the first argument to the function
     * @param u the value of the second argument to the function
     * @return the function result
     * @throws Throwable anything thrown
     */
    R orThrow(T t, U u) throws Throwable;
  }

  /**
   * represents a function that accepts three arguments and produces a result .
   * <p>
   * this is the three-arity specialization of {@link Function} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @see {@link Function}
   * @see {@link BiFunction}
   */
  @FunctionalInterface
  static interface Tri<T, U, V, R> {

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <V> the type of the third argument to the function
     * @param <R> the type of the result of the function
     * @param <EX> anything thrown
     * @param function {@link Tri}, may not be null
     * @param fallen {@link Function}, or the function that always return null if this is null
     * @return {@link Tri}
     * @throws NullPointerException if function is null
     */
    @SuppressWarnings({"unchecked"})/* @formatter:off */
    static <T, U, V, R, EX extends Throwable> Tri<T, U, V, R> of(final Tri<? super T, ? super U, ? super V, ? extends R> function, final Function<? super EX, ? extends R> fallen) {
      return (t, u, v) -> {try {return function.apply(t, u, v);} catch (Throwable e) {return theFallen(fallen).apply((EX) e);}};
    }/* @formatter:on */

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <V> the type of the third argument to the function
     * @param <R> the type of the result of the function
     * @param function {@link Tri}, may not be null
     * @param fallen {@link Tri}, or the function that always return null if this is null
     * @return {@link Tri}
     * @throws NullPointerException if function is null
     *//* @formatter:off */
    static <T, U, V, R> Tri<T, U, V, R> of(final Tri<? super T, ? super U, ? super V, ? extends R> function, final Tri<? super T, ? super U, ? super V, ? extends R> fallen) {
      return (t, u, v) -> {try {return function.apply(t, u, v);} catch (Throwable e) {return theFallen(fallen).apply(t, u, v);}};
    }/* @formatter:on */

    /**
     * returns a composed function that first applies this function to its input, and then applies
     * the after function to the result .
     * <p>
     * if evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function .
     *
     * @param <X> the type of output of the after function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the after
     *         function
     * @throws NullPointerException if after is null
     */
    default <X> Tri<T, U, V, X> andThen(Function<? super R, ? extends X> after) {/* @formatter:off */return (t, u, v) -> after.apply(apply(t, u, v));/* @formatter:on */}

    /**
     * applies this function to the given arguments .
     *
     * @param t the value of the first argument to the function
     * @param u the value of the second argument to the function
     * @param v the value of the third argument to the function
     * @return the function result
     */
    default R apply(T t, U u, V v) {/* @formatter:off */try {return orThrow(t, u, v);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

    /**
     * applies this function to the given arguments .
     *
     * @param t the value of the first argument to the function
     * @param u the value of the second argument to the function
     * @param v the value of the third argument to the function
     * @return the function result
     * @throws Throwable anything thrown
     */
    R orThrow(T t, U u, V v) throws Throwable;
  }

  /**
   * {@link java.util.function.Function Function} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @see {@link java.util.function.Function Function}
   */
  @FunctionalInterface
  static interface Uni<T, R> extends Function<T, R> {

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param <EX> anything thrown
     * @param function {@link Function}, may not be null
     * @param fallen {@link Function}, or the function that always return null if
     *        this is null
     * @return {@link Functions.Uni}
     * @throws NullPointerException if function is null
     */
    @SuppressWarnings({"unchecked"})/* @formatter:off */
    static <T, R, EX extends Throwable> Function<T, R> of(final Function<? super T, ? extends R> function, final BiFunction<? super T, ? super EX, ? extends R> fallen) {
      return (t) -> {try {return function.apply(t);} catch (Throwable e) {return theFallen(fallen).apply(t, (EX) e);}};
    }/* @formatter:on */

    /** {@inheritDoc} */
    @Override
    default R apply(T t) {/* @formatter:off */try {return orThrow(t);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

    /**
     * applies this function to the given argument .
     *
     * @param t the function argument
     * @return the function result
     * @throws Throwable anything thrown
     */
    R orThrow(T t) throws Throwable;
  }

  /**
   * returns another function that always return null if the function is null .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param fallen {@link BiFunction}
   * @return the function that always return null if the function is null
   *//* @formatter:off */
  static <T, U, R> BiFunction<? super T, ? super U, ? extends R> theFallen(final BiFunction<? super T, ? super U, ? extends R> fallen) {
    return Objects.requireNonNullElse(fallen, (t, ex) -> null);
  }/* @formatter:on */

  /**
   * returns another function that always return null if the function is null .
   *
   * @param <T> the type of the first argument to the function
   * @param <R> the type of the result of the function
   * @param fallen {@link Function}
   * @return the function that always return null if the function is null
   *//* @formatter:off */
  static <T, R> Function<? super T, ? extends R> theFallen(final Function<? super T, ? extends R> fallen) {
    return Objects.requireNonNullElse(fallen, (ex) -> null);
  }/* @formatter:on */

  /**
   * returns another function that always return null if the function is null .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param fallen {@link Tri}
   * @return the function that always return null if the function is null
   *//* @formatter:off */
  static <T, U, V, R> Tri<? super T, ? super U, ? super V, ? extends R> theFallen(final Tri<? super T, ? super U, ? super V, ? extends R> fallen) {
    return Objects.requireNonNullElse(fallen, (t, u, v) -> null);
  }/* @formatter:on */
}
