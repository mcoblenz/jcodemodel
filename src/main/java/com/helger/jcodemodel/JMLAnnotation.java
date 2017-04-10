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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.jcodemodel.util.JCValueEnforcer;

/**
 * JML annotation block. A JML annotation consists of lines, each of which
 * begins with a keyword indicating the type of annotation. Supports non-JML annotation keywords.
 */
public class JMLAnnotation extends JCommentPart implements IJGenerable, IJOwned
{
  private static final long serialVersionUID = 2L;

  private final JCodeModel m_aOwner;

  /** list of generic non-JML specification keywords */
  private final Map <String, Map <String, String>> m_Keywords = new LinkedHashMap <> ();

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
   * Add a new non-JML specification keyword. This is a way of adding keywords
   * not currently supported by the JMLAnnotation object. If the keyword already
   * exists, it's uses will be returned.
   *
   * @param name
   *        keyword's syntactic token
   * @return Map with the key/value pairs
   */
  @Nonnull
  public Map <String, String> addKeyword (@Nonnull final String name)
  {
    Map <String, String> p = m_Keywords.get (name);
    if (p == null)
    {
      p = new LinkedHashMap <> ();
      m_Keywords.put (name, p);
    }
    return p;
  }

  /**
   * Add a specification keyword.
   *
   * @param name
   *        keyword's syntactic token
   * @param attributes
   *        Initial uses of the keyword.
   * @return Returns all uses of the specification keyword, including those
   *         specified just now.
   */
  @Nonnull
  public Map <String, String> addKeyword (@Nonnull final String name, @Nonnull final Map <String, String> attributes)
  {
    final Map <String, String> p = addKeyword (name);
    p.putAll (attributes);
    return p;
  }

  /**
   * Add a specification keyword with the form
   * <code>@name attribute = "value"</code>. If value is <code>null</code> then
   * the specification will be <code>@name attribute</code>.
   *
   * @param name
   *        keyword's syntactic token
   * @param attribute
   *        Attribute expression to be added
   * @param value
   *        Attribute value to be added
   * @return Map with the key/value pairs
   */
  @Nonnull
  public Map <String, String> addKeyword (@Nonnull final String name,
                                          @Nonnull final String attribute,
                                          @Nullable final String value)
  {
    final Map <String, String> p = addKeyword (name);
    p.put (attribute, value);
    return p;
  }

  @Nullable
  public Map <String, String> removeKeyword (@Nullable final String name)
  {
    return m_Keywords.remove (name);
  }

  public void removeAllKeywords ()
  {
    m_Keywords.clear ();
  }

  public void generate (@Nonnull final JFormatter f)
  {
    // Is any keyword used? TODO: add support for explicit annotation keywords
    final boolean bHasAnnotation = // !m_aAtParams.isEmpty () ||
    // m_aAtReturn != null ||
    // !m_aAtThrows.isEmpty () ||
    // !m_aAtTags.isEmpty () ||
                                 !m_Keywords.isEmpty ();

    // TODO: figure out how the heck this.isEmpty() might return false...
    if (!isEmpty () || bHasAnnotation)
    {
      final String sIndent = " @ ";
      final String sIndentLarge = sIndent + "\t";

      // Start comment
      f.print ("/*").newline ();

      // Print all simple text elements
      format (f, sIndent);
      if (!isEmpty () && bHasAnnotation)
        f.print (sIndent).newline ();

      /*
       * for (final Map.Entry <String, JCommentPart> aEntry :
       * m_aAtParams.entrySet ()) { f.print (sIndent + "@param ").print
       * (aEntry.getKey ()).newline (); aEntry.getValue ().format (f,
       * sIndentLarge); } if (m_aAtReturn != null) { f.print (sIndent +
       * "@return").newline (); m_aAtReturn.format (f, sIndentLarge); } for
       * (final Map.Entry <AbstractJClass, JCommentPart> aEntry :
       * m_aAtThrows.entrySet ()) { f.print (sIndent + "@throws ").type
       * (aEntry.getKey ()).newline (); aEntry.getValue ().format (f,
       * sIndentLarge); } for (final Map.Entry <String, JCommentPart> aEntry :
       * m_aAtTags.entrySet ()) { f.print (sIndent + "@" + aEntry.getKey () +
       * " "); aEntry.getValue ().format (f, ""); }
       */

      // Output ensures that nonterminals are not split across separate
      // annotations.
      // Each annotation must be a single grammatical unit.
      for (final Map.Entry <String, Map <String, String>> aEntry : m_Keywords.entrySet ())
      {
        f.print (sIndent).print (aEntry.getKey ());
        if (aEntry.getValue () != null)
        {
          for (final Map.Entry <String, String> aEntry2 : aEntry.getValue ().entrySet ())
          {
            final String sName = aEntry2.getKey ();
            f.print (" ").print (sName);

            // Print value only if present
            final String sValue = aEntry2.getValue ();
            if (sValue != null && sValue.length () > 0)
              f.print ("= \"").print (sValue).print ("\"");
          }
        }
        f.newline ();
      }

      // End comment
      f.print ("@*/").newline ();
    }
  }
}
