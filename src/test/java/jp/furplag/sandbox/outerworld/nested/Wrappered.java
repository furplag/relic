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
package jp.furplag.sandbox.outerworld.nested;

import jp.furplag.sandbox.outerworld.TheObject;

public class Wrappered extends TheObject {

  private final Boolean theBoolean;
  private final Byte theByte;
  private final Character theChar;
  private final Double theDouble;
  private final Float theFloat;
  private final Integer theInt;
  private final Long theLong;
  private final Short theShort;

  public Wrappered() {
    super();
    theBoolean = !super.isTheBoolean();
    theByte = 1;
    theChar = '呪';
    theDouble = 12.3d;
    theFloat = 123.4f;
    theInt = (int) (theByte + theByte);
    theLong = (long) (theByte + theByte + theByte);
    theShort = (short) (theByte + theByte + theByte + theByte);
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
}
