/**
 * 
 */
package com.helger.jcodemodel;

import javax.annotation.Nonnull;

/**
 * Provides default implementations for {@link IJMLExpression}. JML operators
 * (e.g. implication, equivalence) only support JExpressions, which means that
 * nesting JML operators is not supported.
 * <p>
 * JML expressions do not support real value types (e.g. double, float),
 * therefore methods for operators with real valued operands are not
 * implemented.
 */
public class JMLExpressionSimple implements IJMLExpression
{ 
  public static final JMLExpressionSimple NULL = new JMLExpressionSimple(JExpr._null());
  
  IJExpression expr;
  
  public JMLExpressionSimple (@Nonnull IJExpression e)
  {
    expr = e;
  }

  public void generate (@Nonnull JFormatter f)
  {
    expr.generate (f);
  }

}
