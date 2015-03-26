/*******************************************************************************
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package org.jetbrains.kotlin.ui.formatter;

import org.jetbrains.kotlin.JetNodeTypes;
import org.jetbrains.kotlin.eclipse.ui.utils.IndenterUtil;
import org.jetbrains.kotlin.eclipse.ui.utils.LineEndUtil;
import org.jetbrains.kotlin.lexer.JetTokens;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public class AlignmentStrategy {
    
    private final ASTNode parsedFile;
    private StringBuilder edit;
    private final int lineIndentation;
    
    public static final TokenSet CODE_BLOCKS = TokenSet.create(
            JetNodeTypes.BLOCK, 
            JetNodeTypes.CLASS_BODY, 
            JetNodeTypes.FUNCTION_LITERAL,
            JetNodeTypes.WHEN,
            JetNodeTypes.IF,
            JetNodeTypes.FOR,
            JetNodeTypes.WHILE,
            JetNodeTypes.DO_WHILE);
    
    public AlignmentStrategy(ASTNode parsedFile, int lineIndentation) {
        this.parsedFile = parsedFile;
        this.lineIndentation = lineIndentation;
    }
    
    public String placeSpaces() {
        edit = new StringBuilder();
        buildFormattedCode(parsedFile, lineIndentation);
        
        return LineEndUtil.replaceAllNewLinesWithSystemLineSeparators(edit.toString());
    }
    
    private void buildFormattedCode(ASTNode node, int indent) {
        if (CODE_BLOCKS.contains(node.getElementType())) {
            indent++;
        }
        
        for (ASTNode child : node.getChildren(null)) {
            PsiElement psiElement = child.getPsi();
            
            if (psiElement instanceof LeafPsiElement) {
                if (IndenterUtil.isNewLine((LeafPsiElement) psiElement)) {
                    int shift = indent;
                    if (isBrace(psiElement.getNextSibling())) {
                        shift--;
                    }
                    
                    int lineSeparatorsOccurences = IndenterUtil.getLineSeparatorsOccurences(psiElement.getText());
                    edit.append(IndenterUtil.createWhiteSpace(shift, lineSeparatorsOccurences, LineEndUtil.NEW_LINE_STRING));
                } else {
                    edit.append(psiElement.getText());
                }
            }
            
            buildFormattedCode(child, indent);
        }
    }
    
    private static boolean isBrace(PsiElement psiElement) {
        LeafPsiElement leafPsiElement = getFirstLeaf(psiElement);
        
        if (leafPsiElement != null) {
            IElementType elementType = leafPsiElement.getElementType();
            if (elementType == JetTokens.LBRACE || elementType == JetTokens.RBRACE) {
                return true;
            }
        }
        
        return false;
    }
    
    private static LeafPsiElement getFirstLeaf(PsiElement psiElement) {
        PsiElement child = psiElement;
        
        while (true) {
            if (child instanceof LeafPsiElement || child == null) {
                return (LeafPsiElement) child;
            }
            
            child = child.getFirstChild();
        }
    }
    
    public static String alignCode(ASTNode parsedFile) {
        return alignCode(parsedFile, 0);
    }
    
    public static String alignCode(ASTNode parsedFile, int lineIndentation) {
        return new AlignmentStrategy(parsedFile, lineIndentation).placeSpaces();
    }
}