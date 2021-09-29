package com.jayway.jsonpath.internal.function.json;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

public class First implements PathFunction {

  @Override
  public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
    Object result = null;
    if (parameters != null && !parameters.isEmpty()) {
      // log.info("parameters:{}", Parameter.toList(Object.class, ctx, parameters));
      if (ctx.configuration().jsonProvider().isArray(Parameter.toList(Object.class, ctx, parameters).get(0))) {
        result = JsonPath.read(Parameter.toList(Object.class, ctx, parameters).get(0), "$[0]");
      } else {
        result = Parameter.toList(Object.class, ctx, parameters).get(0);
      }
    } else {
      if (ctx.configuration().jsonProvider().isArray(model)) {
        result = JsonPath.read(model, "$[0]");
      } else {
        result = model;
      }
    }
    if (ctx.getPathList().isEmpty()) {
      return result;
    } else {
      return concat(ctx, model, result);
    }
  }

  private Object concat(EvaluationContext ctx, Object model, Object preResult) {
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
    if (preResult != null) {
      result.append(preResult);
    }
    return result.toString();
  }
}
