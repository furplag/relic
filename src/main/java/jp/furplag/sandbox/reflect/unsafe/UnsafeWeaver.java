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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jp.furplag.function.ThrowableBiFunction;

/**
 * handle class member whether protected ( or invisible ) using &quot;sun.misc.Unsafe&quot; .
 *
 * @author furplag
 */
interface UnsafeWeaver {

  /** prefix of method handler . */
  public static enum Prefix /* @formatter:off */ { /** getter . */get, /** setter . */put }/* @formatter:on */

  /**
   * returns formatted name for unsafe field accessor of &quot;getter&quot; and &quot;setter&quot; .
   *
   * @param type type of field
   * @param prefix {@link Prefix}
   * @return formatted name for unsafe field accessor of &quot;getter&quot; and &quot;setter&quot;
   */
  static String getFormattedMethodName(final Class<?> type, final Prefix prefix) {
    return String.join("", Objects.toString(prefix, ""), StringUtils.capitalize(Objects.toString(type, "").replaceAll("^.*\\.", "").toLowerCase(Locale.ROOT)));
  }

  /**
   * returns unsafe accessor using {@link MethodHandle} .
   *
   * @param sunMiscUnsafe the class of &quot;sun.misc.Unsafe&quot;
   * @param methodName the name of method to get
   * @param methodType {@link MethodType}
   * @return {@link MethodHandle}
   */
  static MethodHandle getMethodHandle(final Class<?> sunMiscUnsafe, final String methodName, final MethodType methodType) {
    return ThrowableBiFunction.orNull(methodName, methodType, (x, y) -> MethodHandles.privateLookupIn(sunMiscUnsafe, MethodHandles.lookup()).findVirtual(sunMiscUnsafe, x, y));
  }

  /**
   * returns {@link MethodType} for unsafe field accessor of &quot;getter&quot; and &quot;setter&quot; .
   *
   * @param type type of field
   * @param prefix {@link Prefix}, get or put
   * @return {@link MethodType}
   */
  static MethodType getMethodType(final Class<?> type, final Prefix prefix) {
    return Prefix.get.equals(prefix) ? newGetMethodType(type) : newPutMethodType(type);
  }

  /**
   * returns {@link MethodType} for unsafe field accessor of &quot;getter&quot; .
   *
   * @param type type of field
   * @return {@link MethodType}
   */
  private static MethodType newGetMethodType(final Class<?> type) {
    return MethodType.methodType(type, Object.class, long.class);
  }

  /**
   * returns {@link MethodType} for unsafe field accessor of &quot;setter&quot; .
   *
   * @param type type of field
   * @return {@link MethodType}
   */
  private static MethodType newPutMethodType(final Class<?> type) {
    return MethodType.methodType(void.class, Object.class, long.class, type);
  }

}
