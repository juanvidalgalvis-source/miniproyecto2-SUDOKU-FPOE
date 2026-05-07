package com.example.sudokump1.model;

/**
 * Represents a single Sudoku cell (logic-only).
 */
public class SudokuCell {
    // Current value stored in the cell (0 means empty).
    private int value;

    // If true, the user cannot modify this cell (initial clues).
    private boolean fixed;

    // If true, this cell violates Sudoku rules (duplicate in row/column/block).
    private boolean invalid;

    // Logical position of the cell (row index in [0..5]).
    private final int row;

    // Logical position of the cell (column index in [0..5]).
    private final int column;

    public SudokuCell(int row, int column) {
        this(row, column, 0, false);
    }

    public SudokuCell(int row, int column, int value, boolean fixed) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.fixed = fixed;
        this.invalid = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (!fixed) {
            this.value = value;
            // invalid flag is handled by the model during validation
        }
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}

