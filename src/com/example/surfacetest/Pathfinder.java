package com.example.surfacetest;

import java.util.ArrayList;
import java.util.Stack;

import android.util.Log;

/*
 * Uses an implementation of the A* pathfinding algorithm
 * to determine a shortest path from the current position to a
 * selected position.
 */
public class Pathfinder {
	
	// open list of nodes that can be checked for a path
	private ArrayList<Node> openList;
	
	// closed list of nodes -- have already been traversed
	private ArrayList<Node> closedList;
	
	// ful map of nodes
	private Node[][] map;
	
	public int calcGScore(Node n) {
		int score = 0;
		
		// recursively count the steps back to the parent
		if (n.getParent() != null)
			score = 1+ this.calcGScore(n.getParent());
		
		return score;
	}
	
	public int calcHScore(int x, int y, Node b) {
		return (Math.abs(x - b.getX()) + Math.abs(y - b.getY()));
	}
	
	public int calcFScore(int x, int y, Node n) {
		return (calcGScore(n) + calcHScore(x, y, n));
	}
	
	// find the node with the lowest F score
	public Node getLowestF(ArrayList<Node> list, int targetX, int targetY) {
		
		// holds the index of the node with the lowest F score
		int lowestIndex = 0;
		
		// temporary node
		Node temp;
		
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			
			// check if the current node has a lower F score than the
			// current lowest one
			if (calcFScore(targetX, targetY, temp) < 
					calcFScore(targetX, targetY, list.get(lowestIndex))) {
				lowestIndex = i;
			}
		}
		
		// return the node with the lowest F score
		return (list.get(lowestIndex));
	}
	
	/*
	 * Node sub-class -- simply a tuple of two integers (x, y coords).
	 * This will be used for finding paths. Also has a boolean value for
	 * whether the node can be "walked on" or not.
	 */
	public class Node {
		private int x;
		private int y;
		private boolean walkable;
		private Node parent;
		
		public Node(int x, int y, boolean w) {
			this.x = x;
			this.y = y;
			this.walkable = w;
			parent = null;
		}
		
		public void setParent(Node n) {
			parent = n;
		}
		
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public boolean getWalkable() {
			return walkable;
		}
		public Node getParent() {
			return parent;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this.x == ((Node)o).x && this.y == ((Node)o).y)
				return true;
			return false;
		}
	}
	
	/*
	 * Takes in an array of tiles, constructs nodes, and then begins
	 * pathfinding. Returns a stack of ints that will represent each move
	 * the actor must take to follow this path.
	 */
	public Stack<Integer> findPath(Tile[][] t, int mapSize, int startX, int startY, 
									int targetX, int targetY) {
		// Create an empty stack of moves
		Stack<Integer> moveStack = new Stack<Integer>();
		
		// create new empty lists
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		
		// if the target isn't a walkable tile, check for a spot around it
		if (!t[targetX+1][targetY+1].isWalkable()) {
			if (targetY+1 < mapSize && t[targetX+1][targetY+2].isWalkable()) {
				targetY++;
			}
			else if (targetX+2 < mapSize && t[targetX+2][targetY+1].isWalkable()) {
				targetX++;
			}
			else if (targetX > 0 && t[targetX][targetY+1].isWalkable()) {
				targetX--;
			}
			else if (targetY > 0 && t[targetX+1][targetY].isWalkable()) {
				targetY--;
			}
			else
				return moveStack;
		}
		
		// create the map
		Log.d("Creating map of nodes, size: ", ""+mapSize);
		map = new Node[mapSize][mapSize];
		
		if (map == null)
			Log.d("This is", "Bad.");
		
		// create each node
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				map[i][j] = new Node(i, j, t[i+1][j+1].isWalkable());
				if (map[i][j] == null)
					Log.d("This is", "Bad.");
			}
		}
		
		// add the starting point to the open list
		Log.d("Staring node: ", "X: "+startX+", Y: "+startY);
		openList.add(map[startX][startY]);
		Node currNode = null;
		
		// false as long as the target has not been found
		boolean foundTarget = false;
		
		// loop until the target is found, or we have checked each node without
		// finding our target (ie. it is determined to be unreachable)
		while (openList.size() > 0 && !foundTarget) {
			
			// get the current node in the open list -- that which has the lowest
			// F score
			currNode = getLowestF(openList, targetX, targetY);
			
			// remove current node from open list, add to closed list
			openList.remove(currNode);
			closedList.add(currNode);
			
			// we have found our target, and completed the path
			if (currNode.getX() == targetX && currNode.getY() == targetY) {
				foundTarget = true;
			}
			else {
				
				// try adding the node above to the open list
				if (currNode.getY()-1 >= 0 && map[currNode.getX()][currNode.getY()-1].getWalkable() == true) {
					if (!openList.contains(map[currNode.getX()][currNode.getY()-1]) &&
							!closedList.contains(map[currNode.getX()][currNode.getY()-1])) {
						// make the parent of the node equal to the current node
						map[currNode.getX()][currNode.getY()-1].setParent(currNode);
						openList.add(map[currNode.getX()][currNode.getY()-1]);
					}
					else {
						if (!closedList.contains(map[currNode.getX()][currNode.getY()-1])) {
							// calculate the current G score of this node
							int g1 = calcGScore(map[currNode.getX()][currNode.getY()-1]);
							
							// save the old parent, and change it to the current node
							Node oldParent = map[currNode.getX()][currNode.getY()-1].getParent();
							map[currNode.getX()][currNode.getY()-1].setParent(currNode);
							
							// check if this change gives a lower G score. If it does, keep as is,
							// otherwise change the parent back to its old one -- its current path
							// is already more efficient
							int g2 = calcGScore(map[currNode.getX()][currNode.getY()-1]);
							
							// change the parent back
							if (g1 < g2) {
								map[currNode.getX()][currNode.getY()-1].setParent(oldParent);
							}
						}
					}
				}
				
				// try adding the node to the right to the open list
				if (currNode.getX()+1 < mapSize && map[currNode.getX()+1][currNode.getY()].getWalkable() == true) {
					if (!openList.contains(map[currNode.getX()+1][currNode.getY()]) &&
							!closedList.contains(map[currNode.getX()+1][currNode.getY()])) {
						// make the parent of the node equal to the current node
						map[currNode.getX()+1][currNode.getY()].setParent(currNode);
						openList.add(map[currNode.getX()+1][currNode.getY()]);
					}
					else {
						if (!closedList.contains(map[currNode.getX()+1][currNode.getY()])) {
							// calculate the current G score of this node
							int g1 = calcGScore(map[currNode.getX()+1][currNode.getY()]);
							
							// save the old parent, and change it to the current node
							Node oldParent = map[currNode.getX()+1][currNode.getY()].getParent();
							map[currNode.getX()+1][currNode.getY()].setParent(currNode);
							
							// check if this change gives a lower G score. If it does, keep as is,
							// otherwise change the parent back to its old one -- its current path
							// is already more efficient
							int g2 = calcGScore(map[currNode.getX()+1][currNode.getY()]);
							
							// change the parent back
							if (g1 < g2) {
								map[currNode.getX()+1][currNode.getY()].setParent(oldParent);
							}
						}
					}
				}
				
				// try adding the node below to the open list
				if (currNode.getY()+1 < mapSize && map[currNode.getX()][currNode.getY()+1].getWalkable() == true) {
					if (!openList.contains(map[currNode.getX()][currNode.getY()+1]) &&
							!closedList.contains(map[currNode.getX()][currNode.getY()+1])) {
						// make the parent of the node equal to the current node
						map[currNode.getX()][currNode.getY()+1].setParent(currNode);
						openList.add(map[currNode.getX()][currNode.getY()+1]);
					}
					else {
						if (!closedList.contains(map[currNode.getX()][currNode.getY()+1])) {
							// calculate the current G score of this node
							int g1 = calcGScore(map[currNode.getX()][currNode.getY()+1]);
							
							// save the old parent, and change it to the current node
							Node oldParent = map[currNode.getX()][currNode.getY()+1].getParent();
							map[currNode.getX()][currNode.getY()+1].setParent(currNode);
							
							// check if this change gives a lower G score. If it does, keep as is,
							// otherwise change the parent back to its old one -- its current path
							// is already more efficient
							int g2 = calcGScore(map[currNode.getX()][currNode.getY()+1]);
							
							// change the parent back
							if (g1 < g2) {
								map[currNode.getX()][currNode.getY()+1].setParent(oldParent);
							}
						}
					}
				}
				
				// try adding the node to the left to the open list
				if (currNode.getX()-1 >= 0 && map[currNode.getX()-1][currNode.getY()].getWalkable() == true) {
					if (!openList.contains(map[currNode.getX()-1][currNode.getY()]) &&
							!closedList.contains(map[currNode.getX()-1][currNode.getY()])) {
						// make the parent of the node equal to the current node
						map[currNode.getX()-1][currNode.getY()].setParent(currNode);
						openList.add(map[currNode.getX()-1][currNode.getY()]);
					}
					else {
						if (!closedList.contains(map[currNode.getX()-1][currNode.getY()])) {
							// calculate the current G score of this node
							int g1 = calcGScore(map[currNode.getX()-1][currNode.getY()]);
							
							// save the old parent, and change it to the current node
							Node oldParent = map[currNode.getX()-1][currNode.getY()].getParent();
							map[currNode.getX()-1][currNode.getY()].setParent(currNode);
							
							// check if this change gives a lower G score. If it does, keep as is,
							// otherwise change the parent back to its old one -- its current path
							// is already more efficient
							int g2 = calcGScore(map[currNode.getX()-1][currNode.getY()]);
							
							// change the parent back
							if (g1 < g2) {
								map[currNode.getX()-1][currNode.getY()].setParent(oldParent);
							}
						}
					}
				}
			}
		}
		
		if (foundTarget) {
			// The current node is the target -- construct our move stack
			
			// add a move to the stack for each node that has a parent
			while (currNode.getParent() != null) {
				Node parent = currNode.getParent();
				
				// add a move up to the stack
				if (parent.getX() == currNode.getX() && parent.getY() > currNode.getY()) {
					moveStack.push(1);
				}
				
				// add a move down to the stack
				if (parent.getX() == currNode.getX() && parent.getY() < currNode.getY()) {
					moveStack.push(3);
				}
				
				// add a move left to the stack
				if (parent.getX() > currNode.getX() && parent.getY() == currNode.getY()) {
					moveStack.push(4);
				}
				
				// add a move right to the stack
				if (parent.getX() < currNode.getX() && parent.getY() == currNode.getY()) {
					moveStack.push(2);
				}
				
				// move to the next node
				currNode = parent;
			}
			
			// return the stack of moves
			return moveStack;
		}
		
		// otherwise, we will return nothing
		return moveStack;
	}
	
}
