// Student Name: Kenneth Riles

import java.util.*;

/* This program creates a Red-Black Tree */
public class RB_Tree<E extends Comparable<E>> {
	
	Node<E> root;
	int size = 0;

	@SuppressWarnings("hiding")
	private class Node<E> {
		E value;
		Node<E> left;
		Node<E> right;
		Node<E> parent;
		boolean isBlack;
		boolean isRed;
		
		Node(E key)
		{
			value = key;
			left = null;
			right = null;
		}
		
		public int compareTo(E key)
		{
			if ((int)this.value < (int)key)
			{
				return -1;
			}
			
			else if ((int)this.value > (int)key)
			{
				return 1;
			}
			
			else
			{
				return 0;
			}
		}
		
		
		public void setParent(Node<E> parent)
		{
			this.parent = parent;
		}
		
		
		public boolean isLeaf()
		{
			return ((this.left == null) && (this.right == null));
		}
		
		
		public boolean hasOneChild()
		{
			return ((this.left == null) || (this.right == null)); 
		}
		
		
		public boolean hasTwoChildren()
		{
			return ((this.left != null) && (this.right != null));
		}
	}
		
	
   // Inserts a node with the given key
	public void insert(E key)
	{	
			if (root == null)
			{
				root = new Node<>(key);
				size++;
				root.setParent(null);
				root.isBlack = true;
				return;
			}
		
			insert(root, key);
			
	}
	
	
   // Insert helper-method
	private void insert(Node<E> current, E key)
	{
		if (current.compareTo(key) > 0)
		{
			if (current.left != null)
			{
				insert(current.left, key);
			}
		
			else  
			{
				current.left = new Node<>(key);
				current.left.isRed = true;
				current.left.setParent(current);
				size++;
				checkForColor(current.left);
			}
		}
		
		else
		{
			if (current.right != null)
			{
				insert(current.right, key);
			}
		
			else  
			{
				current.right = new Node<>(key); 
				current.right.isRed = true;
				current.right.setParent(current);
				size++;
				checkForColor(current.right);
			}
		}
		
		return;
	}
	
	
	
   // Deletes a node with the given key
	public void delete(E key)
	{
		if (root == null)
		{
			return;
		}
		
		Node<E> current = root;
		current.setParent(null);;
		while (current != null)
		{	
			Node<E> parent = current;
			if (current.compareTo(key) > 0)
			{
				current = current.left;
			}
			
			else if (current.compareTo(key) < 0)
			{
				current = current.right;
			}
			
			current.setParent(parent);
			
		   // If leaf node
			if (current.isLeaf() && (current.compareTo(key) == 0))
			{
				
			   // checks if black node has two children (if so, it is Double-Black)
				if (current.isBlack)
				{
					current.isBlack = false;
					current.isRed = false;
					
					if (current.compareTo(parent.value) > 0)
					{
						if (parent.left.left == null)
						{
						   // checks if rotation is necessary or not and then recolors
							if (parent.left.isLeaf())
							{
								parent.right = current.right;
								recolorParentAndChild(parent, parent.left);
							}
							
							else
							{
								rotateLeftRight(parent);
								parent = current;
								current = parent.left;
								current.setParent(parent);
							}
						}
						
						else
						{
							rotateRightRight(parent);
						}
					}
					
					else
					{
						if (parent.right.right == null)
						{
							if (parent.right.isLeaf())
							{
								parent.left = current.left;
								recolorParentAndChild(parent, parent.right);
							}
							
							else
							{
								rotateRightLeft(parent);
								parent = current;
								current = parent.right;
								current.setParent(parent);
							}
						}
						
						else
						{
							rotateLeftLeft(parent);
						}
					}
				}
				
				else
				{
					if (current.compareTo(current.parent.value) > 0)
					{
						current.parent.right = current.right;
					}
				
					else
					{
						current.parent.left = current.left;
					}
				}
				
				size--;
				return;
			}
			
			
		   // If contains one child
			else if (current.hasOneChild() && (current.compareTo(key) == 0))
			{
				if ((current.compareTo(parent.value) > 0))
				{
					if (current.left == null)
					{
						current.parent.right = current.right;
						current.right = null;
					}
				
					else
					{
						current.parent.right = current.left;
						current.left = null;
					}
					
					current = parent.right;
				}
			
				else
				{
					if (current.right == null)
					{
						current.parent.left = current.left;
						current.left = null;
					}
				
					else
					{
						current.parent.left = current.right;
						current.right = null;
					}
					
					current = parent.left;  
				}
				
				if (current.hasOneChild())
				{
					current.isRed = false;
					current.isBlack = true;
				}
				
				size--;
				return;
			}
		
			
		   // If contains two children
			else if (((current.compareTo(key) == 0)) && current.hasTwoChildren())
			{
				Node<E> temp;
				temp = findSuccessor(current);
				E newLeaf = current.value;
				current.value = temp.value;
				temp.value = newLeaf;
				
				if (current.left == temp)
				{
					current.left = temp.right;
				}
				
				else
				{
				   // check if successor has left child
					if (temp.left != null)
					{
						Node<E> newParent = current.left;
						newParent.right = temp.left;
					}
					
					else
					{
						temp.parent.right = temp.right;
					}
				}
				
				if (current.hasOneChild())
				{
					current.isRed = false;
					current.isBlack = true;
				}
				
				size--;
				return;
			}
			}
		}
	
	
	   // checks child node and parent node for red-to-red relation
		private void checkForColor(Node<E> child)
		{
			Node<E> parent = child.parent;
			Node<E> grandParent = parent.parent;
				
			if (parent.isRed)
			{
				if (grandParent.compareTo(child.value) < 0)
				{
					if ((grandParent.left == null) || (grandParent.left.isBlack))
					{
						if (child == parent.left)
						{
							rotateRightLeft(child);
							parent = child;
							child = parent.right;
							child.setParent(parent);
						}
						
						else
						{
							rotateLeftLeft(grandParent);
						}
						
						recolorParentAndChild(parent, parent.left);
					}
					
					else 
					{
						if (grandParent != root)
						{
							grandParent.isRed = true;
							grandParent.isBlack = false;
						}
						
						recolorParentAndUncle(parent, grandParent.left);
						search(grandParent.value);
						
					   // check if grandparent does not have red-to-red relation
						if ((grandParent.isRed) && (grandParent.parent.isRed))
						{
							checkForColor(grandParent);
						}
					}	
					
					checkForColor(child);
				}
				
				else
				{
					if ((grandParent.right == null) || (grandParent.right.isBlack))
					{
						if (child == parent.right)
						{
							rotateLeftRight(child);
							parent = child;
							child = parent.left;
							child.setParent(parent);
						}
						
						else
						{
							rotateRightRight(grandParent);
						}
						
						recolorParentAndChild(parent, parent.right);
					}
					
					else
					{
						if (grandParent != root)
						{
							grandParent.isRed = true;
							grandParent.isBlack = false;
						}
						
						recolorParentAndUncle(parent, grandParent.right);
						search(grandParent.value);
						
						if ((grandParent.isRed) && (grandParent.parent.isRed))
						{
							checkForColor(grandParent);
						}
					}
					
					checkForColor(child);
				}
			}
		}
		
		
	  // Recolors parent and child after rotation
		private void recolorParentAndChild(Node<E> parent, Node<E> child)
		{
			child.setParent(parent);
			parent.isRed = false;
			parent.isBlack = true;
			child.isBlack = false;
			child.isRed = true;
		}
		
		
	  // Recolors parent and parent sibling(uncle) when rotation is not needed
		private void recolorParentAndUncle(Node<E> parent, Node<E> uncle)
		{
			uncle.isBlack = true;
			uncle.isRed = false;
			parent.isBlack = true;
			parent.isRed = false;
		}
		
	
	// Four different rotations: Left-Left, Right-Right, Left-Right, Right-Left
		private void rotateRightRight(Node<E> current)
		{
			Node<E> newParent = current.left;
			Node<E> newChild = newParent.right;
			newParent.right = current;
			current.left = newChild;
			
			if (current == root)
			{
				root = newParent;
				current.setParent(null);
			}
			
			else if (current.compareTo(current.parent.value) > 0)
			{
			    current.parent.right = newParent;
			}
			
			else 
			{
				current.parent.left = newParent;
			}	
		}
		
		
		private void rotateLeftLeft(Node<E> current)
		{
			Node<E> newParent = current.right;
			Node<E> newChild = newParent.left;
			newParent.left = current;
			current.right = newChild;
			
			
			if (current == root)
			{
				root = newParent;
				current.setParent(null);
			}
			
			else if (current.compareTo(current.parent.value) < 0)
			{
				current.parent.left = newParent;
			}
			
			else
			{
				current.parent.right = newParent;
			}
		}
		
		
		private void rotateLeftRight(Node<E> current)
		{	
			rotateLeftLeft(current.parent.parent.left);
			rotateRightRight(current.parent.parent);
		}
		
		
		private void rotateRightLeft(Node<E> current)
		{
			rotateRightRight(current.parent.parent.right);
			rotateLeftLeft(current.parent.parent);
		}
	
	
	// Searches for inorder-successor for deletion algorithm
	public Node<E> findSuccessor(Node<E> current)
	{
		current = current.left;
		while (current.right != null)
		{
			Node<E> parent = current;
			current = current.right;
			current.setParent(parent);
		}
		
		return current;
	}
	
	
   // Searches for key, if it exists
	public boolean search(E key)
	{
		Node<E> current = root;
		while (current != null)
		{
			Node<E> parent = current;
			if (current.compareTo(key) > 0)
			{
				current = current.left;
			}
			
			else if (current.compareTo(key) < 0)
			{
				current = current.right;
			}
			
			else
			{
				return true;
			}
			
			current.setParent(parent);
		}
		
		return false;
	}
	

	
   // Prints Tree nodes with values
	public void printTree()
	{
		Queue<Node<E>> queue = new LinkedList<>();
		Node<E> current = root;
		queue.offer(current);
		int level = 0;
		while (!(queue.isEmpty()))
		{
			int qSize = queue.size();
			level++;
			System.out.print("Level " + level + ":  ");
			while (qSize > 0)
			{
				qSize--;
				Node<E> temp = queue.poll();
				System.out.print(temp.value + "    ");
				if (temp.left != null)
				{
					queue.offer(temp.left);
				} 
				
				if (temp.right != null)
				{
					queue.offer(temp.right);
				}
			}
			
			System.out.print("\n\n");
		}
	}
	
	
	public static void main(String args[])
	{
		RB_Tree<Integer> tree = new RB_Tree<>();
		
	   // Performs insertion operations
		tree.insert(14);
		tree.insert(95);
		tree.insert(71);
		tree.insert(36);
		tree.insert(25);
		tree.insert(88);
		tree.insert(27);
		tree.insert(33);
		tree.insert(79);
		tree.insert(81);
		tree.insert(63);
		tree.insert(26);
		tree.insert(30);
		tree.insert(44);
		tree.insert(62);
		
		System.out.print("Tree after insertion:\n\n");
		tree.printTree();
		
	  // Performs deletion operations
		tree.delete(88);
		tree.delete(30);
		tree.delete(27);
		tree.delete(79);
		tree.delete(62);
		tree.delete(44);  
		
		System.out.print("Tree after deletion:\n\n");
		tree.printTree();
	}
}
