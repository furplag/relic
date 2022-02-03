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
package jp.furplag.sandbox.outerworld;

public class TheExtendedObject extends TheObject {
  private static final Boolean THE_BOOLEAN_STATIC = true;
  private static final Byte THE_BYTE_STATIC = 1;
  private static final Character THE_CHARACTER_STATIC = '呪';
  private static final Double THE_DOUBLE_STATIC = 12.3d;
  private static final Float THE_FLOAT_STATIC = 123.4f;
  private static final Integer THE_INTEGER_STATIC = (int) (THE_BYTE_STATIC + THE_BYTE_STATIC);
  private static final Long THE_LONG_STATIC = (long) (THE_BYTE_STATIC + THE_BYTE_STATIC + THE_BYTE_STATIC);
  private static final Short THE_SHORT_STATIC = (short) (THE_BYTE_STATIC + THE_BYTE_STATIC + THE_BYTE_STATIC + THE_BYTE_STATIC);

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
}
