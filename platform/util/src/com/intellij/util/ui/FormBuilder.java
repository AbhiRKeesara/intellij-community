/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
 */

/*
 * @author max
 */
package com.intellij.util.ui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.intellij.util.ui.UIUtil.DEFAULT_HGAP;
import static com.intellij.util.ui.UIUtil.DEFAULT_VGAP;
import static java.awt.GridBagConstraints.*;

public class FormBuilder {
  private boolean myAlignLabelOnRight;

  private int myLineCount = 0;
  private int myIndent;
  private final JPanel myPanel;
  private boolean myVertical;

  private int myVerticalGap;
  private int myHorizontalGap;

  public FormBuilder() {
    myPanel = new JPanel(new GridBagLayout());
    myVertical = false;
    myIndent = 0;
    myAlignLabelOnRight = false;
    myVerticalGap = DEFAULT_VGAP;
    myHorizontalGap = DEFAULT_HGAP;
  }

  public static FormBuilder createFormBuilder() {
    return new FormBuilder();
  }

  public FormBuilder addLabeledComponent(@Nullable JComponent label, JComponent component) {
    return addLabeledComponent(label, component, myVerticalGap, false);
  }

  public FormBuilder addLabeledComponent(@Nullable JComponent label, JComponent component, final int topInset) {
    return addLabeledComponent(label, component, topInset, false);
  }

  public FormBuilder addLabeledComponent(@Nullable JComponent label, JComponent component, boolean labelOnTop) {
    return addLabeledComponent(label, component, myVerticalGap, labelOnTop);
  }

  public FormBuilder addLabeledComponent(String labelText, JComponent component) {
    return addLabeledComponent(labelText, component, myVerticalGap, false);
  }

  public FormBuilder addLabeledComponent(String labelText, JComponent component, final int topInset) {
    return addLabeledComponent(labelText, component, topInset, false);
  }

  public FormBuilder addLabeledComponent(String labelText, JComponent component, boolean labelOnTop) {
    return addLabeledComponent(labelText, component, myVerticalGap, labelOnTop);
  }

  public FormBuilder addLabeledComponent(String labelText, JComponent component, final int topInset, boolean labelOnTop) {
    JLabel label = null;
    if (labelText != null) {
      label = new JLabel(UIUtil.removeMnemonic(labelText));
      final int index = UIUtil.getDisplayMnemonicIndex(labelText);
      if (index != -1) {
        label.setDisplayedMnemonic(labelText.charAt(index + 1));
      }
      label.setLabelFor(component);
    }

    return addLabeledComponent(label, component, topInset, labelOnTop);
  }

  public FormBuilder addComponent(JComponent component) {
    return addLabeledComponent((JLabel)null, component, myVerticalGap, false);
  }

  public FormBuilder addComponent(JComponent component, final int topInset) {
    return addLabeledComponent((JLabel)null, component, topInset, false);
  }

  public FormBuilder addSeparator(final int topInset) {
    return addComponent(new JSeparator(), topInset);
  }

  public FormBuilder addSeparator() {
    return addSeparator(myVerticalGap);
  }

  public FormBuilder addVerticalGap(final int height) {
    return addLabeledComponent((JLabel)null,
                               new Box.Filler(new Dimension(0, height), new Dimension(0, height), new Dimension(Short.MAX_VALUE, height)));
  }

  public FormBuilder addLabeledComponent(@Nullable JComponent label, JComponent component, int topInset, boolean labelOnTop) {
    GridBagConstraints c = new GridBagConstraints();
    topInset = myLineCount > 0 ? topInset : 0;

    if (myVertical || labelOnTop || label == null) {
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = myLineCount;
      c.weightx = 0;
      c.weighty = 0;
      c.fill = NONE;
      c.anchor = WEST;
      c.insets = new Insets(topInset, myIndent, DEFAULT_VGAP, 0);

      if (label != null) myPanel.add(label, c);

      c.gridx = 0;
      c.gridy = myLineCount + 1;
      c.weightx = 1.0;
      c.weighty = getWeightY(component);
      c.fill = getFill(component);
      c.anchor = WEST;
      c.insets = new Insets(label == null ? topInset : 0, myIndent, 0, 0);

      myPanel.add(component, c);

      myLineCount += 2;
    }
    else {
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = myLineCount;
      c.weightx = 0;
      c.weighty = 0;
      c.fill = NONE;
      c.anchor = myAlignLabelOnRight ? EAST : WEST;
      c.insets = new Insets(topInset, myIndent, 0, myHorizontalGap);

      myPanel.add(label, c);

      c.gridx = 1;
      c.weightx = 1;
      c.weighty = getWeightY(component);
      c.fill = getFill(component);
      c.anchor = WEST;
      c.insets = new Insets(topInset, myIndent, 0, 0);

      myPanel.add(component, c);

      myLineCount++;
    }

    return this;
  }

  private static int getFill(JComponent value) {
    if (value instanceof JComboBox) {
      return NONE;
    }
    else if (value instanceof JScrollPane) return BOTH;
    return HORIZONTAL;
  }

  private static int getWeightY(JComponent value) {
    if (value instanceof JScrollPane) return 1;
    return 0;
  }

  public JPanel getPanel() {
    return myPanel;
  }

  public int getLineCount() {
    return myLineCount;
  }

  public FormBuilder setAlignLabelOnRight(boolean alignLabelOnRight) {
    myAlignLabelOnRight = alignLabelOnRight;
    return this;
  }

  public FormBuilder setVertical(boolean vertical) {
    myVertical = vertical;
    return this;
  }

  public FormBuilder setVerticalGap(int verticalGap) {
    myVerticalGap = verticalGap;
    return this;
  }

  public FormBuilder setHorizontalGap(int horizontalGap) {
    myHorizontalGap = horizontalGap;
    return this;
  }

  public FormBuilder setIndent(int indent) {
    myIndent = indent;
    return this;
  }
}
