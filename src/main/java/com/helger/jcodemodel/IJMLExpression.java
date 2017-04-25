/**
 * 
 */
package com.helger.jcodemodel;

import javax.annotation.Nonnull;

/**
 * A JML expression.
 * <p>
 * {@link IJMLExpression} defines a series of composer methods, which returns a
 * complicated expression (by often taking other {@link IJExpression}s as
 * parameters. For example, you can build "5+2" by
 * <tt>JExpr.lit(5).add(JExpr.lit(2))</tt>
 * <p>
 * JML expressions support JExpressions, with some additional operators that are
 * based on those supported by JExpressions.
 * <p>
 * However, the new operators (e.g. implication, equivalence) only support
 * JExpressions, which means that nesting JML operators is not supported.
 */
public interface IJMLExpression
{
  public void generate(@Nonnull final JFormatter f);
}
