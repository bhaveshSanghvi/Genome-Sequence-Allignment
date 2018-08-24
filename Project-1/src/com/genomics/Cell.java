package com.genomics;

public class Cell {
	private int substitution;
	private int deletion;
	private int insertion;
	
	public Cell() {
		
	}
	
	public Cell(int substitution, int deletion, int insertion) {
		this.substitution = substitution;
		this.deletion = deletion;
		this.insertion = insertion;
	}

	public int getSubstitution() {
		return substitution;
	}

	public void setSubstitution(int substitution) {
		this.substitution = substitution;
	}

	public int getDeletion() {
		return deletion;
	}

	public void setDeletion(int deletion) {
		this.deletion = deletion;
	}

	public int getInsertion() {
		return insertion;
	}

	public void setInsertion(int insertion) {
		this.insertion = insertion;
	}

	@Override
	public String toString() {
		return "Cell [substitution=" + substitution + ", deletion=" + deletion + ", insertion=" + insertion + "]";
	}
}
