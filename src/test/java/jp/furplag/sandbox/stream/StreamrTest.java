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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import jp.furplag.sandbox.stream.Streamr.Filter.FilteringMode;

public class StreamrTest {

  @Test
  public void test() {
    assertArrayEquals(new Object[] {}, Streamr.stream((Stream<Object>) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((List<Object>) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Set<Object>) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Queue<Object>) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Deque<Object>) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Object) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Object[]) null).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream((Integer[]) null).toArray());

    assertArrayEquals(new Object[] {}, Streamr.stream(Stream.empty()).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(Collections.emptyList()).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(Collections.emptySet()).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new DelayQueue<>()).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new ArrayDeque<>()).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new Object[] {}).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new Integer[] {}).toArray());

    assertArrayEquals(new Object[] {}, Streamr.stream(Stream.of((Object) null)).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(Arrays.asList((Object) null)).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new HashSet<>(Arrays.asList((Object) null))).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new Object[] { null }).toArray());
    assertArrayEquals(new Object[] {}, Streamr.stream(new Integer[] { null }).toArray());

    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Stream.of(new Integer[] { 1, 2, 3 })).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Stream.of(new Integer[] { 1, null, 2, null, 3 })).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Arrays.asList(new Integer[] { 1, 2, 3 })).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(new HashSet<>(Arrays.asList(new Integer[] { 1, null, 2, null, 3 }))).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(new Integer[] { 1, null, 2, null, 3 }).toArray(Integer[]::new));

    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Stream.of(1, 2, 3)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, Streamr.stream(Stream.of(1, 2, 3), Stream.of(4, 5, 6)).toArray(Integer[]::new));

    assertArrayEquals(new Integer[] {}, Streamr.stream(Stream.ofNullable((Integer) null)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3 }, Streamr.stream(Stream.of(1, 2, 3)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, Streamr.stream(Stream.of(1, 2, 3), Stream.of(4, 5, 6)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, Streamr.stream(Stream.of(1, 2, 3), null, Stream.of(4, 5, 6)).toArray(Integer[]::new));
    assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, Streamr.stream(Stream.of(1, 2, 3), null, Stream.of(4, null, 5, null, 6)).toArray(Integer[]::new));
  }

  @Test
  public void testToList() {
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Stream<Object>) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((List<Object>) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Set<Object>) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Queue<Object>) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Deque<Object>) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Object) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Object[]) null), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream((Integer[]) null), ArrayList::new));

    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(Stream.empty()), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(Collections.emptyList()), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(Collections.emptySet()), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new DelayQueue<>()), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new ArrayDeque<>()), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new Object[] {}), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new Integer[] {}), ArrayList::new));

    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(Stream.of((Object) null)), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(Arrays.asList((Object) null)), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new HashSet<>(Arrays.asList((Object) null))), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new Object[] { null }), ArrayList::new));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.collect(Streamr.stream(new Integer[] { null }), ArrayList::new));

    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(Stream.of(new Integer[] { 1, 2, 3 })), ArrayList::new));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(Stream.of(new Integer[] { 1, null, 2, null, 3 })), ArrayList::new));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(Arrays.asList(new Integer[] { 1, 2, 3 })), ArrayList::new));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(new HashSet<>(Arrays.asList(new Integer[] { 1, null, 2, null, 3 }))), ArrayList::new));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), ArrayList::new));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.collect(Streamr.stream(new Integer[] { 1, null, 2, null, 3 }), ArrayList::new));
  }

  @Test
  public void testToSet() {
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Stream<Object>) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((List<Object>) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Set<Object>) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Queue<Object>) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Deque<Object>) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Object) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Object[]) null), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream((Integer[]) null), HashSet::new));

    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(Stream.empty()), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(Collections.emptyList()), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(Collections.emptySet()), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new DelayQueue<>()), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new ArrayDeque<>()), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new Object[] {}), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new Integer[] {}), HashSet::new));

    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(Stream.of((Object) null)), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(Arrays.asList((Object) null)), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new HashSet<>(Arrays.asList((Object) null))), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new Object[] { null }), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Object[] {})), Streamr.collect(Streamr.stream(new Integer[] { null }), HashSet::new));

    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(Stream.of(new Integer[] { 1, 2, 3 })), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(Stream.of(new Integer[] { 1, null, 2, null, 3 })), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(Arrays.asList(new Integer[] { 1, 2, 3 })), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(new HashSet<>(Arrays.asList(new Integer[] { 1, null, 2, null, 3 }))), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), HashSet::new));
    assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3 })), Streamr.collect(Streamr.stream(new Integer[] { 1, null, 2, null, 3 }), HashSet::new));
  }

  @Test
  public void testFilteringMode() {
    assertThat(Streamr.Filter.FilteringMode.And.and(), is(true));
    assertThat(Streamr.Filter.FilteringMode.Or.and(), is(false));
  }

  @Test
  public void testFiltering() {
    assertArrayEquals(Stream.empty().toArray(), Streamr.Filter.filtering(null, Stream.empty(), (Function<Object, Boolean>)null).toArray());
    assertArrayEquals(Stream.empty().toArray(), Streamr.Filter.filtering(null, Stream.of(null, null, null), (Function<Object, Boolean>)null).toArray());
    assertArrayEquals(new Integer[] { 2, 1, 3 }, Streamr.Filter.filtering(null, Stream.of(2, null, 1, 3), (Function<Object, Boolean>)null).toArray());
    assertArrayEquals(new Integer[] { 1, 3 }, Streamr.Filter.filtering(null, Stream.of(2, null, 1, 3), (x) -> x % 2 != 0).toArray());
    assertArrayEquals(new Integer[] { 1, 3 }, Streamr.Filter.filtering(null, Stream.of(2, null, 1, 3), (x) -> x % 2 != 0).toArray());
    assertArrayEquals(new Integer[] { 1 }, Streamr.Filter.filtering(null, Stream.of(2, null, 1, 3), (x) -> x % 2 != 0, (x) -> x < 3).toArray());
    assertArrayEquals(new Integer[] { 1 }, Streamr.Filter.filtering(FilteringMode.And, Stream.of(2, null, 1, 3), (x) -> x % 2 != 0, (x) -> x < 3).toArray());
    assertArrayEquals(new Integer[] { 2, 1, 3 }, Streamr.Filter.filtering(FilteringMode.Or, Stream.of(2, null, 1, 3, 4), (x) -> x % 2 != 0, (x) -> x < 3).toArray());
    assertArrayEquals(new Integer[] {}, Streamr.Filter.filtering(FilteringMode.And, Stream.of(2, null, 1, 3, 4), (x) -> x % 2 != 0, (x) -> x % 2 == 0).toArray());
    assertArrayEquals(new Integer[] { 2, 1, 3, 4 }, Streamr.Filter.filtering(FilteringMode.Or, Stream.of(2, null, 1, 3, 4), (x) -> x % 2 != 0, (x) -> x % 2 == 0).toArray());
  }

  @Test
  public void testFilterTweak() {
    assertArrayEquals(new Object[] {}, Streamr.Filter.tweak(Streamr.stream((Stream<Object>) null), (o) -> o).toArray());
    assertArrayEquals(new Object[] {}, Streamr.Filter.tweak(Streamr.stream((Stream<Object>) null), Objects::toString).toArray());
    assertArrayEquals(new String[] {}, Streamr.Filter.tweak(Stream.of(null, null, null), Objects::toString).toArray());
    assertArrayEquals(new Object[] {}, Streamr.Filter.tweak(Streamr.stream(null, null, null), Objects::toString).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, Streamr.Filter.tweak(IntStream.rangeClosed(0, 9).boxed(), (Function<Integer, Integer>[]) null).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, Streamr.Filter.tweak(IntStream.rangeClosed(0, 9).boxed(), (o) -> o).toArray());
    assertArrayEquals(new Object[] { null, null, null, null, null, null, null, null, null, null }, Streamr.Filter.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> null).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 }, Streamr.Filter.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> o % 2).toArray());
    assertThat(Streamr.Filter.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> o % 2).mapToInt(Integer::intValue).sum(), is(5));
  }
}
