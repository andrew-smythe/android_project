package com.example.surfacetest;

import java.util.Queue;

public class MoveQueue {
	private Queue<MoveTuple> mq;
	
	public void addToQueue(char c, int i) {
		// add the move to the queue
		mq.add(new MoveTuple(c, i));
	}
	
	public MoveTuple getMove() {
		return(mq.poll());
	}
	
	// defines a tuple with a character for the axis, "X" or "Y", and
	// an integer length for the move
	public class MoveTuple {
		private char axis;
		private int length;
		
		public MoveTuple(char c, int i) {
			axis = c;
			length = i;
		}
		
		public char getAxis() {
			return axis;
		}
		
		public int getLength() {
			return length;
		}
	}
}
