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
package jp.furplag.sandbox.text.normalize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.Character.UnicodeBlock;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.text.optimize.Optimizr;
import jp.furplag.sandbox.text.regex.RegexrOrigin;

public class CjkNormalizrTest {

  @Test
  void test() throws SecurityException, ReflectiveOperationException {
    Constructor<?> c = CjkNormalizr.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertTrue(c.newInstance() instanceof CjkNormalizr);
  }

  @Test
  void testNormalize() {
    assertNull(CjkNormalizr.normalize(null));
    assertEquals("", CjkNormalizr.normalize(""));
    assertEquals("Hello World.", CjkNormalizr.normalize("Hello World."));
    assertEquals("Hello World.", CjkNormalizr.normalize("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));
    assertEquals("こんにちは 世界", CjkNormalizr.normalize("こんにちは　世界"));
    assertEquals("コンニチハ 世界", CjkNormalizr.normalize("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", CjkNormalizr.normalize("コンニチハ　世界"));
    assertEquals("バーバパパ", CjkNormalizr.normalize("バーバパパ"));
    assertEquals("バーバパパ", CjkNormalizr.normalize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", CjkNormalizr.normalize("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", CjkNormalizr.normalize("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", CjkNormalizr.normalize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", CjkNormalizr.normalize("ヷヸヴヹヺ"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", CjkNormalizr.normalize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", CjkNormalizr.normalize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", CjkNormalizr.normalize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("パ～やん", CjkNormalizr.normalize("パ～やん"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testDenormalize() throws SecurityException, ReflectiveOperationException {
    assertNull(CjkNormalizr.denormalize(null));
    assertEquals("", CjkNormalizr.denormalize(""));
    assertEquals("", CjkNormalizr.denormalize("   \r\n   \r\n   \r\n"));
    assertEquals("諸行無常", CjkNormalizr.denormalize("諸行無常"));
    assertEquals("南　無　阿　弥　陀　仏", CjkNormalizr.denormalize("南\t無\t阿\t弥\t陀\t仏"));
    assertEquals("Ｈｅｌｌｏ　Ｗｏｒｌｄ．", CjkNormalizr.denormalize("Hello World."));
    assertEquals("Ｈｅｌｌｏ　Ｗｏｒｌｄ．", CjkNormalizr.denormalize("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));

    Map<Integer, Integer> exclusives = (Map<Integer, Integer>) SavageReflection.get(CjkNormalizr.class, "exclusives");
    String latins = RegexrOrigin.newString(IntStream.rangeClosed(0, 0x00FF).toArray());
    String expect = Optimizr.optimize(RegexrOrigin.newString(latins.codePoints().map(codePoint->exclusives.getOrDefault(codePoint, codePoint + (UnicodeBlock.BASIC_LATIN.equals(UnicodeBlock.of(codePoint)) && !Character.isWhitespace(codePoint) && !Character.isISOControl(codePoint) ? 65248 : 0))).toArray())).replaceAll(" ", "　");
    assertEquals(expect, CjkNormalizr.denormalize(latins));
  }

  @Test
  void testIsNormalized() {
    assertTrue(CjkNormalizr.isNormalized(null));
    assertTrue(CjkNormalizr.isNormalized(""));
    assertTrue(CjkNormalizr.isNormalized("theString."));
    assertTrue(CjkNormalizr.isNormalized("the String."));
    assertTrue(CjkNormalizr.isNormalized("the String ."));
    assertFalse(CjkNormalizr.isNormalized("the String. "));
    assertFalse(CjkNormalizr.isNormalized(" the String."));
    assertFalse(CjkNormalizr.isNormalized(" the String. "));
    assertTrue(CjkNormalizr.isNormalized(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n"))));
    assertFalse(CjkNormalizr.isNormalized(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n\n"))));
    assertTrue(CjkNormalizr.isNormalized("Hello World."));
    assertFalse(CjkNormalizr.isNormalized("Hello　World."));
    assertFalse(CjkNormalizr.isNormalized("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"));
    assertFalse(CjkNormalizr.isNormalized("こんにちは　世界"));
    assertTrue(CjkNormalizr.isNormalized("こんにちは 世界"));
    assertFalse(CjkNormalizr.isNormalized("ｺﾝﾆﾁﾊ　世界"));
    assertFalse(CjkNormalizr.isNormalized("コンニチハ　世界"));
    assertTrue(CjkNormalizr.isNormalized("コンニチハ 世界"));
    assertTrue(CjkNormalizr.isNormalized("バーバパパ"));
    assertFalse(CjkNormalizr.isNormalized("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertFalse(CjkNormalizr.isNormalized("ハ゛ーハ゛ハ゜ハ゜"));
    assertTrue(CjkNormalizr.isNormalized("ヷヸヴヹヺ"));
    assertFalse(CjkNormalizr.isNormalized("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertFalse(CjkNormalizr.isNormalized("ヷヸヴヹヺ"));
    assertFalse(CjkNormalizr.isNormalized("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertTrue(CjkNormalizr.isNormalized("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertFalse(CjkNormalizr.isNormalized("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertTrue(CjkNormalizr.isNormalized("パ～やん"));
    assertTrue(CjkNormalizr.isNormalized("パ~やん"));
  }
}
