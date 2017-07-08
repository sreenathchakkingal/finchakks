package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

class Node
{
	Node left;
	Node right;
	int data;
	
	public Node(Node left, Node right, int data) {
		this.left = left;
		this.right = right;
		this.data = data;
	}
	
	public String toString()
	{
		return String.valueOf(this.data);
	}
}

public class Tester {
	
	
	public static void main(String[] args) throws Exception
	{
		Node one= new Node(null, null,1);
		Node seven= new Node(null, null,7);
		Node four= new Node(null, null,4);
        Node three= new Node(null, null,3);
        Node five = new Node(one, null,5);
        Node six= new Node(null, seven,6);
        Node eight= new Node(four, three,8);
        Node nine= new Node(five, six,9);
        Node root= new Node(eight, nine,10);
//        new Tester().top_view(root.left, root.right);
        new Tester().queue_top_view(root);
	}
	void queue_top_view(Node node)
	{
		
		List<Node> arrayList = new ArrayList();
		arrayList.add(node);
		while(arrayList.size()>0)
		{
			System.out.print(arrayList.get(0).data+" ");
			if(arrayList.get(0).left!=null)
			{
				arrayList.add(arrayList.get(0).left);
			}
			if(arrayList.get(0).right!=null)
			{
				arrayList.add(arrayList.get(0).right);
			}
			arrayList.remove(arrayList.get(0));
		}
	}
	
	void printList(List<Node> arrayList)
	{
		for (Node n : arrayList)
		{
			System.out.println(n.data);
		}
	}
	
	void top_view(Node leftNode, Node rightNode)
	{
		if(leftNode==null && rightNode==null)
		{
			return;
		}
		if (leftNode!=null)
		{
			System.out.print(leftNode.data+" ");
		}
		if (rightNode!=null)
		{
			System.out.print(rightNode.data+" ");
		}
//
//		System.out.println("invoking top_view with: "+leftNode.left+" : "+leftNode.right);
		Node n1=leftNode==null || leftNode.left==null ? null: leftNode.left;
		Node n2=leftNode==null || leftNode.right==null ? null: leftNode.right;
		
		top_view(n1, n2);
		
		Node n3=rightNode==null || rightNode.left==null ? null: rightNode.left;
		Node n4=rightNode==null || rightNode.right==null ? null: rightNode.right;
		
		top_view(n3, n4);
	}
}

