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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import jp.furplag.sandbox.stream.Streamr;
import org.junit.jupiter.api.Test;

class FunctionTest {

  @Test
  void paintItGreen() {
    Trebuchet.Functions.Uni<String, String> function = String::toUpperCase;
    Trebuchet.Functions.Bi<String, Integer, String> biFunction = String::repeat;
    Trebuchet.Functions.Tri<String, Integer, Integer, String> triFunction = (t, u, v) -> t.repeat(u * v);
    assertEquals("HELLO WORLD .", function.apply("hello world ."));
    assertEquals("HelloHelloHello", biFunction.apply("Hello", 3));
    assertEquals("HelloHelloHelloHelloHelloHelloHelloHelloHello", triFunction.apply("Hello", 3, 3));
    try {
      function.apply(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      biFunction.apply(null, 3);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      triFunction.apply(null, 3, 3);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
  }

  @Test
  void function() {
    final String[] actual = {null};
    Function<String, String> function = String::toUpperCase;
    try {
      actual[0] = function.apply(actual[0]);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    function = Trebuchet.Functions.Uni.of(function);
    try {
      assertEquals((String) null, function.apply(actual[0]));
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    function = Trebuchet.Functions.Uni.of(String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName());
    Trebuchet.Functions.Uni<String, String> trebuchet = Trebuchet.Functions.Uni.of(String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName());
    assertEquals("HELLO WORLD .", function.apply("hello world ."));
    assertEquals(function.apply("hello world ."), trebuchet.apply("hello world ."));
    assertEquals(function.apply(null), trebuchet.apply(null));
    assertEquals("HELLO WORLD .", trebuchet.apply("hello world ."));
    assertEquals("NullPointerException", function.apply(null));
    assertEquals("NullPointerException", trebuchet.apply(null));
    assertEquals("NullPointerException", Trebuchet.Functions.orElse((String) null, Trebuchet.Functions.Uni.of(String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName()), (t, ex) -> null));
    assertEquals("nullPointerException", Trebuchet.Functions.orElse((String) null, String::toUpperCase, () -> "nullPointerException"));
    assertEquals(Trebuchet.Functions.orNot("hello world .", function::apply), function.apply("hello world ."));
    assertEquals("NullPointerException", Trebuchet.Functions.Uni.of(function::apply, (t, ex) -> ex.getClass().getSimpleName()).apply(null));
    assertEquals("NULLPOINTEREXCEPTION", Trebuchet.Functions.Uni.of(function::apply, (t, ex) -> ex.getClass().getSimpleName()).andThen(String::toUpperCase).apply(null));

    assertEquals((String) null, Trebuchet.Functions.Uni.of((UnaryOperator<String>) String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName()).andThen(null).apply(null));
    assertEquals("NULLPOINTEREXCEPTION", Trebuchet.Functions.Uni.of((UnaryOperator<String>) String::toUpperCase, (t, ex) -> ex.getClass().getSimpleName()).andThen(String::toUpperCase).apply(null));

    trebuchet = trebuchet.compose((String t) -> Objects.toString(t));
    assertEquals("NULL", trebuchet.apply(null));
    assertEquals("HELLO WORLD .", trebuchet.apply("hello world ."));

    assertEquals((String) null, Trebuchet.Functions.orNot((String) null, String::toUpperCase));
    assertEquals("NULL", Trebuchet.Functions.orNot(Objects.toString(null), String::toUpperCase));
  }

  @Test
  void biFunction() {
    final String expects[] = {/* @formatter:off */
        "321321321321321321321321321321"
      , "321321321321321321321321321"
      , "321321321321321321321321"
      , "321321321321321321321"
      , "321321321321321321"
      , "321321321321321"
      , "321321321321"
      , "321321321"
      , "321321"
      , "321"
      , ""
      , "123"
      , "123123"
      , "123123123"
      , "123123123123"
      , "123123123123123"
      , "123123123123123123"
      , "123123123123123123123"
      , "123123123123123123123123"
      , "123123123123123123123123123"
      , "123123123123123123123123123123"
    };/* @formatter:on */
    final Trebuchet.Operators.Uni<String> reverser = (t) -> Streamr.stream(Objects.toString(t, "").split("")).reduce((a, b) -> b.concat(a)).get();
    IntStream.rangeClosed(-10, 10).forEach((i) -> {
      String actual = null;
      final Trebuchet.Functions.Bi<String, Integer, String> repeater = (t, u) -> Objects.toString(t, "").repeat(Objects.requireNonNullElse(u, 0));
      try {
        actual = repeater.apply("123", i);
      } catch (Exception e) {
        try {/* @formatter:off */
          actual = Trebuchet.Functions.orElse("123", i, repeater::apply, (x, y, ex) -> repeater.apply(reverser.apply(x), Objects.requireNonNullElse(y, 0) * -1));
        } catch (Exception ex) {/* @formatter:on */
          fail(String.format("\"%d\"", i));
        }
      }
      assertEquals(expects[i + 10], actual);
    });

    assertEquals((String) null, Trebuchet.Functions.orNot((String) null, 3, String::repeat));
    assertEquals(Trebuchet.Functions.Bi.of(String::repeat).apply(null, 3), Trebuchet.Functions.orNot((String) null, 3, String::repeat));
    assertEquals(Trebuchet.Functions.Bi.of(String::repeat, (x, y, ex) -> null).apply(null, 3), Trebuchet.Functions.orNot((String) null, 3, String::repeat));
    assertEquals(Trebuchet.Functions.orElse((String) null, 3, String::repeat, (x, y, ex) -> null), Trebuchet.Functions.orNot((String) null, 3, String::repeat));
    assertEquals(Trebuchet.Functions.orElse((String) null, 3, String::repeat, () -> null), Trebuchet.Functions.orNot((String) null, 3, String::repeat));

    assertEquals("123123123", Trebuchet.Functions.orNot("123", 3, String::repeat));
    assertEquals(Trebuchet.Functions.Bi.of(String::repeat).apply("123", 3), Trebuchet.Functions.orNot("123", 3, String::repeat));
    assertEquals(Trebuchet.Functions.Bi.of(String::repeat, (x, y, ex) -> null).apply("123", 3), Trebuchet.Functions.orNot("123", 3, String::repeat));
    assertEquals(Trebuchet.Functions.orElse("123", 3, String::repeat, (x, y, ex) -> null), Trebuchet.Functions.orNot("123", 3, String::repeat));
    assertEquals(Trebuchet.Functions.orElse("123", 3, String::repeat, () -> null), Trebuchet.Functions.orNot("123", 3, String::repeat));

    assertEquals("null", Trebuchet.Functions.Bi.of(String::repeat).andThen(Objects::toString).apply(null, 3));
    assertEquals("NULLNULLNULL", Trebuchet.Functions.Bi.of((String t, Integer u) -> t.repeat(u), (x, y, ex) -> Objects.toString(x).repeat(y)).andThen(String::toUpperCase).apply(null, 3));
  }

  @Test
  void triFunction() {
    final String expects[] = {/* @formatter:off */
        "321321321321321321321321321321"
      , "321321321321321321321321321"
      , "321321321321321321321321"
      , "321321321321321321321"
      , "321321321321321321"
      , "321321321321321"
      , "321321321321"
      , "321321321"
      , "321321"
      , "321"
      , ""
      , "123"
      , "123123"
      , "123123123"
      , "123123123123"
      , "123123123123123"
      , "123123123123123123"
      , "123123123123123123123"
      , "123123123123123123123123"
      , "123123123123123123123123123"
      , "123123123123123123123123123123"
    };/* @formatter:on */
    final Trebuchet.Operators.Uni<String> reverser = (t) -> Streamr.stream(Objects.toString(t, "").split("")).reduce((a, b) -> b.concat(a)).get();
    final Trebuchet.Functions.Tri<String, Integer, Integer, String> repeater = (t, u, v) -> t.repeat(Objects.requireNonNullElse(u, 0)).repeat(Objects.requireNonNullElse(v, 0));
    IntStream.rangeClosed(-10, 10).forEach((i) -> {
      String actual = null;
      try {
        actual = repeater.apply("123", i, 2);
      } catch (Exception e) {
        try {/* @formatter:off */
          actual = Trebuchet.Functions.orElse("123", i, 2, repeater::apply, (x, y, z) -> repeater.apply(reverser.apply(x), Objects.requireNonNullElse(y, 0) * -1, z));
        } catch (Exception ex) {/* @formatter:on */
          fail(String.format("\"%d\"", i));
        }
      }
      assertEquals(expects[i + 10].repeat(2), actual);
    });

    assertEquals(null, Trebuchet.Functions.orNot(null, 3, 3, repeater));
    assertEquals("123123123123123123123123123", Trebuchet.Functions.orNot("123", 3, 3, repeater));
    assertEquals(Trebuchet.Functions.Tri.of(repeater).apply(null, 3, 3), Trebuchet.Functions.orNot((String) null, 3, 3, repeater));
    assertEquals(Trebuchet.Functions.Tri.of(repeater, (x, y, z) -> null).apply(null, 3, 3), Trebuchet.Functions.orNot((String) null, 3, 3, repeater));
    assertEquals(Trebuchet.Functions.orElse((String) null, 3, 3, repeater, (x, y, ex) -> null), Trebuchet.Functions.orNot((String) null, 3, 3, repeater));
    assertEquals(Trebuchet.Functions.orElse((String) null, 3, 3, repeater, () -> null), Trebuchet.Functions.orNot((String) null, 3, 3, repeater));

    assertEquals("123123123123123123123123123", Trebuchet.Functions.orNot("123", 3, 3, repeater));
    assertEquals(Trebuchet.Functions.Tri.of(repeater).apply("123", 3, 3), Trebuchet.Functions.orNot("123", 3, 3, repeater));
    assertEquals(Trebuchet.Functions.Tri.of(repeater, (x, y, ex) -> null).apply("123", 3, 3), Trebuchet.Functions.orNot("123", 3, 3, repeater));
    assertEquals(Trebuchet.Functions.orElse("123", 3, 3, repeater, (x, y, ex) -> null), Trebuchet.Functions.orNot("123", 3, 3, repeater));
    assertEquals(Trebuchet.Functions.orElse("123", 3, 3, repeater, () -> null), Trebuchet.Functions.orNot("123", 3, 3, repeater));

    assertEquals("null", Trebuchet.Functions.Tri.of(repeater).andThen(Objects::toString).apply(null, 3, 3));
    assertEquals("NULLNULLNULL", Trebuchet.Functions.Tri.of(repeater, (x, y, z) -> Objects.toString(x).repeat(y)).andThen(String::toUpperCase).apply(null, 3, 3));
  }
}
