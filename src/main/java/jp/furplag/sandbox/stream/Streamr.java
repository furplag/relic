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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * do less coding when using Stream API .
 *
 * @author furplag
 *
 */
public interface Streamr {

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
   * returns the first of element in the stream that match the given predicate .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @param condition {@link Predicate}
   * @return the first of element in the stream that match the given predicate
   */
  static <T> T firstOf(final Stream<T> stream, final Predicate<? super T> condition) {
    return filtering(stream, condition).findFirst().orElse(null);
  }

  /**
   * returns elements in the stream that match the given predicate .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @param condition {@link Predicate}
   * @return elements in the stream that match the given predicate
   */
  static <T> Stream<T> filtering(final Stream<T> stream, final Predicate<? super T> condition) {
    return condition == null ? stream : stream.dropWhile(condition.negate()).filter(condition);
  }

  /**
   * returns the last of element in the stream that match the given predicate .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @param condition {@link Predicate}
   * @return the last of element in the stream that match the given predicate
   */
  static <T> T lastOf(final Stream<T> stream, final Predicate<? super T> condition) {
    return filtering(stream, condition).reduce((current, next) -> next).orElse(null);
  }

  /**
   * do less coding in case of {@link Stream#map(java.util.function.Function) Stream.map()}.{@link Stream#collect(java.util.stream.Collector) collect(Collectors.toList())}.{@link Collection#stream() stream()} .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @param tweaker {@link UnaryOperator}
   * @return unclosed (actually, duplicated) stream of T which modified each elements
   */
  static <T> Stream<T> tweak(final Stream<T> stream, final UnaryOperator<T> tweaker) {
    return stream(stream).map(Optional.ofNullable(tweaker).orElse(UnaryOperator.identity())).collect(Collectors.toList()).stream();
  }

  /**
   * we might to use this many .
   *
   * @param <T> the type of the stream elements
   * @param stream {@link Stream}, maybe null
   * @return {@link Collection} of T
   */
  static <T, C extends Collection<T>> C collect(final Stream<T> stream, final Supplier<? extends C> supplier) {
    return stream(stream).collect(Collectors.toCollection(supplier));
  }
}
