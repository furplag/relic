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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class RegexrOriginTest {

  @Test
  void test() {
    RegexrOrigin regexr = new RegexrOrigin() {

      @Override
      public List<String> find(String string) {
        return null;
      }

      @Override
      public boolean matches(String string) {
        return false;
      }

      @Override
      public int order() {
        return 0;
      }

      @Override
      public Pattern pattern() {
        return null;
      }

      @Override
      public String replaceAll(String string) {
        return null;
      }
    };
    assertTrue(RegexrOrigin.class.isAssignableFrom(regexr.getClass()));
    assertNull(regexr.replaceAll("nothing to do."));
    assertNull(regexr.find("nothing to do."));
    assertFalse(regexr.matches("nothing to do."));
    assertEquals(0, regexr.order());
    assertNull(regexr.pattern());
    assertNull(regexr.regex());

    regexr = new RegexrOrigin() {

      @Override
      public List<String> find(String string) {
        return Arrays.asList(string);
      }

      @Override
      public boolean matches(String string) {
        return !Objects.toString(string, "").isEmpty();
      }

      @Override
      public int order() {
        return 1;
      }

      @Override
      public Pattern pattern() {
        return Pattern.compile("^[\\x{0000}]$");
      }

      @Override
      public String replaceAll(String string) {
        return string;
      }
    };
    assertTrue(RegexrOrigin.class.isAssignableFrom(regexr.getClass()));
    assertEquals("nothing to do.", regexr.replaceAll("nothing to do."));
    assertEquals(Arrays.asList("nothing to do."), regexr.find("nothing to do."));
    assertTrue(regexr.matches("nothing to do."));
    assertEquals(1, regexr.order());
    assertEquals(Pattern.compile("^[\\x{0000}]$").pattern(), regexr.pattern().pattern());
    assertEquals("^[\\x{0000}]$", regexr.regex());
  }

  @Test
  void testAnyMatch() {
    assertFalse(RegexrOrigin.anyMatch(null));
    assertFalse(RegexrOrigin.anyMatch(""));
    assertFalse(RegexrOrigin.anyMatch("色不異空"));
    assertFalse(RegexrOrigin.anyMatch("色不異空", ((Regexr) null)));
    assertFalse(RegexrOrigin.anyMatch("色不異空", ((Regexr[]) null)));
    assertFalse(RegexrOrigin.anyMatch("色不異空", null, null, null));
    assertFalse(RegexrOrigin.anyMatch("色不異空", new Regexr[]{}));
    assertFalse(RegexrOrigin.anyMatch("色不異空", new Regexr[]{null}));
    assertTrue(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591))));
    assertTrue(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("色", RegexrOrigin.newString(128591))));
    assertTrue(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrStandard("[一切苦厄]", RegexrOrigin.newString(128591))));
    assertFalse(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrStandard("[一切苦厄]", RegexrOrigin.newString(128591))));
    assertTrue(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))));
    assertFalse(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))));
  }

  @Test
  void testCodePoints() {

    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints((String) null).toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints("").toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints((String[]) null).toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints(new String[] {}).toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints(new String[] {null, null}).toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints(new String[] {null, ""}).toArray());
    assertArrayEquals(new int[] {}, RegexrOrigin.codePoints(new String[] {"", ""}).toArray());
    assertArrayEquals("南無阿弥陀仏".codePoints().toArray(), RegexrOrigin.codePoints("南無阿弥陀仏").toArray());
    assertArrayEquals("諸行無常 盛者必衰".codePoints().toArray(), RegexrOrigin.codePoints("諸行無常", null, "", " ", "盛者必衰").toArray());
  }

  @Test
  void testfindAny() {
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny(null));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny(""));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空"));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", ((Regexr) null)));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", ((Regexr[]) null)));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", null, null, null));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", new Regexr[]{}));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", new Regexr[]{null}));
    assertEquals(Arrays.asList("色不異空".split("")), RegexrOrigin.findAny("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591))));
    assertEquals(Arrays.asList("色"), RegexrOrigin.findAny("色不異空", new RegexrStandard("色", RegexrOrigin.newString(128591))));
    assertEquals(Arrays.asList("色不異空".split("")), RegexrOrigin.findAny("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))));
    assertEquals(Collections.EMPTY_LIST, RegexrOrigin.findAny("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))));
  }

  @Test
  void testIsEmpty() {
    assertTrue(RegexrOrigin.isEmpty((String) null));
    assertTrue(RegexrOrigin.isEmpty(""));
    assertFalse(RegexrOrigin.isEmpty("\u0000"));
    assertFalse(RegexrOrigin.isEmpty("\t\r\n\u0020"));
    assertFalse(RegexrOrigin.isEmpty("南無阿弥陀仏"));

    assertTrue(RegexrOrigin.isEmpty((String[]) null));
    assertTrue(RegexrOrigin.isEmpty(new String[] {}));
    assertTrue(RegexrOrigin.isEmpty(new String[] {null}));
    assertTrue(RegexrOrigin.isEmpty(null, null));
    assertTrue(RegexrOrigin.isEmpty(null, ""));
    assertTrue(RegexrOrigin.isEmpty("", null));
    assertTrue(RegexrOrigin.isEmpty("", ""));
    assertFalse(RegexrOrigin.isEmpty("宇", null));
    assertFalse(RegexrOrigin.isEmpty(null, "宙"));
    assertFalse(RegexrOrigin.isEmpty("南無阿弥陀仏".split("")));
  }

  @Test
  void testJoin() {
    assertEquals("", RegexrOrigin.join((String) null));
    assertEquals("", RegexrOrigin.join(""));
    assertEquals("", RegexrOrigin.join((String[]) null));
    assertEquals("", RegexrOrigin.join(new String[] {}));
    assertEquals("", RegexrOrigin.join(new String[] {null}));
    assertEquals("", RegexrOrigin.join(null, null));
    assertEquals("", RegexrOrigin.join(null, ""));
    assertEquals("", RegexrOrigin.join("", null));
    assertEquals("", RegexrOrigin.join("", ""));

    assertEquals("三千世界", RegexrOrigin.join("三千", "世界"));
    assertEquals("色即是空", RegexrOrigin.join("", "色即是空"));
    assertEquals("諸行無常 盛者必衰", RegexrOrigin.join("諸行無常", null, "", " ", "盛者必衰"));
  }

  @Test
  void testNewString() {
    assertEquals("", RegexrOrigin.newString(-1));
    assertEquals("劫", RegexrOrigin.newString(-1, 21163));
    assertEquals("\u0000", RegexrOrigin.newString(0));
    assertEquals("南無阿弥陀仏", RegexrOrigin.newString("南無阿弥陀仏".codePoints().toArray()));

    assertEquals(RegexrOrigin.newString(IntStream.rangeClosed(0, 128).toArray()), RegexrOrigin.newString(IntStream.rangeClosed(-128, 128).toArray()));
  }

  @Test
  void testReplaceAll() {
    assertNull(RegexrOrigin.replaceAll(null, ((Regexr) null)));
    assertEquals("", RegexrOrigin.replaceAll("", ((Regexr) null)));
    assertEquals("", RegexrOrigin.replaceAll("", ((Regexr[]) null)));
    assertEquals("色即是空", RegexrOrigin.replaceAll("色即是空", null, null, null));
    assertEquals("色即是空", RegexrOrigin.replaceAll("色即是空", new Regexr[]{}));
    assertEquals("色即是空", RegexrOrigin.replaceAll("色即是空", new Regexr[]{null}));
    assertEquals("色即是空", RegexrOrigin.replaceAll("色即是空", Regexr.Trimr, Regexr.CtrlRemovr));
    assertEquals("空即是色", RegexrOrigin.replaceAll("色即是空", new RegexrStandard("色即是空", "空即是色")));
    assertEquals("🙏", RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128591)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))));
    assertEquals("😈😈😈😈", RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128520)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))));
    assertEquals("🙏", RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128520)), new RegexrStandard("[\\x{1F608}]", RegexrOrigin.newString(128591)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))));
    assertEquals("🙏", RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]+", RegexrOrigin.newString(128591))));
  }
}
