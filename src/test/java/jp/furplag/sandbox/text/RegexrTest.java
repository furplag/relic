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
package jp.furplag.sandbox.text;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.Character.UnicodeBlock;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.furplag.sandbox.reflect.SavageReflection;

public class RegexrTest {

  @Test
  void paintItGreen() {
    final Regexr regexr = new Regexr.Origin("o", Regexr.newString(129503)) {};
    assertEquals("🧟 🧟 🧟", regexr.replaceAll("o o o"));
    SavageReflection.set(regexr, "pattern", null);
    assertEquals("nothing to do.", regexr.replaceAll("nothing to do."));
  }

  @Test
  void test() {/* @formatter:off */
    Regexr regexr = new Regexr() {

      @Override public List<String> find(String text) { return null; }

      @Override public boolean matches(String text) { return false; }

      @Override public String replaceAll(String text) { return null; }

      @Override public Pattern getPattern() { return null; }

      @Override public String getReplacement() { return null; }

      @Override public int getOrder() { return 0; }
    };
    assertTrue(Regexr.class.isAssignableFrom(regexr.getClass()));
    assertNull(regexr.replaceAll("nothing to do."));
    assertNull(regexr.find("nothing to do."));
    assertFalse(regexr.matches("nothing to do."));
    assertEquals(0, regexr.getOrder());
    assertNull(regexr.getPattern());
    assertNull(regexr.getRegex());

    regexr = new Regexr() {

      final Pattern _pattern = Pattern.compile("^[\\x{0000}]$");

      @Override public List<String> find(String text) { return Arrays.asList(text); }

      @Override public boolean matches(String text) { return !Objects.toString(text, "").isEmpty(); }

      @Override public String replaceAll(String text) { return text; }

      @Override public Pattern getPattern() { return _pattern; }

      @Override public String getReplacement() { return null; }

      @Override public int getOrder() { return 1; }
    };
    assertTrue(Regexr.class.isAssignableFrom(regexr.getClass()));
    assertEquals("nothing to do.", regexr.replaceAll("nothing to do."));
    assertEquals(Arrays.asList("nothing to do."), regexr.find("nothing to do."));
    assertTrue(regexr.matches("nothing to do."));
    assertEquals(1, regexr.getOrder());
    assertEquals(Pattern.compile("^[\\x{0000}]$").pattern(), regexr.getPattern().pattern());
    assertEquals("^[\\x{0000}]$", regexr.getRegex());

    assertEquals(new Regexr.Origin(null, null, 0) {}, new Regexr.Origin(null, null) {});
    assertEquals(0, new Regexr.Origin(null, null) {}.getOrder());
    assertEquals(new Regexr.Recursive(null, null) {}, new Regexr.Recursive(null, null) {});
    assertEquals(new Regexr.Recursive(null, null) {}, new Regexr.Recursive(null, null, 10) {});
    assertNotEquals(new Regexr.Origin(null, null) {}, new Regexr.Recursive(null, null) {});
  /* @formatter:off */}

  @Test
  void testAnyMatch() {
    assertFalse(Regexr.anyMatch(null));
    assertFalse(Regexr.anyMatch(""));
    assertFalse(Regexr.anyMatch("色不異空"));
    assertFalse(Regexr.anyMatch("色不異空", ((Regexr) null)));
    assertFalse(Regexr.anyMatch("色不異空", ((Regexr[]) null)));
    assertFalse(Regexr.anyMatch("色不異空", null, null, null));
    assertFalse(Regexr.anyMatch("色不異空", new Regexr[]{}));
    assertFalse(Regexr.anyMatch("色不異空", new Regexr[]{ null }));
    assertTrue(Regexr.anyMatch("色不異空", new Regexr.Origin("[空不異色]", Regexr.newString(128591)) {}));
    assertTrue(Regexr.anyMatch("色不異空", new Regexr.Origin("色", Regexr.newString(128591)) {}));
    assertTrue(Regexr.anyMatch("色不異空", new Regexr.Origin("[空不異色]", Regexr.newString(128591)) {}, new Regexr.Origin("[一切苦厄]", Regexr.newString(128591)) {}));
    assertFalse(Regexr.anyMatch("色不異空", new Regexr.Origin("[諸行無常]", Regexr.newString(128591)) {}, new Regexr.Origin("[一切苦厄]", Regexr.newString(128591)) {}));
    assertTrue(Regexr.anyMatch("色不異空", new Regexr.Origin("[空不異色]", Regexr.newString(128591)) {}, new Regexr.Origin("[一切苦厄]", Regexr.newString(128591)) {}));
    assertFalse(Regexr.anyMatch("色不異空", new Regexr.Origin("[諸行無常]", Regexr.newString(128591)) {}, new Regexr.Origin("[一切苦厄]", Regexr.newString(128591)) {}));
  }

  @Test
  void testCodePoints() {
    assertArrayEquals(new int[] {}, Regexr.codePoints((String) null).toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints("").toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints((String[]) null).toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints(new String[] {}).toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints(new String[] {null, null}).toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints(new String[] {null, ""}).toArray());
    assertArrayEquals(new int[] {}, Regexr.codePoints(new String[] {"", ""}).toArray());
    assertArrayEquals("南無阿弥陀仏".codePoints().toArray(), Regexr.codePoints("南無阿弥陀仏").toArray());
    assertArrayEquals("諸行無常 盛者必衰".codePoints().toArray(), Regexr.codePoints("諸行無常", null, "", " ", "盛者必衰").toArray());
  }

  @Test
  void testFind() {
    assertTrue(Regexr.removeEmpties.find(null).isEmpty());
    assertTrue(Regexr.removeEmpties.find("").isEmpty());
    assertFalse(Regexr.removeEmpties.find("\u200A\u200B").isEmpty());
  }

  @Test
  void testFindAny() {
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny(null));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny(""));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空"));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", ((Regexr) null)));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", ((Regexr[]) null)));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", null, null, null));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", new Regexr[]{}));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", new Regexr[]{null}));
    assertEquals(Arrays.asList("色不異空".split("")), Regexr.findAny("色不異空", new Regexr.Origin("[空不異色]", Regexr.newString(128591)) {}));
    assertEquals(Arrays.asList("色"), Regexr.findAny("色不異空", new Regexr.Origin("色", Regexr.newString(128591)) {}));
    assertEquals(Arrays.asList("色不異空".split("")), Regexr.findAny("色不異空", new Regexr.Origin("[空不異色]", Regexr.newString(128591)) {}, new Regexr.Recursive("[一切苦厄]", Regexr.newString(128591)) {}));
    assertEquals(Collections.EMPTY_LIST, Regexr.findAny("色不異空", new Regexr.Origin("[諸行無常]", Regexr.newString(128591)) {}, new Regexr.Recursive("[一切苦厄]", Regexr.newString(128591)) {}));
  }

  @Test
  void testNewString() {
    assertEquals("", Regexr.newString(-1));
    assertEquals("劫", Regexr.newString(-1, 21163));
    assertEquals("\u0000", Regexr.newString(0));
    assertEquals("南無阿弥陀仏", Regexr.newString("南無阿弥陀仏".codePoints().toArray()));

    assertEquals(Regexr.newString(IntStream.rangeClosed(0, 128).toArray()), Regexr.newString(IntStream.rangeClosed(-128, 128).toArray()));
  }

  @Test
  void testnormalizeCjk() {
    assertEquals(100000, Regexr.normalizeCjk.getOrder());
    assertNull(Regexr.normalizeCjk.replaceAll(null));
    assertEquals("", Regexr.normalizeCjk.replaceAll(""));

    final Set<UnicodeBlock> unicodeBlocks = Arrays.asList(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION, UnicodeBlock.HIRAGANA, UnicodeBlock.KATAKANA, UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS).stream().collect(Collectors.toSet());
    final Set<Integer> excludes = Arrays.asList(0xFF04, 0xFF5E, 0xFFE0, 0xFFE1, 0xFFE5, 0xFFE6).stream().collect(Collectors.toSet());
    String halfwidthAndFullwidthForms = IntStream.rangeClosed(0, 200_000).filter(codePoint -> unicodeBlocks.contains(UnicodeBlock.of(codePoint))).filter(codePoint -> !excludes.contains(codePoint)).mapToObj(Regexr::newString).collect(Collectors.joining());
    String halfwidthAndFullwidthFormsR = IntStream.rangeClosed(0, 200_000).mapToObj(Regexr::newString).filter(s -> Regexr.normalizeCjk.getPattern().matcher(s).matches()).collect(Collectors.joining());
    assertEquals(halfwidthAndFullwidthFormsR, halfwidthAndFullwidthForms);
    assertArrayEquals(halfwidthAndFullwidthFormsR.codePoints().toArray(), halfwidthAndFullwidthForms.codePoints().toArray());

    String uglified = IntStream.rangeClosed(0xFF00, 0xFFEF).mapToObj(Regexr::newString).collect(Collectors.joining());
    // @formatter:off
    String expect = uglified.codePoints()
      .mapToObj(Regexr::newString)
      .map(s -> Normalizer.normalize(s, Form.NFKC)
        .replaceAll("\u0020?([\u3099])", "\u309B")
        .replaceAll("\u0020?([\u309A])", "\u309C")
        .replaceAll("\u007E", "\uFF5E")
        .replaceAll("\\$", "\uFF04")
        .replaceAll("\u00A2", "\uFFE0")
        .replaceAll("\u00A3", "\uFFE1")
        .replaceAll("\u00A5", "\uFFE5")
        .replaceAll("\u20A9", "\uFFE6")
      ).collect(Collectors.joining());

    // @formatter:on
    // 0xFF04, 0xFF5E, 0xFFE0, 0xFFE1, 0xFFE5, 0xFFE6

    assertEquals(expect, Regexr.normalizeCjk.replaceAll(uglified));

    expect = "ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワワイエヲンヴカケヷイ゛エ゛ヺ";
    String actual = Regexr.normalizeCjk.replaceAll("ｧｱｨｲｩｳｪｴｫｵｶｶﾞｷｷﾞｸｸﾞｹｹﾞｺｺﾞｻｻﾞｼｼﾞｽｽﾞｾｾﾞｿｿﾞﾀﾀﾞﾁﾁﾞｯﾂﾂﾞﾃﾃﾞﾄﾄﾞﾅﾆﾇﾈﾉﾊﾊﾞﾊﾟﾋﾋﾞﾋﾟﾌﾌﾞﾌﾟﾍﾍﾞﾍﾟﾎﾎﾞﾎﾟﾏﾐﾑﾒﾓｬﾔｭﾕｮﾖﾗﾘﾙﾚﾛﾜﾜｲｴｦﾝｳﾞｶｹﾜﾞｲﾞｴﾞｦﾞ");
    assertEquals(expect, actual);

    expect = "ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワワイエヲンヴカケヷヸヹヺ";
    assertEquals(expect, Regexr.normalizeCjk.replaceAll(expect));
    assertEquals(expect, Regexr.normalizeCjk.replaceAll(Normalizer.normalize(expect, Form.NFD)));

    assertEquals("Hello World.", Regexr.normalizeCjk.replaceAll("Hello World."));
    assertEquals("Hello World.", Regexr.normalizeCjk.replaceAll("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));
    assertEquals("こんにちは 世界", Regexr.normalizeCjk.replaceAll("こんにちは　世界"));
    assertEquals("コンニチハ 世界", Regexr.normalizeCjk.replaceAll("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", Regexr.normalizeCjk.replaceAll("コンニチハ　世界"));
    assertEquals("バーバパパ", Regexr.normalizeCjk.replaceAll("バーバパパ"));
    assertEquals("バーバパパ", Regexr.normalizeCjk.replaceAll("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", Regexr.normalizeCjk.replaceAll("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", Regexr.normalizeCjk.replaceAll("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", Regexr.normalizeCjk.replaceAll("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", Regexr.normalizeCjk.replaceAll("ヷヸヴヹヺ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Regexr.normalizeCjk.replaceAll("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Regexr.normalizeCjk.replaceAll("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("パ～やん", Regexr.normalizeCjk.replaceAll("パ～やん"));
  }

  @Test
  void testOrigin() {
    // @formatter:off
    assertTrue(new Regexr.Origin(null,  null, 0){} instanceof Regexr);

    Regexr one = new Regexr.Origin("one",  "壱", 1) {};
    Regexr two = new Regexr.Origin("two",  "弐", 2) {};
    Regexr three = new Regexr.Origin("three",  "参", 3) {};
    Regexr minus = new Regexr.Origin("minus",  "▲壱", -1) {};
    // @formatter:on

    assertArrayEquals(new Regexr[] {null, two, one, three, minus}, Arrays.asList(null, two, one, three, minus).stream().toArray());
    assertArrayEquals(new Regexr[] {null, minus, one, two, three}, Arrays.asList(null, two, one, three, minus).stream().sorted().toArray());

    assertFalse(one.equals(null));
    assertFalse(one.equals((Regexr) null));
    /* @formatter:off */
    Regexr anotherOne = new Regexr() {

      @Override public List<String> find(String text) { return null; }

      @Override public boolean matches(String text) { return false; }

      @Override public String replaceAll(String text) { return null; }

      @Override public Pattern getPattern() { return null; }

      @Override public String getReplacement() { return null; }

      @Override public int getOrder() { return 0; }
    };
    assertFalse(one.equals(anotherOne));
    assertFalse(one.equals(two));
    assertFalse(one.equals(new Regexr.Origin("one", "1", 1) {}));
    assertFalse(one == two);
    anotherOne = one;
    assertTrue(one == anotherOne);
    assertFalse(one == new Regexr.Origin("one", "壱", 1) {});
    assertTrue(one.equals(one));
    assertTrue(one.equals(new Regexr.Origin("one", "壱", 1) {}));
    assertTrue(one.equals(new Regexr.Origin("one", "壱", 1) {}));

    assertFalse(one.hashCode() == two.hashCode());
    assertFalse(one.hashCode() == new Regexr.Origin("one", "1", 1) {}.hashCode());
    assertTrue(one.hashCode() == anotherOne.hashCode());
    assertTrue(one.hashCode() == new Regexr.Origin("one", "壱", 1) {}.hashCode());
    assertTrue(one.hashCode() == new Regexr.Origin("one", "壱", 1) {}.hashCode());

    try {
      assertFalse(one.hashCode() == new Regexr.Origin("one", "壱", -100) {
        {
          SavageReflection.set(this, Regexr.Origin.class.getDeclaredField("pattern"), null);
          SavageReflection.set(this, Regexr.Origin.class.getDeclaredField("replacement"), null);
        }
      }.hashCode());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void testRecursive() {
    assertEquals(0, new Regexr.Recursive(null, null) {}.getOrder());
    assertNull(new Regexr.Recursive("one", "壱") {}.replaceAll(null));
    assertEquals("", new Regexr.Recursive("one", "壱") {}.replaceAll(""));
    assertEquals("1", new Regexr.Recursive("one", "壱") {}.replaceAll("1"));
    assertEquals("o ne", new Regexr.Recursive("one", "壱") {}.replaceAll("o ne"));
    assertEquals("One", new Regexr.Recursive("one", "壱") {}.replaceAll("One"));
    assertEquals("壱", new Regexr.Recursive("[Oo]ne", "壱") {}.replaceAll("One"));
    assertEquals("\t\t\to\tne\t\t\t", new Regexr.Recursive("one", "壱") {}.replaceAll("\t\t\to\tne\t\t\t"));
    assertEquals("\t\t\to\nne\t\t\t", new Regexr.Recursive("one", "壱") {}.replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("壱", new Regexr.Recursive("one", "壱") {}.replaceAll("one"));
    assertEquals("壱 two three", new Regexr.Recursive("one", "壱") {}.replaceAll("one two three"));
    assertEquals("three two 壱 zero", new Regexr.Recursive("one", "壱") {}.replaceAll("three two one zero"));
    assertEquals("three\ntwo\n壱\nzero", new Regexr.Recursive("one", "壱") {}.replaceAll("three\ntwo\none\nzero"));
    assertEquals("壱壱壱壱壱", new Regexr.Recursive("one", "壱") {}.replaceAll("oneoneoneoneone"));
    assertEquals("ne壱壱壱壱o", new Regexr.Recursive("one", "壱") {}.replaceAll("neoneoneoneoneo"));
    assertEquals("\t\t\t壱\t\t\t", new Regexr.Recursive("one", "壱") {}.replaceAll("\t\t\tone\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", new Regexr.Recursive("o\\s?n\\s?e", "壱") {}.replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", new Regexr.Recursive("o\\s*n\\s*e", "壱") {}.replaceAll("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));

    String spaces = IntStream.rangeClosed(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT).filter(Character::isWhitespace).mapToObj(Regexr::newString).collect(Collectors.joining("\u0020"));
    assertEquals(" ", new Regexr.Recursive("[\\p{javaWhitespace}]{2,}", "\u0020") {}.replaceAll(spaces));
  }

  @Test
  void testRemoveCtls() {
    assertEquals(new Regexr.Origin("[\\p{Cc}&&[^\\s\\x{001C}-\\x{001F}]]", "") {}, Regexr.removeCtrls);
    assertEquals(0, Regexr.removeCtrls.getOrder());

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(Regexr::newString).collect(Collectors.joining());
    String ctrlsR = IntStream.rangeClosed(0, 200_000).mapToObj(Regexr::newString).filter(s -> Regexr.removeCtrls.getPattern().matcher(s).matches()).collect(Collectors.joining());
    assertArrayEquals(ctrls.codePoints().toArray(), ctrlsR.codePoints().toArray());
    assertEquals(ctrls, ctrlsR);
    assertEquals("", Regexr.removeCtrls.replaceAll(ctrls));
    assertEquals("", Regexr.removeCtrls.replaceAll(ctrlsR));

    final String string = "PrettyfyMe.";
    String uglified = "";
    for (String ctrl : ctrls.split("")) {
      uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrl + ctrl + ctrl + ctrl + ctrl));
      assertTrue(Character.isISOControl(ctrl.codePointAt(0)));
      assertEquals(string, Regexr.removeCtrls.replaceAll(uglified));
    }
    uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrls));
    assertEquals(string, Regexr.removeCtrls.replaceAll(uglified));
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(ctrlsR));
    assertEquals(string, Regexr.removeCtrls.replaceAll(uglified));
  }

  @Test
  void testRemoveEmpties() {
    assertEquals(Regexr.removeEmpties, new Regexr.Origin(Regexr.removeEmpties.getRegex(), null) {});
    final String string = "catch me, if you can.";
    IntStream.rangeClosed(0, 200_000).mapToObj(Regexr::newString).filter(Regexr.removeEmpties::matches).forEach((t) -> {
      assertEquals(string, Regexr.removeEmpties.replaceAll(string.replaceAll(" ", t + t + " " + t + t)));
    });
  }

  @Test
  void testReplaceAll() {
    assertNull(Regexr.replaceAll(null, ((Regexr) null)));
    assertEquals("", Regexr.replaceAll("", ((Regexr) null)));
    assertEquals("", Regexr.replaceAll("", ((Regexr[]) null)));
    assertNull(Regexr.replaceAll(null, Regexr.Origin.trim, Regexr.Origin.removeCtrls));
    assertEquals("", Regexr.replaceAll("", Regexr.Origin.trim, Regexr.Origin.removeCtrls));
    assertEquals("色即是空", Regexr.replaceAll("色即是空", null, null, null));
    assertEquals("色即是空", Regexr.replaceAll("色即是空", new Regexr[]{}));
    assertEquals("色即是空", Regexr.replaceAll("色即是空", new Regexr[]{null}));
    assertEquals("色即是空", Regexr.replaceAll("色即是空", Regexr.Origin.trim, Regexr.Origin.removeCtrls));
    assertEquals("空即是色", Regexr.replaceAll("色即是空", new Regexr.Origin("色即是空", "空即是色") {}));
    assertEquals("🙏", Regexr.replaceAll("色即是空", new Regexr.Origin("[色即是空]", Regexr.newString(128591)) {}, new Regexr.Recursive("[\\x{1F64F}]{2,}", Regexr.newString(128591)) {}));
    assertEquals("😈😈😈😈", Regexr.replaceAll("色即是空", new Regexr.Origin("[色即是空]", Regexr.newString(128520)) {}, new Regexr.Recursive("[\\x{1F64F}]{2,}", Regexr.newString(128591)) {}));
    assertEquals("🙏", Regexr.replaceAll("色即是空", new Regexr.Origin("[色即是空]", Regexr.newString(128520)) {}, new Regexr.Origin("[\\x{1F608}]", Regexr.newString(128591)) {}, new Regexr.Recursive("[\\x{1F64F}]{2,}", Regexr.newString(128591)) {}));
    assertEquals("🙏", Regexr.replaceAll("色即是空", new Regexr.Origin("[色即是空]+", Regexr.newString(128591)) {}));
  }
}
