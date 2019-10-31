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
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Predicates {

  /**
   * {@link Predicate} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the predicate
   * @see {@link Predicate}
   * @see {@link Functions.Uni}
   */
  @FunctionalInterface
  static interface Uni<T> extends Functions.Uni<T, Boolean>, Predicate<T> {

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the input to the predicate
     * @param <EX> anything thrown
     * @param predicate {@link Predicate}, may not be null
     * @param fallen {@link BiPredicate}, do nothing if this is null
     * @return {@link Predicate}
     * @throws NullPointerException if predicate is null
     *//* @formatter:off */
    static <T, EX extends Throwable> Predicate<T> of(final Predicate<T> predicate, final BiPredicate<? super T, ? super EX> fallen) {
      return Functions.Uni.of(predicate::test, fallen::test)::apply;
    }/* @formatter:on */

    /** {@inheritDoc} */
    @Override
    default boolean test(T t) {/* @formatter:off */return apply(t);/* @formatter:on */}
  }

  /**
   * {@link BiFunction} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @see {@link BiFunction}
   * @see {@link Functions.Bi}
   */
  @FunctionalInterface
  static interface Bi<T, U> extends BiFunction<T, U, Boolean>, BiPredicate<T, U> {

    /**
     * should never write ugly try-catch block to handle exceptions in lambda expression .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param <EX> anything thrown
     * @param predicate {@link BiPredicate}, may not be null
     * @param fallen {@link Tri}, or the predicate that always return false if this is null
     * @return {@link BiPredicate}
     * @throws NullPointerException if {@code predicate} is null
     *//* @formatter:off */
    static <T, U, EX extends Throwable> BiPredicate<T, U> of(final BiPredicate<T, U> predicate, final Tri<? super T, ? super U, ? super EX> fallen) {
      return Functions.Bi.of(predicate::test, fallen)::apply;
    }/* @formatter:on */

    /** {@inheritDoc} */
    @Override
    default boolean test(T t, U u) {/* @formatter:off */return apply(t, u);/* @formatter:on */}
  }

  /**
   * represents a function that accepts three arguments and produces a result .
   * <p>
   * this is the three-arity specialization of {@link Predicate} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @see {@link Predicate}
   * @see {@link Functions.Tri}
   */
  @FunctionalInterface
  public interface Tri<T, U, V> extends Functions.Tri<T, U, V, Boolean> {

    /**
     * returns a predicate that is the negation of the supplied predicate. this is accomplished by
     * returning result of the calling {@code target.negate()} .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param <V> the type of the third argument to the predicate
     * @param target predicate to negate
     * @return a predicate that negates the results of the supplied predicate
     * @throws NullPointerException if target is null
     */
    @SuppressWarnings("unchecked")/* @formatter:off */
    static <T, U, V> Tri<T, U, V> not(final Tri<? super T, ? super U, ? super V> target) {
      return (Tri<T, U, V>) target.negate();
    }/* @formatter:on */

    /**
     * returns a composed predicate that represents a short-circuiting logical AND of this predicate
     * and another .
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of this
     *         predicate and the other predicate
     *//* @formatter:off */
    default Tri<T, U, V> and(Tri<? super T, ? super U, ? super V> other) {
      return (t, u, v) -> test(t, u, v) && theFallen(other).test(t, u, v);
    }/* @formatter:off */

    /**
     * returns a predicate that represents the logical negation of this predicate .
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default Tri<T, U, V> negate() {
      return (t, u, v) -> !test(t, u, v);
    }

    /**
     * returns a composed predicate that represents a short-circuiting logical OR of this
     * predicate and another. When evaluating the composed predicate, if this predicate is
     * {@code true}, then the other predicate is not evaluated .
     *
     * <p>
     * any exceptions thrown during evaluation of either predicate are relayed to the caller; if
     * evaluation of this predicate throws an exception, the {@code other} predicate will not be
     * evaluated .
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this
     *         predicate and the other predicate
     * @throws NullPointerException if other is null
     *//* @formatter:off */
    default Tri<T, U, V> or(Tri<? super T, ? super U, ? super V> other) {
      return (t, u, v) -> test(t, u, v) || theFallen(other).apply(t, u, v);
    }/* @formatter:on */

    /**
     * evaluates this predicate on the given arguments .
     *
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param v the value of the third argument to the predicate
     * @return true if the input arguments match the predicate, otherwise false
     */
    default boolean test(T t, U u, V v) {/* @formatter:off */return Functions.Tri.super.apply(t, u, v);/* @formatter:on */}
  }

  /**
   * returns another predicate that always return false if the predicate is null .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param fallen {@link BiPredicate}
   * @return the predicate that always return null if the predicate is null
   *//* @formatter:off */
  static <T, U, R> BiPredicate<? super T, ? super U> theFallen(final BiPredicate<? super T, ? super U> fallen) {
    return Objects.requireNonNullElse(fallen, (t, ex) -> false);
  }/* @formatter:on */

  /**
   * returns another predicate that always return false if the predicate is null .
   *
   * @param <T> the type of the first argument to the predicate
   * @param fallen {@link Predicate}
   * @return the predicate that always return null if the predicate is null
   *//* @formatter:off */
  static <T, R> Predicate<? super T> theFallen(final Predicate<? super T> fallen) {
    return Objects.requireNonNullElse(fallen, (ex) -> false);
  }/* @formatter:on */

  /**
   * returns another predicate that always return false if the predicate is null .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param fallen {@link Tri}
   * @return the predicate that always return null if the predicate is null
   *//* @formatter:off */
  static <T, U, V> Tri<? super T, ? super U, ? super V> theFallen(final Tri<? super T, ? super U, ? super V> fallen) {
    return Objects.requireNonNullElse(fallen, (t, u, v) -> false);
  }/* @formatter:on */
}
