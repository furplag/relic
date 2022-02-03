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
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

class OperatorTest {

  @Test
  void paintItGreen() {
    Trebuchet.Operators.Uni<String> operator = String::toUpperCase;
    Trebuchet.Operators.Bi<String> binaryOperator = String::concat;
    Trebuchet.Operators.Tri<String> trinaryOperator = String::replace;
    assertEquals("HELLO WORLD .", operator.apply("hello world ."));
    assertEquals("Hello World .", binaryOperator.apply("Hello", " World ."));
    assertEquals("hello★world .", trinaryOperator.apply("hello world .", "o w", "o★w"));
    try {
      operator.apply(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      binaryOperator.apply(null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      trinaryOperator.apply(null, null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
  }

  @Test
  void unaryOperator() {
    UnaryOperator<String> operator = String::toUpperCase;
    try {
      operator.apply(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    operator = Trebuchet.Operators.Uni.of(operator);
    try {
      assertEquals((String) null, operator.apply(null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    operator = Trebuchet.Operators.Uni.of(String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName());
    Trebuchet.Operators.Uni<String> trebuchet = Trebuchet.Operators.Uni.of(String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName());
    assertEquals("HELLO WORLD .", operator.apply("hello world ."));
    assertEquals(operator.apply("hello world ."), trebuchet.apply("hello world ."));
    assertEquals(operator.apply(null), trebuchet.apply(null));
    assertEquals("HELLO WORLD .", trebuchet.apply("hello world ."));
    assertEquals("NullPointerException", operator.apply(null));
    assertEquals("NullPointerException", trebuchet.apply(null));

    assertEquals("hello world .", operator.andThen(String::toLowerCase).apply("hello world ."));
  }

  @Test
  void binaryOperator() {
    BinaryOperator<String> operator = String::concat;
    try {
      operator.apply(null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    operator = Trebuchet.Operators.Bi.of(operator);
    try {
      assertEquals((String) null, operator.apply(null, null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    operator = Trebuchet.Operators.Bi.of(String::concat, (t, u, ex) -> ex.getClass().getSimpleName());
    Trebuchet.Operators.Bi<String> trebuchet = Trebuchet.Operators.Bi.of(String::concat, (t, u, ex) -> ex.getClass().getSimpleName());
    assertEquals("hello world .", operator.apply("hello", " world ."));
    assertEquals(operator.apply("hello", " world ."), trebuchet.apply("hello", " world ."));
    assertEquals(operator.apply(null, null), trebuchet.apply(null, null));
    assertEquals("hello world .", trebuchet.apply("hello", " world ."));
    assertEquals("NullPointerException", operator.apply(null, null));
    assertEquals("NullPointerException", trebuchet.apply(null, null));

    assertEquals("HELLO WORLD .", operator.andThen(String::toUpperCase).apply("hello", " world ."));
  }

  @Test
  void trinaryOperator() {
    Trebuchet.Operators.Tri<String> operator = String::replace;
    try {
      operator.apply(null, null, null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    operator = Trebuchet.Operators.Tri.of(operator);
    try {
      assertEquals((String) null, operator.apply(null, null, null));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    operator = Trebuchet.Operators.Tri.of(String::replace, (ex) -> ex.getClass().getSimpleName());
    assertEquals("hello★world .", operator.apply("hello world .", "o w", "o★w"));
    assertEquals("NullPointerException", operator.apply(null, null, null));

    assertEquals("HELLO★WORLD .", operator.andThen(String::toUpperCase).apply("hello world .", "o w", "o★w"));

    assertEquals(3, Trebuchet.Operators.Tri.maxBy(Integer::compareTo).apply(1, 3, 2));
    assertEquals(1, Trebuchet.Operators.Tri.minBy(Integer::compareTo).apply(1, 3, 2));
  }
}
