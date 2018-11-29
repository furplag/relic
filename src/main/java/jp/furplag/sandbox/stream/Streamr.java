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

package jp.furplag.sandbox.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.furplag.function.ThrowablePredicate;

/**
 * do less coding when using Stream API .
 *
 * @author furplag
 *
 */
public interface Streamr {

  /**
   * we might to use this many .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @return {@link Collection} of T
   */
  static <T, R extends Collection<T>> R collect(final Stream<T> stream, final Supplier<R> supplier) {
    return stream(stream).collect(Collectors.toCollection(supplier));
  }

  /**
   * do less coding in case of {@link Stream#filter(java.util.function.Predicate) Stream#filter}({@link Objects#nonNull(Object) Objects::nonNull}) .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @return the stream which excluded null
   */
  private static <T> Stream<T> excludeNull(final Stream<T> stream) {
    return Optional.ofNullable(stream).orElseGet(Stream::empty).filter(Objects::nonNull);
  }

  /**
   * do less coding in case of {@link Collection#stream()} .
   *
   * @param <T> the type of stream elements
   * @param collection {@link Collection}, maybe null
   * @return the stream which excluded null
   */
  static <T> Stream<T> stream(final Collection<T> collection) {
    return collection == null ? Stream.empty() : excludeNull(collection.stream());
  }

  /**
   * do less coding when using {@link Stream} .
   *
   * @param <T> the type of stream elements
   * @param streams {@link Stream Stream(s)}, maybe null contains
   * @return the stream which excluded empty element
   */
  @SuppressWarnings("unchecked")
  @SafeVarargs
  static <T> Stream<T> stream(final Stream<? super T>... streams) {
    return streams == null ? Stream.empty() : (Stream<T>) Arrays.stream(streams).filter(Objects::nonNull).reduce(Stream::concat).orElseGet(Stream::empty).filter(Objects::nonNull);
  }

  /**
   * do less coding when using {@link Stream} .
   *
   * @param <T> the type of stream elements
   * @param stream {@link Stream}, maybe null
   * @return the stream which excluded empty element
   */
  static <T> Stream<T> stream(final Stream<T> stream) {
    return excludeNull(stream);
  }

  /**
   * do less coding in case of {@link Arrays#stream(Object[])} .
   * <p>
   * <strong>notice</strong>:
   *  do not do that, <em>"Arrays.stream(new int[]{1, 2, 3})"</em> .
   * </p>
   *
   * @param <T> the type of the stream elements
   * @param elements an array of T, maybe null
   * @return the stream which excluded null
   */
  @SafeVarargs
  static <T> Stream<T> stream(final T... elements) {
    return elements == null ? Stream.empty() : excludeNull(Arrays.stream(elements));
  }

  /**
   * do less coding when using Stream API .
   *
   * @author furplag
   *
   */
  public interface Filter {

    /**
     * controls of {@link Predicate#and(java.util.function.Predicate)} /  {@link Predicate#or(java.util.function.Predicate)} .
     *
     * @author furplag
     *
     */
    public static enum FilteringMode {
      /** {@link Predicate#and(Predicate)} . */
      And,
      /** {@link Predicate#or(Predicate)} . */
      Or;

      /**
       * detect the {@link FilteringMode} is &quot;{@link FilteringMode#And And}&quot; .
       *
       * @return {@code true} if {@link FilteringMode} is &quot;{@link FilteringMode#And And}&quot;, otherwise {@code false} .
       */
      public boolean and() {
        return this.equals(And);
      }

      /**
       * returns a {@link Predicate} which united specified conditions .
       *
       * @param <T> the type of the stream elements
       * @param function condition(s)
       * @return a {@link Predicate} which united specified conditions .
       */
      public <T> Predicate<T> predicate(Function<? super T, Boolean> function) {
        return ThrowablePredicate.of(function::apply, (t) -> and());
      }
    }

    /**
     * do less coding in case of {@link Stream#map(java.util.function.Function) Stream.map()}.{@link Stream#collect(java.util.stream.Collector) collect(Collectors.toList())}.{@link Collection#stream() stream()} .
     *
     * @param <T> the type of the stream elements
     * @param stream {@link Stream}, maybe null
     * @param operators {@link UnaryOperator}
     * @return unclosed (actually, duplicated) stream of T which modified each elements
     */
    @SafeVarargs
    static <T> Stream<T> tweak(final Stream<T> stream, final Function<T, T>... operators) {
      return Streamr.stream(stream).map(Streamr.stream(operators).reduce((a, b) -> a.andThen(b)).orElse(UnaryOperator.identity())).collect(Collectors.toList()).stream();
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.and(anotherOne).and(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param filteringMode unite specified conditions to one Predicate with &quot;{@link FilteringMode#And And}&quot; or &quot;{@link FilteringMode#Or Or}&quot;
     * @param stream {@link Stream}, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> filtering(final FilteringMode filteringMode, final Stream<T> stream, final Function<? super T, Boolean>... filters) {
      return Streamr.stream(stream).filter(unite(filteringMode, filters)).collect(Collectors.toList()).stream();
    }

    /**
     * unite specified conditions to one Predicate with &quot;{@link FilteringMode#And And}&quot; or &quot;{@link FilteringMode#Or Or}&quot; .
     *
     * @param filteringMode &quot;{@link FilteringMode#And And}&quot; or &quot;{@link FilteringMode#Or Or}&quot;
     * @param filters condition(s), maybe null
     * @return a {@link Predicate} which united specified conditions
     */
    @SafeVarargs
    private static <T> Predicate<? super T> unite(final FilteringMode filteringMode, final Function<? super T, Boolean>... filters) {
      final FilteringMode andOr = Objects.requireNonNullElse(filteringMode, FilteringMode.And);

      return Streamr.stream(filters).map(andOr::predicate).reduce((a, b) -> andOr.and() ? a.and(b) : a.or(b)).orElse((t) -> andOr.and());
    }
  }
}
