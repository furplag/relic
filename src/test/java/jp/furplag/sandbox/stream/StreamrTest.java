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

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamrTest {

  @Test
  public void test() throws ReflectiveOperationException {
    Constructor<?> c = Streamr.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertTrue(c.newInstance() instanceof Streamr);
  }

  @Test
  public void testStream() {
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
  }

  @Test
  public void testToList() {
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Stream<Object>) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((List<Object>) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Set<Object>) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Queue<Object>) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Deque<Object>) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Object) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Object[]) null)));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream((Integer[]) null)));

    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(Stream.empty())));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(Collections.emptyList())));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(Collections.emptySet())));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new DelayQueue<>())));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new ArrayDeque<>())));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new Object[] {})));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new Integer[] {})));

    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(Stream.of((Object) null))));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(Arrays.asList((Object) null))));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new HashSet<>(Arrays.asList((Object) null)))));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new Object[] { null })));
    assertEquals(Arrays.asList(new Object[] {}), Streamr.toList(Streamr.stream(new Integer[] { null })));

    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(Stream.of(new Integer[] { 1, 2, 3 }))));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(Stream.of(new Integer[] { 1, null, 2, null, 3 }))));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(Arrays.asList(new Integer[] { 1, 2, 3 }))));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(new HashSet<>(Arrays.asList(new Integer[] { 1, null, 2, null, 3 })))));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3))));
    assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), Streamr.toList(Streamr.stream(new Integer[] { 1, null, 2, null, 3 })));
  }

  @Test
  public void testTweak() {
    assertArrayEquals(new Object[] {}, Streamr.tweak(Streamr.stream((Stream<Object>) null), o -> o).toArray());
    assertArrayEquals(new Object[] {}, Streamr.tweak(Streamr.stream((Stream<Object>) null), Objects::toString).toArray());
    assertArrayEquals(new String[] { "null", "null", "null" }, Streamr.tweak(Stream.of(null, null, null), Objects::toString).toArray());
    assertArrayEquals(new Object[] {}, Streamr.tweak(Streamr.stream(null, null, null), Objects::toString).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, Streamr.tweak(IntStream.rangeClosed(0, 9).boxed(), null).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, Streamr.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> o).toArray());
    assertArrayEquals(new Object[] { null, null, null, null, null, null, null, null, null, null }, Streamr.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> null).toArray());
    assertArrayEquals(new Integer[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 }, Streamr.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> o % 2).toArray());
    assertEquals(5, Streamr.tweak(IntStream.rangeClosed(0, 9).boxed(), o -> o % 2).mapToInt(Integer::intValue).sum());
  }

  @Test
  public void testFirstOf() {
    assertEquals(null, Streamr.firstOf(Streamr.stream((Stream<Long>) null), o -> o.intValue() % 2 == 0));
    assertEquals(null, Streamr.firstOf(LongStream.rangeClosed(0, 9).boxed(), o -> o.intValue() > 10));
    assertEquals((Long) 6L, Streamr.firstOf(LongStream.rangeClosed(0, 9).boxed(), o -> o.intValue() > 5));
    assertEquals("南", Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), null));
    assertEquals("南", Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "南".equals(o)));
    assertEquals("無", Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "無".equals(o)));
    assertEquals("仏", Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "仏".equals(o)));
    assertEquals("南", Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), Objects::nonNull));
    assertEquals(null, Streamr.firstOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "虚".equals(o)));
  }

  @Test
  public void testLastOf() {
    assertEquals(null, Streamr.lastOf(Streamr.stream((Stream<Long>) null), o -> o.intValue() % 2 == 0));
    assertEquals(null, Streamr.lastOf(LongStream.rangeClosed(0, 9).boxed(), o -> o.intValue() > 10));
    assertEquals((Long) 9L, Streamr.lastOf(LongStream.rangeClosed(0, 9).boxed(), o -> o.intValue() > 5));
    assertEquals("仏", Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), null));
    assertEquals("南", Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "南".equals(o)));
    assertEquals("無", Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "無".equals(o)));
    assertEquals("仏", Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "仏".equals(o)));
    assertEquals("仏", Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), Objects::nonNull));
    assertEquals(null, Streamr.lastOf(Arrays.stream("南無阿弥陀仏".split("")), o -> "虚".equals(o)));
  }
}
