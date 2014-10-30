package com.example.surfacetest;

public class Message {
	private String msg;
	private int numParts;
	private int subSize = 48;
	
	public Message(String s) {
		msg = s;
		numParts = (s.length() / subSize)+1;
	}
	
	public String[] getParts() {
		int subStart = 0;
		int subEnd = subSize;
		
		String[] msgParts = new String[numParts];
		int it = 0;
		while (subEnd <= numParts*subSize) {
			int end;
			if (subEnd < msg.length())
				end = subEnd;
			else
				end = msg.length();
			msgParts[it] = msg.substring(subStart,end);
			subStart = subEnd;
			subEnd += subSize;
			it++;
		}
		return msgParts;
	}
}
