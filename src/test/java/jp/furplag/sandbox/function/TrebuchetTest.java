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
package jp.furplag.sandbox.function;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrebuchetTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @BeforeEach
  public void setUpStreams() {
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void cleanUpStreams() {
      System.setOut(null);
      System.setErr(null);
  }

  @Test
  void test() {
  }

  @Test
  void testThrowableConsumer() throws Exception {
    Trebuchet.ThrowableConsumer<Integer> isOdd = (i) -> System.out.println(i % 2 != 0 ? "odd" : "even");
    int i = 10;
    isOdd.accept(i);
    try {
      isOdd.accept(null);
      fail("must raise NullPointerException .");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  void testThrowableBiConsumer() throws Exception {
    Trebuchet.ThrowableBiConsumer<Integer, Integer> divider = (x, y) -> System.out.println(x / y);
    divider.accept(10, 10);
    try {
      divider.accept(null, null);
      fail("must raise NullPointerException .");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
    try {
      divider.accept(10, 0);
      fail("must raise ArithmeticException .");
    } catch (Exception e) {
      assertTrue(e instanceof ArithmeticException);
    }
  }

  @Test
  void testThrowableFunction() throws Exception {
    Trebuchet.ThrowableFunction<Integer, Boolean> isOdd = (i) -> (i % 2 != 0);
    assertFalse(isOdd.apply(10));
    assertTrue(isOdd.apply(1));
    try {
      isOdd.apply(null);
      fail("must raise NullPointerException .");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  void testThrowableBiFunction() throws Exception {
    Trebuchet.ThrowableBiFunction<Integer, Integer, Integer> divider = (x, y) -> (x / y);
    assertEquals(1, (int) divider.apply(10, 10));
    assertEquals(2, (int) divider.apply(10, 5));
    assertEquals(3, (int) divider.apply(10, 3));
    assertEquals(0, (int) divider.apply(0, 4));
    try {
      divider.apply(null, null);
      fail("must raise NullPointerException .");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
    try {
      divider.apply(10, 0);
      fail("must raise ArithmeticException .");
    } catch (Exception e) {
      assertTrue(e instanceof ArithmeticException);
    }
  }

  @Test
  void testAccept() {
    // @formatter:off
    final StringJoiner expectOfOut = new StringJoiner(System.getProperty("line.separator", "\n"), "", System.getProperty("line.separator", "\n"));
    final StringJoiner expectOfError = new StringJoiner(System.getProperty("line.separator", "\n"), "", System.getProperty("line.separator", "\n"));

    final Trebuchet.ThrowableConsumer<Integer> divider = (i) -> System.out.println(10 / i);
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null})
    .forEach(i -> {
      try {
        divider.accept(i);
        expectOfOut.add(Integer.toString(10 / i));
      } catch (Exception e) {
        System.err.println(e.getClass().getSimpleName());
        expectOfError.add(e.getClass().getSimpleName());
      }
    });
    assertEquals(expectOfOut.toString(), outContent.toString());
    assertEquals(expectOfError.toString(), errContent.toString());

    final Consumer<Integer> trebuchet = Trebuchet.orElse((x) -> {System.out.println(10 / x);expectOfOut.add(Integer.toString(10 / x));}, (e, x) -> {System.err.println(e.getClass().getSimpleName());expectOfError.add(e.getClass().getSimpleName());});
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(trebuchet::accept);

    assertEquals(expectOfOut.toString(), outContent.toString());
    assertEquals(expectOfError.toString(), errContent.toString());
    // @formatter:on
  }

  @Test
  void testAcceptBi() {
    Trebuchet.ThrowableBiConsumer<Integer, Integer> divider = (x, y) -> System.out.println(x / y);
    final StringJoiner expectOfOut = new StringJoiner(System.getProperty("line.separator", "\n"), "", System.getProperty("line.separator", "\n"));
    final StringJoiner expectOfError = new StringJoiner(System.getProperty("line.separator", "\n"), "", System.getProperty("line.separator", "\n"));
    // @formatter:off
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null})
    .forEach(i -> {
      try {
        divider.accept(i, i - 1);
        expectOfOut.add(Integer.toString(i / (i - 1)));
      } catch (Exception e) {
        System.err.println(e.getClass().getSimpleName());
        expectOfError.add(e.getClass().getSimpleName());
      }
    });
    assertEquals(expectOfOut.toString(), outContent.toString());
    assertEquals(expectOfError.toString(), errContent.toString());

    final BiConsumer<Integer, Integer> trebuchet = Trebuchet.orElse((x, y) -> {System.out.println(x / y);expectOfOut.add(Integer.toString(x / y));}, (e, x) -> {System.err.println(e.getClass().getSimpleName());expectOfError.add(e.getClass().getSimpleName());});
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4}).forEach((i) -> trebuchet.accept(i, i - 1));

    assertEquals(expectOfOut.toString(), outContent.toString());
    assertEquals(expectOfError.toString(), errContent.toString());
    // @formatter:on
  }

  @Test
  void testApply() {
    Trebuchet.ThrowableFunction<Integer, Integer> divider = (i) -> (10 / i);
    Integer[] expect = {0, 10, 5, 3, 2, 0};
    // @formatter:off
    assertArrayEquals(expect, Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null})
      .map(i -> {
        try {
          return divider.apply(i);
        } catch (Exception e) {}
        return 0;
      }).toArray(Integer[]::new));

    assertArrayEquals(expect, Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).map(i -> Trebuchet.orElse(divider, (e, x) -> 0).apply(i)).toArray(Integer[]::new));
    // @formatter:on
  }

  @Test
  void testApplyBi() {
    Trebuchet.ThrowableBiFunction<Integer, Integer, Integer> divider = (x, y) -> (x / (y - 1));
    Integer[] expect = {0, 0, 2, 1, 1, 0};
    // @formatter:off
    assertArrayEquals(expect,
      Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null})
        .map(i -> {
          try {
            return divider.apply(i, i);
          } catch (Exception e) {}
          return 0;
        }).toArray(Integer[]::new));

    assertArrayEquals(expect, Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).map(i -> Trebuchet.orElse(divider, (e, x) -> 0).apply(i, i)).toArray(Integer[]::new));
    // @formatter:on
  }
}
