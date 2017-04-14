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
public abstract class AbstractJMLExpressionImpl implements IJMLExpression
{

  protected AbstractJMLExpressionImpl ()
  {}

  // from JOp
  @Nonnull
  public final JOpUnary minus ()
  {
    return JOp.minus (this);
  }

  /**
   * Logical 'not' <tt>'!x'</tt>.
   */
  @Nonnull
  public final IJExpression not ()
  {
    return JOp.not (this);
  }

  @Nonnull
  public final JOpUnary complement ()
  {
    return JOp.complement (this);
  }

  @Nonnull
  public final JOpUnaryTight incr ()
  {
    return JOp.postincr (this);
  }

  @Nonnull
  public final JOpUnaryTight preincr ()
  {
    return JOp.preincr (this);
  }

  @Nonnull
  public final JOpUnaryTight decr ()
  {
    return JOp.postdecr (this);
  }

  @Nonnull
  public final JOpUnaryTight predecr ()
  {
    return JOp.predecr (this);
  }

  @Nonnull
  public final JOpBinary plus (@Nonnull final IJExpression right)
  {
    return JOp.plus (this, right);
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary plus (final double right)
  {
    throw new UnsupportedOperationException ();
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary plus (final float right)
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public final JOpBinary plus (final int right)
  {
    return plus (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary plus (final long right)
  {
    return plus (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary plus (@Nonnull final String right)
  {
    return plus (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary minus (@Nonnull final IJExpression right)
  {
    return JOp.minus (this, right);
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary minus (final double right)
  {
    throw new UnsupportedOperationException ();
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary minus (final float right)
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public final JOpBinary minus (final int right)
  {
    return minus (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary minus (final long right)
  {
    return minus (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary mul (@Nonnull final IJExpression right)
  {
    return JOp.mul (this, right);
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary mul (final double right)
  {
    throw new UnsupportedOperationException ();
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary mul (final float right)
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public final JOpBinary mul (final int right)
  {
    return mul (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary mul (final long right)
  {
    return mul (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary div (@Nonnull final IJExpression right)
  {
    return JOp.div (this, right);
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary div (final double right)
  {
    throw new UnsupportedOperationException ();
  }

  // Real values are not supported.
  @Nonnull
  public final JOpBinary div (final float right)
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public final JOpBinary div (final int right)
  {
    return div (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary div (final long right)
  {
    return div (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary mod (@Nonnull final IJExpression right)
  {
    return JOp.mod (this, right);
  }

  @Nonnull
  public final JOpBinary shl (@Nonnull final IJExpression right)
  {
    return JOp.shl (this, right);
  }

  @Nonnull
  public final JOpBinary shl (final int right)
  {
    return shl (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary shr (@Nonnull final IJExpression right)
  {
    return JOp.shr (this, right);
  }

  @Nonnull
  public final JOpBinary shr (final int right)
  {
    return shr (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary shrz (@Nonnull final IJExpression right)
  {
    return JOp.shrz (this, right);
  }

  @Nonnull
  public final JOpBinary shrz (final int right)
  {
    return shrz (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary band (@Nonnull final IJExpression right)
  {
    return JOp.band (this, right);
  }

  @Nonnull
  public final JOpBinary bor (@Nonnull final IJExpression right)
  {
    return JOp.bor (this, right);
  }

  @Nonnull
  public final IJExpression cand (@Nonnull final IJExpression right)
  {
    return JOp.cand (this, right);
  }

  @Nonnull
  public final IJExpression cor (@Nonnull final IJExpression right)
  {
    return JOp.cor (this, right);
  }

  @Nonnull
  public final JOpBinary xor (@Nonnull final IJExpression right)
  {
    return JOp.xor (this, right);
  }

  @Nonnull
  public final JOpBinary lt (@Nonnull final IJExpression right)
  {
    return JOp.lt (this, right);
  }

  @Nonnull
  public final JOpBinary lt0 ()
  {
    return lt (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary lte (@Nonnull final IJExpression right)
  {
    return JOp.lte (this, right);
  }

  @Nonnull
  public final JOpBinary lte0 ()
  {
    return lte (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary gt (@Nonnull final IJExpression right)
  {
    return JOp.gt (this, right);
  }

  @Nonnull
  public final JOpBinary gt0 ()
  {
    return gt (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary gte (@Nonnull final IJExpression right)
  {
    return JOp.gte (this, right);
  }

  @Nonnull
  public final JOpBinary gte0 ()
  {
    return gte (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary eq (@Nonnull final IJExpression right)
  {
    return JOp.eq (this, right);
  }

  @Nonnull
  public final JOpBinary eqNull ()
  {
    return eq (JExpr._null ());
  }

  @Nonnull
  public final JOpBinary eq0 ()
  {
    return eq (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary ne (@Nonnull final IJExpression right)
  {
    return JOp.ne (this, right);
  }

  @Nonnull
  public final JOpBinary neNull ()
  {
    return ne (JExpr._null ());
  }

  @Nonnull
  public final JOpBinary ne0 ()
  {
    return ne (JExpr.lit (0));
  }

  @Nonnull
  public final JOpBinary _instanceof (@Nonnull final AbstractJType right)
  {
    return JOp._instanceof (this, right);
  }

  //
  //
  // from JExpr
  //
  //
  @Nonnull
  public final JInvocation invoke (@Nonnull final JMethod method)
  {
    return JExpr.invoke (this, method);
  }

  @Nonnull
  public final JInvocation invoke (@Nonnull final String method)
  {
    return JExpr.invoke (this, method);
  }

  @Nonnull
  public final JFieldRef ref (@Nonnull final JVar field)
  {
    return JExpr.ref (this, field);
  }

  @Nonnull
  public final JFieldRef ref (@Nonnull final String field)
  {
    return JExpr.ref (this, field);
  }

  @Nonnull
  public final JArrayCompRef component (@Nonnull final IJExpression index)
  {
    return JExpr.component (this, index);
  }

  @Nonnull
  public final JArrayCompRef component (final int index)
  {
    return component (JExpr.lit (index));
  }

  @Nonnull
  public final JArrayCompRef component0 ()
  {
    return component (JExpr.lit (0));
  }

  @Nonnull
  public final IJExpression implies (IJExpression right)
  {
    return JOp.cor (JOp.not (this), right);
  }

  @Nonnull
  public final IJExpression implies (boolean right)
  {
    return JOp.cor (JOp.not (this), JExpr.lit (right));
  }

  @Nonnull
  public final IJExpression rimplies (IJExpression right)
  {
    return JOp.cor (this, JOp.not (right));
  }

  @Nonnull
  public final IJExpression rimplies (boolean right)
  {
    return JOp.cor (this, JOp.not (JExpr.lit (right)));
  }

  @Nonnull
  public final JOpBinary equiv (IJExpression right)
  {
    return JOp.eq (this, right);
  }

  @Nonnull
  public final JOpBinary equiv (boolean right)
  {
    return equiv (JExpr.lit (right));
  }

  @Nonnull
  public final JOpBinary inequiv (IJExpression right)
  {
    return JOp.ne (this, right);
  }

  @Nonnull
  public final JOpBinary inequiv (boolean right)
  {
    return inequiv (JExpr.lit (right));
  }

}
