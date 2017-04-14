/**
 * 
 */
package com.helger.jcodemodel;

import javax.annotation.Nonnull;

/**
 * @author dsmullen
 *
 */
public interface IJMLExpression extends IJExpression
{
  
  /**
   * Implication (p implies q; p ==> q)
   * Equivalent to !p || q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] ==> [right]</code>.
   */
  @Nonnull
  IJExpression implies (@Nonnull IJExpression right);

  /**
   * Implication (p implies q; p ==> q)
   * Equivalent to !p || q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] ==> [right]</code>.
   */
  @Nonnull
  IJExpression implies (boolean right);
  
  /**
   * Reverse Implication (q implies p; p <== q)
   * Equivalent to p || !q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] <== [right]</code>.
   */
  @Nonnull
  IJExpression rimplies (@Nonnull IJExpression right);

  /**
   * Reverse Implication (q implies p; p <== q)
   * Equivalent to p || !q.
   * 
   * @param right
   *        expression for q
   * @return <code>[this] <== [right]</code>.
   */
  @Nonnull
  IJExpression rimplies (boolean right);

  /**
   * Equivalence
   * Equivalent to == except only takes boolean operands and has lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <==> <em>right</em></code>
   */
  @Nonnull
  IJExpression equiv (@Nonnull IJExpression right);

  /**
   * Equivalence
   * Equivalent to == except only takes boolean operands and has lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <==> <em>right</em></code>
   */
  @Nonnull
  IJExpression equiv (boolean right);

  /**
   * Inequivalence
   * Equivalent to != except only takes boolean operands and has lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <=!=> <em>right</em></code>
   */
  @Nonnull
  IJExpression inequiv (@Nonnull IJExpression right);

  /**
   * Inequivalence
   * Equivalent to != except only takes boolean operands and has lower precedence.
   *
   * @param right
   *        expression to compare to
   * @return <code><em>expr</em> <=!=> <em>right</em></code>
   */
  @Nonnull
  IJExpression inequiv (boolean right);

}
