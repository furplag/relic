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
package jp.furplag.sandbox.text.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import jp.furplag.sandbox.text.regex.RegexrOrigin;

public class TrimrTest {

  @Test
  void test() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Constructor<?> c = Trimr.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertTrue(c.newInstance() instanceof Trimr);
  }

  @Test
  void testIsTrimmed() {
    assertTrue(Trimr.isTrimmed(null));
    assertTrue(Trimr.isTrimmed(""));
    assertTrue(Trimr.isTrimmed("theString."));
    assertTrue(Trimr.isTrimmed("the String."));
    assertTrue(Trimr.isTrimmed("the String ."));
    assertFalse(Trimr.isTrimmed("the String. "));
    assertFalse(Trimr.isTrimmed(" the String."));
    assertFalse(Trimr.isTrimmed(" the String. "));
    assertTrue(Trimr.isTrimmed(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n"))));

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    Function<IntStream, String> randomizr = new Function<IntStream, String>() {
      @Override
      public String apply(IntStream t) {
        List<String> list = t.mapToObj(RegexrOrigin::newString).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(list);

        return list.stream().collect(Collectors.joining());
      }
    };

    final String string = "the string.";
    IntStream.range(0, 10000).forEach(i -> {
      String uglified = randomizr.apply((ctrls + whitespaces).codePoints());
      uglified += string;
      uglified += randomizr.apply((ctrls + whitespaces).codePoints());
      assertFalse(Trimr.isTrimmed(uglified));
      assertTrue(Trimr.isTrimmed(Trimr.trim(uglified)));
    });
  }

  @Test
  void testTrim() {
    assertNull(Trimr.trim(null));
    assertEquals("", Trimr.trim(""));
    assertEquals("theString.", Trimr.trim("theString."));
    assertEquals("the String.", Trimr.trim("the String."));
    assertEquals("the String .", Trimr.trim("the String ."));
    assertEquals("the String.", Trimr.trim("the String. "));
    assertEquals("the String.", Trimr.trim(" the String."));
    assertEquals("the String.", Trimr.trim(" the String. "));
    assertEquals("the String.", Trimr.trim("   the String.   "));
    assertEquals("the String.", Trimr.trim(" \u0010 the String. \u0002 "));
    assertEquals("the String.", Trimr.trim("\n\n\nthe String.\n\n\n"));
    assertEquals("the String.", Trimr.trim("\nthe String.\n"));
    assertEquals("the String.", Trimr.trim("\t\t\tthe String.\t\t\t"));
    assertEquals("the String.", Trimr.trim("\tthe String.\t"));
    assertEquals("the String.", Trimr.trim(" 　 the String. 　 "));

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    Function<IntStream, String> randomizr = new Function<IntStream, String>() {
      @Override
      public String apply(IntStream t) {
        return t.mapToObj(RegexrOrigin::newString).collect(Collectors.toCollection(HashSet::new)).stream().collect(Collectors.joining());
      }
    };

    final String string = "the string.";
    IntStream.range(0, 10000).forEach(i -> {
      String uglified = randomizr.apply((ctrls + whitespaces).codePoints());
      uglified += string;
      uglified += randomizr.apply((ctrls + whitespaces).codePoints());

      assertEquals(string, Trimr.trim(uglified));
    });
  }
}
