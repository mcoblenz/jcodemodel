package com.helger.jcodemodel;

import javax.annotation.Nonnull;

public class JMLExpressionOpBinary implements IJMLExpression
{
  String opName;
  IJMLExpression left;
  IJMLExpression right;
  
  public JMLExpressionOpBinary (@Nonnull IJMLExpression l, @Nonnull String o, @Nonnull IJMLExpression r) {
    this.opName = o;
    this.left = l;
    this.right = r;
  }

  public void generate (@Nonnull JFormatter f)
  {
    left.generate (f);
    f.print (" ");
    f.print (opName);
    f.print (" ");
    right.generate (f);
  }

}
