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

package jp.furplag.sandbox.tuple;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import jp.furplag.sandbox.stream.Streamr;

class TagTest {

  @Test
  void test() {
    assertEquals(Map.entry(1, "one"), Tag.entry(1, "one"));
    try {
      Tag.entry(null, "Null");
      fail("there must raise NullPointerException .");
    } catch (NullPointerException ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    try {
      Tag.entry(0, "Zero").setValue("zero");
      fail("there must raise UnsupportedOperationException .");
    } catch (UnsupportedOperationException ex) {
      assertTrue(ex instanceof UnsupportedOperationException);
    }
    final Map<Integer, String> ascend = Streamr.collect(Streamr.stream(Map.entry(1, "one"), Tag.entry(2, "two"), Tag.entry(3, "three")).sorted(Map.Entry.comparingByKey()), (e1, e2) -> e2, LinkedHashMap::new);
    assertArrayEquals(new Integer[] {1, 2, 3}, ascend.keySet().toArray(new Integer[] {}));
    assertArrayEquals(new String[] {"one", "two", "three"}, ascend.values().toArray(new String[] {}));

    assertArrayEquals(new String[] {"one", "three", "two"}, Streamr.stream(ascend.entrySet()).sorted(Comparator.comparing((Map.Entry::getValue))).map(Map.Entry::getValue).toArray(String[]::new));

    assertEquals(String.class, Tag.entry(4, "four").getValueType());
    assertTrue(Map.Entry.class.isAssignableFrom(Tag.entry(5, Map.entry(5, "five")).getValueType()));
  }
}
