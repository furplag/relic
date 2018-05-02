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

package jp.furplag.sandbox.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * code snippets for some problems when using Stream API .
 *
 * @author furplag
 *
 */
public interface Trebuchet {

  /**
   * {@link java.util.function.Consumer Consumer} now get enable to throw {@link Exception} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @see java.util.function.Consumer
   */
  @FunctionalInterface
  public interface ThrowableConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws Exception an exception
     */
    void accept(T t) throws Exception;

    /**
     * should never write "ugly" try-catch block for exceptions in stream .
     *
     * @param <T> the type of the input to the function
     * @param function {@link ThrowableConsumer}
     * @param fallen {@link BiConsumer}
     * @return {@link ThrowableConsumer#accept(Object) function#accept(T)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen#accept(Throwable, T)} if error occured
     */
    private static <T> Consumer<T> acceptOrElse(final ThrowableConsumer<T> function, final BiConsumer<Exception, T> fallen) {
      return (x) -> {
        try {
          function.accept(x);
        } catch (Exception e) {
          fallen.accept(e, x);
        }
      };
    }
  }


  /**
   * {@link java.util.function.BiConsumer BiConsumer} now get enable to throw {@link Exception} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @see java.util.function.BiConsumer
   */
  @FunctionalInterface
  public interface ThrowableBiConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @throws Exception an exception
     */
    void accept(T t, U u) throws Exception;

    /**
     * should never write "ugly" try-catch block for exceptions in stream .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param function {@link ThrowableBiConsumer}
     * @param fallen {@link BiConsumer}
     * @return {@link ThrowableBiConsumer#accept(Object, Object) function#accept(T, U)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen#accept(Throwable, T)} if error occured
     */
    private static <T, U> BiConsumer<T, U> acceptOrElse(final ThrowableBiConsumer<T, U> function, final BiConsumer<Exception, T> fallen) {
      return (x, y) -> {
        try {
          function.accept(x, y);
        } catch (Exception e) {
          fallen.accept(e, x);
        }
      };
    }
  }

  /**
   * {@link java.util.function.Function Function} now get enable to throw {@link Exception} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @see java.util.function.Function
   */
  @FunctionalInterface
  public interface ThrowableFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws Exception an exception
     */
    R apply(T t) throws Exception;

    /**
     * should never write "ugly" try-catch block for exceptions in stream .
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param function {@link ThrowableFunction}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableFunction#apply(Object) function#apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen#apply(Throwable, T)} if error occured
     */
    private static <T, R> Function<T, R> applyOrElse(final ThrowableFunction<T, R> function, final BiFunction<Exception, T, R> fallen) {
      return (x) -> {
        try {
          return function.apply(x);
        } catch (Exception e) {
          return fallen.apply(e, x);
        }
      };
    }
  }

  /**
   * {@link java.util.function.BiFunction BiFunction} now get enable to throw {@link Exception} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @see java.util.function.BiFunction
   */
  @FunctionalInterface
  public interface ThrowableBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws Exception an exception
     */
    R apply(T t, U u) throws Exception;

    /**
     * should never write "ugly" try-catch block for exceptions in stream .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     * @param function {@link ThrowableBiFunction}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableBiFunction#apply(Object, Object) function#apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen#apply(Throwable, T)} if error occured
     */
    private static <T, U, R> BiFunction<T, U, R> applyOrElse(final ThrowableBiFunction<T, U, R> function, final BiFunction<Exception, T, R> fallen) {
      return (x, y) -> {
        try {
          return function.apply(x, y);
        } catch (Exception e) {
          return fallen.apply(e, x);
        }
      };
    }
  }

  /**
   * should never write "ugly" try-catch block for exceptions in stream .
   *
   * @param <T> the type of the input to the function
   * @param function {@link ThrowableConsumer}
   * @param fallen {@link BiConsumer}
   * @return {@link ThrowableConsumer#accept(Object) function#accept(T)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen#accept(Throwable, T)} if error occured
   */
  static <T> Consumer<T> orElse(final ThrowableConsumer<T> function, final BiConsumer<Exception, T> fallen) {
    return ThrowableConsumer.acceptOrElse(function, fallen);
  }

  /**
   * should never write "ugly" try-catch block for exceptions in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param function {@link ThrowableBiConsumer}
   * @param fallen {@link BiConsumer}
   * @return {@link ThrowableBiConsumer#accept(Object, Object) function#accept(T, U)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen#accept(Throwable, T)} if error occured
   */
  static <T, U> BiConsumer<T, U> orElse(final ThrowableBiConsumer<T, U> function, final BiConsumer<Exception, T> fallen) {
    return ThrowableBiConsumer.acceptOrElse(function, fallen);
  }

  /**
   * should never write "ugly" try-catch block for exceptions in stream .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param function {@link ThrowableFunction}
   * @param fallen {@link BiFunction}
   * @return {@link ThrowableFunction#apply(Object) function#apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen#apply(Throwable, T)} if error occured
   */
  static <T, R> Function<T, R> orElse(final ThrowableFunction<T, R> function, final BiFunction<Exception, T, R> fallen) {
    return ThrowableFunction.applyOrElse(function, fallen);
  }

  /**
   * should never write "ugly" try-catch block for exceptions in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param function {@link ThrowableBiFunction}
   * @param fallen {@link BiFunction}
   * @return {@link ThrowableBiFunction#apply(Object, Object) function#apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen#apply(Throwable, T)} if error occured
   */
  static <T, U, R> BiFunction<T, U, R> orElse(final ThrowableBiFunction<T, U, R> function, final BiFunction<Exception, T, R> fallen) {
    return ThrowableBiFunction.applyOrElse(function, fallen);
  }
}
