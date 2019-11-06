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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import jp.furplag.sandbox.stream.Streamr;
import jp.furplag.sandbox.trebuchet.Trebuchet.Consumers;

class ConsumerTest {

  @Test
  void paintItGreen() {
    final String[] actual = {null};
    Trebuchet.Consumers.Uni<String> consumer = (t) -> actual[0] = t.toUpperCase();
    Trebuchet.Consumers.Bi<String, Integer> biConsumer = (t, u) -> actual[0] = t.repeat(u);
    Trebuchet.Consumers.Tri<String, Integer, Integer> triConsumer = (t, u, v) -> actual[0] = t.repeat(u * v);
    consumer.accept("Hello");
    assertEquals("HELLO", actual[0]);
    biConsumer.accept("Hello", 3);
    assertEquals("HelloHelloHello", actual[0]);
    triConsumer.accept("Hello", 3, 3);
    assertEquals("HelloHelloHelloHelloHelloHelloHelloHelloHello", actual[0]);
    try {
      consumer.accept(null);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      biConsumer.accept(null, 3);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      triConsumer.accept(null, 3, 3);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
  }

  @Test
  void consumer() throws Throwable {
    final String[] actual = {null};
    Consumer<String> consumer = (t) -> actual[0] = t.toUpperCase();
    try {
      consumer.accept(actual[0]);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    consumer = Trebuchet.Consumers.Uni.of((t) -> actual[0] = t.toUpperCase());
    consumer.accept(actual[0]);
    assertEquals((String) null, actual[0]);

    consumer = Trebuchet.Consumers.Uni.of((t) -> actual[0] = t.toUpperCase());
    consumer.accept("hello world .");
    assertEquals("HELLO WORLD .", actual[0]);

    consumer = Consumers.Uni.of((t) -> actual[0] = t.toUpperCase(), null);
    consumer.accept(null);
    assertEquals("HELLO WORLD .", actual[0]);

    consumer = Consumers.Uni.of((t) -> actual[0] = t.toUpperCase(), (x, ex) -> {
      actual[0] = ex.getClass().getSimpleName();
    });
    consumer.accept(null);
    assertEquals("NullPointerException", actual[0]);

    consumer.accept(actual[0]);
    assertEquals("NULLPOINTEREXCEPTION", actual[0]);

    actual[0] = null;
    Trebuchet.Consumers.orNot((String) null, (t) -> {
      actual[0] = t.toUpperCase();
    });
    assertNull(actual[0]);
    consumer = consumer.andThen((t) -> {
      actual[0] = t.toUpperCase();
    });
    try {
      consumer.accept(actual[0]);
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }
    try {
      consumer = Consumers.Uni.of(consumer.andThen((t) -> {
        actual[0] = t.toUpperCase();
      }), null);
      consumer.accept(actual[0]);
      assertEquals("NULLPOINTEREXCEPTION", actual[0]);
    } catch (Throwable ex) {
      fail("there must not raise any exceptions .");
    }

    final String[] tests = {null, null};
    Trebuchet.Consumers.orNot((Integer) null, (t) -> tests[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> {}).accept((Integer) null);
    assertEquals(tests[0], tests[1]);
    assertNull(tests[1]);
    Trebuchet.Consumers.orNot(0, (t) -> tests[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> {}).accept(0);
    assertEquals(tests[0], tests[1]);
    assertEquals("0", tests[1]);
    Trebuchet.Consumers.orNot(1, (t) -> tests[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd")).accept(1);
    assertEquals(tests[0], tests[1]);
    assertEquals("odd", tests[1]);
    Trebuchet.Consumers.orNot(2, (t) -> tests[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd")).accept(2);
    assertEquals(tests[0], tests[1]);
    assertEquals("even", tests[1]);

    Trebuchet.Consumers.orElse((Integer) null, (t) -> tests[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> tests[0] = "NaN");
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> tests[1] = "NaN").accept((Integer) null);
    assertEquals(tests[0], tests[1]);
    assertEquals("NaN", tests[1]);

    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> tests[1] = "NaN").andThen(null).accept(2);
    assertEquals("even", tests[1]);
    Trebuchet.Consumers.Uni.of((Integer t) -> tests[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, ex) -> tests[1] = "NaN").andThen((t) -> tests[1] += (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd")).accept(2);
    assertEquals("eveneven", tests[1]);
  }

  @Test
  void biConsumer() throws Throwable {
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
    final List<String> actuals = new ArrayList<>();
    final BiFunction<String, Integer, String> repeater =
        (t, u) -> Objects.toString(t, "").repeat(Objects.requireNonNullElse(u, 0));
    final UnaryOperator<String> reverser = (t) -> Streamr.stream(Objects.toString(t, "").split(""))
        .reduce((a, b) -> b.concat(a)).get();
    IntStream.rangeClosed(-10, 10).forEach((i) -> {
      final String actual = "123";
      final Consumers.Bi<String, Integer> injector = (u, v) -> actuals.add(repeater.apply(u, v));
      try {
        injector.accept(actual, i);
      } catch (Exception e) {
        try {/* @formatter:off */
          Trebuchet.Consumers.orElse(actual, i, injector, (u, v, ex) -> injector.accept(reverser.apply(u), Objects.requireNonNullElse(v, 0) * -1));
        } catch (Exception ex) {/* @formatter:on */
          fail(String.format("\"%d\"", i));
        }
      }
      assertEquals(expects[i + 10], actuals.get(i + 10));
    });

    final String[] tests = {null, null};
    Trebuchet.Consumers.orNot((Integer) null, tests, (t, u) -> u[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).accept((Integer) null, tests);
    assertEquals(tests[0], tests[1]);
    assertNull(tests[1]);
    Trebuchet.Consumers.orNot(0, tests, (t, u) -> u[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).accept(0, tests);
    assertEquals(tests[0], tests[1]);
    assertEquals("0", tests[1]);
    Trebuchet.Consumers.orNot(1, tests, (t, u) -> u[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).accept(1, tests);
    assertEquals(tests[0], tests[1]);
    assertEquals("odd", tests[1]);
    Trebuchet.Consumers.orNot(2, tests, (t, u) -> u[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).accept(2, tests);
    assertEquals(tests[0], tests[1]);
    assertEquals("even", tests[1]);

    Trebuchet.Consumers.orElse((Integer) null, tests, (t, u) -> u[0] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> u[0] = "NaN");
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> u[1] = "NaN").accept((Integer) null, tests);
    assertEquals(tests[0], tests[1]);
    assertEquals("NaN", tests[1]);

    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).andThen(null).accept(2, tests);
    Trebuchet.Consumers.Bi.of((Integer t, String[] u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, ex) -> {}).andThen((t, u) -> u[1] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd")).accept(2, tests);
    assertEquals("even", tests[1]);
  }

  @Test
  void triConsumer() throws Throwable {
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
    final List<String> actuals = new ArrayList<>();
    final BiFunction<String, Integer, String> repeater =
        (t, u) -> Objects.toString(t, "").repeat(Objects.requireNonNullElse(u, 0));
    final UnaryOperator<String> reverser = (t) -> Streamr.stream(Objects.toString(t, "").split(""))
        .reduce((a, b) -> b.concat(a)).get();
    IntStream.rangeClosed(-10, 10).forEach((i) -> {
      final String actual = "123";
      final Consumers.Tri<List<String>, String, Integer> injector = (t, u, v) -> t.add(repeater.apply(u, v));
      try {
        injector.accept(actuals, actual, i);
      } catch (Exception e) {
        try {/* @formatter:off */
          Trebuchet.Consumers.orElse(actuals, actual, i, injector, (t, u, v) -> injector.accept(t, reverser.apply(u), Objects.requireNonNullElse(v, 0) * -1));
        } catch (Exception ex) {/* @formatter:on */
          fail(String.format("\"%d\"", i));
        }
      }
      assertEquals(expects[i + 10], actuals.get(i + 10));
    });

    final String[] tests = {null, null};
    Trebuchet.Consumers.orNot((Integer) null, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).accept((Integer) null, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertNull(tests[1]);
    Trebuchet.Consumers.orNot(0, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).accept(0, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertEquals("0", tests[1]);
    Trebuchet.Consumers.orNot(1, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).accept(1, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertEquals("odd", tests[1]);
    Trebuchet.Consumers.orNot(2, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"));
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).accept(2, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertEquals("even", tests[1]);

    Trebuchet.Consumers.orElse((Integer) null, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> u[0] = "NaN");
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> u[v] = "NaN").accept((Integer) null, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertEquals("NaN", tests[1]);

    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).andThen(null).accept(2, tests, 1);
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (t, u, v) -> {}).andThen((t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd")).accept(2, tests, 1);
    assertEquals("even", tests[1]);

    Trebuchet.Consumers.orElse((Integer) null, tests, 0, (t, u, v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (ex) -> tests[0] = ex.getClass().getSimpleName());
    Trebuchet.Consumers.Tri.of((Integer t, String[] u, Integer v) -> u[v] = (t == 0 ? "0" : t % 2 == 0 ? "even" : "odd"), (ex) -> tests[1] = ex.getClass().getSimpleName()).accept(null, tests, 1);
    assertEquals(tests[0], tests[1]);
    assertEquals("NullPointerException", tests[1]);
  }
}
