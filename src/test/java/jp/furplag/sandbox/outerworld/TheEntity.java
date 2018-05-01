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

import java.util.Objects;

public class TheEntity {

  public static final String PUBLIC_STATIC_FINAL_STRING;

  static final long PACKAGE_STATIC_FINAL_LONG;

  protected static final double PROTECTED_STATIC_FINAL_DOUBLE;

  private static final String PRIVATE_STATIC_FINAL_STRING;

  private static final Object NULL = null;

  static {
    PUBLIC_STATIC_FINAL_STRING = "a static String.";
    PACKAGE_STATIC_FINAL_LONG = 123456L;
    PROTECTED_STATIC_FINAL_DOUBLE = .123456;
    PRIVATE_STATIC_FINAL_STRING = "a static String.";
  }

  public int thePrimitive;

  boolean thePrimitiveOfPackage;

  protected float thePrimitiveOfClan;

  private final String message;

  public TheEntity() {
    this.message = PRIVATE_STATIC_FINAL_STRING;
  }

  public TheEntity(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return new StringBuilder("TheEntity {")
        .append("PUBLIC_STATIC_FINAL_STRING=")
        .append(PUBLIC_STATIC_FINAL_STRING)
        .append(",PACKAGE_STATIC_FINAL_LONG=")
        .append(Objects.toString(PACKAGE_STATIC_FINAL_LONG))
        .append(",PROTECTED_STATIC_FINAL_DOUBLE=")
        .append(Objects.toString(PROTECTED_STATIC_FINAL_DOUBLE))
        .append(",PRIVATE_STATIC_FINAL_STRING=")
        .append(Objects.toString(PRIVATE_STATIC_FINAL_STRING))
        .append(",NULL=")
        .append(Objects.toString(NULL))
        .append(",thePrimitive=")
        .append(Objects.toString(thePrimitive))
        .append(",thePrimitiveOfPackage=")
        .append(Objects.toString(thePrimitiveOfPackage))
        .append(",thePrimitiveOfClan=")
        .append(Objects.toString(thePrimitiveOfClan))
        .append(",message=")
        .append(Objects.toString(message))
        .append("}")
        .toString();
  }
}
