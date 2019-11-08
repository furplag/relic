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

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import jp.furplag.sandbox.stream.Streamr;

/**
 * code snippets against some problems when handling exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface Trebuchet {

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

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the operation
       * @param <U> the type of the second argument to the operation
       * @param consumer {@link BiConsumer}, may not be null
       * @return {@link BiConsumer}
       * @throws NullPointerException if {@code consumer} is null
       */
      static <T, U> Bi<T, U> of(final BiConsumer<? super T, ? super U> consumer) {
        return of(consumer, null);
      }


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
      @SuppressWarnings({"unchecked"})
      static <T, U, EX extends Throwable> Bi<T, U> of(final BiConsumer<? super T, ? super U> consumer, final Tri<? super T, ? super U, ? super EX> fallen) {
        /* @formatter:off */
        return (t, u) -> {try {consumer.accept(t, u);} catch (Throwable e) {De.fault(fallen).accept(t, u, (EX) e);}};
        /* @formatter:on */
      }

      /** {@inheritDoc} */
      @Override
      default void accept(T t, U u) {/* @formatter:off */try {orThrow(t, u);} catch (Throwable e) {sneakyThrow(e);}/* @formatter:on */}

      /** {@inheritDoc} */
      @Override
      default Bi<T, U> andThen(BiConsumer<? super T, ? super U> after) {/* @formatter:off */return of(BiConsumer.super.andThen(De.fault(after)));/* @formatter:on */}

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
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the operation
       * @param <U> the type of the second argument to the operation
       * @param <V> the type of the third argument to the operation
       * @param consumer {@link Tri}, may not be null
       * @return {@link Tri}
       * @throws NullPointerException if consumer is null
       */
      static <T, U, V> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> consumer) {
        return of(consumer, (Consumers.Tri<T, U, V>) null);
      }

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
      @SuppressWarnings({"unchecked"})
      static <T, U, V, EX extends Throwable> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> consumer, final Consumer<? super EX> fallen) {
        /* @formatter:off */
        return (t, u, v) -> {try {consumer.accept(t, u, v);} catch (Throwable e) {De.fault(fallen).accept((EX) e);}};
        /* @formatter:on */
      }

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
       */
      static <T, U, V> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> consumer, final Tri<? super T, ? super U, ? super V> fallen) {
        /* @formatter:off */
        return (t, u, v) -> {try {consumer.accept(t, u, v);} catch (Throwable e) {De.fault(fallen).accept(t, u, v);}};
        /* @formatter:on */
      }

      /**
       * performs this operation on the given arguments .
       *
       * @param t the value of the first argument to the operation
       * @param u the value of the second argument to the operation
       * @param v the value of the third argument to the operation
       */
      default void accept(T t, U u, V v) {/* @formatter:off */try {orThrow(t, u, v);} catch (Throwable e) {sneakyThrow(e);}/* @formatter:on */}

      /**
       * returns a composed comsumer that performs, in sequence, this operation followed by the after operation .
       * <p>
       * if performing either operation throws an exception, it is relayed to the caller of the composed operation .
       * <p>
       * if performing this operation throws an exception, the {@code after} operation will not be performed .
       *
       * @param after the operation to perform after this operation
       * @return a composed comsumer that performs in sequence this operation followed by the after operation
       * @throws NullPointerException if after is null
       */
      default Tri<T, U, V> andThen(Tri<? super T, ? super U, ? super V> after) {
        return (t, u, v) -> {/* @formatter:off */accept(t, u, v); De.fault(after).accept(t, u, v);/* @formatter:on */};
      }

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

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the input to the operation
       * @param consumer {@link Consumer}, may not be null
       * @return {@link Consumer}
       * @throws NullPointerException if consumer is null
       */
      static <T> Uni<T> of(final Consumer<? super T> consumer) {/* @formatter:off */return of(consumer, null);/* @formatter:on */}

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
      static <T, EX extends Throwable> Uni<T> of(final Consumer<? super T> consumer, final BiConsumer<? super T, ? super EX> fallen) {
        /* @formatter:off */
        return (t) -> {try {consumer.accept(t);} catch (Throwable e) {De.fault(fallen).accept(t, (EX) e);}};
        /* @formatter:on */
      }

      /** {@inheritDoc} */
      @Override
      default void accept(T t) {/* @formatter:off */try {orThrow(t);} catch (Throwable ex) {sneakyThrow(ex);}/* @formatter:on */}

      /** {@inheritDoc} */
      @Override
      default Consumer<T> andThen(Consumer<? super T> after) {/* @formatter:off */return of(Consumer.super.andThen(De.fault(after)));/* @formatter:on */}

      /**
       * performs this operation on the given arguments .
       *
       * @param t the value of the input to the operation
       * @throws Throwable anything thrown
       */
      void orThrow(T t) throws Throwable;
    }

    /**
     * consumer.accept(t, u) if done it normally, or fallen.accept(t, EX) if error occurred .
     *
     * @param <T> the type of the input to the operation
     * @param <EX> anything throwan
     * @param t the value of the input to the operation
     * @param consumer {@link Consumer}, may not be null
     * @param fallen {@link BiConsumer}, do nothing if this is null
     * @see {@link Consumers.Uni#accept(Object)}
     */
    static <T, EX extends Throwable> void orElse(final T t, final Consumer<? super T> consumer, final BiConsumer<? super T, ? super EX> fallen) {
      Uni.of(consumer, fallen).accept(t);
    }

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
     */
    static <T, U, EX extends Throwable> void orElse(final T t, final U u, final BiConsumer<? super T, ? super U> consumer, final Tri<? super T, ? super U, ? super EX> fallen) {
      Bi.of(consumer, fallen).accept(t, u);
    }

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
     */
    static <T, U, V, EX extends Throwable> void orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> consumer, final Consumer<? super EX> fallen) {
      Tri.of(consumer, fallen).accept(t, u, v);
    }

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
     */
    static <T, U, V> void orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> consumer, final Tri<? super T, ? super U, ? super V> fallen) {
      Tri.of(consumer, fallen).accept(t, u, v);
    }

    /**
     * mute out any exceptions whether the operation throws it .
     *
     * @param <T> the type of the input to the operation
     * @param t the value of the input to the operation
     * @param consumer {@link Consumer}, may not be null
     */
    static <T, U, V> void orNot(final T t, final Consumer<? super T> consumer) {
      Uni.of(consumer).accept(t);
    }

    /**
     * mute out any exceptions whether the operation throws it .
     *
     * @param <T> the type of the first argument to the operation
     * @param <U> the type of the second argument to the operation
     * @param t the value of the first argument to the operation
     * @param u the value of the second argument to the operation
     * @param consumer {@link BiConsumer}, may not be null
     */
    static <T, U> void orNot(final T t, final U u, final BiConsumer<? super T, ? super U> consumer) {
      Bi.of(consumer).accept(t, u);
    }

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
     */
    static <T, U, V> void orNot(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> consumer) {
      Tri.of(consumer).accept(t, u, v);
    }
  }

  /** shorthands for {@link Objects#requireNonNullElse(Object, Object)} . */
  static interface De {/* @formatter:off */
    @SuppressWarnings({"unchecked"}) private static <T, U> BiConsumer<T, U> fault(final BiConsumer<? super T, ? super U> consumer) {return (BiConsumer<T, U>) Objects.requireNonNullElse(consumer, (t, u) -> {});}
    @SuppressWarnings({"unchecked"}) private static <T, U, R> BiFunction<T, U, R> fault(final BiFunction<? super T, ? super U, ? extends R> function) {return (BiFunction<T, U, R>) Objects.requireNonNullElse(function, (t, u) -> null);}
    @SuppressWarnings({"unchecked"}) private static <T, U> BiPredicate<T, U> fault(final BiPredicate<? super T, ? super U> predicate) {return (BiPredicate<T, U>) Objects.requireNonNullElse(predicate, (t, u) -> false);}
    @SuppressWarnings({"unchecked"}) private static <T> Comparator<T> fault(final Comparator<? super T> comparator) {return (Comparator<T>) Objects.requireNonNullElse(comparator, new Comparator<>() {@Override public int compare(Object o1, Object o2) {return 0;}});}
    @SuppressWarnings({"unchecked"}) private static <T> Consumer<T> fault(final Consumer<? super T> consumer) {return (Consumer<T>) Objects.requireNonNullElse(consumer, (t) -> {});}
    @SuppressWarnings({"unchecked"}) private static <T, U, V> Consumers.Tri<T, U, V> fault(final Consumers.Tri<? super T, ? super U, ? super V> consumer) {return (Consumers.Tri<T, U, V>) Objects.requireNonNullElse(consumer, (t, u, v) -> {});}
    @SuppressWarnings({"unchecked"}) private static <T, R> Function<T, R> fault(final Function<? super T, ? extends R> function) {return (Function<T, R>) Objects.requireNonNullElse(function, (t) -> null);}
    @SuppressWarnings({"unchecked"}) private static <T, U, V, R> Functions.Tri<T, U, V, R> fault(final Functions.Tri<? super T, ? super U, ? super V, ? extends R> function) {return (Functions.Tri<T, U, V, R>) Objects.requireNonNullElse(function, (t, u, v) -> null);}
    @SuppressWarnings({"unchecked"}) private static <T> Predicate<T> fault(final Predicate<? super T> predicate) {return (Predicate<T>) Objects.requireNonNullElse(predicate, (t) -> false);}
    @SuppressWarnings({"unchecked"}) private static <T, U, V> Predicates.Tri<T, U, V> fault(final Predicates.Tri<? super T, ? super U, ? super V> predicate) {return (Predicates.Tri<T, U, V>) Objects.requireNonNullElse(predicate, (t, u, v) -> false);}
    @SuppressWarnings({"unchecked"}) private static <R> Supplier<R> fault(final Supplier<? extends R> supplier) {return (Supplier<R>) Objects.requireNonNullElse(supplier, () -> null);}
  /* @formatter:on */}

  /**
   * the {@link Function} against some problems when handling exceptions in lambda expression .
   *
   * @author furplag
   *
   */
  static interface Functions {

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
       * @param function {@link BiFunction}, may not be null
       * @return {@link BiFunction}
       * @throws NullPointerException if function is null
       */
      static <T, U, R> Bi<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function) {
        return of(function, null);
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the function
       * @param <U> the type of the second argument to the function
       * @param <R> the type of the result of the function
       * @param <EX> anything thrown
       * @param function {@link BiFunction}, may not be null
       * @param fallen {@link Tri}, or the function that always return null if this is null
       * @return {@link BiFunction}
       * @throws NullPointerException if function is null
       */
      @SuppressWarnings({"unchecked"})
      static <T, U, R, EX extends Throwable> Bi<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function, final Tri<? super T, ? super U, ? super EX, ? extends R> fallen) {
        /* @formatter:off */
        return (t, u) -> {try {return function.apply(t, u);} catch (Throwable e) {return De.fault(fallen).apply(t, u, (EX) e);}};
        /* @formatter:on */
      }

      /** {@inheritDoc} */
      @Override
      default <V> Bi<T, U, V> andThen(Function<? super R, ? extends V> after) {/* @formatter:off */return of(BiFunction.super.andThen(De.fault(after)));/* @formatter:on */}

      /** {@inheritDoc} */
      @Override
      default R apply(T t, U u) {/* @formatter:off */try {return orThrow(t, u);} catch (Throwable e) {sneakyThrow(e);} return null;/* @formatter:on */}

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
       * @param function {@link Tri}, may not be null
       * @return {@link Tri}
       * @throws NullPointerException if function is null
       */
      static <T, U, V, R> Tri<T, U, V, R> of(final Tri<? super T, ? super U, ? super V, ? extends R> function) {
        /* @formatter:off */
        return of(function, (Tri<T, U, V, R>) null);
        /* @formatter:on */
      }

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
      @SuppressWarnings({"unchecked"})
      static <T, U, V, R, EX extends Throwable> Tri<T, U, V, R> of(final Tri<? super T, ? super U, ? super V, ? extends R> function, final Function<? super EX, ? extends R> fallen) {
        /* @formatter:off */
        return (t, u, v) -> {try {return function.apply(t, u, v);} catch (Throwable e) {return De.fault(fallen).apply((EX) e);}};
        /* @formatter:on */
      }

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
       */
      static <T, U, V, R> Tri<T, U, V, R> of(final Tri<? super T, ? super U, ? super V, ? extends R> function, final Tri<? super T, ? super U, ? super V, ? extends R> fallen) {
        /* @formatter:off */
        return (t, u, v) -> {try {return function.apply(t, u, v);} catch (Throwable e) {return De.fault(fallen).apply(t, u, v);}};
        /* @formatter:on */
      }

      /**
       * returns a composed function that first applies this function to its input, and then applies the after function to the result .
       * <p>
       * if evaluation of either function throws an exception, it is relayed to the caller of the composed function .
       *
       * @param <X> the type of output of the after function, and of the composed function
       * @param after the function to apply after this function is applied
       * @return a composed function that first applies this function and then applies the after function
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
      default R apply(T t, U u, V v) {/* @formatter:off */try {return orThrow(t, u, v);} catch (Throwable e) {sneakyThrow(e);} return null;/* @formatter:on */}

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
       * @param function {@link Function}, may not be null
       * @return {@link Function}
       * @throws NullPointerException if function is null
       *//* @formatter:off */
      static <T, R> Uni<T, R> of(final Function<? super T, ? extends R> function) {return of(function, null);}
      /* @formatter:on */

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the input to the function
       * @param <R> the type of the result of the function
       * @param <EX> anything thrown
       * @param function {@link Function}, may not be null
       * @param fallen {@link BiFunction}, or the function that always return null if this is null
       * @return {@link Function}
       * @throws NullPointerException if function is null
       */
      @SuppressWarnings({"unchecked"})
      static <T, R, EX extends Throwable> Uni<T, R> of(final Function<? super T, ? extends R> function, final BiFunction<? super T, ? super EX, ? extends R> fallen) {
        /* @formatter:off */
        return (t) -> {try {return function.apply(t);} catch (Throwable e) {return De.fault(fallen).apply(t, (EX) e);}};
        /* @formatter:on */
      }

      /** {@inheritDoc} */
      @Override
      default <V> Uni<T, V> andThen(Function<? super R, ? extends V> after) {
        return of(Function.super.andThen(De.fault(after)));
      }

      /** {@inheritDoc} */
      @Override
      default R apply(T t) {/* @formatter:off */try {return orThrow(t);} catch (Throwable e) {sneakyThrow(e);} return null;/* @formatter:on */}

      /** {@inheritDoc} */
      @Override
      default <V> Uni<V, R> compose(Function<? super V, ? extends T> before) {/* @formatter:off */return of(Function.super.compose(De.fault(before)));/* @formatter:on */}

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
     * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error occurred .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     * @param t the value of the first argument to the function
     * @param u the value of the second argument to the function
     * @param function {@link BiFunction}, may not be null
     * @param fallen {@link Supplier}, or the function that always return null if this is null
     * @return the result of function.apply(t, u) if done it normally, or fallen.get() if error occurred
     * @see {@link Functions.Bi#apply(Object, Object)}
     */
    static <T, U, R> R orElse(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final Supplier<R> fallen) {
      /* @formatter:off */
      return Bi.of(function, (x, y, ex) -> De.fault(fallen).get()).apply(t, u);
      /* @formatter:on */
    }

    /**
     * returns the result of function.apply(t, u) if done it normally, or fallen.apply(t, u, EX) if error occurred .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     * @param <EX> anything throwan
     * @param t the value of the first argument to the function
     * @param u the value of the second argument to the function
     * @param function {@link BiFunction}, may not be null
     * @param fallen {@link Functions.Tri}, or the function that always return null if this is null
     * @return the result of function.apply(t, u) if done it normally, or fallen.apply(t, u, EX) if error occurred
     * @see {@link Functions.Bi#apply(Object, Object)}
     */
    static <T, U, R, EX extends Throwable> R orElse(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final Tri<? super T, ? super U, ? super EX, ? extends R> fallen) {
      return Bi.of(function, fallen).apply(t, u);
    }

    /**
     * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error occurred .
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
     * @return the result of function.apply(t, u, v) if done it normally, or fallen.get() if error occurred
     * @see {@link Functions.Tri#apply(Object, Object, Object)}
     */
    static <T, U, V, R> R orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V, ? extends R> function, final Supplier<R> fallen) {
      return Tri.of(function, (x, y, z) -> De.fault(fallen).get()).apply(t, u, v);
    }

    /**
     * returns the result of function.apply(t, u, v) if done it normally, or fallen.apply(t, u, v) if error occurred .
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
     * @return the result of function.apply(t, u, v) if done it normally, or fallen.apply(t, u, v) if error occurred
     * @see {@link Functions.Tri#apply(Object, Object, Object)}
     */
    static <T, U, V, R> R orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V, ? extends R> function, final Tri<? super T, ? super U, ? super V, ? extends R> fallen) {
      return Tri.of(function, fallen).apply(t, u, v);
    }

    /**
     * returns the result of function.apply(t, u, v) if done it normally, or fallen.apply(EX) if error occurred .
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
     * @return the result of function.apply(t, u, v) if done it normally, or fallen.apply(EX) if error occurred
     * @see {@link Functions.Tri#apply(Object, Object, Object)}
     */
    static <T, U, V, R, EX extends Throwable> R orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V, R> function, final Function<? super EX, ? extends R> fallen) {
      return Tri.of(function, fallen).apply(t, u, v);
    }

    /**
     * returns the result of function.apply(t) if done it normally, or fallen.apply(t, EX) if error occurred .
     *
     * @param <T> the type of the first argument to the function
     * @param <R> the type of the result of the function
     * @param <EX> anything throwan
     * @param t the value of the first argument to the function
     * @param function {@link Function}, may not be null
     * @param fallen {@link BiFunction}, or the function that always return null if this is null
     * @return the result of function.apply(t) if done it normally, or fallen.apply(t, EX) if error occurred
     * @see {@link Functions.Uni#apply(Object)}
     *//* @formatter:off */
    static <T, R, EX extends Throwable> R orElse(final T t, final Uni<? super T, ? extends R> function, final BiFunction<? super T, ? super EX, ? extends R> fallen) {
      return Uni.of(function, fallen).apply(t);
    }/* @formatter:on */

    /**
     * returns the result of function.apply(t, u, v) if done it normally, or fallen.get() if error occurred .
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param t the value of the input to the function
     * @param function {@link Function}, may not be null
     * @param fallen {@link Supplier}, or the function that always return null if this is null
     * @return the result of function.apply(t) if done it normally, or fallen.get() if error occurred
     * @see {@link Functions.Uni#apply(Object)}
     */
    static <T, R> R orElse(final T t, final Uni<? super T, ? extends R> function, final Supplier<R> fallen) {
      return Uni.of(function, (x, ex) -> De.fault(fallen).get()).apply(t);
    }

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
     */
    static <T, U, R> R orNot(final T t, final U u, final Bi<? super T, ? super U, ? extends R> function) {
      return Bi.of(function, (x, y, ex) -> null).apply(t, u);
    }

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
     */
    static <T, U, V, R> R orNot(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V, ? extends R> function) {
      return Tri.of(function, (ex) -> null).apply(t, u, v);
    }

    /**
     * returns the result of function.apply(t) if done it normally, or null if error occurred .
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param t the value of the first argument to the function
     * @param function {@link Function}, may not be null
     * @return the result of function.apply(t) if done it normally, or null if error occurred
     */
    static <T, R> R orNot(final T t, final Uni<? super T, ? extends R> function) {
      return Uni.of(function, (x, ex) -> null).apply(t);
    }
  }

  /**
   * the {@link UnaryOperator} against some problems when handling exceptions in lambda expression .
   *
   * @author furplag
   *
   */
  static interface Operators {

    /**
     * {@link BinaryOperator} now get enable to throw {@link Throwable} .
     *
     * @author furplag
     *
     * @param <T> the type of the operands and result of the operator
     * @see {@link Functions.Bi}
     * @see {@link BinaryOperator}
     */
    @FunctionalInterface
    static interface Bi<T> extends Functions.Bi<T, T, T>, BinaryOperator<T> {

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param operator {@link BinaryOperator}, may not be null
       * @return {@link BinaryOperator}
       * @throws NullPointerException if {@code operator} is null
       */
      static <T> Bi<T> of(final BiFunction<? super T, ? super T, ? extends T> operator) {/* @formatter:off */return of(operator, null);/* @formatter:on */}

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param <EX> anything thrown
       * @param operator {@link BinaryOperator}, may not be null
       * @param fallen {@link Functions.Tri}, or the operator that always return null if this is null
       * @return {@link BinaryOperator}
       * @throws NullPointerException if {@code operator} is null
       */
      static <T, EX extends Throwable> Bi<T> of(final BiFunction<? super T, ? super T, ? extends T> operator, final Functions.Tri<? super T, ? super T, ? super EX, ? extends T> fallen) {
        /* @formatter:off */
        return Functions.Bi.of(operator, De.fault(fallen))::apply;
        /* @formatter:on */
      }
    }

    /**
     * represents a function that accepts three arguments and produces a result .
     * <p>
     * this is the three-arity specialization of {@link UnaryOperator} .
     *
     * @author furplag
     *
     * @param <T> the type of the operands and result of the operator
     * @see {@link Functions.Tri}
     * @see {@link UnaryOperator}
     */
    @FunctionalInterface
    static interface Tri<T> extends Functions.Tri<T, T, T, T> {

      /**
       * returns a {@link Tri} which returns the greater of three elements according to the specified {@code Comparator} .
       *
       * @param <T> the type of the input arguments of the comparator
       * @param comparator a {@code Comparator} for comparing the three values
       * @return a {@link Tri} which returns the greater of its operands, according to the supplied {@link Comparator}
       * @throws NullPointerException if the argument is null
       */
      static <T> Tri<T> maxBy(Comparator<? super T> comparator) {
        return (a, b, c) -> Streamr.stream(a, b, c).max(De.fault(comparator)).orElse(null);
      }

      /**
       * returns a {@link Tri} which returns the lesser of three elements according to the specified {@code Comparator} .
       *
       * @param <T> the type of the input arguments of the comparator
       * @param comparator a {@code Comparator} for comparing the three values
       * @return a {@link Tri} which returns the lesser of its operands, according to the supplied {@link Comparator}
       * @throws NullPointerException if the argument is null
       */
      static <T> Tri<T> minBy(Comparator<? super T> comparator) {
        return (a, b, c) -> Streamr.stream(a, b, c).min(De.fault(comparator)).orElse(null);
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param operator {@link Tri}, may not be null
       * @return {@link Tri}
       * @throws NullPointerException if operator is null
       */
      static <T> Tri<T> of(final Functions.Tri<? super T, ? super T, ? super T, ? extends T> operator) {
        return of(operator, (Tri<T>) null);
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param <EX> anything thrown
       * @param operator {@link Tri}, may not be null
       * @param fallen {@link Function}, or the function that always return null if this is null
       * @return {@link Tri}
       * @throws NullPointerException if operator is null
       */
      static <T, EX extends Throwable> Tri<T> of(final Functions.Tri<? super T, ? super T, ? super T, ? extends T> operator, final Function<? super EX, ? extends T> fallen) {
        return Functions.Tri.of(operator, De.fault(fallen))::apply;
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param operator {@link Tri}, may not be null
       * @param fallen {@link Tri}, or the operator that always return false if this is null
       * @return {@link Tri}
       * @throws NullPointerException if operator is null
       */
      static <T> Tri<T> of(final Functions.Tri<? super T, ? super T, ? super T, ? extends T> operator, final Functions.Tri<? super T, ? super T, ? super T, ? extends T> fallen) {
        return Functions.Tri.of(operator, De.fault(fallen))::apply;
      }
    }

    /**
     * {@link UnaryOperator} now get enable to throw {@link Throwable} .
     *
     * @author furplag
     *
     * @param <T> the type of the operands and result of the operator
     * @see {@link Functions.Uni}
     * @see {@link UnaryOperator}
     */
    @FunctionalInterface
    static interface Uni<T> extends Functions.Uni<T, T>, UnaryOperator<T> {

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param operator {@link UnaryOperator}, may not be null
       * @return {@link UnaryOperator}
       * @throws NullPointerException if {@code operator} is null
       */
      static <T> Uni<T> of(final Function<? super T, ? extends T> operator) {/* @formatter:off */return Functions.Uni.of(operator)::apply;/* @formatter:on */}

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the operands and result of the operator
       * @param <EX> anything thrown
       * @param operator {@link BinaryOperator}, may not be null
       * @param fallen {@link Functions.Tri}, or the operator that always return null if this is null
       * @return {@link BinaryOperator}
       * @throws NullPointerException if {@code operator} is null
       */
      static <T, EX extends Throwable> Uni<T> of(final Function<? super T, ? extends T> operator, final Functions.Bi<? super T, ? super EX, ? extends T> fallen) {
        return Functions.Uni.of(operator, fallen)::apply;
      }
    }
  }

  /**
   * the {@link Predicate} against some problems when handling exceptions in lambda expression .
   *
   * @author furplag
   *
   */
  static interface Predicates {

    /**
     * {@link BiPredicate} now get enable to throw {@link Throwable} .
     *
     * @author furplag
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @see {@link Functions.Bi}
     * @see {@link BiPredicate}
     */
    @FunctionalInterface
    static interface Bi<T, U> extends Functions.Bi<T, U, Boolean>, BiPredicate<T, U> {

      /**
       * returns a predicate that is the negation of the supplied predicate. this is accomplished by returning result of the calling {@code target.negate()} .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param target predicate to negate
       * @return a predicate that negates the results of the supplied predicate
       * @throws NullPointerException if target is null
       */
      @SuppressWarnings("unchecked")
      static <T, U> Bi<T, U> not(final BiPredicate<? super T, ? super U> target) {
        return (Bi<T, U>) of(target).negate();
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param predicate {@link BiPredicate}, may not be null
       * @return {@link BiPredicate}
       * @throws NullPointerException if {@code predicate} is null
       */
      static <T, U> Bi<T, U> of(final BiPredicate<? super T, ? super U> predicate) {/* @formatter:off */return of(predicate, null)::apply;/* @formatter:on */}

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
       */
      static <T, U, EX extends Throwable> Bi<T, U> of(final BiPredicate<? super T, ? super U> predicate, final Tri<? super T, ? super U, ? super EX> fallen) {
        return Functions.Bi.of(predicate::test, De.fault(fallen))::apply;
      }

      /** {@inheritDoc} */
      @Override
      default Bi<T, U> and(BiPredicate<? super T, ? super U> other) {
        return of(BiPredicate.super.and(De.fault(other)));
      }

      /** {@inheritDoc} */
      @Override
      default <V> Functions.Bi<T, U, V> andThen(Function<? super Boolean, ? extends V> after) {
        throw new UnsupportedOperationException();
      }

      /** {@inheritDoc} */
      @Override
      default Bi<T, U> negate() {
        return of(BiPredicate.super.negate());
      }

      /** {@inheritDoc} */
      @Override
      default Bi<T, U> or(BiPredicate<? super T, ? super U> other) {
        return of(BiPredicate.super.or(De.fault(other)));
      }

      /** {@inheritDoc} */
      @Override
      default boolean test(T t, U u) {
        return apply(t, u);
      }
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
     * @see {@link Functions.Tri}
     * @see {@link Predicate}
     */
    @FunctionalInterface
    public interface Tri<T, U, V> extends Functions.Tri<T, U, V, Boolean> {

      /**
       * returns a predicate that is the negation of the supplied predicate. this is accomplished by returning result of the calling {@code target.negate()} .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param <V> the type of the third argument to the predicate
       * @param target predicate to negate
       * @return a predicate that negates the results of the supplied predicate
       * @throws NullPointerException if target is null
       */
      @SuppressWarnings("unchecked")
      static <T, U, V> Tri<T, U, V> not(final Tri<? super T, ? super U, ? super V> target) {
        return (Tri<T, U, V>) target.negate();
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param <V> the type of the third argument to the predicate
       * @param predicate {@link Tri}, may not be null
       * @return {@link Tri}
       * @throws NullPointerException if predicate is null
       */
      static <T, U, V> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> predicate) {
        return of(predicate, (Tri<T, U, V>) null)::apply;
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param <V> the type of the third argument to the predicate
       * @param <EX> anything thrown
       * @param predicate {@link Tri}, may not be null
       * @param fallen {@link Predicate}, or the predicate that always return false if this is null
       * @return {@link Tri}
       * @throws NullPointerException if predicate is null
       */
      static <T, U, V, R, EX extends Throwable> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> predicate, final Predicate<? super EX> fallen) {
        return Functions.Tri.of(predicate, De.fault(fallen)::test)::apply;
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the first argument to the predicate
       * @param <U> the type of the second argument to the predicate
       * @param <V> the type of the third argument to the predicate
       * @param predicate {@link Tri}, may not be null
       * @param fallen {@link Tri}, or the predicate that always return false if this is null
       * @return {@link Tri}
       * @throws NullPointerException if predicate is null
       */
      static <T, U, V> Tri<T, U, V> of(final Tri<? super T, ? super U, ? super V> predicate, final Tri<? super T, ? super U, ? super V> fallen) {
        return Functions.Tri.of(predicate, De.fault(fallen))::apply;
      }

      /**
       * returns a composed predicate that represents a short-circuiting logical AND of this predicate and another .
       *
       * @param other a predicate that will be logically-ANDed with this predicate
       * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the other predicate
       */
      default Tri<T, U, V> and(Tri<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> test(t, u, v) && De.fault(other).apply(t, u, v);
      }

      /** {@inheritDoc} */
      @Override
      default <X> jp.furplag.sandbox.trebuchet.Trebuchet.Functions.Tri<T, U, V, X> andThen(Function<? super Boolean, ? extends X> after) {
        throw new UnsupportedOperationException();
      }

      /**
       * returns a predicate that represents the logical negation of this predicate .
       *
       * @return a predicate that represents the logical negation of this predicate
       */
      default Tri<T, U, V> negate() {
        return (t, u, v) -> !test(t, u, v);
      }

      /**
       * returns a composed predicate that represents a short-circuiting logical OR of this predicate and another. When evaluating the composed predicate, if this predicate is {@code true}, then the other predicate is not evaluated .
       *
       * <p>
       * any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated .
       *
       * @param other a predicate that will be logically-ORed with this predicate
       * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the other predicate
       * @throws NullPointerException if other is null
       */
      default Tri<T, U, V> or(Tri<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> test(t, u, v) || De.fault(other).apply(t, u, v);
      }

      /**
       * evaluates this predicate on the given arguments .
       *
       * @param t the value of the first argument to the predicate
       * @param u the value of the second argument to the predicate
       * @param v the value of the third argument to the predicate
       * @return true if the input arguments match the predicate, otherwise false
       */
      default boolean test(T t, U u, V v) {
        return Functions.Tri.super.apply(t, u, v);
      }
    }

    /**
     * {@link Predicate} now get enable to throw {@link Throwable} .
     *
     * @author furplag
     *
     * @param <T> the type of the input to the predicate
     * @see {@link Functions.Uni}
     * @see {@link Predicate}
     */
    @FunctionalInterface
    static interface Uni<T> extends Functions.Uni<T, Boolean>, Predicate<T> {

      /**
       * returns a predicate that is the negation of the supplied predicate .
       *
       * @param <T> the type of the input to the predicate
       * @param target predicate to negate
       *
       * @return a predicate that negates the results of the supplied predicate
       */
      @SuppressWarnings("unchecked")
      static <T> Uni<T> not(final Predicate<? super T> target) {
        return (Uni<T>) of(Objects.requireNonNullElse(target, (x) -> true)).negate();
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the input to the predicate
       * @param predicate {@link Predicate}, may not be null
       * @return {@link Predicate}
       * @throws NullPointerException if predicate is null
       */
      static <T> Uni<T> of(final Predicate<T> predicate) {
        return of(predicate, null);
      }

      /**
       * should never write ugly try-catch block to handle exceptions in lambda expression .
       *
       * @param <T> the type of the input to the predicate
       * @param <EX> anything thrown
       * @param predicate {@link Predicate}, may not be null
       * @param fallen {@link BiPredicate}, do nothing if this is null
       * @return {@link Predicate}
       * @throws NullPointerException if predicate is null
       */
      static <T, EX extends Throwable> Uni<T> of(final Predicate<T> predicate, final BiPredicate<? super T, ? super EX> fallen) {
        return Functions.Uni.of(predicate::test, De.fault(fallen)::test)::apply;
      }

      /** {@inheritDoc} */
      @Override
      default Uni<T> and(Predicate<? super T> other) {
        return of(Predicate.super.and(De.fault(other)));
      }

      /** {@inheritDoc} */
      @Override
      default <V> Functions.Uni<T, V> andThen(Function<? super Boolean, ? extends V> after) {
        throw new UnsupportedOperationException();
      }

      /** {@inheritDoc} */
      @Override
      default Uni<T> negate() {
        return of(Predicate.super.negate());
      }

      /** {@inheritDoc} */
      @Override
      default Uni<T> or(Predicate<? super T> other) {
        return of(Predicate.super.or(De.fault(other)));
      }

      /** {@inheritDoc} */
      @Override
      default boolean test(T t) {
        return apply(t);
      }
    }

    /**
     * returns the result of predicate.test(t, u) if done it normally, or fallen.test(t, u, EX) if error occurred .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param predicate {@link Bi}, may not be null
     * @param fallen {@link Tri}, or the function that always return false if this is null
     * @return the result of predicate.test(t, u, v) if done it normally, or fallen.test(t, u, EX) if error occurred
     */
    static <T, U, EX extends Throwable> boolean orElse(final T t, final U u, final Bi<? super T, ? super U> predicate, final Tri<? super T, ? super U, ? super EX> fallen) {
      return Functions.orElse(t, u, predicate::test, fallen);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or fallen.test(t, u, v) if error occurred .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param <V> the type of the third argument to the predicate
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param v the value of the third argument to the predicate
     * @param predicate {@link Tri}, may not be null
     * @param fallen {@link Tri}, or the function that always return false if this is null
     * @return the result of predicate.test(t, u, v) if done it normally, or fallen.test(t, u, v) if error occurred
     */
    static <T, U, V> boolean orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> predicate, final Tri<? super T, ? super U, ? super V> fallen) {
      return Functions.orElse(t, u, v, predicate, fallen);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or fallen.test(EX) if error occurred .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param <V> the type of the third argument to the predicate
     * @param <EX> anything thrown
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param v the value of the third argument to the predicate
     * @param predicate {@link Tri}, may not be null
     * @param fallen {@link Uni}, or the function that always return false if this is null
     * @return the result of predicate.test(t, u, v) if done it normally, or fallen.test(EX) if error occurred
     */
    static <T, U, V, EX extends Throwable> boolean orElse(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> predicate, final Uni<? super EX> fallen) {
      return Functions.orElse(t, u, v, predicate, fallen::test);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or fallen.test(t, EX) if error occurred .
     *
     * @param <T> the type of the input to the predicate
     * @param <EX> anything thrown
     * @param t the value of the input to the predicate
     * @param predicate {@link Uni}, may not be null
     * @param fallen {@link Bi}, or the function that always return false if this is null
     * @return the result of predicate.test(t, u, v) if done it normally, or fallen.test(t, EX) if error occurred
     */
    @SuppressWarnings("unchecked")
    static <T, EX extends Throwable> boolean orElse(final T t, final Uni<? super T> predicate, final Bi<? super T, ? super EX> fallen) {
      return Functions.orElse(t, predicate::test, De.fault((BiPredicate<T, EX>) fallen)::test);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param predicate {@link Bi}, may not be null
     * @return the result of predicate.test(t, u) if done it normally, or false if error occurred
     */
    static <T, U> boolean orNot(final T t, final U u, final Bi<? super T, ? super U> predicate) {
      return Functions.orElse(t, u, predicate::test, () -> false);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
     *
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument to the predicate
     * @param <V> the type of the third argument to the predicate
     * @param t the value of the first argument to the predicate
     * @param u the value of the second argument to the predicate
     * @param v the value of the third argument to the predicate
     * @param predicate {@link Tri}, may not be null
     * @return the result of predicate.test(t, u, v) if done it normally, or false if error occurred
     */
    static <T, U, V> boolean orNot(final T t, final U u, final V v, final Tri<? super T, ? super U, ? super V> predicate) {
      return Functions.orElse(t, u, v, predicate, () -> false);
    }

    /**
     * returns the result of predicate.test(t, u, v) if done it normally, or false if error occurred .
     *
     * @param <T> the type of the input to the predicate
     * @param t the value of the input to the predicate
     * @param predicate {@link Uni}, may not be null
     * @return the result of predicate.test(t) if done it normally, or false if error occurred
     */
    static <T> boolean orNot(final T t, final Uni<? super T> predicate) {
      return Functions.orElse(t, predicate::test, () -> false);
    }
  }

  /**
   * throws any throwable 'sneakily' .
   *
   * @param <EX> anything thrown
   * @param ex anything thrown
   * @throws EX anything thrown
   * @see <a href= "https://projectlombok.org/features/SneakyThrows">lombok.Lombok#sneakyThrow(Throwable)</a>
   */
  @SuppressWarnings({"unchecked"})
  static <EX extends Throwable> void sneakyThrow(final Throwable ex) throws EX {
    throw (EX) Objects.requireNonNullElse(ex, new IllegalArgumentException("hmm, no way call me with null ."));
  }
}
