package com.helger.jcodemodel;

import javax.annotation.Nonnull;

public class JMLExpr extends AbstractJExpressionImpl
{

  public void generate (JFormatter f)
  {
    // TODO Auto-generated method stub

  }
  
  /**
   * Creates a single JML expression directly from a code fragment.
   * <p>
   * This method can be used as a short-cut to create a JMLExpression. For
   * example, instead of <code>_a.gt(_b)</code>, you can write it as:
   * <code>JMLExpr.direct("a&gt;b")</code>.
   * <p>
   * Be warned that there is a danger in using this method, as it obfuscates the
   * object model.
   *
   * @param source
   *        JML specification code
   * @return Direct expression
   */
  @Nonnull
  public static AbstractJExpressionImpl direct (@Nonnull final String source)
  {
    return new AbstractJExpressionImpl ()
    {
      public void generate (final JFormatter f)
      {
        f.print ('(').print (source).print (')');
      }
    };
  }

}
