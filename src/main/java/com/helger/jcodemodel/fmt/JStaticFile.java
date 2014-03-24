/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.helger.jcodemodel.fmt;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import com.helger.jcodemodel.AbstractJResourceFile;
import com.helger.jcodemodel.util.SecureLoader;

/**
 * Allows an application to copy a resource file to the output.
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class JStaticFile extends AbstractJResourceFile
{
  private final ClassLoader _classLoader;
  private final String _resourceName;
  private final boolean _isResource;

  public JStaticFile (@Nonnull final String resourceName)
  {
    this (resourceName, !resourceName.endsWith (".java"));
  }

  public JStaticFile (@Nonnull final String resourceName, final boolean isResource)
  {
    this (SecureLoader.getClassClassLoader (JStaticFile.class), resourceName, isResource);
  }

  /**
   * @param isResource
   *        false if this is a Java source file. True if this is other resource
   *        files.
   */
  public JStaticFile (@Nonnull final ClassLoader classLoader,
                      @Nonnull final String resourceName,
                      final boolean isResource)
  {
    super (resourceName.substring (resourceName.lastIndexOf ('/') + 1));
    this._classLoader = classLoader;
    this._resourceName = resourceName;
    this._isResource = isResource;
  }

  @Override
  protected boolean isResource ()
  {
    return _isResource;
  }

  @Override
  protected void build (@Nonnull final OutputStream os) throws IOException
  {
    final DataInputStream dis = new DataInputStream (_classLoader.getResourceAsStream (_resourceName));

    final byte [] buf = new byte [256];
    int sz;
    while ((sz = dis.read (buf)) > 0)
      os.write (buf, 0, sz);

    dis.close ();
  }
}
