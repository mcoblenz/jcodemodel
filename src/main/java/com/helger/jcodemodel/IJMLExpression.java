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
public interface IJMLExpression extends IJExpression
{

  /**
   * Implication (p implies q; p ==> q) Equivalent to !p || q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] ==> [right]</code>.
   */
  @Nonnull
  IJExpression implies (@Nonnull IJExpression right);

  /**
   * Implication (p implies q; p ==> q) Equivalent to !p || q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] ==> [right]</code>.
   */
  @Nonnull
  IJExpression implies (boolean right);

  /**
   * Reverse Implication (q implies p; p <== q) Equivalent to p || !q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] <== [right]</code>.
   */
  @Nonnull
  IJExpression rimplies (@Nonnull IJExpression right);

  /**
   * Reverse Implication (q implies p; p <== q) Equivalent to p || !q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] <== [right]</code>.
   */
  @Nonnull
  IJExpression rimplies (boolean right);

  /**
   * Equivalence Equivalent to == except only takes boolean operands and has
   * lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <==> <em>right</em></code>
   */
  @Nonnull
  IJExpression equiv (@Nonnull IJExpression right);

  /**
   * Equivalence Equivalent to == except only takes boolean operands and has
   * lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <==> <em>right</em></code>
   */
  @Nonnull
  IJExpression equiv (boolean right);

  /**
   * Inequivalence Equivalent to != except only takes boolean operands and has
   * lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <=!=> <em>right</em></code>
   */
  @Nonnull
  IJExpression inequiv (@Nonnull IJExpression right);

  /**
   * Inequivalence Equivalent to != except only takes boolean operands and has
   * lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <=!=> <em>right</em></code>
   */
  @Nonnull
  IJExpression inequiv (boolean right);

}
