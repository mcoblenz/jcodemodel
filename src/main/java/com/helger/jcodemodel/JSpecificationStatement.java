package com.helger.jcodemodel;

public class JSpecificationStatement implements IJStatement
{
  JMLAnnotation annotation;
  
  JSpecificationStatement(JMLAnnotation a) {
    annotation = a;
  }
  
  public void state (JFormatter f)
  {
    annotation.generate (f);
  }

}
