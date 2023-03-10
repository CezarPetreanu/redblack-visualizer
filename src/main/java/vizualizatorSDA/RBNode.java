package vizualizatorSDA;

import javax.swing.tree.DefaultMutableTreeNode;

public class RBNode {
	private int key;
	private DefaultMutableTreeNode jTreeNode;
	private RBNode left;
	private RBNode right;
	private RBNode parent;
	private RBNodeColor color;
	private boolean nullFlag;
	
	public RBNode(int key) {
		this.key = key;
		this.jTreeNode = new DefaultMutableTreeNode(key);
		this.left = null;
		this.right = null;
		this.parent = null;
		this.color = RBNodeColor.BLACK;
		this.nullFlag = true;
	}
	public RBNode() {
		this.key = 0;
		this.jTreeNode = new DefaultMutableTreeNode("null");
		this.left = null;
		this.right = null;
		this.parent = null;
		this.color = RBNodeColor.BLACK;
		this.nullFlag = true;
	}

	public int getKey() {
		return key;
	}
	public DefaultMutableTreeNode getTreeNode() {
		return jTreeNode;
	}
	public void setKey(int key) {
		this.key = key;
	}

	public RBNode getLeft() {
		return left;
	}
	public void setLeft(RBNode left) {
		this.left = left;
	}

	public RBNode getRight() {
		return right;
	}
	public void setRight(RBNode right) {
		this.right = right;
	}

	public RBNode getParent() {
		return parent;
	}
	public void setParent(RBNode parent) {
		this.parent = parent;
	}

	public RBNodeColor getColor() {
		return color;
	}
	public void setColor(RBNodeColor color) {
		this.color = color;
	}

	public void setNull(boolean n) {
		this.nullFlag = n;
	}
	public boolean isNull() {
		return nullFlag;
	}
	
	@Override
	public String toString() {
		return "Node [key=" + key + ", color=" + color + "]";
	}
}
