package vizualizatorSDA;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionListener;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JTextPane;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private RBTree rb;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		
		rb = new RBTree();
		final JTree tree = new JTree(new DefaultMutableTreeNode("Red Black Tree"));
		
		setTitle("Red Black Tree Visualizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		tabbedPane.addTab("Red Black Tree", null, splitPane, null);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		final JLabel lblMessage = new JLabel("...");
		panel_1.add(lblMessage, BorderLayout.SOUTH);
		
		panel_1.add(tree, BorderLayout.CENTER);
		
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(!textField.getText().isBlank()) {
						int key = Integer.parseInt(textField.getText());
						rb.add(new RBNode(key));
						textField.setText("");
						updateTree(tree);
						lblMessage.setText("Added node " + key + ".");
					}
						
				}catch(Exception exc){
					lblMessage.setText("Node keys must be integers.");
				}
			}
		});
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!textField.getText().isBlank()) {
						int key = Integer.parseInt(textField.getText());
						if(!rb.search(key).isNull()) {
							rb.remove(rb.search(key));
							textField.setText("");
							updateTree(tree);
							lblMessage.setText("Removed node " + key + ".");
						}
						else
							lblMessage.setText("Node " + key + " not found.");
					}
				}catch(Exception exc) {
					lblMessage.setText("Node keys must be integers.");
				}
			}
		});
		panel.add(btnRemove);
			
		final JTextPane textPaneProperties = new JTextPane();
		textPaneProperties.setText("Properties");
		textPaneProperties.setEditable(false);
		splitPane.setRightComponent(textPaneProperties);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				textPaneProperties.setText("Properties");
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(selectedNode != null && !selectedNode.isRoot()) {
					int nodeKey = Integer.parseInt((selectedNode.toString()).substring(0, (selectedNode.toString()).length()-2));
					textField.setText(Integer.toString(nodeKey));
					
					String color;
					if(selectedNode.toString().charAt(selectedNode.toString().length()-1) == '\u25cb') color = "RED";
					else color = "BLACK";
					
					boolean isLeaf = selectedNode.isLeaf();
					boolean isRoot = ((DefaultMutableTreeNode) selectedNode.getParent()).isRoot();
					
					String parent = "null";
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
					if(!isRoot)
						parent = parentNode.toString().substring(0, parentNode.toString().length()-2);
					
					String left = "null";
					String right = "null";
					if(selectedNode.getChildCount() != 0) {
						String child1 = selectedNode.getFirstChild().toString();
						String child2 = selectedNode.getLastChild().toString();
						child1 = child1.substring(0, child1.length()-2);
						child2 = child2.substring(0, child2.length()-2);
						int key1 = Integer.parseInt(child1);
						int key2 = Integer.parseInt(child2);
						if(key1 == key2) {
							if(key1 < nodeKey)
								left = Integer.toString(key1);
							else
								right = Integer.toString(key1);
						}
						else if(key1 < key2) {
							left = Integer.toString(key1);
							right = Integer.toString(key2);
						}
						else {
							left = Integer.toString(key2);
							right = Integer.toString(key1);
						}
					}
					
					textPaneProperties.setText(
							"Properties\n"
							+ "\nKey: "     + nodeKey
							+ "\nLeft: "    + left
							+ "\nRight: "   + right
							+ "\nParent: "  + parent
							+ "\nColor: "   + color
							+ "\nIs Root: " + isRoot
							+ "\nIs Leaf: " + isLeaf
							);
				}			
			}
		});
	}
	
	private void updateTree(JTree tree) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload();
		if(rb.getRoot() != null && !rb.getRoot().isNull()) {
			root.add(buildTree());
			for(int i=0; i<tree.getRowCount(); i++)
				tree.expandRow(i);
		}
	}
	
	private String getColorChar(RBNode n) {
		if(n.getColor() == RBNodeColor.RED)
			return " \u25cb";
		return " \u25cf";
	}
	
	private DefaultMutableTreeNode buildTree() {
		String nodeName = rb.getRoot().getKey()+getColorChar(rb.getRoot());
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeName);
		if(rb.getRoot().getLeft() != null && rb.getRoot().getRight() != null) {
			buildPreorder(rb.getRoot().getLeft(), node);
			buildPreorder(rb.getRoot().getRight(), node);
		}
		return node;
	}
	private void buildPreorder(RBNode node, DefaultMutableTreeNode treeNode) {
		if(node != null && !node.isNull()) {
			String nodeName = node.getKey()+getColorChar(node);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
			treeNode.add(newNode);
			buildPreorder(node.getLeft(), newNode);
			buildPreorder(node.getRight(), newNode);
		}
	}

}
