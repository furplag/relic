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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

class TrebuchetTest {

  @Test
  void paintItGreen() {
    assertTrue(new Trebuchet() {} instanceof Trebuchet);
    assertTrue(Trebuchet.class.isAssignableFrom(new Trebuchet() {}.getClass()));
  }

  @Test
  void sneakyThrow() throws Throwable {
    MethodHandle sneakyThrow =
        MethodHandles.privateLookupIn(Trebuchet.class, MethodHandles.lookup()).findStatic(
            Trebuchet.class, "sneakyThrow", MethodType.methodType(void.class, Throwable.class));
    try {
      sneakyThrow.invoke(null);
    } catch (Throwable ex) {
      assertTrue(ex instanceof IllegalArgumentException);
    }
    try {
      sneakyThrow.invoke(new NullPointerException());
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }

    final Throwable[] throwable = {null};
    // @formatter:off
    Trebuchet.Consumers.orElse("Test", (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertNull(throwable[0]);
    Trebuchet.Consumers.orElse((String) null, (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof NullPointerException);
    Trebuchet.Consumers.orElse(0, (x) -> {@SuppressWarnings("unused") int test = x / x;}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof ArithmeticException);
    // @formatter:on
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
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      consumer = Consumers.Uni.of(consumer.andThen((t) -> {
        actual[0] = t.toUpperCase();
      }), null);
      consumer.accept(actual[0]);
      assertEquals("NULLPOINTEREXCEPTION", actual[0]);
    } catch (Throwable ex) {
      fail("there must not raise NullPointerException .");
    }
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
  }
}
