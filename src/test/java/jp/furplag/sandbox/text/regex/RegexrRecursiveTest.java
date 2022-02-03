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
package jp.furplag.sandbox.text.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class RegexrRecursiveTest {

  @Test
  void test() {
    assertEquals(new RegexrRecursive(null, null), new RegexrRecursive(null, null));

    assertEquals(0, new RegexrRecursive(null, null).order());
  }

  @Test
  void testReplaceAll() {
    assertNull(new RegexrRecursive("one", "壱").replaceAll(null));
    assertEquals("", new RegexrRecursive("one", "壱").replaceAll(""));
    assertEquals("1", new RegexrRecursive("one", "壱").replaceAll("1"));
    assertEquals("o ne", new RegexrRecursive("one", "壱").replaceAll("o ne"));
    assertEquals("One", new RegexrRecursive("one", "壱").replaceAll("One"));
    assertEquals("壱", new RegexrRecursive("[Oo]ne", "壱").replaceAll("One"));
    assertEquals("\t\t\to\tne\t\t\t", new RegexrRecursive("one", "壱").replaceAll("\t\t\to\tne\t\t\t"));
    assertEquals("\t\t\to\nne\t\t\t", new RegexrRecursive("one", "壱").replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("壱", new RegexrRecursive("one", "壱").replaceAll("one"));
    assertEquals("壱 two three", new RegexrRecursive("one", "壱").replaceAll("one two three"));
    assertEquals("three two 壱 zero", new RegexrRecursive("one", "壱").replaceAll("three two one zero"));
    assertEquals("three\ntwo\n壱\nzero", new RegexrRecursive("one", "壱").replaceAll("three\ntwo\none\nzero"));
    assertEquals("壱壱壱壱壱", new RegexrRecursive("one", "壱").replaceAll("oneoneoneoneone"));
    assertEquals("ne壱壱壱壱o", new RegexrRecursive("one", "壱").replaceAll("neoneoneoneoneo"));
    assertEquals("\t\t\t壱\t\t\t", new RegexrRecursive("one", "壱").replaceAll("\t\t\tone\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", new RegexrRecursive("o\\s?n\\s?e", "壱").replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", new RegexrRecursive("o\\s*n\\s*e", "壱").replaceAll("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));

    String spaces = IntStream.rangeClosed(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining("\u0020"));
    assertEquals(" ", new RegexrRecursive("[\\p{javaWhitespace}]{2,}", "\u0020").replaceAll(spaces));
  }
}
