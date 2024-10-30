/*
 * MIT License
 *
 * Copyright (c) 2023 BlvckBytes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.blvckbytes.bbconfigmapper;

import me.blvckbytes.gpeee.interpreter.IEvaluationEnvironment;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ScalarType<T> {

  public static final ScalarType<Integer> INT = new ScalarType<>(int.class, (i, e) -> (int) e.getValueInterpreter().asLong(i));
  public static final ScalarType<Long> LONG = new ScalarType<>(long.class, (i, e) -> e.getValueInterpreter().asLong(i));
  public static final ScalarType<Double> DOUBLE = new ScalarType<>(double.class, (i, e) -> e.getValueInterpreter().asDouble(i));
  public static final ScalarType<Boolean> BOOLEAN = new ScalarType<>(boolean.class, (i, e) -> e.getValueInterpreter().asBoolean(i));
  public static final ScalarType<String> STRING = new ScalarType<>(String.class, (i, e) -> e.getValueInterpreter().asString(i));
  public static final ScalarType<Component> COMPONENT = new ScalarType<>(Component.class, (i, e) -> e.getValueInterpreter().asComponent(i));
  public static final ScalarType<String> STRING_PRESERVE_NULLS = new ScalarType<>(String.class, (i, e) -> {
    if (i == null)
      return null;

    return e.getValueInterpreter().asString(i);
  });

  private final Class<?> type;
  private final BiFunction<@Nullable Object, IEvaluationEnvironment, Object> interpreter;

  private static final Map<Class<?>, ScalarType<?>> lookupTable;

  static {
    lookupTable = new HashMap<>();
    lookupTable.put(int.class, INT);
    lookupTable.put(Integer.class, INT);
    lookupTable.put(long.class, LONG);
    lookupTable.put(Long.class, LONG);
    lookupTable.put(double.class, DOUBLE);
    lookupTable.put(Double.class, DOUBLE);
    lookupTable.put(boolean.class, BOOLEAN);
    lookupTable.put(Boolean.class, BOOLEAN);
    lookupTable.put(String.class, STRING);
    lookupTable.put(Component.class, COMPONENT);
  }

  private ScalarType(Class<?> type, BiFunction<@Nullable Object, IEvaluationEnvironment, Object> interpreter) {
    this.type = type;
    this.interpreter = interpreter;
  }

  public Class<?> getType() {
    return type;
  }

  public BiFunction<Object, IEvaluationEnvironment, Object> getInterpreter() {
    return interpreter;
  }

  public static @Nullable ScalarType<?> fromClass(Class<?> c) {
    return lookupTable.get(c);
  }
}
