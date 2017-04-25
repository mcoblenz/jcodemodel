/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 * Portions Copyright 2013-2017 Philip Helger + contributors
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.helger.jcodemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.jcodemodel.util.JCValueEnforcer;

/**
 * A block of Java code, which may contain statements and local declarations.
 * <p>
 * {@link JBlock} contains a large number of factory methods that creates new
 * statements/declarations. Those newly created statements/declarations are
 * inserted into the {@link #pos() "current position"}. The position advances
 * one every time you add a new instruction.
 */
public class JBlock implements IJGenerable, IJStatement
{
  public static final boolean DEFAULT_VIRTUAL_BLOCK = false;
  public static final boolean DEFAULT_BRACES_REQUIRED = true;
  public static final boolean DEFAULT_INDENT_REQUIRED = true;

  /**
   * Declarations and statements contained in this block. Either
   * {@link IJStatement} or {@link IJDeclaration}.
   */
  protected final List <Object> m_aContentList = new ArrayList <> ();

  private boolean m_bVirtualBlock = DEFAULT_VIRTUAL_BLOCK;

  /**
   * Whether or not this block must be braced and indented
   */
  private boolean m_bBracesRequired = DEFAULT_BRACES_REQUIRED;
  private boolean m_bIndentRequired = DEFAULT_INDENT_REQUIRED;

  /**
   * Current position.
   */
  private int m_nPos;

  public JBlock ()
  {}

  @Deprecated
  protected JBlock (final boolean bBracesRequired, final boolean bIndentRequired)
  {
    bracesRequired (bBracesRequired);
    indentRequired (bIndentRequired);
  }

  /**
   * @return <code>true</code> if this is a virtual block never emitting braces
   *         or indent. The default is {@link #DEFAULT_VIRTUAL_BLOCK}
   */
  public boolean virtual ()
  {
    return m_bVirtualBlock;
  }

  /**
   * Mark this block virtual or not. Default is <code>false</code>. Virtual
   * blocks NEVER have braces and are never indented!
   *
   * @param bVirtualBlock
   *        <code>true</code> to make this block a virtual block.
   * @return this for chaining
   */
  @Nonnull
  public JBlock virtual (final boolean bVirtualBlock)
  {
    m_bVirtualBlock = bVirtualBlock;
    return this;
  }

  public boolean bracesRequired ()
  {
    return m_bBracesRequired;
  }

  @Nonnull
  public JBlock bracesRequired (final boolean bBracesRequired)
  {
    m_bBracesRequired = bBracesRequired;
    return this;
  }

  public boolean indentRequired ()
  {
    return m_bIndentRequired;
  }

  @Nonnull
  public JBlock indentRequired (final boolean bIndentRequired)
  {
    m_bIndentRequired = bIndentRequired;
    return this;
  }

  /**
   * @return a read-only view of {@link IJStatement}s and {@link IJDeclaration}
   *         in this block.
   */
  @Nonnull
  public List <Object> getContents ()
  {
    return Collections.unmodifiableList (m_aContentList);
  }

  @Nonnull
  protected final <T> T _insert (@Nonnull final T aStatementOrDeclaration)
  {
    return _insertAt (m_nPos, aStatementOrDeclaration);
  }

  @Nonnull
  protected final <T> T _insertAt (final int nIndex, @Nonnull final T aStatementOrDeclaration)
  {
    JCValueEnforcer.isGE0 (nIndex, "Index");
    JCValueEnforcer.notNull (aStatementOrDeclaration, "StatementOrDeclaration");

    m_aContentList.add (nIndex, aStatementOrDeclaration);
    m_nPos++;

    if (aStatementOrDeclaration instanceof JVar)
    {
      m_bBracesRequired = true;
      m_bIndentRequired = true;
    }

    return aStatementOrDeclaration;
  }

  public void remove (final Object o)
  {
    m_aContentList.remove (o);
  }

  public void remove (@Nonnegative final int index)
  {
    m_aContentList.remove (index);
  }

  /**
   * Remove all elements.
   */
  public void removeAll ()
  {
    m_aContentList.clear ();
    m_nPos = 0;
  }

  /**
   * @return the current position to which new statements will be inserted. For
   *         example if the value is 0, newly created instructions will be
   *         inserted at the very beginning of the block.
   * @see #pos(int)
   */
  @Nonnegative
  public int pos ()
  {
    return m_nPos;
  }

  /**
   * Sets the current position.
   *
   * @param nNewPos
   *        The new position to set
   * @return the old value of the current position.
   * @throws IllegalArgumentException
   *         if the new position value is illegal.
   * @see #pos()
   */
  @Nonnegative
  public int pos (@Nonnegative final int nNewPos)
  {
    final int nOldPos = m_nPos;
    if (nNewPos > m_aContentList.size () || nNewPos < 0)
      throw new IllegalArgumentException ("Illegal position provided: " + nNewPos);
    m_nPos = nNewPos;
    return nOldPos;
  }

  /**
   * @return <code>true</code> if this block is empty and does not contain any
   *         statement.
   */
  public boolean isEmpty ()
  {
    return m_aContentList.isEmpty ();
  }

  /**
   * @return The number of elements contained in the block. Always &ge; 0.
   */
  @Nonnegative
  public int size ()
  {
    return m_aContentList.size ();
  }

  /**
   * Adds a local variable declaration to this block. This enforces braces and
   * indentation to be enabled!
   *
   * @param type
   *        JType of the variable
   * @param name
   *        Name of the variable
   * @return Newly generated {@link JVar}
   */
  @Nonnull
  public JVar decl (@Nonnull final AbstractJType type, @Nonnull final String name)
  {
    return decl (JMod.NONE, type, name, null);
  }

  /**
   * Adds a local variable declaration to this block. This enforces braces and
   * indentation to be enabled!
   *
   * @param mods
   *        Modifiers for the variable
   * @param type
   *        JType of the variable
   * @param name
   *        Name of the variable
   * @return Newly generated {@link JVar}
   */
  @Nonnull
  public JVar decl (final int mods, @Nonnull final AbstractJType type, @Nonnull final String name)
  {
    return decl (mods, type, name, null);
  }

  /**
   * Adds a local variable declaration to this block. This enforces braces and
   * indentation to be enabled!
   *
   * @param type
   *        JType of the variable
   * @param name
   *        Name of the variable
   * @param init
   *        Initialization expression for this variable. May be null.
   * @return Newly generated {@link JVar}
   */
  @Nonnull
  public JVar decl (@Nonnull final AbstractJType type, @Nonnull final String name, @Nullable final IJExpression init)
  {
    return decl (JMod.NONE, type, name, init);
  }

  /**
   * Adds a local variable declaration to this block. This enforces braces and
   * indentation to be enabled!
   *
   * @param mods
   *        Modifiers for the variable
   * @param type
   *        JType of the variable
   * @param name
   *        Name of the variable
   * @param init
   *        Initialization expression for this variable. May be null.
   * @return Newly generated {@link JVar}
   */
  @Nonnull
  public JVar decl (final int mods,
                    @Nonnull final AbstractJType type,
                    @Nonnull final String name,
                    @Nullable final IJExpression init)
  {
    final JVar v = new JVar (JMods.forVar (mods), type, name, init);
    _insert (v);
    return v;
  }

  /**
   * Insert a variable before another element of this block. This enforces
   * braces and indentation to be enabled!
   *
   * @param var
   *        The variable to be inserted. May not be <code>null</code>.
   * @param before
   *        The object before the variable should be inserted. If the passed
   *        object is not contained in this block, an
   *        {@link IndexOutOfBoundsException} is thrown.
   * @return this for chaining
   */
  @Nonnull
  public JBlock insertBefore (@Nonnull final JVar var, @Nonnull final Object before)
  {
    final int i = m_aContentList.indexOf (before);
    _insertAt (i, var);
    return this;
  }

  /**
   * Creates an assignment statement and adds it to this block.
   *
   * @param lhs
   *        Assignable variable or field for left hand side of expression
   * @param exp
   *        Right hand side expression
   * @return this for chaining
   */
  @Nonnull
  public JBlock assign (@Nonnull final IJAssignmentTarget lhs, @Nonnull final IJExpression exp)
  {
    _insert (JExpr.assign (lhs, exp));
    return this;
  }

  @Nonnull
  public JBlock assignPlus (@Nonnull final IJAssignmentTarget lhs, @Nonnull final IJExpression exp)
  {
    _insert (JExpr.assignPlus (lhs, exp));
    return this;
  }

  @Nonnull
  public JBlock assignMinus (@Nonnull final IJAssignmentTarget lhs, @Nonnull final IJExpression exp)
  {
    _insert (JExpr.assignMinus (lhs, exp));
    return this;
  }

  @Nonnull
  public JBlock assignTimes (@Nonnull final IJAssignmentTarget lhs, @Nonnull final IJExpression exp)
  {
    _insert (JExpr.assignTimes (lhs, exp));
    return this;
  }

  @Nonnull
  public JBlock assignDivide (@Nonnull final IJAssignmentTarget lhs, @Nonnull final IJExpression exp)
  {
    _insert (JExpr.assignDivide (lhs, exp));
    return this;
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param expr
   *        {@link IJExpression} evaluating to the class or object upon which
   *        the named method will be invoked
   * @param method
   *        Name of method to invoke
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invoke (@Nonnull final IJExpression expr, @Nonnull final String method)
  {
    return _insert (new JInvocation (expr, method));
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param method
   *        Name of method to invoke on this
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invokeThis (@Nonnull final String method)
  {
    return invoke (JExpr._this (), method);
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param expr
   *        {@link IJExpression} evaluating to the class or object upon which
   *        the method will be invoked
   * @param method
   *        {@link JMethod} to invoke
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invoke (@Nonnull final IJExpression expr, @Nonnull final JMethod method)
  {
    return _insert (new JInvocation (expr, method));
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param method
   *        {@link JMethod} to invoke on this
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invokeThis (@Nonnull final JMethod method)
  {
    return invoke (JExpr._this (), method);
  }

  /**
   * Creates a static invocation statement.
   *
   * @param aType
   *        Type upon which the method should be invoked
   * @param sMethod
   *        Name of method to invoke
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation staticInvoke (@Nonnull final AbstractJClass aType, @Nonnull final String sMethod)
  {
    return _insert (new JInvocation (aType, sMethod));
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param sMethod
   *        Name of method to invoke
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invoke (@Nonnull final String sMethod)
  {
    return _insert (new JInvocation ((IJExpression) null, sMethod));
  }

  /**
   * Creates an invocation statement and adds it to this block.
   *
   * @param aMethod
   *        JMethod to invoke
   * @return Newly generated {@link JInvocation}
   */
  @Nonnull
  public JInvocation invoke (@Nonnull final JMethod aMethod)
  {
    return _insert (new JInvocation ((IJExpression) null, aMethod));
  }

  @Nonnull
  public JInvocation _new (@Nonnull final AbstractJClass c)
  {
    return _insert (new JInvocation (c));
  }

  @Nonnull
  public JInvocation _new (@Nonnull final AbstractJType t)
  {
    return _insert (new JInvocation (t));
  }

  /**
   * Adds an arbitrary statement to this block
   *
   * @param s
   *        {@link IJStatement} to be added. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public JBlock add (@Nonnull final IJStatement s)
  {
    _insert (s);
    return this;
  }

  /**
   * Adds an empty single line comment
   *
   * @return this for chaining
   */
  @Nonnull
  public JBlock addSingleLineComment ()
  {
    return addSingleLineComment ("");
  }

  /**
   * Adds a single line comment to this block
   *
   * @param sComment
   *        The comment string to be added. <code>null</code> is ignored, empty
   *        string lead to an empty single line comment.
   * @return this for chaining
   */
  @Nonnull
  public JBlock addSingleLineComment (@Nullable final String sComment)
  {
    if (sComment != null)
      _insert (new JSingleLineCommentStatement (sComment));
    return this;
  }

  @Nonnull
  public JBlock addJMLAnnotation (@Nonnull final JMLAnnotation a)
  {
    _insert (new JSpecificationStatement(a));
    return this;
  }
  
  /**
   * Create an If statement and add it to this block
   *
   * @param aTestExpr
   *        {@link IJExpression} to be tested to determine branching
   * @return Newly generated {@link JConditional} statement
   */
  @Nonnull
  public JConditional _if (@Nonnull final IJExpression aTestExpr)
  {
    return _insert (new JConditional (aTestExpr));
  }

  /**
   * Create an If statement with the respective then statement and add it to
   * this block
   *
   * @param aTestExpr
   *        {@link IJExpression} to be tested to determine branching
   * @param aThen
   *        The then-block. May not be <code>null</code>.
   * @return Newly generated {@link JConditional} statement
   */
  @Nonnull
  public JConditional _if (@Nonnull final IJExpression aTestExpr, @Nonnull final IJStatement aThen)
  {
    final JConditional aCond = new JConditional (aTestExpr);
    aCond._then ().add (aThen);
    return _insert (aCond);
  }

  /**
   * Create an If statement with the respective then and else statements and add
   * it to this block
   *
   * @param aTestExpr
   *        {@link IJExpression} to be tested to determine branching
   * @param aThen
   *        The then-block. May not be <code>null</code>.
   * @param aElse
   *        The else-block. May not be <code>null</code>.
   * @return Newly generated {@link JConditional} statement
   */
  @Nonnull
  public JConditional _if (@Nonnull final IJExpression aTestExpr,
                           @Nonnull final IJStatement aThen,
                           @Nonnull final IJStatement aElse)
  {
    final JConditional aCond = new JConditional (aTestExpr);
    aCond._then ().add (aThen);
    aCond._else ().add (aElse);
    return _insert (aCond);
  }

  /**
   * Create a For statement and add it to this block
   *
   * @return Newly generated {@link JForLoop} statement. Never <code>null</code>
   *         .
   */
  @Nonnull
  public JForLoop _for ()
  {
    return _insert (new JForLoop ());
  }

  /**
   * Create a While statement and add it to this block
   *
   * @param test
   *        Test expression for the while statement
   * @return Newly generated {@link JWhileLoop} statement
   */
  @Nonnull
  public JWhileLoop _while (@Nonnull final IJExpression test)
  {
    return _insert (new JWhileLoop (test));
  }

  /**
   * Create a switch/case statement and add it to this block
   *
   * @param test
   *        Test expression for the switch statement
   * @return Newly created {@link JSwitch}
   */
  @Nonnull
  public JSwitch _switch (@Nonnull final IJExpression test)
  {
    return _insert (new JSwitch (test));
  }

  /**
   * Create a Do statement and add it to this block
   *
   * @param test
   *        Test expression for the while statement
   * @return Newly generated {@link JDoLoop} statement
   */
  @Nonnull
  public JDoLoop _do (@Nonnull final IJExpression test)
  {
    return _insert (new JDoLoop (test));
  }

  /**
   * Create a Try statement and add it to this block
   *
   * @return Newly generated {@link JTryBlock} statement
   */
  @Nonnull
  public JTryBlock _try ()
  {
    return _insert (new JTryBlock ());
  }

  /**
   * Create a return statement and add it to this block
   *
   * @return Newly created {@link JReturn} statement
   */
  @Nonnull
  public JReturn _return ()
  {
    return _insert (new JReturn (null));
  }

  /**
   * Create a return statement and add it to this block
   *
   * @param aExpr
   *        Expression to be returned. May be <code>null</code>.
   * @return Newly created {@link JReturn} statement
   */
  @Nonnull
  public JReturn _return (@Nullable final IJExpression aExpr)
  {
    return _insert (new JReturn (aExpr));
  }

  /**
   * Create a throw statement and add it to this block
   *
   * @param aExpr
   *        Expression to be thrown
   * @return Newly created {@link JThrow}
   */
  @Nonnull
  public JThrow _throw (@Nonnull final IJExpression aExpr)
  {
    return _insert (new JThrow (aExpr));
  }

  /**
   * Create a break statement without a label and add it to this block
   *
   * @return Newly created {@link JBreak}
   */
  @Nonnull
  public JBreak _break ()
  {
    return _break ((JLabel) null);
  }

  /**
   * Create a break statement with an optional label and add it to this block
   *
   * @param aLabel
   *        Optional label for the break statement
   * @return Newly created {@link JBreak}
   */
  @Nonnull
  public JBreak _break (@Nullable final JLabel aLabel)
  {
    return _insert (new JBreak (aLabel));
  }

  /**
   * Create a label, which can be referenced from <code>continue</code> and
   * <code>break</code> statements.
   *
   * @param name
   *        Label name
   * @return Newly created {@link JLabel}
   */
  @Nonnull
  public JLabel label (@Nonnull final String name)
  {
    final JLabel l = new JLabel (name);
    _insert (l);
    return l;
  }

  /**
   * Create a continue statement without a label and add it to this block
   *
   * @return New {@link JContinue}
   */
  @Nonnull
  public JContinue _continue ()
  {
    return _continue (null);
  }

  /**
   * Create a continue statement with an optional label and add it to this block
   *
   * @param aLabel
   *        Optional label statement.
   * @return New {@link JContinue}
   */
  @Nonnull
  public JContinue _continue (@Nullable final JLabel aLabel)
  {
    return _insert (new JContinue (aLabel));
  }

  /**
   * Create a sub-block and add it to this block. By default braces and indent
   * are required.
   *
   * @return New {@link JBlock}
   * @see #block(boolean, boolean)
   * @see #blockSimple()
   */
  @Nonnull
  public JBlock block ()
  {
    return _insert (new JBlock ());
  }

  /**
   * Create a sub-block and add it to this block. By default braces and indent
   * are not required.
   *
   * @return New {@link JBlock}
   * @see #block()
   * @see #block(boolean, boolean)
   */
  @Nonnull
  public JBlock blockSimple ()
  {
    return block (false, false);
  }

  /**
   * Create a sub-block and add it to this block. This kind of block will never
   * create braces or indent!
   *
   * @return New {@link JBlock}
   * @see #block()
   * @see #block(boolean, boolean)
   */
  @Nonnull
  public JBlock blockVirtual ()
  {
    return blockSimple ().virtual (true);
  }

  /**
   * Create a sub-block and add it to this block
   *
   * @param bBracesRequired
   *        <code>true</code> if braces should be required
   * @param bIndentRequired
   *        <code>true</code> if indentation is required
   * @return New {@link JBlock}
   * @see #block()
   * @see #blockSimple()
   */
  @Nonnull
  public JBlock block (final boolean bBracesRequired, final boolean bIndentRequired)
  {
    return _insert (new JBlock ().bracesRequired (bBracesRequired).indentRequired (bIndentRequired));
  }

  /**
   * Creates an enhanced For statement based on j2se 1.5 JLS and add it to this
   * block
   *
   * @param aVarType
   *        Variable type
   * @param sName
   *        Variable name
   * @param aCollection
   *        Collection to be iterated
   * @return Newly generated enhanced For statement per j2se 1.5 specification
   */
  @Nonnull
  public JForEach forEach (@Nonnull final AbstractJType aVarType,
                           @Nonnull final String sName,
                           @Nonnull final IJExpression aCollection)
  {
    return _insert (new JForEach (aVarType, sName, aCollection));
  }

  /**
   * Create a synchronized block statement and add it to this block
   *
   * @param aExpr
   *        The expression to synchronize on. May not be <code>null</code>.
   * @return Newly generated synchronized block. Never <code>null</code>.
   * @since 2.7.10
   */
  @Nonnull
  public JSynchronizedBlock synchronizedBlock (@Nonnull final IJExpression aExpr)
  {
    return _insert (new JSynchronizedBlock (aExpr));
  }

  /**
   * Creates a "literal" statement directly.
   * <p>
   * Specified string is printed as-is. This is useful as a short-cut.
   * <p>
   * For example, you can invoke this method as:
   * <code>directStatement("a=b+c;")</code>.
   *
   * @param sSource
   *        The source code to state. May not be <code>null</code>.
   * @return The created direct statement.
   */
  @Nonnull
  public IJStatement directStatement (@Nonnull final String sSource)
  {
    final JDirectStatement aStatement = new JDirectStatement (sSource);
    add (aStatement);
    return aStatement;
  }

  public void generate (@Nonnull final JFormatter f)
  {
    if (m_bVirtualBlock)
    {
      // Body only
      generateBody (f);
    }
    else
    {
      if (m_bBracesRequired)
      {
        f.print ('{');
        f.newline ();
      }
      if (m_bIndentRequired)
        f.indent ();
      generateBody (f);
      if (m_bIndentRequired)
        f.outdent ();
      if (m_bBracesRequired)
        f.print ('}');
    }
  }

  void generateBody (@Nonnull final JFormatter f)
  {
    for (final Object aContentElement : m_aContentList)
    {
      if (aContentElement instanceof IJDeclaration)
        f.declaration ((IJDeclaration) aContentElement);
      else
        if (aContentElement instanceof IJStatement)
          f.statement ((IJStatement) aContentElement);
        else
        {
          // For lambda expressions in JLambdaBlock
          f.generable ((IJGenerable) aContentElement);
        }
    }
  }

  public void state (@Nonnull final JFormatter f)
  {
    f.generable (this);
    if (m_bBracesRequired)
      f.newline ();
  }
}
