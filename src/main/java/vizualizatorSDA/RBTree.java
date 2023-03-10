package vizualizatorSDA;

public class RBTree {
	private RBNode root;
	
	public RBTree() {
		root = new RBNode();
		root.setNull(true);
	}
	
	public RBNode getRoot() {
		return root;
	}
	
	// Query methods
	public RBNode search(int key) {
		RBNode x = root;
		while(!x.isNull() && x.getKey() != key) {
			if(key < x.getKey())
				x = x.getLeft();
			else
				x = x.getRight();
		}
		return x;
	}
	public RBNode max() {
		RBNode x = root;
		while(!x.getRight().isNull())
			x = x.getRight();
		return x;
	}
	public RBNode max(RBNode n) {
		RBNode x = n;
		while(!x.getRight().isNull())
			x = x.getRight();
		return x;
	}
	public RBNode min() {
		RBNode x = root;
		while(!x.getLeft().isNull())
			x = x.getLeft();
		return x;
	}
	public RBNode min(RBNode n) {
		RBNode x = n;
		while(!x.getLeft().isNull())
			x = x.getLeft();
		return x;
	}
	public RBNode successor(RBNode n) {
		if(n.isNull())
			return null;
		RBNode x = n;
		if(!x.getRight().isNull())
			return min(x.getRight());
		RBNode y = x.getParent();
		while(!y.isNull() && x == y.getRight()) {
			x = y;
			y = y.getParent();
		}
		return y;
	}
	public RBNode predecessor(RBNode n) {
		if(n.isNull())
			return null;
		RBNode x = n;
		if(!x.getLeft().isNull())
			return max(x.getLeft());
		RBNode y = x.getParent();
		while(!y.isNull() && x == y.getLeft()) {
			x = y;
			y = y.getParent();
		}
		return y;
	}
	
	// Modification methods
	public void rotateLeft(RBNode x) {
		RBNode y = x.getRight();
		x.setRight(y.getLeft());
		if(!y.getLeft().isNull())
			y.getLeft().setParent(x);
		y.setParent(x.getParent());
		if(x.getParent().isNull())
			root = y;
		else if(x == x.getParent().getLeft())
			x.getParent().setLeft(y);
		else
			x.getParent().setRight(y);
		y.setLeft(x);
		x.setParent(y);
	}
	
	public void rotateRight(RBNode x) {
		RBNode y = x.getLeft();
		x.setLeft(y.getRight());
		if(!y.getRight().isNull())
			y.getRight().setParent(x);
		y.setParent(x.getParent());
		if(x.getParent().isNull())
			root = y;
		else if(x == x.getParent().getRight())
			x.getParent().setRight(y);
		else
			x.getParent().setLeft(y);
		y.setRight(x);
		x.setParent(y);
	}
	
	public void add(RBNode z) {	
		RBNode y = new RBNode();
		RBNode x = root;
		
		z.setNull(false);
		
		while(!x.isNull()) {
			y = x;
			if(z.getKey() < x.getKey())
				x = x.getLeft();
			else
				x = x.getRight();
		}
		z.setParent(y);
		if(y.isNull())
			root = z;
		else if(z.getKey() < y.getKey())
			y.setLeft(z);
		else
			y.setRight(z);
		z.setLeft(new RBNode());
		z.setRight(new RBNode());
		z.getLeft().setParent(z);
		z.getRight().setParent(z);
		z.setColor(RBNodeColor.RED);
		
		addFixup(z);
	}
	
	private void addFixup(RBNode z) {
		while(z.getParent().getColor() == RBNodeColor.RED) {
			if(z.getParent() == z.getParent().getParent().getLeft()) {
				RBNode y = z.getParent().getParent().getRight();
				if(y.getColor() == RBNodeColor.RED) {
					z.getParent().setColor(RBNodeColor.BLACK);
					y.setColor(RBNodeColor.BLACK);
					z.getParent().getParent().setColor(RBNodeColor.RED);
				}
				else {
					if(z == z.getParent().getRight()) {
						z = z.getParent();
						rotateLeft(z);
					}
					z.getParent().setColor(RBNodeColor.BLACK);
					z.getParent().getParent().setColor(RBNodeColor.RED);
					rotateRight(z.getParent().getParent());
				}
			}
			else {
				RBNode y = z.getParent().getParent().getLeft();
				if(y.getColor() == RBNodeColor.RED) {
					z.getParent().setColor(RBNodeColor.BLACK);
					y.setColor(RBNodeColor.BLACK);
					z.getParent().getParent().setColor(RBNodeColor.RED);
				}
				else {
					if(z == z.getParent().getLeft()) {
						z = z.getParent();
						rotateRight(z);
					}
					z.getParent().setColor(RBNodeColor.BLACK);
					z.getParent().getParent().setColor(RBNodeColor.RED);
					rotateLeft(z.getParent().getParent());
				}
			}
		}
		root.setColor(RBNodeColor.BLACK);
	}
	
	public RBNode remove(RBNode z) {
		RBNode deleted = z;
		if(!search(z.getKey()).isNull()) {
			RBNode y = z;
			RBNode x = null;
			RBNodeColor yOriginalColor = y.getColor();
			if(z.getLeft().isNull()) {
				x = z.getRight();
				transplant(z, z.getRight());
			}
			else if(z.getRight().isNull()) {
				x = z.getLeft();
				transplant(z, z.getLeft());
			}
			else {
				y = min(z.getRight());
				yOriginalColor = y.getColor();
				x = y.getRight();
				if(y.getParent() == z)
					x.setParent(y);
				else {
					transplant(y, y.getRight());
					y.setRight(z.getRight());
					y.getRight().setParent(y);
				}
				transplant(z, y);
				y.setLeft(z.getLeft());
				y.getLeft().setParent(y);
				y.setColor(z.getColor());
			}
			
			if(yOriginalColor == RBNodeColor.BLACK)
				removeFixup(x);
			return deleted;
		}
		return null;
	}
	private void removeFixup(RBNode x) {
		RBNode w = new RBNode();
		while(x != root && x.getColor() == RBNodeColor.BLACK) {
			if(x == x.getParent().getLeft()) {
				w = x.getParent().getRight();
				if(w.getColor() == RBNodeColor.RED) {
					w.setColor(RBNodeColor.BLACK);
					x.getParent().setColor(RBNodeColor.RED);
					rotateLeft(x.getParent());
					w = x.getParent().getRight();
				}
				if(w.getLeft().getColor() == RBNodeColor.BLACK && w.getRight().getColor() == RBNodeColor.BLACK) {
					w.setColor(RBNodeColor.RED);
					x = x.getParent();
				}
				else {
					if(w.getRight().getColor() == RBNodeColor.BLACK) {
						w.getLeft().setColor(RBNodeColor.BLACK);
						w.setColor(RBNodeColor.RED);
						rotateRight(w);
						w = x.getParent().getRight();
					}
					w.setColor(x.getParent().getColor());
					x.getParent().setColor(RBNodeColor.BLACK);
					w.getRight().setColor(RBNodeColor.BLACK);
					rotateLeft(x.getParent());
					x = root;
				}
			}
			else {
				w = x.getParent().getLeft();
				if(w.getColor() == RBNodeColor.RED) {
					w.setColor(RBNodeColor.BLACK);
					x.getParent().setColor(RBNodeColor.RED);
					rotateRight(x.getParent());
					w = x.getParent().getLeft();
				}
				if(w.getRight().getColor() == RBNodeColor.BLACK && w.getLeft().getColor() == RBNodeColor.BLACK) {
					w.setColor(RBNodeColor.RED);
					x = x.getParent();
				}
				else {
					if(w.getLeft().getColor() == RBNodeColor.BLACK) {
						w.getRight().setColor(RBNodeColor.BLACK);
						w.setColor(RBNodeColor.RED);
						rotateLeft(w);
						w = x.getParent().getLeft();
					}
					w.setColor(x.getParent().getColor());
					x.getParent().setColor(RBNodeColor.BLACK);
					w.getLeft().setColor(RBNodeColor.BLACK);
					rotateRight(x.getParent());
					x = root;
				}
			}
		}
		
		x.setColor(RBNodeColor.BLACK);
	}
	

	public void transplant(RBNode u, RBNode v) {
		if(u.getParent().isNull())
			root = v;
		else if(u == u.getParent().getLeft())
			u.getParent().setLeft(v);
		else
			u.getParent().setRight(v);
		if(!v.isNull())
			v.setParent(u.getParent());
	}
	
	// Printing methods
	public void printPreorder(RBNode n) {
		if(!n.isNull()) {
			RBNode x = n;
			System.out.println(x.getKey());
			printPreorder(x.getLeft());
			printPreorder(x.getRight());
		}
	}
	public void printPreorder() {
		printPreorder(root);
	}
	
	public void printInorder(RBNode n) {
		if(!n.isNull()) {
			RBNode x = n;
			printInorder(x.getLeft());
			System.out.println(x.getKey());
			printInorder(x.getRight());
		}
	}
	public void printInorder() {
		printInorder(root);
	}
	
	public void printPostorder(RBNode n) {
		if(!n.isNull()) {
			RBNode x = n;
			printPostorder(x.getLeft());
			printPostorder(x.getRight());
			System.out.println(x.getKey());
		}
	}
	public void printPostorder() {
		printPostorder(root);
	}
}
