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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.Character.UnicodeBlock;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.furplag.sandbox.reflect.SavageReflection;

public class CommonizrTest {

  @Test
  @SuppressWarnings("unchecked")
  void test() {
    try {
      new Commonizr.Normalizr.Kanizr(0, null) {};
      fail("must raise NullPointerException .");
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertTrue(((Set<Integer>) SavageReflection.get(new Commonizr.Normalizr.Kanizr(0, UnicodeBlock.BASIC_LATIN) {}, "exclusions")).isEmpty());
    assertTrue(((Set<Integer>) SavageReflection.get(new Commonizr.Normalizr.Kanizr(0, UnicodeBlock.BASIC_LATIN, (int[]) null) {}, "exclusions")).isEmpty());
  }

  @Test
  void testDenormalizeCjk() {
    assertNull(Commonizr.denormalizeCjk(null));
    assertEquals("", Commonizr.denormalizeCjk(""));
    assertEquals("", Commonizr.denormalizeCjk("   \r\n   \r\n   \r\n"));
    assertEquals("諸行無常", Commonizr.denormalizeCjk("諸行無常"));
    assertEquals("南　無　阿　弥　陀　仏", Commonizr.denormalizeCjk("南\t無\t阿\t弥\t陀\t仏"));
    assertEquals("Ｈｅｌｌｏ　Ｗｏｒｌｄ．", Commonizr.denormalizeCjk("Hello World."));
    assertEquals("Ｈｅｌｌｏ　Ｗｏｒｌｄ．", Commonizr.denormalizeCjk("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));

    @SuppressWarnings("unchecked")
    Map<Integer, Integer> exclusives = (Map<Integer, Integer>) SavageReflection.get(((Commonizr.Normalizr) SavageReflection.get(Commonizr.class, "cjkNormalizr")), "exclusives");
    String latins = Regexr.newString(IntStream.rangeClosed(0, 0x00FF).toArray());
    String expect = Commonizr.optimize(Regexr.newString(latins.codePoints().map(codePoint->exclusives.getOrDefault(codePoint, codePoint + (UnicodeBlock.BASIC_LATIN.equals(UnicodeBlock.of(codePoint)) && !Character.isWhitespace(codePoint) && !Character.isISOControl(codePoint) ? 65248 : 0))).toArray())).replaceAll(" ", "　");
    assertEquals(expect, Commonizr.denormalizeCjk(latins));
  }

  @Test
  void testHiraganize() {
    assertNull(Commonizr.hiraganize(null));
    assertEquals("", Commonizr.hiraganize(""));
    assertEquals("Hello World.", Commonizr.hiraganize("Hello World."));
    assertEquals("こんにちは 世界", Commonizr.hiraganize("こんにちは　世界"));
    assertEquals("こんにちは 世界", Commonizr.hiraganize("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("こんにちは 世界", Commonizr.hiraganize("コンニチハ　世界"));
    assertEquals("ばーばぱぱ", Commonizr.hiraganize("バーバパパ"));
    assertEquals("ばーばぱぱ", Commonizr.hiraganize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("ばーばぱぱ", Commonizr.hiraganize("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Commonizr.hiraganize("ヷヸヴヹヺ"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Commonizr.hiraganize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Commonizr.hiraganize("ヷヸヴヹヺ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.hiraganize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.hiraganize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.hiraganize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.hiraganize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.hiraganize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("ぱ～やん", Commonizr.hiraganize("パ～やん"));
    assertEquals("あれん・ぎんずばーぐ", Commonizr.hiraganize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"));
  }

  @Test
  void testKatakanize() {
    assertNull(Commonizr.katakanize(null));
    assertEquals("", Commonizr.katakanize(""));
    assertEquals("Hello World.", Commonizr.katakanize("Hello World."));
    assertEquals("コンニチハ 世界", Commonizr.katakanize("こんにちは　世界"));
    assertEquals("コンニチハ 世界", Commonizr.katakanize("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", Commonizr.katakanize("コンニチハ　世界"));
    assertEquals("バーバパパ", Commonizr.katakanize("バーバパパ"));
    assertEquals("バーバパパ", Commonizr.katakanize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", Commonizr.katakanize("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", Commonizr.katakanize("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", Commonizr.katakanize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", Commonizr.katakanize("ヷヸヴヹヺ"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.katakanize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.katakanize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.katakanize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.katakanize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.katakanize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("パ～ヤン", Commonizr.katakanize("パ～やん"));
    assertEquals("アレン・ギンズバーグ", Commonizr.katakanize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"));
  }

  @Test
  void testNormalizeCjk() {
    assertNull(Commonizr.normalizeCjk(null));
    assertEquals("", Commonizr.normalizeCjk(""));
    assertEquals("Hello World.", Commonizr.normalizeCjk("Hello World."));
    assertEquals("Hello World.", Commonizr.normalizeCjk("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));
    assertEquals("こんにちは 世界", Commonizr.normalizeCjk("こんにちは　世界"));
    assertEquals("コンニチハ 世界", Commonizr.normalizeCjk("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", Commonizr.normalizeCjk("コンニチハ　世界"));
    assertEquals("バーバパパ", Commonizr.normalizeCjk("バーバパパ"));
    assertEquals("バーバパパ", Commonizr.normalizeCjk("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", Commonizr.normalizeCjk("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", Commonizr.normalizeCjk("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", Commonizr.normalizeCjk("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", Commonizr.normalizeCjk("ヷヸヴヹヺ"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Commonizr.normalizeCjk("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.normalizeCjk("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Commonizr.normalizeCjk("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("パ～やん", Commonizr.normalizeCjk("パ～やん"));
  }

  @Test
  void testOptimize() {
    assertNull(Commonizr.optimize(null));
    assertEquals("", Commonizr.optimize(""));
    assertEquals("theString.", Commonizr.optimize("theString."));
    assertEquals("the String.", Commonizr.optimize("the String."));
    assertEquals("the String.", Commonizr.optimize("the      String."));
    assertEquals("the String .", Commonizr.optimize("the String ."));
    assertEquals("the String.", Commonizr.optimize("the String. "));
    assertEquals("the String.", Commonizr.optimize(" the String."));
    assertEquals("the String.", Commonizr.optimize(" the String. "));
    assertEquals("the String.", Commonizr.optimize("   the String.   "));
    assertEquals("the String.", Commonizr.optimize(" \u0010 the String. \u0002 "));
    assertEquals("the String.", Commonizr.optimize("\n\n\nthe String.\n\n\n"));
    assertEquals("the String.", Commonizr.optimize("\nthe String.\n"));
    assertEquals("the String.", Commonizr.optimize("\t\t\tthe String.\t\t\t"));
    assertEquals("the String.", Commonizr.optimize("\tthe String.\t"));
    assertEquals("the String.", Commonizr.optimize(" 　 the String. 　 "));
    assertEquals("the String.\nthe String.\nthe String.", Commonizr.optimize(" 　 the String. 　 \n 　 the String. 　 \n 　 the String. 　 \n"));
  }

  @Test
  void testTrim() {
    assertNull(Commonizr.trim(null));
    assertEquals("", Commonizr.trim(""));
    assertEquals("", Commonizr.trim("\n"));
    assertEquals("trimmed", Commonizr.trim("  \ntrimmed\n  "));
    assertEquals("trimmed", Commonizr.trim("  \n  trimmed  \n  "));
    assertEquals("trimmed  trimmed", Commonizr.trim("  \n  trimmed  trimmed  \n  "));
    assertEquals("trimmed  \n  trimmed  \n  trimmed", Commonizr.trim("  \n  trimmed  \n  trimmed  \n  trimmed  \n  "));
  }

  @Test
  void testTrimMultiline() {
    assertNull(Commonizr.trimMultiline(null));
    assertEquals("", Commonizr.trimMultiline(""));
    assertEquals("", Commonizr.trimMultiline("\n"));
    assertEquals("trimmed", Commonizr.trimMultiline("  \ntrimmed\n  "));
    assertEquals("trimmed", Commonizr.trimMultiline("  \n  trimmed  \n  "));
    assertEquals("trimmed  trimmed", Commonizr.trimMultiline("  \n  trimmed  trimmed  \n  "));
    assertEquals("trimmed\ntrimmed\ntrimmed", Commonizr.trimMultiline("  \n  trimmed  \n  trimmed  \n  trimmed  \n  "));
  }
}
