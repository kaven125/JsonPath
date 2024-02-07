package com.jayway.jsonpath.internal.function.text;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

/**
 * String function concat - simple takes a list of arguments and/or an array and concatenates them
 * together to form a single string
 *
 */
public class Concatenate implements PathFunction {
  @Override
  public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
    StringBuilder result = new StringBuilder();
    if (ctx.configuration().jsonProvider().isArray(model)) {
      Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
      for (Object obj : objects) {
        if (obj instanceof String) {
          result.append(obj.toString());
        }
      }
    } else if (!(model instanceof Map)) {
      result.append(model);
    }
    if (parameters != null) {
      for (Object value : Parameter.toList(Object.class, ctx, parameters)) {
        result.append(value);
      }
    }
    return result.toString();
  }
}
