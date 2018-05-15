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

package jp.furplag.sandbox.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.junit.Test;

import jp.furplag.sandbox.outerworld.Duplicate;
import jp.furplag.sandbox.outerworld.TheEntity;
import jp.furplag.sandbox.outerworld.TheExtendedObject;
import jp.furplag.sandbox.outerworld.TheObject;
import jp.furplag.sandbox.outerworld.nested.Overriden;
import jp.furplag.sandbox.outerworld.nested.Wrappered;
import jp.furplag.sandbox.stream.Streamr;

public class SavageReflectionTest {

  @Test
  public void test() {
    assertFalse(SavageReflection.exclusions.test(null, Reflections.getField(TheEntity.class, "thePrimitive")));
    assertTrue(SavageReflection.exclusions.test(Streamr.stream(Reflections.getFields(TheEntity.class)).map(Field::getName).collect(Collectors.toSet()), Reflections.getField(TheEntity.class, "thePrimitive")));
  }

  @Test
  public void testGet() {
    assertNull(SavageReflection.get(null, (String) null));
    assertNull(SavageReflection.get(TheEntity.class, (String) null));
    assertNull(SavageReflection.get(TheEntity.class, (Field) null));
    assertNull(SavageReflection.get(TheEntity.class, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertNull(SavageReflection.get(TheEntity.class, Reflections.getField(Overriden.class, "theBoolean")));
    assertNull(SavageReflection.get(null, "PUBLIC_STATIC_FINAL_STRING"));
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, SavageReflection.get(TheEntity.class, "PUBLIC_STATIC_FINAL_STRING"));
    assertEquals("a static String.", SavageReflection.get(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING"));
    assertEquals("a static String.", SavageReflection.get(Duplicate.class, "PRIVATE_STATIC_FINAL_STRING"));

    assertNull(SavageReflection.get(TheObject.class, "notExists"));
    assertNull(SavageReflection.get(TheObject.class, "theBoolean"));
    assertNull(SavageReflection.get(TheObject.class, "theByte"));
    assertNull(SavageReflection.get(TheObject.class, "theChar"));
    assertNull(SavageReflection.get(TheObject.class, "theDouble"));
    assertNull(SavageReflection.get(TheObject.class, "theFloat"));
    assertNull(SavageReflection.get(TheObject.class, "theInt"));
    assertNull(SavageReflection.get(TheObject.class, "theLong"));
    assertNull(SavageReflection.get(TheObject.class, "theShort"));

    TheObject theObject = new TheObject();
    assertEquals(theObject.isTheBoolean(), SavageReflection.get(theObject, "theBoolean"));
    assertEquals(theObject.getTheByte(), SavageReflection.get(theObject, "theByte"));
    assertEquals(theObject.getTheChar(), SavageReflection.get(theObject, "theChar"));
    assertEquals(theObject.getTheDouble(), SavageReflection.get(theObject, "theDouble"));
    assertEquals(theObject.getTheFloat(), SavageReflection.get(theObject, "theFloat"));
    assertEquals(theObject.getTheInt(), SavageReflection.get(theObject, "theInt"));
    assertEquals(theObject.getTheLong(), SavageReflection.get(theObject, "theLong"));
    assertEquals(theObject.getTheShort(), SavageReflection.get(theObject, "theShort"));

    theObject = new TheExtendedObject();
    assertEquals(TheExtendedObject.getTheBooleanStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_BOOLEAN_STATIC")));
    assertEquals(TheExtendedObject.getTheByteStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_BYTE_STATIC")));
    assertEquals(TheExtendedObject.getTheCharacterStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_CHARACTER_STATIC")));
    assertEquals(TheExtendedObject.getTheDoubleStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_DOUBLE_STATIC")));
    assertEquals(TheExtendedObject.getTheFloatStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_FLOAT_STATIC")));
    assertEquals(TheExtendedObject.getTheIntegerStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_INTEGER_STATIC")));
    assertEquals(TheExtendedObject.getTheLongStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_LONG_STATIC")));
    assertEquals(TheExtendedObject.getTheShortStatic(), SavageReflection.get(TheExtendedObject.class, Reflections.getField(theObject, "THE_SHORT_STATIC")));

    assertEquals(TheExtendedObject.getTheBooleanStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_BOOLEAN_STATIC")));
    assertEquals(TheExtendedObject.getTheByteStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_BYTE_STATIC")));
    assertEquals(TheExtendedObject.getTheCharacterStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_CHARACTER_STATIC")));
    assertEquals(TheExtendedObject.getTheDoubleStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_DOUBLE_STATIC")));
    assertEquals(TheExtendedObject.getTheFloatStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_FLOAT_STATIC")));
    assertEquals(TheExtendedObject.getTheIntegerStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_INTEGER_STATIC")));
    assertEquals(TheExtendedObject.getTheLongStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_LONG_STATIC")));
    assertEquals(TheExtendedObject.getTheShortStatic(), SavageReflection.get(theObject, Reflections.getField(theObject, "THE_SHORT_STATIC")));

    assertEquals(theObject.isTheBoolean(), SavageReflection.get(theObject, Reflections.getField(theObject, "theBoolean")));
    assertEquals(theObject.getTheByte(), SavageReflection.get(theObject, Reflections.getField(theObject, "theByte")));
    assertEquals(theObject.getTheChar(), SavageReflection.get(theObject, Reflections.getField(theObject, "theChar")));
    assertEquals(theObject.getTheDouble(), SavageReflection.get(theObject, Reflections.getField(theObject, "theDouble")));
    assertEquals(theObject.getTheFloat(), SavageReflection.get(theObject, Reflections.getField(theObject, "theFloat")));
    assertEquals(theObject.getTheInt(), SavageReflection.get(theObject, Reflections.getField(theObject, "theInt")));
    assertEquals(theObject.getTheLong(), SavageReflection.get(theObject, Reflections.getField(theObject, "theLong")));
    assertEquals(theObject.getTheShort(), SavageReflection.get(theObject, Reflections.getField(theObject, "theShort")));
  }

  @Test
  public void testSetStatic() throws ReflectiveOperationException {
    assertFalse(SavageReflection.set(null, (String) null, null));
    assertFalse(SavageReflection.set(null, (Field) null, null));
    assertFalse(SavageReflection.set(null, (String) null, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertFalse(SavageReflection.set(null, (Field) null, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertFalse(SavageReflection.set(TheEntity.class, (String) null, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertFalse(SavageReflection.set(TheEntity.class, (Field) null, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertFalse(SavageReflection.set(TheEntity.class, TheEntity.PUBLIC_STATIC_FINAL_STRING, TheEntity.PUBLIC_STATIC_FINAL_STRING));
    assertFalse(SavageReflection.set(TheEntity.class, "NOT_EXISTS", TheEntity.PUBLIC_STATIC_FINAL_STRING));

    assertTrue(SavageReflection.set(new TheEntity(), "PUBLIC_STATIC_FINAL_STRING", 123));
    assertEquals("123", TheEntity.PUBLIC_STATIC_FINAL_STRING);
    assertTrue(SavageReflection.set(new TheEntity(), "PUBLIC_STATIC_FINAL_STRING", "a static String."));
    assertEquals("a static String.", TheEntity.PUBLIC_STATIC_FINAL_STRING);

    assertTrue(SavageReflection.set(new TheEntity(), "PUBLIC_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING + " But I modified."));
    assertEquals("a static String. But I modified.", TheEntity.PUBLIC_STATIC_FINAL_STRING);
    assertTrue(SavageReflection.set(new TheEntity(), "PUBLIC_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING.replace(" But I modified.", "")));
    assertEquals("a static String.", TheEntity.PUBLIC_STATIC_FINAL_STRING);

    assertTrue(SavageReflection.set(new TheEntity(), "PACKAGE_STATIC_FINAL_LONG", 123L));
    assertEquals(123L, SavageReflection.get(new TheEntity(), "PACKAGE_STATIC_FINAL_LONG"));
    assertTrue(SavageReflection.set(new TheEntity(), "PACKAGE_STATIC_FINAL_LONG", 123456L));

    assertTrue(SavageReflection.set(new TheEntity(), "PROTECTED_STATIC_FINAL_DOUBLE", 123.456));
    assertEquals(123.456, SavageReflection.get(new TheEntity(), "PROTECTED_STATIC_FINAL_DOUBLE"));
    assertTrue(SavageReflection.set(new TheEntity(), "PROTECTED_STATIC_FINAL_DOUBLE", .123456));

    assertTrue(SavageReflection.set(new TheEntity(), "PRIVATE_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING + " But I modified."));
    assertEquals("a static String. But I modified.", SavageReflection.get(new TheEntity(), "PRIVATE_STATIC_FINAL_STRING"));
    assertTrue(SavageReflection.set(new TheEntity(), "PRIVATE_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING.replace(" But I modified.", "")));

    assertTrue(SavageReflection.set(TheEntity.class, "PUBLIC_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING + " But I modified."));
    assertEquals("a static String. But I modified.", TheEntity.PUBLIC_STATIC_FINAL_STRING);
    assertTrue(SavageReflection.set(TheEntity.class, "PUBLIC_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING.replace(" But I modified.", "")));
    assertEquals("a static String.", TheEntity.PUBLIC_STATIC_FINAL_STRING);

    assertTrue(SavageReflection.set(TheEntity.class, "PACKAGE_STATIC_FINAL_LONG", 123L));
    assertEquals(123L, SavageReflection.get(TheEntity.class, "PACKAGE_STATIC_FINAL_LONG"));
    assertTrue(SavageReflection.set(TheEntity.class, "PACKAGE_STATIC_FINAL_LONG", 123456L));

    assertTrue(SavageReflection.set(TheEntity.class, "PROTECTED_STATIC_FINAL_DOUBLE", 123.456));
    assertEquals(123.456, SavageReflection.get(TheEntity.class, "PROTECTED_STATIC_FINAL_DOUBLE"));
    assertTrue(SavageReflection.set(TheEntity.class, "PROTECTED_STATIC_FINAL_DOUBLE", .123456));

    assertTrue(SavageReflection.set(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING + " But I modified."));
    assertEquals("a static String. But I modified.", SavageReflection.get(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING"));
    assertTrue(SavageReflection.set(TheEntity.class, "PRIVATE_STATIC_FINAL_STRING", TheEntity.PUBLIC_STATIC_FINAL_STRING.replace(" But I modified.", "")));
  }

  @Test
  public void testSet() throws ReflectiveOperationException {

    Field thePrimitive = TheEntity.class.getDeclaredField("thePrimitive");
    Field thePrimitiveOfPackage = TheEntity.class.getDeclaredField("thePrimitiveOfPackage");
    Field thePrimitiveOfClan = TheEntity.class.getDeclaredField("thePrimitiveOfClan");
    Field message = TheEntity.class.getDeclaredField("message");
    Field thePrimitiveOfPackageOverriden = Overriden.class.getDeclaredField("thePrimitiveOfPackage");

    try {
      SavageReflection.set(null, (Field) null, null);
      SavageReflection.set(null, (String) null, null);
      SavageReflection.set(null, (Field) null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      SavageReflection.set(null, (String) null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      SavageReflection.set(TheEntity.class, (Field) null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      SavageReflection.set(TheEntity.class, (String) null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      SavageReflection.set(null, message, TheEntity.PUBLIC_STATIC_FINAL_STRING);
    } catch (Exception e) {
      fail("do not raise up any of Exceptions.");
    }

    TheEntity overriden = new Overriden();

    assertEquals(0, SavageReflection.get(overriden, thePrimitive));
    SavageReflection.set(overriden, thePrimitive, 1);
    assertEquals(1, SavageReflection.get(overriden, thePrimitive));
    SavageReflection.set(overriden, thePrimitive, 0);
    assertEquals(0, SavageReflection.get(overriden, thePrimitive));
    SavageReflection.set(overriden, thePrimitive, Integer.valueOf(1));
    assertEquals(1, SavageReflection.get(overriden, thePrimitive));
    SavageReflection.set(overriden, thePrimitive, 0);

    assertEquals(false, SavageReflection.get(overriden, thePrimitiveOfPackage));
    SavageReflection.set(overriden, thePrimitiveOfPackage, true);
    assertEquals(true, SavageReflection.get(overriden, thePrimitiveOfPackage));
    SavageReflection.set(overriden, thePrimitiveOfPackage, false);

    assertEquals(true, SavageReflection.get(overriden, thePrimitiveOfPackageOverriden));
    SavageReflection.set(overriden, thePrimitiveOfPackageOverriden, false);
    assertEquals(false, SavageReflection.get(overriden, thePrimitiveOfPackageOverriden));
    SavageReflection.set(overriden, thePrimitiveOfPackageOverriden, true);

    assertEquals(0f, SavageReflection.get(overriden, thePrimitiveOfClan));
    SavageReflection.set(overriden, thePrimitiveOfClan, 1);
    assertEquals(1f, SavageReflection.get(overriden, thePrimitiveOfClan));
    SavageReflection.set(overriden, thePrimitiveOfClan, 0f);

    assertEquals(0f, SavageReflection.get(overriden, thePrimitiveOfClan));
    SavageReflection.set(overriden, thePrimitiveOfClan, 1);
    assertEquals(1f, SavageReflection.get(overriden, thePrimitiveOfClan));
    SavageReflection.set(overriden, thePrimitiveOfClan, 0f);
  }

  @Test
  public void testRead() {
    SavageReflection.read(new Overriden()).entrySet().stream().forEach(e -> {
      assertEquals(e.getValue(), SavageReflection.read(new Overriden()).get(e.getKey()));
      assertEquals((Object) null, SavageReflection.read(new Overriden(), e.getKey()).get(e.getKey()));
      assertNotEquals(e.getValue(), SavageReflection.read(new Wrappered()).get(e.getKey()));
    });
    assertEquals(new LinkedHashMap<>(), SavageReflection.read(null));
    assertEquals(new LinkedHashMap<>(), SavageReflection.read(Overriden.class));
    assertEquals(SavageReflection.read(new Overriden()), SavageReflection.read(new Overriden(), (String) null));
    assertEquals(SavageReflection.read(new Overriden()), SavageReflection.read(new Overriden(), (String[]) null));
    assertEquals(SavageReflection.read(new Overriden()), SavageReflection.read(new Overriden(), new String[] { null }));
    assertEquals(SavageReflection.read(new Overriden()), SavageReflection.read(new Overriden(), new String[] { null, null }));
    assertEquals(SavageReflection.read(new Overriden()), SavageReflection.read(new Overriden(), "notExists"));
    assertEquals(false, SavageReflection.read(new Overriden(), "message").containsKey("message"));

    assertEquals("a static String.", SavageReflection.read(new TheEntity()).get("message"));
    assertEquals("string", SavageReflection.read(new TheEntity("string")).get("message"));

    assertTrue(SavageReflection.read(new TheEntity(null)).containsKey("message"));
    assertEquals((String) null, SavageReflection.read(new TheEntity(null)).get("message"));
  }
}
