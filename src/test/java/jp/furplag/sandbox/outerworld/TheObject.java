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

package jp.furplag.sandbox.outerworld;

import java.util.List;

public class TheObject {

  private static final Boolean THE_BOOLEAN_STATIC;
  private static final Byte THE_BYTE_STATIC;
  private static final Character THE_CHARACTER_STATIC;
  private static final Double THE_DOUBLE_STATIC;
  private static final Float THE_FLOAT_STATIC;
  private static final Integer THE_INTEGER_STATIC;
  private static final Long THE_LONG_STATIC;
  private static final Short THE_SHORT_STATIC;
  static {
    THE_BOOLEAN_STATIC = null;
    THE_BYTE_STATIC = null;
    THE_CHARACTER_STATIC = null;
    THE_DOUBLE_STATIC = null;
    THE_FLOAT_STATIC = null;
    THE_INTEGER_STATIC = null;
    THE_LONG_STATIC = null;
    THE_SHORT_STATIC = null;
  }

  private final boolean theBoolean;
  private final byte theByte;
  private final char theChar;
  private final double theDouble;
  private final float theFloat;
  private final int theInt;
  private final long theLong;
  private final short theShort;
  private final String theString;

  private final List<?> list;

  public TheObject() {

    theBoolean = true;
    theByte = 1;
    theChar = '呪';
    theDouble = 12.3d;
    theFloat = 123.4f;
    theInt = (int) (theByte + theByte);
    theLong = (long) (theByte + theByte + theByte);
    theShort = (short) (theByte + theByte + theByte + theByte);
    theString = new String(new int[] {(int) theChar, (int) theChar}, 0, 2);
    list = null;
  }

  public static Boolean getTheBooleanStatic() {
    return THE_BOOLEAN_STATIC;
  }

  public static Byte getTheByteStatic() {
    return THE_BYTE_STATIC;
  }

  public static Character getTheCharacterStatic() {
    return THE_CHARACTER_STATIC;
  }

  public static Double getTheDoubleStatic() {
    return THE_DOUBLE_STATIC;
  }

  public static Float getTheFloatStatic() {
    return THE_FLOAT_STATIC;
  }

  public static Integer getTheIntegerStatic() {
    return THE_INTEGER_STATIC;
  }

  public static Long getTheLongStatic() {
    return THE_LONG_STATIC;
  }

  public static Short getTheShortStatic() {
    return THE_SHORT_STATIC;
  }

  public boolean isTheBoolean() {
    return theBoolean;
  }

  public byte getTheByte() {
    return theByte;
  }

  public char getTheChar() {
    return theChar;
  }

  public double getTheDouble() {
    return theDouble;
  }

  public float getTheFloat() {
    return theFloat;
  }

  public int getTheInt() {
    return theInt;
  }

  public long getTheLong() {
    return theLong;
  }

  public short getTheShort() {
    return theShort;
  }

  public String getTheString() {
    return theString;
  }

  public List<?> getList() {
    return list;
  }
}
