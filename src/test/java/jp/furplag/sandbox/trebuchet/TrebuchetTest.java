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
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class TrebuchetTest {

  @Test
  void paintItGreen() {
    assertTrue(new Trebuchet() {} instanceof Trebuchet);
    assertTrue(Trebuchet.class.isAssignableFrom(new Trebuchet() {}.getClass()));
  }

  @Test
  void sneakyThrow() throws Throwable {
    MethodHandle sneakyThrow = MethodHandles.privateLookupIn(Trebuchet.class, MethodHandles.lookup()).findStatic(Trebuchet.class, "sneakyThrow", MethodType.methodType(void.class, Throwable.class));
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

    final Throwable[] throwable = { null };
    // @formatter:off
    Trebuchet.operateOrElse("Test", (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertNull(throwable[0]);
    Trebuchet.operateOrElse((String) null, (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof NullPointerException);
    Trebuchet.operateOrElse(0, (x) -> {@SuppressWarnings("unused") int test = x / x;}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof ArithmeticException);
    // @formatter:on
  }

  @Test
  void consumer() throws Throwable {
    final String[] actual = { null };
    Consumer<String> consumer = (t) -> actual[0] = t.toUpperCase();
    try {
      consumer.accept(actual[0]);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    consumer = Consumers.of((t) -> actual[0] = t.toUpperCase(), (x, ex) -> {  });
    consumer.accept(actual[0]);
    assertEquals((String) null, actual[0]);
    consumer = Consumers.of((t) -> actual[0] = t.toUpperCase(), null);
    consumer.accept(actual[0]);
    assertEquals((String) null, actual[0]);
    consumer = Consumers.of((t) -> actual[0] = t.toUpperCase(), (x, ex) -> { actual[0] = ex.getClass().getSimpleName(); });
    consumer.accept(actual[0]);
    assertEquals("NullPointerException", actual[0]);
    consumer.accept(actual[0]);
    assertEquals("NULLPOINTEREXCEPTION", actual[0]);
    actual[0] = null;
    Trebuchet.orNone((String) null, String::toUpperCase);
    assertNull(actual[0]);
    consumer = consumer.andThen((t) -> {actual[0] = t.toUpperCase();});
    try {
      consumer.accept(actual[0]);
      fail("there must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      consumer = Consumers.of(consumer.andThen((t) -> {actual[0] = t.toUpperCase();}), null);
      consumer.accept(actual[0]);
      assertEquals("NULLPOINTEREXCEPTION", actual[0]);
    } catch (Throwable ex) {
      fail("there must not raise NullPointerException .");
    }
  }
}
