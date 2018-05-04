package com.dls.aa.tableview;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class TableUtils {

  /**
   * Install the keyboard handler:
   * + CTRL + C = copy to clipboard
   */
  public static void installCopyPasteHandler(TreeTableView<?> table) {

    // install copy/paste keyboard handler
    table.setOnKeyPressed(new TableKeyEventHandler());

  }

  /**
   * Copy/Paste keyboard event handler.
   * The handler uses the keyEvent's source for the clipboard data. The source must be of type TableView.
   */
  public static class TableKeyEventHandler implements EventHandler<KeyEvent> {

    KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C,
        KeyCombination.CONTROL_ANY);

    public void handle(final KeyEvent keyEvent) {

      if (copyKeyCodeCompination.match(keyEvent)) {

        if (keyEvent.getSource() instanceof TreeTableView) {

          // copy to clipboard
          copySelectionToClipboard((TreeTableView<?>) keyEvent.getSource());

          System.out.println("Selection copied to clipboard");

          // event is handled, consume it
          keyEvent.consume();

        }

      }

    }

  }

  /**
   * Get table selection and copy it to the clipboard.
   */
  public static void copySelectionToClipboard(TreeTableView<?> table) {

    StringBuilder clipboardString = new StringBuilder();

    ObservableList<? extends TreeTablePosition<?, ?>> positionList = table.getSelectionModel().getSelectedCells();


    int prevRow = -1;

    for (TreeTablePosition<?, ?> position : positionList) {

      int row = position.getRow();
      int col = position.getColumn();

      Object cell = (Object) table.getColumns().get(col).getCellData(row);

      // null-check: provide empty string for nulls
      if (cell == null) {
        cell = "";
      }

      // determine whether we advance in a row (tab) or a column
      // (newline).
      if (prevRow == row) {

        clipboardString.append('\t');

      } else if (prevRow != -1) {

        clipboardString.append('\n');

      }

      // create string from cell
      String text = cell.toString();

      // add new item to clipboard
      clipboardString.append(text);

      // remember previous
      prevRow = row;
    }

    // create clipboard content
    final ClipboardContent clipboardContent = new ClipboardContent();
    clipboardContent.putString(clipboardString.toString());

    // set clipboard content
    Clipboard.getSystemClipboard().setContent(clipboardContent);
  }
}
