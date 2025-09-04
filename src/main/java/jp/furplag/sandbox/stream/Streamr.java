/*
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * do less coding when using Stream API .
 *
 * @author furplag
 *
 */
public interface Streamr {

  /**
   * do less coding when using {@link Stream#filter(Predicate)} .
   *
   * @author furplag
   *
   */
  public interface Filter {

    /**
     * controls of {@link Predicate#and(java.util.function.Predicate)} / {@link Predicate#or(java.util.function.Predicate)} .
     *
     * @author furplag
     *
     */
    static enum FilteringMode {
      /* @formatter:off *//** {@link Predicate#and(Predicate)} . */And, /** {@link Predicate#or(Predicate)} . */ Or;/* @formatter:on */

      /**
       * detect the {@link FilteringMode} is &quot;{@link FilteringMode#And And}&quot; .
       *
       * @return {@code true} if {@link FilteringMode} is &quot;{@link FilteringMode#And And}&quot;, otherwise {@code false} .
       */
      public boolean and() {/* @formatter:off */return this.equals(And);/* @formatter:on */}

      /**
       * returns a {@link Predicate} which united specified conditions .
       *
       * @param <T> the type of the stream elements
       * @param function condition(s)
       * @return a {@link Predicate} which united specified conditions .
       */
      public <T> Predicate<T> predicate(Function<? super T, Boolean> function) {
        return Trebuchet.Functions.Uni.of(function, (t, ex) -> and())::apply;
      }
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.or(anotherOne).or(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param collection {@link Collection}, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> anyOf(final Collection<T> collection, final Function<? super T, Boolean>... filters) {
      return anyOf(Streamr.stream(collection), filters);
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.or(anotherOne).or(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param stream {@link Stream}, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> anyOf(final Stream<T> stream, final Function<? super T, Boolean>... filters) {
      return filter(FilteringMode.Or, stream, filters);
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.or(anotherOne).or(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param elements an array of T, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> anyOf(final T[] elements, final Function<? super T, Boolean>... filters) {
      return anyOf(Streamr.stream(elements), filters);
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
    private static <T> Stream<T> filter(final FilteringMode filteringMode, final Stream<T> stream, final Function<? super T, Boolean>... filters) {
      return Streamr.stream(stream).filter(unite(filteringMode, filters)).collect(Collectors.toList()).stream();
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.and(anotherOne).and(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param collection {@link Collection}, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> filtering(final Collection<T> collection, final Function<? super T, Boolean>... filters) {
      return filtering(Streamr.stream(collection), filters);
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.and(anotherOne).and(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param stream {@link Stream}, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> filtering(final Stream<T> stream, final Function<? super T, Boolean>... filters) {
      return filter(FilteringMode.And, stream, filters);
    }

    /**
     * do less coding in case of {@link Stream#filter(Predicate) Stream.filter(predicate.and(anotherOne).and(other)...)} .
     *
     * @param <T> the type of the stream elements
     * @param elements an array of T, maybe null
     * @param filters condition(s), maybe null
     * @return unclosed (actually, duplicated) stream of T which filtered elements
     */
    @SafeVarargs
    static <T> Stream<T> filtering(final T[] elements, final Function<? super T, Boolean>... filters) {
      return filtering(Streamr.stream(elements), filters);
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
      return Streamr.stream(stream).map(Streamr.stream(operators).reduce((a, b) -> a.andThen(b)).orElse(UnaryOperator.identity()));
    }

    /**
     * unite specified conditions to one {@link Predicate} with &quot;{@link FilteringMode#And And}&quot; or &quot;{@link FilteringMode#Or Or}&quot; .
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

  /**
   * we might to use this many .
   *
   * @param <T> the type of the key of stream elements
   * @param <U> the type of the value of stream elements
   * @param entries {@link Stream} of {@link Map.Entry}, maybe null
   * @param mergeFunction a merge function, used to resolve collisions betweenvalues associated with the same key, as supplied to {@link Map#merge(Object, Object, java.util.function.BiFunction)}
   * @param supplier {@link Supplier} of result
   * @return {@link Map}
   */
  static <T, U> Map<T, U> collect(final Stream<? extends Map.Entry<T, U>> entries, BinaryOperator<U> mergeFunction, final Supplier<Map<T, U>> supplier) {
    return Streamr.stream(entries).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Objects.requireNonNullElse(mergeFunction, (current, next) -> next), Objects.requireNonNullElse(supplier, HashMap::new)));
  }

  /**
   * we might to use this many .
   *
   * @param <T> the type of the stream elements
   * @param <R> the type of results supplied by this supplier
   * @param stream {@link Stream}, maybe null
   * @param supplier {@link Supplier} of result
   * @return {@link Collection} of T
   */
  static <T, R extends Collection<T>> R collect(final Stream<T> stream, final Supplier<R> supplier) {
    return stream(stream).collect(Collectors.toCollection(supplier));
  }

  /**
   * we might to use this many .
   *
   * @param <R> the type of results supplied by this supplier
   * @param supplier {@link Supplier} of result
   * @param elements an array of {@link String}, maybe null
   * @return {@link Collection} of {@link String}, except null and empty
   */
  static <R extends Collection<String>> R collect(final Supplier<R> supplier, final String... elements) {
    return stream(elements).filter((e) -> !e.isEmpty()).collect(Collectors.toCollection(supplier));
  }

  /**
   * we might to use this many .
   *
   * @param elements an array of {@link String}, maybe null
   * @return {@link List}, except null and empty
   */
  static List<String> collect(final String... elements) {
    return collect(ArrayList::new, elements);
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
   * detect the parameter is array of {@link Stream} .
   *
   * @param <T> the type of the stream elements
   * @param elements an array of T, maybe null
   * @return true if the type of elements is {@link Stream}
   */
  @SafeVarargs
  private static <T> boolean isStream(final T... elements) {
    return Trebuchet.Predicates.orNot(elements, (t) -> excludeNull(Arrays.stream(t)).map(Object::getClass).allMatch(Stream.class::isAssignableFrom));
  }

  /**
   * detect the parameter is array of {@link Stream} .
   *
   * @param <T> the type of the stream elements
   * @param elements an array of T, maybe null
   * @return true if the type of elements is an array of {@link Stream}
   */
  @SafeVarargs
  private static <T> boolean isStreamArray(final T... elements) {
    return isStream(elements) && Trebuchet.Predicates.orNot(elements, (t) -> excludeNull(Arrays.stream(t)).count() > 1);
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
   * @param stream {@link Stream}, maybe null
   * @return the stream which excluded empty element
   */
  static <T> Stream<T> stream(final Stream<T> stream) {
    return excludeNull(stream);
  }

  /**
   * do less coding when using {@link Stream} .
   *
   * @param <T> the type of stream elements
   * @param streams {@link Stream Stream(s)}, maybe null contains
   * @return the stream which excluded empty element
   */
  @SafeVarargs
  static <T> Stream<T> stream(final Stream<T>... streams) {
    return streamInternal((Object[]) streams);
  }

  /**
   * Creates a null-safe Stream from a varargs array, treating nested Stream elements specially.
   *
   * <p>If {@code elements} is null or empty this returns {@link Stream#empty()}. If the provided
   * varargs contain Stream instances, they are handled as follows: a single Stream element is
   * returned (with nulls excluded); multiple Stream elements are concatenated into one Stream.
   * Otherwise the method returns a Stream of the supplied elements with any null elements filtered out.</p>
   *
   * <p>Note: this method does not support primitive arrays (e.g. {@code new int[]{...}}) — use the
   * appropriate boxed types or {@code Arrays.stream(int[])} instead.</p>
   *
   * @param <T> the stream element type
   * @param elements an array of elements or Streams (may be null)
   * @return a Stream of non-null elements; empty if input is null or contains no non-null elements
   */
  @SuppressWarnings("unchecked")
  @SafeVarargs
  static <T> Stream<T> stream(final T... elements) {
    if (elements == null || elements.length < 1) { return Stream.empty(); }
    return isStream(elements) ? isStreamArray(elements) ? streamInternal(elements) : stream((Stream<T>) elements[0]) : excludeNull(Arrays.stream(elements));
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
  private static <T> Stream<T> streamInternal(final Object... streams) {
    return streams == null ? Stream.empty() : (Stream<T>) Arrays.stream(streams).map((t) -> (Stream<T>) t).filter(Objects::nonNull).reduce(Stream::concat).orElseGet(Stream::empty).filter(Objects::nonNull);
  }
}
