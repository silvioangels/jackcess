/*
Copyright (c) 2017 James Ahlborn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.healthmarketscience.jackcess.impl.expr;

import java.math.BigDecimal;

import com.healthmarketscience.jackcess.expr.EvalContext;
import com.healthmarketscience.jackcess.expr.Function;
import com.healthmarketscience.jackcess.expr.Value;
import static com.healthmarketscience.jackcess.impl.expr.DefaultFunctions.*;

/**
 *
 * @author James Ahlborn
 */
public class DefaultNumberFunctions 
{

  private DefaultNumberFunctions() {}

  static void init() {
    // dummy method to ensure this class is loaded
  }
  
  public static final Function ABS = registerFunc(new Func1NullIsNull("Abs") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
        // FIXME, cast to number, date as date?
      }
      if(type.isIntegral()) {
        return BuiltinOperators.toValue(Math.abs(param1.getAsLong()));
      }
      if(type.getPreferredFPType() == Value.Type.DOUBLE) {
        return BuiltinOperators.toValue(Math.abs(param1.getAsDouble()));
      }
      return BuiltinOperators.toValue(param1.getAsBigDecimal().abs());
    }
  });

  public static final Function ATAN = registerFunc(new Func1("Atan") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
      }
      return BuiltinOperators.toValue(Math.atan(param1.getAsDouble()));
    }
  });

  public static final Function COS = registerFunc(new Func1("Cos") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
      }
      return BuiltinOperators.toValue(Math.cos(param1.getAsDouble()));
    }
  });

  public static final Function EXP = registerFunc(new Func1("Exp") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
      }
      return BuiltinOperators.toValue(Math.exp(param1.getAsDouble()));
    }
  });

  public static final Function FIX = registerFunc(new Func1NullIsNull("Fix") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
      }
      if(type.isIntegral()) {
        return param1;
      }
      return BuiltinOperators.toValue(param1.getAsDouble().longValue());
    }
  });

  public static final Function INT = registerFunc(new Func1NullIsNull("Int") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      Value.Type type = param1.getType();
      if(!type.isNumeric()) {
        // FIXME how to handle text/date?
      }
      if(type.isIntegral()) {
        return param1;
      }
      return BuiltinOperators.toValue((long)Math.floor(param1.getAsDouble()));
    }
  });

  public static final Function RND = registerFunc(new FuncVar("Rnd", 0, 1) {
    @Override
    public boolean isPure() {
      return false;
    }
    @Override
    protected Value evalVar(EvalContext ctx, Value[] params) {
      Long seed = ((params.length > 0) ? params[0].getAsLong() : null);
      return BuiltinOperators.toValue(ctx.getRandom(seed).nextFloat());
    }
  });

  public static final Function ROUND = registerFunc(new FuncVar("Round", 1, 2) {
    @Override
    protected Value evalVar(EvalContext ctx, Value[] params) {
      Value param1 = params[0];
      if(param1.isNull()) {
        return null;
      }
      if(param1.getType().isIntegral()) {
        return param1;
      }
      int scale = 0;
      if(params.length > 1) {
        scale = params[1].getAsLong().intValue();
      }
      BigDecimal bd = param1.getAsBigDecimal().setScale(scale, DEFAULT_ROUND_MODE);
      return BuiltinOperators.toValue(bd);
    }
  });

  public static final Function SGN = registerFunc(new Func1NullIsNull("Sgn") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      int signum = 0;
      if(param1.getType().isIntegral()) {
        long lv = param1.getAsLong();
        signum = ((lv > 0) ? 1 : ((lv < 0) ? -1 : 0));
      } else {
        signum = param1.getAsBigDecimal().signum();
      }
      return BuiltinOperators.toValue(signum);
    }
  });

  public static final Function SQR = registerFunc(new Func1("Sqr") {
    @Override
    protected Value eval1(EvalContext ctx, Value param1) {
      double dv = param1.getAsDouble();
      if(dv < 0.0d) {
        throw new IllegalStateException("Invalid value '" + dv + "'");
      }
      return BuiltinOperators.toValue(Math.sqrt(dv));
    }
  });

  // public static final Function Val = registerFunc(new Func1("Val") {
  //   @Override
  //   protected Value eval1(EvalContext ctx, Value param1) {
  //     // FIXME, maybe leverage ExpressionTokenizer.maybeParseNumberLiteral (note, leading - or + is valid, exponent form is valid)
  //   }
  // });


}
