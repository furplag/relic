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
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class KanizrTest {

  @Test
  void testHiraganize() {
    assertNull(Kanizr.hiraganize(null));
    assertEquals("", Kanizr.hiraganize(""));
    assertEquals("Hello World.", Kanizr.hiraganize("Hello World."));
    assertEquals("こんにちは 世界", Kanizr.hiraganize("こんにちは　世界"));
    assertEquals("こんにちは 世界", Kanizr.hiraganize("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("こんにちは 世界", Kanizr.hiraganize("コンニチハ　世界"));
    assertEquals("ばーばぱぱ", Kanizr.hiraganize("バーバパパ"));
    assertEquals("ばーばぱぱ", Kanizr.hiraganize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("ばーばぱぱ", Kanizr.hiraganize("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Kanizr.hiraganize("ヷヸヴヹヺ"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Kanizr.hiraganize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("わ゛ゐ゛ゔゑ゛を゛", Kanizr.hiraganize("ヷヸヴヹヺ"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Kanizr.hiraganize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Kanizr.hiraganize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Kanizr.hiraganize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Kanizr.hiraganize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"));
    assertEquals("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛", Kanizr.hiraganize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("ぱ～やん", Kanizr.hiraganize("パ～やん"));
    assertEquals("あれん・ぎんずばーぐ", Kanizr.hiraganize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"));
  }

  @Test
  void testKatakanize() {
    assertNull(Kanizr.katakanize(null));
    assertEquals("", Kanizr.katakanize(""));
    assertEquals("Hello World.", Kanizr.katakanize("Hello World."));
    assertEquals("コンニチハ 世界", Kanizr.katakanize("こんにちは　世界"));
    assertEquals("コンニチハ 世界", Kanizr.katakanize("ｺﾝﾆﾁﾊ　世界"));
    assertEquals("コンニチハ 世界", Kanizr.katakanize("コンニチハ　世界"));
    assertEquals("バーバパパ", Kanizr.katakanize("バーバパパ"));
    assertEquals("バーバパパ", Kanizr.katakanize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"));
    assertEquals("バーバパパ", Kanizr.katakanize("ハ゛ーハ゛ハ゜ハ゜"));
    assertEquals("ヷヸヴヹヺ", Kanizr.katakanize("ヷヸヴヹヺ"));
    assertEquals("ヷヸヴヹヺ", Kanizr.katakanize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertEquals("ヷヸヴヹヺ", Kanizr.katakanize("ヷヸヴヹヺ"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Kanizr.katakanize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Kanizr.katakanize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Kanizr.katakanize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Kanizr.katakanize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"));
    assertEquals("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛", Kanizr.katakanize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"));
    assertEquals("パ～ヤン", Kanizr.katakanize("パ～やん"));
    assertEquals("アレン・ギンズバーグ", Kanizr.katakanize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"));
  }
}
