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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.jcodemodel.util.JCValueEnforcer;

/**
 * This is a single Java 8 lambda method reference expression.
 *
 * @author Philip Helger
 * @since 2.8.3
 */
public class JLambdaMethodRef extends AbstractJExpressionImpl
{
  private final JMethod m_aMethod;
  private final AbstractJType m_aType;
  private final JVar m_aVar;
  private final String m_sMethodName;

  /**
   * Constructor to reference the passed method
   *
   * @param aMethod
   *        The static method to reference. May not be <code>null</code>.
   */
  public JLambdaMethodRef (@Nonnull final JMethod aMethod)
  {
    JCValueEnforcer.notNull (aMethod, "Method");
    if (!aMethod.mods ().isStatic ())
      throw new IllegalArgumentException ("Only static methods can be used with this constructor. Use the constructor with JVar for instance methods.");

    m_aMethod = aMethod;
    m_aType = null;
    m_aVar = null;
    m_sMethodName = null;
  }

  /**
   * Constructor for a constructor method reference (<code>type::new</code>).
   *
   * @param aType
   *        Type to reference the constructor from. May not be <code>null</code>
   *        .
   */
  public JLambdaMethodRef (@Nonnull final AbstractJType aType)
  {
    this (aType, "new");
  }

  /**
   * Constructor for an arbitrary method reference.
   *
   * @param aType
   *        Type the method belongs to. May not be <code>null</code>.
   * @param sMethod
   *        Name of the static method to reference. May neither be
   *        <code>null</code> nor empty.
   */
  public JLambdaMethodRef (@Nonnull final AbstractJType aType, @Nonnull final String sMethod)
  {
    m_aMethod = null;
    m_aType = JCValueEnforcer.notNull (aType, "Type");
    m_aVar = null;
    m_sMethodName = JCValueEnforcer.notEmpty (sMethod, "Method");
  }

  /**
   * Constructor for an arbitrary instance method reference.
   *
   * @param aVar
   *        Variable containing the instance. May not be <code>null</code>.
   * @param sMethod
   *        Name of the method to reference. May neither be <code>null</code>
   *        nor empty.
   */
  public JLambdaMethodRef (@Nonnull final JVar aVar, @Nonnull final String sMethod)
  {
    JCValueEnforcer.notNull (aVar, "Var");
    JCValueEnforcer.notEmpty (sMethod, "Method");

    m_aMethod = null;
    m_aType = null;
    m_aVar = aVar;
    m_sMethodName = sMethod;
  }

  /**
   * Constructor for an arbitrary instance method reference.
   *
   * @param aVar
   *        Variable containing the instance. May not be <code>null</code>.
   * @param aMethod
   *        The instance method to reference. May not be <code>null</code>.
   */
  public JLambdaMethodRef (@Nonnull final JVar aVar, @Nonnull final JMethod aMethod)
  {
    JCValueEnforcer.notNull (aVar, "Var");
    JCValueEnforcer.notNull (aMethod, "Method");
    if (aMethod.mods ().isStatic ())
      throw new IllegalArgumentException ("Only instance methods can be used with this constructor. Use the constructor with JMethod only for static methods.");

    m_aMethod = aMethod;
    m_aType = null;
    m_aVar = aVar;
    m_sMethodName = null;
  }

  /**
   * @return <code>true</code> if this is a static reference, <code>false</code>
   *         if this is an instance reference.
   */
  public boolean isStaticRef ()
  {
    if (m_aMethod != null)
      return m_aMethod.mods ().isStatic ();

    return m_aType != null;
  }

  /**
   * @return The owning method. May be <code>null</code> if a constructor with
   *         method name was used.
   */
  @Nullable
  public JMethod method ()
  {
    return m_aMethod;
  }

  /**
   * @return The type owning the method. Never <code>null</code> .
   */
  @Nonnull
  public AbstractJType type ()
  {
    if (m_aVar != null)
      return m_aVar.type ();
    if (m_aMethod != null)
      return m_aMethod.owningClass ();
    return m_aType;
  }

  /**
   * @return The variable for the instance reference. May be <code>null</code>
   *         if this is a static reference.
   */
  @Nullable
  public JVar var ()
  {
    return m_aVar;
  }

  /**
   * @return The name of the referenced method. Never <code>null</code>.
   */
  @Nonnull
  public String methodName ()
  {
    return m_aMethod != null ? m_aMethod.name () : m_sMethodName;
  }

  public void generate (@Nonnull final JFormatter f)
  {
    final AbstractJType aType = type ();
    if (isStaticRef ())
      f.type (aType);
    else
      f.generable (m_aVar);
    f.print ("::").print (methodName ());
  }
}
