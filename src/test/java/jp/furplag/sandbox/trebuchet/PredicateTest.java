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
package jp.furplag.sandbox.trebuchet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class PredicateTest {

  @Test
  void paintItGreen() {
    Trebuchet.Predicates.Uni<Integer> predicate = (i) -> i % 2 != 0;
    Trebuchet.Predicates.Bi<String, String> biPredicate = String::contains;
    Trebuchet.Predicates.Tri<String, String, Integer> triPredicate = (t, u, v) -> t.split(u).length > v;
    assertEquals(true, predicate.test(1));
    assertEquals(false, predicate.test(10));
    assertEquals(true, biPredicate.test("南無阿弥陀仏", "無"));
    assertEquals(false, biPredicate.test("南無阿弥陀仏", "業"));
    assertEquals(true, triPredicate.test("南無阿弥陀仏 諸行無常", "無", 2));
    assertEquals(false, triPredicate.test("南無阿弥陀仏 諸行無常", "無", 3));
    try {
      predicate.test(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      biPredicate.test(null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      triPredicate.test(null, null, 3);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
  }

  @Test
  void predicate() {
    Predicate<Integer> predicate = (i) -> i % 2 != 0;
    try {
      predicate.test(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    predicate = Trebuchet.Predicates.Uni.of(predicate);
    try {
      assertEquals(false, predicate.test(null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    predicate = Trebuchet.Predicates.Uni.of((i) -> i % 2 != 0, (t, ex) -> false);
    Trebuchet.Predicates.Uni<Integer> trebuchet = Trebuchet.Predicates.Uni.of((i) -> i % 2 != 0, (t, ex) -> false);
    assertEquals(true, predicate.test(13));
    assertEquals(predicate.test(1), trebuchet.test(1));
    assertEquals(predicate.test(null), trebuchet.test(null));
    assertEquals(false, trebuchet.test(null));
    assertEquals(true, Trebuchet.Predicates.orElse((Integer) null, Trebuchet.Predicates.Uni.of((i) -> i % 2 != 0, (t, ex) -> ex instanceof NullPointerException), (t, ex) -> false));
    assertEquals(false, Trebuchet.Predicates.Uni.of(predicate::test, (t, ex) -> ex instanceof NullPointerException).test(null));
    assertEquals(Trebuchet.Predicates.orNot(3, predicate::test), predicate.test(3));
    try {
      Trebuchet.Predicates.Uni.of(predicate::test, (t, ex) -> ex instanceof NullPointerException).andThen((b) -> !b).apply(null);
      fail("there must raise UnsupportedOperationException .");
    } catch (UnsupportedOperationException ex) {
      assertTrue(ex instanceof UnsupportedOperationException);
    }

    assertEquals(trebuchet.negate().test(88), Trebuchet.Predicates.Uni.not(trebuchet).test(88));

    assertEquals(false, trebuchet.and((i) -> i % 3 == 0).test(7));
    assertEquals(true, trebuchet.and((i) -> i % 3 == 0).test(21));

    assertEquals(false, trebuchet.or((i) -> i % 30 == 0).test(18));
    assertEquals(true, trebuchet.or((i) -> i % 30 == 0).test(60));
  }

  @Test
  void biPredicate() throws Throwable {
    BiPredicate<String, String> predicate = String::contains;
    try {
      predicate.test(null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    predicate = Trebuchet.Predicates.Bi.of(predicate);
    try {
      assertEquals(false, predicate.test(null, null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    Trebuchet.Predicates.Bi<String, String> trebuchet = Trebuchet.Predicates.Bi.of(String::contains);
    assertEquals(true, predicate.test("南無阿弥陀仏", "阿弥陀"));
    assertEquals(predicate.test("南無阿弥陀仏", "仏"), trebuchet.test("南無阿弥陀仏", "仏"));
    assertEquals(predicate.test(null, "南無阿弥陀仏"), trebuchet.test(null, "南無阿弥陀仏"));
    assertEquals(false, trebuchet.test("南無阿弥陀仏", null));
    assertEquals(true, Trebuchet.Predicates.orElse((String) null, (String) null, Trebuchet.Predicates.Bi.of(String::contains, (t, u, ex) -> ex instanceof NullPointerException), (t, u, ex) -> false));
    assertEquals(false, Trebuchet.Predicates.Bi.of(predicate::test, (t, u, ex) -> ex instanceof NullPointerException).test(null, null));
    assertEquals(Trebuchet.Predicates.orNot("南無阿弥陀仏", "南無", predicate::test), predicate.test("南無阿弥陀仏", "南無"));
    try {
      Trebuchet.Predicates.Bi.of(predicate::test, (t, u, ex) -> ex instanceof NullPointerException).andThen((b) -> !b).apply("南無阿弥陀仏", "南無");
      fail("there must raise UnsupportedOperationException .");
    } catch (UnsupportedOperationException ex) {
      assertTrue(ex instanceof UnsupportedOperationException);
    }

    assertEquals(trebuchet.negate().test("南無阿弥陀仏", "南無"), Trebuchet.Predicates.Bi.not(trebuchet).test("南無阿弥陀仏", "南無"));

    assertEquals(false, trebuchet.and((t, u) -> u.codePoints().toArray().length > 2).test("南無阿弥陀仏", "南無"));
    assertEquals(true, trebuchet.and((t, u) -> u.codePoints().toArray().length > 2).test("南無阿弥陀仏", "阿弥陀"));

    assertEquals(false, trebuchet.or((t, u) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "色即是空"));
    assertEquals(true, trebuchet.or((t, u) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "無"));
    assertEquals(true, trebuchet.or((t, u) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "諸行無常"));
  }

  @Test
  void triPredicate() {
    Trebuchet.Predicates.Tri<String, String, Integer> predicate = (t, u, v) -> t.split(u).length == v + 1;
    try {
      predicate.test(null, null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    predicate = Trebuchet.Predicates.Tri.of(predicate);
    try {
      assertEquals(false, predicate.test(null, null, null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    assertEquals(true, predicate.test("南無阿弥陀仏", "阿弥陀", 1));
    assertEquals(false, predicate.test("南★無★阿★弥★陀★仏", "★", 3));
    assertEquals(true, predicate.test("南★無★阿★弥★陀★仏", "★", 5));
    assertEquals(false, predicate.test(null, "南無阿弥陀仏", 1));
    assertEquals(false, Trebuchet.Predicates.orElse((String) null, (String) null, (Integer) null, predicate, (t, u, v) -> false));
    assertEquals(false, Trebuchet.Predicates.orElse((String) null, (String) null, (Integer) null, predicate, (ex) -> false));
    assertEquals(false, Trebuchet.Predicates.orElse((String) null, (String) null, (Integer) null, predicate, (t, u, v) -> true));
    assertEquals(false, Trebuchet.Predicates.orElse((String) null, (String) null, (Integer) null, predicate, (ex) -> true));
    assertEquals(false, Trebuchet.Predicates.Tri.of(predicate::test, (ex) -> ex instanceof NullPointerException).test(null, null, null));
    assertEquals(Trebuchet.Predicates.orNot("南無阿弥陀仏", "南無", 1, predicate), predicate.test("南無阿弥陀仏", "南無", 1));
    try {
      Trebuchet.Predicates.Tri.of(predicate, (ex) -> ex instanceof NullPointerException).andThen((b) -> !b).apply("南無阿弥陀仏", "南無", 1);
      fail("there must raise UnsupportedOperationException .");
    } catch (UnsupportedOperationException ex) {
      assertTrue(ex instanceof UnsupportedOperationException);
    }

    assertEquals(predicate.negate().test("南無阿弥陀仏", "南無", 1), Trebuchet.Predicates.Tri.not(predicate).test("南無阿弥陀仏", "南無", 1));

    assertEquals(false, predicate.and((t, u, v) -> u.codePoints().toArray().length > 2).test("南無阿弥陀仏", "南無", 1));
    assertEquals(true, predicate.and((t, u, v) -> u.codePoints().toArray().length > 2).test("南無阿弥陀仏", "阿弥陀", 1));

    assertEquals(false, predicate.or((t, u, v) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "色即是空", 1));
    assertEquals(true, predicate.or((t, u, v) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "無", 1));
    assertEquals(true, predicate.or((t, u, v) -> "諸行無常".equals(u)).test("南無阿弥陀仏", "諸行無常", 1));
  }
}
