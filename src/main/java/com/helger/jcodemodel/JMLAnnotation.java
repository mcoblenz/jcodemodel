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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.jcodemodel.util.JCValueEnforcer;

/**
 * JML annotation block. A JML annotation consists of lines, each of which
 * begins with a keyword indicating the type of annotation. Supports non-JML
 * annotation keywords.
 */
public class JMLAnnotation extends JCommentPart implements IJGenerable, IJOwned
{
  private static final long serialVersionUID = 2L;

  private final JCodeModel m_aOwner;

  /** list of generic non-JML specification keywords */
  private final Map <String, List <IJMLExpression>> m_Keywords = new HashMap <> ();

  protected JMLAnnotation (@Nonnull final JCodeModel owner)
  {
    m_aOwner = JCValueEnforcer.notNull (owner, "Owner");
  }

  @Nonnull
  public JCodeModel owner ()
  {
    return m_aOwner;
  }

  @Override
  public JMLAnnotation append (@Nullable final Object o)
  {
    add (o);
    return this;
  }

  /**
   * Add a new specification keyword. This is a way of adding keywords not
   * explicitly supported by the JMLAnnotation object. If the keyword already
   * exists, it's uses will be returned.
   *
   * @param name
   *        keyword's syntactic token
   * @return Map with the key/value pairs
   */
  @Nonnull
  private List <IJMLExpression> addKeyword (@Nonnull final String name)
  {
    List <IJMLExpression> p = m_Keywords.get (name);
    if (p == null)
    {
      p = new ArrayList <> ();
      m_Keywords.put (name, p);
    }
    return p;
  }

  /**
   * Add a specification keyword.
   *
   * @param name
   *        keyword's syntactic token
   * @param specs
   *        Initial uses of the keyword.
   * @return Returns all uses of the specification keyword, including those
   *         specified just now.
   */
  @Nonnull
  private List <IJMLExpression> addKeyword (@Nonnull final String name, @Nonnull final List <IJMLExpression> specs)
  {
    final List <IJMLExpression> p = addKeyword (name);
    p.addAll (specs);
    return p;
  }

  /**
   * Remove all JML annotations corresponding to a particular keyword from the
   * annotation block.
   * 
   * @param name
   *        the token for the annotation to be removed
   * @return Returns the map of annotation tokens to their lists of JMLExpr
   *         clauses.
   */
  @Nullable
  public Map <String, List <IJMLExpression>> removeKeyword (@Nullable final IJMLExpression name)
  {
    m_Keywords.remove (name);
    return m_Keywords;
  }

  /**
   * Used for adding an <code>@ensures</code> clause to the annotation block, which are JML postconditions.
   * 
   * @param ensuresClause
   *        the JMLExpr which represents the postcondition clause.
   */
  public void addEnsures (IJMLExpression ensuresClause)
  {
    final List <IJMLExpression> p = new ArrayList <> ();
    p.add (ensuresClause);
    addKeyword ("ensures", p);
  }
  
  /**
   * Used for adding an <code>@requires</code> clause to the annotation block, which are JML preconditions.
   * 
   * @param  requiresClause
   *        the JMLExpr which represents the postcondition clause.
   */
  public void addRequires (IJMLExpression requiresClause)
  {
    final List <IJMLExpression> p = new ArrayList <> ();
    p.add (requiresClause);
    addKeyword ("requires", p);
  }

  public void generate (@Nonnull final JFormatter f)
  {
    // Is any keyword used?
    final boolean bHasAnnotation = // !m_aAtParams.isEmpty () ||
    // m_aAtReturn != null ||
    // !m_aAtThrows.isEmpty () ||
    // !m_aAtTags.isEmpty () ||
                                 !m_Keywords.isEmpty ();

    if (!isEmpty () || bHasAnnotation)
    {
      final String sIndent = " @ ";
      // final String sIndentLarge = sIndent + "\t";

      // Start comment
      f.print ("/*").newline ();

      // Print all simple text elements
      format (f, sIndent);
      if (!isEmpty () && bHasAnnotation)
        f.print (sIndent).newline ();

      // Print programmatically defined keywords with their specification
      // strings.
      // Output ensures that nonterminals are not split across separate
      // annotations. Each annotation must be a single grammatical unit.
      for (final Map.Entry <String, List <IJMLExpression>> aEntry : m_Keywords.entrySet ())
      {
        if (aEntry.getValue () != null)
        {
          for (final IJMLExpression specs : aEntry.getValue ())
          {
            f.print (sIndent).print (aEntry.getKey ());
            f.print (" ");
            specs.generate (f); // emit the JMLExpression inside the spec
                                // corresponding to that spec keyword
            f.newline ();
          }
        }
        else
        {
          f.print (sIndent).print (aEntry.getKey ());
          f.newline ();
        }
      }

      // End comment
      f.print ("@*/").newline ();
    }
  }
}
