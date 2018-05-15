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

package jp.furplag.sandbox.reflect.unsafe;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.security.Permission;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jp.furplag.sandbox.outerworld.Duplicate;
import jp.furplag.sandbox.outerworld.TheEntity;
import jp.furplag.sandbox.outerworld.TheObject;
import jp.furplag.sandbox.outerworld.nested.Overriden;

public class TheUnsafeTest {

  private static class TestSecurityManager extends SecurityManager {

    @Override
    public void checkPackageAccess(String pkg) {
      if ("sun.misc".equals(pkg)) {
        throw new SecurityException();
      }
      super.checkPackageAccess(pkg);
    }

    @Override
    public void checkPermission(Permission perm) {
      return;
    }
  }

  private static void secure() {
    System.setSecurityManager(new TestSecurityManager());
  }

  private static void insecure() {
    System.setSecurityManager(null);
  }

  @Test
  public void test() {
    try {
      secure();
      assertTrue(TheUnsafe.getInstance() instanceof TheUnsafe);
    } catch (ExceptionInInitializerError e) {
      fail(e.getMessage());
    } finally {
      insecure();
    }
    assertTrue(TheUnsafe.getInstance() instanceof TheUnsafe);
    assertEquals(TheUnsafe.getInstance(), TheUnsafe.getInstance());
  }

  @Test
  public void testGet() throws ReflectiveOperationException {
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, null));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, null));
    assertEquals((Object) null, TheUnsafe.getInstance().get(new TheEntity(), null));

    Field PUBLIC_STATIC_FINAL_STRING = TheEntity.class.getDeclaredField("PUBLIC_STATIC_FINAL_STRING");
    Field PACKAGE_STATIC_FINAL_LONG = TheEntity.class.getDeclaredField("PACKAGE_STATIC_FINAL_LONG");
    Field PROTECTED_STATIC_FINAL_DOUBLE = TheEntity.class.getDeclaredField("PROTECTED_STATIC_FINAL_DOUBLE");
    Field PRIVATE_STATIC_FINAL_STRING = TheEntity.class.getDeclaredField("PRIVATE_STATIC_FINAL_STRING");
    Field NULL = TheEntity.class.getDeclaredField("NULL");
    Field thePrimitive = TheEntity.class.getDeclaredField("thePrimitive");
    Field thePrimitiveOfPackage = TheEntity.class.getDeclaredField("thePrimitiveOfPackage");
    Field thePrimitiveOfClan = TheEntity.class.getDeclaredField("thePrimitiveOfClan");
    Field message = TheEntity.class.getDeclaredField("message");

    assertEquals((Object) null, TheUnsafe.getInstance().get(null, PUBLIC_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, PACKAGE_STATIC_FINAL_LONG));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, NULL));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, thePrimitive));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, thePrimitiveOfPackage));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, thePrimitiveOfClan));
    assertEquals((Object) null, TheUnsafe.getInstance().get(null, message));

    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(TheEntity.class, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(TheEntity.class, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(TheEntity.class, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(TheEntity.class, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, NULL));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, thePrimitive));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, thePrimitiveOfPackage));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, thePrimitiveOfClan));
    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, message));

    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(Duplicate.class, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(Duplicate.class, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(Duplicate.class, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(Duplicate.class, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Duplicate.class, NULL));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Duplicate.class, thePrimitive));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Duplicate.class, thePrimitiveOfPackage));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Duplicate.class, thePrimitiveOfClan));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Duplicate.class, message));

    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(Overriden.class, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(Overriden.class, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(Overriden.class, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(Overriden.class, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Overriden.class, NULL));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Overriden.class, thePrimitive));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Overriden.class, thePrimitiveOfPackage));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Overriden.class, thePrimitiveOfClan));
    assertEquals((Object) null, TheUnsafe.getInstance().get(Overriden.class, message));

    TheEntity theEntity = new TheEntity();
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(theEntity, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(theEntity, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(theEntity, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(theEntity, NULL));
    assertEquals(0, TheUnsafe.getInstance().get(theEntity, thePrimitive));
    assertEquals(false, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfPackage));
    assertEquals(0f, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfClan));
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, message));

    theEntity = new Duplicate();
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(theEntity, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(theEntity, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(theEntity, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(theEntity, NULL));
    assertEquals(0, TheUnsafe.getInstance().get(theEntity, thePrimitive));
    assertEquals(false, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfPackage));
    assertEquals(0f, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfClan));
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, message));

    theEntity = new Overriden();
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, PUBLIC_STATIC_FINAL_STRING));
    assertEquals(123456L, TheUnsafe.getInstance().get(theEntity, PACKAGE_STATIC_FINAL_LONG));
    assertEquals(.123456d, TheUnsafe.getInstance().get(theEntity, PROTECTED_STATIC_FINAL_DOUBLE));
    assertEquals("a static String.", TheUnsafe.getInstance().get(theEntity, PRIVATE_STATIC_FINAL_STRING));
    assertEquals((Object) null, TheUnsafe.getInstance().get(theEntity, NULL));
    assertEquals(0, TheUnsafe.getInstance().get(theEntity, thePrimitive));
    assertEquals(false, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfPackage));
    assertEquals(0f, TheUnsafe.getInstance().get(theEntity, thePrimitiveOfClan));
    assertEquals(TheEntity.PUBLIC_STATIC_FINAL_STRING, TheUnsafe.getInstance().get(theEntity, message));

    Field thePrimitiveOfPackageOverriden = Overriden.class.getDeclaredField("thePrimitiveOfPackage");
    assertEquals(true, TheUnsafe.getInstance().get(new Overriden(), thePrimitiveOfPackageOverriden));
  }

  @Test
  public void testSetStatic() throws ReflectiveOperationException {

    Field PUBLIC_STATIC_FINAL_STRING = TheEntity.class.getDeclaredField("PUBLIC_STATIC_FINAL_STRING");
    Field PACKAGE_STATIC_FINAL_LONG = TheEntity.class.getDeclaredField("PACKAGE_STATIC_FINAL_LONG");
    Field PROTECTED_STATIC_FINAL_DOUBLE = TheEntity.class.getDeclaredField("PROTECTED_STATIC_FINAL_DOUBLE");
    Field PRIVATE_STATIC_FINAL_STRING = TheEntity.class.getDeclaredField("PRIVATE_STATIC_FINAL_STRING");
    Field NULL = TheEntity.class.getDeclaredField("NULL");

    try {
      TheUnsafe.getInstance().set(null, null, null);
      TheUnsafe.getInstance().set(null, null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      TheUnsafe.getInstance().set(TheEntity.class, null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      TheUnsafe.getInstance().set(null, PUBLIC_STATIC_FINAL_STRING, TheEntity.PUBLIC_STATIC_FINAL_STRING);
    } catch (Exception e) {
      fail("do not raise up any of Exceptions.");
    }

    assertEquals("a static String.", TheEntity.PUBLIC_STATIC_FINAL_STRING);
    TheUnsafe.getInstance().set(TheEntity.class, PUBLIC_STATIC_FINAL_STRING, "modified.");
    assertEquals("modified.", TheEntity.PUBLIC_STATIC_FINAL_STRING);
    TheUnsafe.getInstance().set(TheEntity.class, PUBLIC_STATIC_FINAL_STRING, "a static String.");

    assertEquals(123456L, TheUnsafe.getInstance().get(TheEntity.class, PACKAGE_STATIC_FINAL_LONG));
    TheUnsafe.getInstance().set(TheEntity.class, PACKAGE_STATIC_FINAL_LONG, 123456789L);
    assertEquals(123456789L, TheUnsafe.getInstance().get(TheEntity.class, PACKAGE_STATIC_FINAL_LONG));
    TheUnsafe.getInstance().set(TheEntity.class, PACKAGE_STATIC_FINAL_LONG, 123456L);

    assertEquals(.123456d, TheUnsafe.getInstance().get(TheEntity.class, PROTECTED_STATIC_FINAL_DOUBLE));
    TheUnsafe.getInstance().set(TheEntity.class, PROTECTED_STATIC_FINAL_DOUBLE, 123.456d);
    assertEquals(123.456d, TheUnsafe.getInstance().get(TheEntity.class, PROTECTED_STATIC_FINAL_DOUBLE));
    TheUnsafe.getInstance().set(TheEntity.class, PROTECTED_STATIC_FINAL_DOUBLE, .123456d);

    assertEquals("a static String.", TheUnsafe.getInstance().get(TheEntity.class, PRIVATE_STATIC_FINAL_STRING));
    TheUnsafe.getInstance().set(TheEntity.class, PRIVATE_STATIC_FINAL_STRING, "Modified.");
    assertEquals("Modified.", TheUnsafe.getInstance().get(TheEntity.class, PRIVATE_STATIC_FINAL_STRING));
    TheUnsafe.getInstance().set(TheEntity.class, PRIVATE_STATIC_FINAL_STRING, "a static String.");

    assertEquals((Object) null, TheUnsafe.getInstance().get(TheEntity.class, NULL));
    TheUnsafe.getInstance().set(TheEntity.class, NULL, new TheEntity());
    assertEquals(new TheEntity().thePrimitive, ((TheEntity) TheUnsafe.getInstance().get(TheEntity.class, NULL)).thePrimitive);
    TheUnsafe.getInstance().set(TheEntity.class, NULL, null);
  }

  @Test
  public void testSet() throws ReflectiveOperationException {

    Field thePrimitive = TheEntity.class.getDeclaredField("thePrimitive");
    Field thePrimitiveOfPackage = TheEntity.class.getDeclaredField("thePrimitiveOfPackage");
    Field thePrimitiveOfClan = TheEntity.class.getDeclaredField("thePrimitiveOfClan");
    Field message = TheEntity.class.getDeclaredField("message");
    Field thePrimitiveOfPackageOverriden = Overriden.class.getDeclaredField("thePrimitiveOfPackage");

    try {
      TheUnsafe.getInstance().set(null, null, null);
      TheUnsafe.getInstance().set(null, null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      TheUnsafe.getInstance().set(TheEntity.class, null, TheEntity.PUBLIC_STATIC_FINAL_STRING);
      TheUnsafe.getInstance().set(null, message, TheEntity.PUBLIC_STATIC_FINAL_STRING);
    } catch (Exception e) {
      fail("do not raise up any of Exceptions.");
    }

    TheEntity overriden = new Overriden();

    assertEquals(0, TheUnsafe.getInstance().get(overriden, thePrimitive));
    TheUnsafe.getInstance().set(overriden, thePrimitive, 1);
    assertEquals(1, TheUnsafe.getInstance().get(overriden, thePrimitive));
    TheUnsafe.getInstance().set(overriden, thePrimitive, 0);
    assertEquals(0, TheUnsafe.getInstance().get(overriden, thePrimitive));
    TheUnsafe.getInstance().set(overriden, thePrimitive, Integer.valueOf(1));
    assertEquals(1, TheUnsafe.getInstance().get(overriden, thePrimitive));
    TheUnsafe.getInstance().set(overriden, thePrimitive, 0);

    assertEquals(false, TheUnsafe.getInstance().get(overriden, thePrimitiveOfPackage));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfPackage, true);
    assertEquals(true, TheUnsafe.getInstance().get(overriden, thePrimitiveOfPackage));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfPackage, false);

    assertEquals(true, TheUnsafe.getInstance().get(overriden, thePrimitiveOfPackageOverriden));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfPackageOverriden, false);
    assertEquals(false, TheUnsafe.getInstance().get(overriden, thePrimitiveOfPackageOverriden));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfPackageOverriden, true);

    assertEquals(0f, TheUnsafe.getInstance().get(overriden, thePrimitiveOfClan));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfClan, 1);
    assertEquals(1f, TheUnsafe.getInstance().get(overriden, thePrimitiveOfClan));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfClan, 0f);

    assertEquals(0f, TheUnsafe.getInstance().get(overriden, thePrimitiveOfClan));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfClan, 1);
    assertEquals(1f, TheUnsafe.getInstance().get(overriden, thePrimitiveOfClan));
    TheUnsafe.getInstance().set(overriden, thePrimitiveOfClan, 0f);
  }

  @Test
  public void testSetToBoolean() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theBoolean = TheObject.class.getDeclaredField("theBoolean");
    theBoolean.setAccessible(true);
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheByte());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheChar());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheDouble());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheFloat());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheInt());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheLong());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheShort());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, theObject.getTheString());
    assertTrue(theObject.isTheBoolean());
    TheUnsafe.getInstance().set(theObject, theBoolean, !theObject.isTheBoolean());
    assertFalse(theObject.isTheBoolean());
  }

  @Test
  public void testSetToByte() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theByte = TheObject.class.getDeclaredField("theByte");

    TheUnsafe.getInstance().set(theObject, theByte, theObject.isTheBoolean());
    assertEquals((byte) 1, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, Byte.valueOf("10"));
    assertEquals((byte) 10, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheChar());
    assertEquals((byte) 10, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheDouble());
    assertEquals((byte) 12, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheFloat());
    assertEquals((byte) 123, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheInt());
    assertEquals((byte) 2, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheLong());
    assertEquals((byte) 3, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheShort());
    assertEquals((byte) 4, theObject.getTheByte());
    TheUnsafe.getInstance().set(theObject, theByte, theObject.getTheString());
    assertEquals((byte) 4, theObject.getTheByte());
  }


  @Test
  public void testSetToChar() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theChar = TheObject.class.getDeclaredField("theChar");

    TheUnsafe.getInstance().set(theObject, theChar, theObject.isTheBoolean());
    assertEquals('呪', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheByte());
    assertEquals('呪', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, '祝');
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheDouble());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheFloat());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheInt());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheLong());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheShort());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, theObject.getTheString());
    assertEquals('祝', theObject.getTheChar());
    TheUnsafe.getInstance().set(theObject, theChar, "呪".codePoints().mapToObj(cp -> new String(new int[] {cp}, 0, 1)).map(s -> s.charAt(0)).findAny().orElse('祝'));
    assertEquals('呪', theObject.getTheChar());
  }

  @Test
  public void testSetToDouble() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theDouble = TheObject.class.getDeclaredField("theDouble");

    TheUnsafe.getInstance().set(theObject, theDouble, theObject.isTheBoolean());
    assertEquals(12.3d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheByte());
    assertEquals(1d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, '祝');
    assertEquals(1d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheDouble());
    assertEquals(1d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, Double.valueOf("12.3"));
    assertEquals(12.3d, theObject.getTheDouble(), 0);

    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheFloat());
    assertEquals(123.4d, theObject.getTheDouble(), 1E-2);

    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheInt());
    assertEquals(2d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheLong());
    assertEquals(3d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheShort());
    assertEquals(4d, theObject.getTheDouble(), 0);
    TheUnsafe.getInstance().set(theObject, theDouble, theObject.getTheString());
    assertEquals(4d, theObject.getTheDouble(), 0);
  }

  @Test
  public void testSetToFloat() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theFloat = TheObject.class.getDeclaredField("theFloat");

    TheUnsafe.getInstance().set(theObject, theFloat, theObject.isTheBoolean());
    assertEquals(123.4f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheByte());
    assertEquals(1f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, '祝');
    assertEquals(1f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheDouble());
    assertEquals(12.3f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheFloat());
    assertEquals(12.3f, theObject.getTheFloat(), 0);

    TheUnsafe.getInstance().set(theObject, theFloat, Float.valueOf(".123456"));
    assertEquals(.123456f, theObject.getTheFloat(), 0);

    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheInt());
    assertEquals(2f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheLong());
    assertEquals(3f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheShort());
    assertEquals(4f, theObject.getTheFloat(), 0);
    TheUnsafe.getInstance().set(theObject, theFloat, theObject.getTheString());
    assertEquals(4f, theObject.getTheFloat(), 0);
  }

  @Test
  public void testSetToInt() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theInt = TheObject.class.getDeclaredField("theInt");

    TheUnsafe.getInstance().set(theObject, theInt, theObject.isTheBoolean());
    assertEquals(2, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheByte());
    assertEquals(1, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, '祝');
    assertEquals(1, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheDouble());
    assertEquals(12, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheFloat());
    assertEquals(123, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheInt());
    assertEquals(123, theObject.getTheInt());

    TheUnsafe.getInstance().set(theObject, theInt, 2);
    assertEquals(2, theObject.getTheInt());

    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheLong());
    assertEquals(3, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheShort());
    assertEquals(4, theObject.getTheInt());
    TheUnsafe.getInstance().set(theObject, theInt, theObject.getTheString());
    assertEquals(4, theObject.getTheInt());
  }

  @Test
  public void testSetToLong() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theLong = TheObject.class.getDeclaredField("theLong");

    TheUnsafe.getInstance().set(theObject, theLong, theObject.isTheBoolean());
    assertEquals(3L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheByte());
    assertEquals(1L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, '祝');
    assertEquals(1L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheDouble());
    assertEquals(12L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheFloat());
    assertEquals(123L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheInt());
    assertEquals(2L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheLong());
    assertEquals(2L, theObject.getTheLong());

    TheUnsafe.getInstance().set(theObject, theLong, 3L);
    assertEquals(3L, theObject.getTheLong());

    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheShort());
    assertEquals(4L, theObject.getTheLong());
    TheUnsafe.getInstance().set(theObject, theLong, theObject.getTheString());
    assertEquals(4L, theObject.getTheLong());
  }

  @Test
  public void testSetToShort() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theShort = TheObject.class.getDeclaredField("theShort");

    TheUnsafe.getInstance().set(theObject, theShort, theObject.isTheBoolean());
    assertEquals((short) 4, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheByte());
    assertEquals((short) 1, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, '祝');
    assertEquals((short) 1, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheDouble());
    assertEquals((short) 12, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheFloat());
    assertEquals((short) 123, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheInt());
    assertEquals((short) 2, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheLong());
    assertEquals((short) 3, theObject.getTheShort());
    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheShort());
    assertEquals((short) 3, theObject.getTheShort());

    TheUnsafe.getInstance().set(theObject, theShort, Short.valueOf("4"));
    assertEquals((short) 4, theObject.getTheShort());

    TheUnsafe.getInstance().set(theObject, theShort, theObject.getTheString());
    assertEquals((short) 4, theObject.getTheShort());
  }

  @Test
  public void testSetToString() throws ReflectiveOperationException {
    TheObject theObject = new TheObject();
    Field theString = TheObject.class.getDeclaredField("theString");

    TheUnsafe.getInstance().set(theObject, theString, theObject.isTheBoolean());
    assertEquals("true", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheByte());
    assertEquals("1", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheChar());
    assertEquals("呪", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheDouble());
    assertEquals("12.3", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheFloat());
    assertEquals("123.4", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheInt());
    assertEquals("2", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheLong());
    assertEquals("3", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, theObject.getTheShort());
    assertEquals("4", theObject.getTheString());
    TheUnsafe.getInstance().set(theObject, theString, "祝");
    assertEquals("祝", theObject.getTheString());

    Field list = TheObject.class.getDeclaredField("list");
    TheUnsafe.getInstance().set(theObject, list, Arrays.asList(1, 2, 3));
    assertEquals(Arrays.asList(1, 2, 3), theObject.getList());
    TheUnsafe.getInstance().set(theObject, list, null);
    assertEquals((List<?>) null, theObject.getList());
  }

}
