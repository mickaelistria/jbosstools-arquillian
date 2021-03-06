/*************************************************************************************
 * Copyright (c) 2008-2012 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.arquillian.ui.internal.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.arquillian.core.internal.util.ArquillianSearchEngine;
import org.jboss.tools.arquillian.core.internal.util.ArquillianUtility;
import org.jboss.tools.arquillian.ui.internal.model.ArquillianZipEntry;

/**
 * 
 * @author snjeza
 *
 */
public class DeploymentContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) parentElement;
			if (ArquillianSearchEngine.isArquillianJUnitTest(cu, true, false)) {
				IType type = cu.findPrimaryType();
				if (type == null) {
					return null;
				}
				//boolean create = !ArquillianUtility.isValidatorEnabled(cu.getJavaProject().getProject());
				boolean create = false;
				List<File> archives = ArquillianSearchEngine.getDeploymentArchives(type, create);
				if (archives == null || archives.size() <= 0) {
					return null;
				}
				List<ArquillianZipEntry> entries = new ArrayList<ArquillianZipEntry>();
				for (File archive:archives) {
					entries.add(new ArquillianZipEntry(archive));
				}
				return entries.toArray(new ArquillianZipEntry[0]);
			}
		}
		if (parentElement instanceof ArquillianZipEntry) {
			ArquillianZipEntry entry = (ArquillianZipEntry) parentElement;
			return entry.getChildren();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ArquillianZipEntry) {
			((ArquillianZipEntry) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		Object[] elements = getChildren(element);
		return elements != null && elements.length > 0;
	}

}
