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
import static org.junit.jupiter.api.Assertions.fail;
import java.lang.Character.UnicodeBlock;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.stream.Streamr;

public class RegexrTest {

  @Test
  void test() {
    // @formatter:off
    assertTrue(new Regexr(null,  null, 0){} instanceof Regexr);

    Regexr one = new Regexr("one",  "壱", 1) {};
    Regexr two = new Regexr("two",  "弐", 2) {};
    Regexr three = new Regexr("three",  "参", 3) {};
    Regexr minus = new Regexr("minus",  "▲壱", -1) {};
    // @formatter:on

    assertArrayEquals(new RegexrOrigin[] {null, two, one, three, minus}, Arrays.asList(null, two, one, three, minus).stream().toArray());
    assertArrayEquals(new RegexrOrigin[] {null, minus, one, two, three}, Arrays.asList(null, two, one, three, minus).stream().sorted().toArray());

    assertFalse(one.equals(null));
    assertFalse(one.equals((Regexr) null));
    RegexrOrigin anotherOne = new RegexrOrigin() {
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
    assertFalse(one.equals(anotherOne));
    assertFalse(one.equals(two));
    assertFalse(one.equals(new Regexr("one", "1", 1) {}));
    assertFalse(one == two);
    anotherOne = one;
    assertTrue(one == anotherOne);
    assertFalse(one == new Regexr("one", "壱", 1) {});
    assertTrue(one.equals(one));
    assertTrue(one.equals(new Regexr("one", "壱", 1) {}));
    assertTrue(one.equals(new Regexr("one", "壱", 1) {}));

    assertFalse(one.hashCode() == two.hashCode());
    assertFalse(one.hashCode() == new Regexr("one", "1", 1) {}.hashCode());
    assertTrue(one.hashCode() == anotherOne.hashCode());
    assertTrue(one.hashCode() == new Regexr("one", "壱", 1) {}.hashCode());
    assertTrue(one.hashCode() == new Regexr("one", "壱", 1) {}.hashCode());

    try {
      assertFalse(one.hashCode() == new Regexr("one", "壱", -100) {
        {
          SavageReflection.set(this, Regexr.class.getDeclaredField("pattern"), null);
          SavageReflection.set(this, Regexr.class.getDeclaredField("replacement"), null);
        }
      }.hashCode());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void testCjkNormalizr() {
    assertEquals(100000, Regexr.CjkNormalizr.order());
    assertNull(Regexr.CjkNormalizr.replaceAll(null));
    assertEquals("", Regexr.CjkNormalizr.replaceAll(""));

    final Set<UnicodeBlock> unicodeBlocks = Arrays.asList(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION, UnicodeBlock.HIRAGANA, UnicodeBlock.KATAKANA, UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS).stream().collect(Collectors.toSet());
    final Set<Integer> excludes = Arrays.asList(0xFF04, 0xFF5E, 0xFFE0, 0xFFE1, 0xFFE5, 0xFFE6).stream().collect(Collectors.toSet());
    String halfwidthAndFullwidthForms = IntStream.rangeClosed(0, 200_000).filter(codePoint -> unicodeBlocks.contains(UnicodeBlock.of(codePoint))).filter(codePoint -> !excludes.contains(codePoint)).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String halfwidthAndFullwidthFormsR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s -> Regexr.CjkNormalizr.pattern.matcher(s).matches()).collect(Collectors.joining());
    assertEquals(halfwidthAndFullwidthFormsR, halfwidthAndFullwidthForms);
    assertArrayEquals(halfwidthAndFullwidthFormsR.codePoints().toArray(), halfwidthAndFullwidthForms.codePoints().toArray());

    String uglified = IntStream.rangeClosed(0xFF00, 0xFFEF).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    // @formatter:off
    String expect = uglified.codePoints()
      .mapToObj(RegexrOrigin::newString)
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

    assertEquals(expect, Regexr.CjkNormalizr.replaceAll(uglified));

    expect = "ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワワイエヲンヴカケヷイ゛エ゛ヺ";
    String actual = Regexr.CjkNormalizr.replaceAll("ｧｱｨｲｩｳｪｴｫｵｶｶﾞｷｷﾞｸｸﾞｹｹﾞｺｺﾞｻｻﾞｼｼﾞｽｽﾞｾｾﾞｿｿﾞﾀﾀﾞﾁﾁﾞｯﾂﾂﾞﾃﾃﾞﾄﾄﾞﾅﾆﾇﾈﾉﾊﾊﾞﾊﾟﾋﾋﾞﾋﾟﾌﾌﾞﾌﾟﾍﾍﾞﾍﾟﾎﾎﾞﾎﾟﾏﾐﾑﾒﾓｬﾔｭﾕｮﾖﾗﾘﾙﾚﾛﾜﾜｲｴｦﾝｳﾞｶｹﾜﾞｲﾞｴﾞｦﾞ");
    assertEquals(expect, actual);

    expect = "ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワワイエヲンヴカケヷヸヹヺ";
    assertEquals(expect, Regexr.CjkNormalizr.replaceAll(expect));
    assertEquals(expect, Regexr.CjkNormalizr.replaceAll(Normalizer.normalize(expect, Form.NFD)));

    assertEquals("Hello World.", Regexr.CjkNormalizr.replaceAll("Hello World."));
    assertEquals("Hello World.", Regexr.CjkNormalizr.replaceAll("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));
    assertEquals("こんにちは 世界", Regexr.CjkNormalizr.replaceAll("こんにちは　世界"));
    assertEquals("コンニチハ 世界", Regexr.CjkNormalizr.replaceAll("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", Regexr.CjkNormalizr.replaceAll("コンニチハ　世界"));
    assertEquals("バーバパパ", Regexr.CjkNormalizr.replaceAll("バーバパパ"));
    assertEquals("バーバパパ", Regexr.CjkNormalizr.replaceAll("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", Regexr.CjkNormalizr.replaceAll("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", Regexr.CjkNormalizr.replaceAll("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", Regexr.CjkNormalizr.replaceAll("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", Regexr.CjkNormalizr.replaceAll("ヷヸヴヹヺ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Regexr.CjkNormalizr.replaceAll("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Regexr.CjkNormalizr.replaceAll("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("パ～やん", Regexr.CjkNormalizr.replaceAll("パ～やん"));
  }

  @Test
  void testConstants() {
    assertArrayEquals(new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr, Regexr.CjkNormalizr},
        new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr, Regexr.CjkNormalizr});
    assertArrayEquals(new Regexr[] {Regexr.CtrlRemovr, Regexr.SpaceNormalizr, Regexr.SpaceLintr, Regexr.LinefeedLintr, Regexr.Trimr, Regexr.CjkNormalizr},
        Streamr.stream(new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.CjkNormalizr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr}).sorted().toArray(Regexr[]::new));
  }

  @Test
  void testCtrlRemovr() {
    assertEquals(new RegexrStandard("[\\p{Cc}&&[^\\s\\x{001C}-\\x{001F}]]", ""), Regexr.CtrlRemovr);
    assertEquals(0, Regexr.CtrlRemovr.order());

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String ctrlsR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s -> Regexr.CtrlRemovr.pattern.matcher(s).matches()).collect(Collectors.joining());
    assertArrayEquals(ctrls.codePoints().toArray(), ctrlsR.codePoints().toArray());
    assertEquals(ctrls, ctrlsR);
    assertEquals("", Regexr.CtrlRemovr.replaceAll(ctrls));
    assertEquals("", Regexr.CtrlRemovr.replaceAll(ctrlsR));

    final String string = "PrettyfyMe.";
    String uglified = "";
    for (String ctrl : ctrls.split("")) {
      // @formatter:off
      uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrl + ctrl + ctrl + ctrl + ctrl));
      // @formatter:on
      assertTrue(Character.isISOControl(ctrl.codePointAt(0)));
      assertEquals(string, Regexr.CtrlRemovr.replaceAll(uglified));
    }
    uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrls));
    assertEquals(string, Regexr.CtrlRemovr.replaceAll(uglified));
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(ctrlsR));
    assertEquals(string, Regexr.CtrlRemovr.replaceAll(uglified));
  }

  @Test
  void testFind() {
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find(null));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find(""));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find("1"));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find("o ne"));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find("One"));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find("\t\t\to\tne\t\t\t"));
    assertEquals(new ArrayList<>(), (new Regexr("one", "壱", 1) {}).find("\t\t\to\nne\t\t\t"));
    assertEquals(Arrays.asList("one"), (new Regexr("one", "壱", 1) {}).find("one"));
    assertEquals(Arrays.asList("one"), (new Regexr("one", "壱", 1) {}).find("one two three"));
    assertEquals(Arrays.asList("one"), (new Regexr("one", "壱", 1) {}).find("three two one zero"));
    assertEquals(Arrays.asList("one"), (new Regexr("one", "壱", 1) {}).find("three\ntwo\none\nzero"));
    assertEquals(Arrays.asList("one", "one", "one", "one", "one"), (new Regexr("one", "壱", 1) {}).find("oneoneoneoneone"));
    assertEquals(Arrays.asList("one", "one", "one", "one"), (new Regexr("one", "壱", 1) {}).find("neoneoneoneoneo"));
    assertEquals(Arrays.asList("one"), (new Regexr("one", "壱", 1) {}).find("\t\t\tone\t\t\t"));
    assertEquals(Arrays.asList("o\nne"), (new Regexr("o\\s?n\\s?e", "壱", 1) {}).find("\t\t\to\nne\t\t\t"));
    assertEquals(Arrays.asList("o\nn\t\t\t\n\t\t\te"), (new Regexr("o\\s*n\\s*e", "壱", 1) {}).find("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));
    assertEquals(Arrays.asList("o", "n", "e"), (new Regexr("[one]", "壱", 1) {}).find("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));
  }

  @Test
  void testLinefeedLintr() {
    assertEquals(new RegexrRecursive("\\s+\\n|\\n\\s+", "\n"), Regexr.LinefeedLintr);
    assertEquals(1000, Regexr.LinefeedLintr.order());

    final String string = "P\nr\ne\nt\nt\ny\nf\ny\nM\ne\n.";
    String uglified = "";
    String space = " \n  \n\n";
    while (space.length() < 100000) {
      uglified = Arrays.stream(string.split("\n")).collect(Collectors.joining(space += space));
      assertEquals(string, Regexr.LinefeedLintr.replaceAll(uglified));
    }
  }

  @Test
  void testMatches() {
    assertFalse((new Regexr("one", "壱", 1) {}).matches(null));
    assertFalse((new Regexr("one", "壱", 1) {}).matches(""));
    assertFalse((new Regexr("one", "壱", 1) {}).matches("1"));
    assertFalse((new Regexr("one", "壱", 1) {}).matches("o ne"));
    assertFalse((new Regexr("one", "壱", 1) {}).matches("One"));
    assertFalse((new Regexr("one", "壱", 1) {}).matches("\t\t\to\tne\t\t\t"));
    assertFalse((new Regexr("one", "壱", 1) {}).matches("\t\t\to\nne\t\t\t"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("one"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("one two three"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("three two one zero"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("three\ntwo\none\nzero"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("oneoneoneoneone"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("neoneoneoneoneo"));
    assertTrue((new Regexr("one", "壱", 1) {}).matches("\t\t\tone\t\t\t"));
    assertTrue((new Regexr("o\\s?n\\s?e", "壱", 1) {}).matches("\t\t\to\nne\t\t\t"));
    assertTrue((new Regexr("o\\s*n\\s*e", "壱", 1) {}).matches("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));
  }

  @Test
  void testReplaceAll() {
    assertNull((new Regexr("one", "壱", 1) {}).replaceAll(null));
    assertEquals("", (new Regexr("one", "壱", 1) {}).replaceAll(""));
    assertEquals("1", (new Regexr("one", "壱", 1) {}).replaceAll("1"));
    assertEquals("o ne", (new Regexr("one", "壱", 1) {}).replaceAll("o ne"));
    assertEquals("One", (new Regexr("one", "壱", 1) {}).replaceAll("One"));
    assertEquals("壱", (new Regexr("[Oo]ne", "壱", 1) {}).replaceAll("One"));
    assertEquals("\t\t\to\tne\t\t\t", (new Regexr("one", "壱", 1) {}).replaceAll("\t\t\to\tne\t\t\t"));
    assertEquals("\t\t\to\nne\t\t\t", (new Regexr("one", "壱", 1) {}).replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("壱", (new Regexr("one", "壱", 1) {}).replaceAll("one"));
    assertEquals("壱 two three", (new Regexr("one", "壱", 1) {}).replaceAll("one two three"));
    assertEquals("three two 壱 zero", (new Regexr("one", "壱", 1) {}).replaceAll("three two one zero"));
    assertEquals("three\ntwo\n壱\nzero", (new Regexr("one", "壱", 1) {}).replaceAll("three\ntwo\none\nzero"));
    assertEquals("壱壱壱壱壱", (new Regexr("one", "壱", 1) {}).replaceAll("oneoneoneoneone"));
    assertEquals("ne壱壱壱壱o", (new Regexr("one", "壱", 1) {}).replaceAll("neoneoneoneoneo"));
    assertEquals("\t\t\t壱\t\t\t", (new Regexr("one", "壱", 1) {}).replaceAll("\t\t\tone\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", (new Regexr("o\\s?n\\s?e", "壱", 1) {}).replaceAll("\t\t\to\nne\t\t\t"));
    assertEquals("\t\t\t壱\t\t\t", (new Regexr("o\\s*n\\s*e", "壱", 1) {}).replaceAll("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"));
  }

  @Test
  void testSpaceLintr() {
    assertEquals(new RegexrRecursive("[\\p{javaWhitespace}&&[^\\n]]{2,}", "\u0020"), Regexr.SpaceLintr);
    assertEquals(100, Regexr.SpaceLintr.order());

    final String string = "S h r i n k m e .";
    String uglified = "";
    String space = " ";
    while (space.length() < 10000) {
      uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(space));
      assertEquals(string, Regexr.SpaceLintr.replaceAll(uglified));
      space += space;
    }
  }

  @Test
  void testSpaceNormalizr() {
    assertEquals(new RegexrStandard("([[\\p{javaWhitespace}\u00A0]&&[^\\n\u0020]]+)", "\u0020"), Regexr.SpaceNormalizr);
    assertEquals(10, Regexr.SpaceNormalizr.order());

    String spaces = IntStream.rangeClosed(0, 200_000).filter(codePoint -> Character.isWhitespace(codePoint) || codePoint == 0x00A0).filter(codePoint -> codePoint != 0x000A && codePoint != 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String spacesR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s -> Regexr.SpaceNormalizr.pattern().matcher(s).matches()).collect(Collectors.joining());
    assertArrayEquals(spaces.codePoints().toArray(), spacesR.codePoints().toArray());
    assertEquals(spaces, spacesR);
    assertEquals(spaces.replaceAll("([[\\p{javaWhitespace}\u00A0]&&[^\\n\u0020]]+)", " "), Regexr.SpaceNormalizr.replaceAll(spaces));
    assertEquals(spacesR.replaceAll("([[\\p{javaWhitespace}\u00A0]&&[^\\n\u0020]]+)", " "), Regexr.SpaceNormalizr.replaceAll(spacesR));

    final String string = "N o r m a l i z e m e .";
    String uglified = "";
    for (String space : spaces.split("")) {
      // @formatter:off
      uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(space + space + space + space + space));
      // @formatter:on
      assertTrue(Character.isWhitespace(space.codePointAt(0)) || "\u00A0".equals(space));
      assertEquals(Arrays.stream(string.split("\u0020")).collect(Collectors.joining("\u0020")), Regexr.SpaceNormalizr.replaceAll(uglified));
    }
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spaces));
    assertEquals(string, Regexr.SpaceNormalizr.replaceAll(uglified));
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spacesR));
    assertEquals(string, Regexr.SpaceNormalizr.replaceAll(uglified));
  }

  @Test
  void testTrimr() {
    assertEquals(new RegexrStandard("^[\\p{javaWhitespace}]+|[\\p{javaWhitespace}]+$", ""), Regexr.Trimr);
    assertEquals(10000, Regexr.Trimr.order());

    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespacesR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s -> Regexr.Trimr.pattern.matcher(s).matches()).collect(Collectors.joining());
    assertEquals(whitespaces, whitespacesR);
    assertArrayEquals(whitespaces.codePoints().toArray(), whitespacesR.codePoints().toArray());
    assertEquals("", Regexr.Trimr.replaceAll(whitespaces));
    assertEquals("", Regexr.Trimr.replaceAll(whitespacesR));

    final String string = "Trim me.";
    String uglified = "";
    for (String space : whitespacesR.split("")) {
    // @formatter:off
    uglified = new StringBuilder(string)
      .insert(0, space + space + space + space + space)
      .append(space + space + space + space + space)
      .toString();
    // @formatter:on
      assertTrue(Character.isWhitespace(space.codePointAt(0)));
      assertEquals(string, Regexr.Trimr.replaceAll(uglified));
    }
    do {
      uglified = new StringBuilder(uglified).insert(0, whitespacesR).append(whitespacesR).toString();
    } while (uglified.length() < 10000);
    assertEquals(string, Regexr.Trimr.replaceAll(uglified));
  }
}
