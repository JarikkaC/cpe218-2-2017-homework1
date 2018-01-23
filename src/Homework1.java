
import sun.reflect.generics.tree.Tree;

import java.util.Stack;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;



public class Homework1  extends JPanel
		implements TreeSelectionListener {

            static ExpressionTree T;
            static TreeNode root;

			JTree tree;
			JEditorPane htmlPane;

			DefaultMutableTreeNode currentNode;
			DefaultMutableTreeNode top;

	public Homework1(){

		super(new GridLayout(1,0));
		//Create the nodes.
		top = new DefaultMutableTreeNode(root.c);
		createNodes(top,root);

		//Create a tree that allows one selection at a time.
		tree = new JTree(top);

		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);

		tree.putClientProperty("JTree.lineStyle","None");
		ImageIcon NodeIcon =  createImageIcon("middle.gif");
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(NodeIcon);
		renderer.setClosedIcon(NodeIcon);
		tree.setCellRenderer(renderer);

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();

		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);

	}


	public static class TreeNode {
		public TreeNode right;
		public TreeNode left;
		public char c;

		TreeNode(char c) {
			this.c = c;
			this.left = left;
			this.right = right;
		}

		public String toString() {
			return (right == null && left == null) ? Character.toString(c) : "(" + left.toString()+ c + right.toString() + ")";
		}
	}

	public static void main(String[] args) {

		String input = "251-*32*+";

		if(args.length>0) input=args[0];

		T = new ExpressionTree(input);
		root = T.constructTree();
		T.inorder(root);
		System.out.printf("infix = ");
		T.infix(root);
		System.out.printf(" = " + T.calculate(root));

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
								createAndShowGUI();
			}
		});

	}



	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Homework1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		Homework1 newContentPane = new Homework1();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}



    private void createNodes(DefaultMutableTreeNode top , TreeNode t) {

        if(t.left!=null)
        {
            DefaultMutableTreeNode L = new DefaultMutableTreeNode(t.left.c);
            top.add(L);
            createNodes(L,t.left);
        }
        if(t.right!=null)
        {
            DefaultMutableTreeNode R = new DefaultMutableTreeNode(t.right.c);
            top.add(R);
            createNodes(R,t.right);
        }
    }



    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Homework1.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    public void valueChanged(TreeSelectionEvent tse) {

        currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

        tree.getLastSelectedPathComponent();
        if(currentNode == null){
            return;
        }

        String text = inorder(currentNode);
        if(!currentNode.isLeaf()) text += "=" + Calculate(currentNode);
        htmlPane.setText(text);

    }



    public String inorder(DefaultMutableTreeNode node) {
        if (node == null) return "";
        if(node == currentNode && !node.isLeaf()) {
            return 	inorder(node.getNextNode()) + node.toString() + inorder(node.getNextNode().getNextSibling());
        }else if(T.isOperator(node.toString().charAt(0)) && node != top) {
            return "(" + inorder(node.getNextNode()) + node.toString() + inorder(node.getNextNode().getNextSibling()) + ")";
        }else {
            return node.toString();
        }
    }


    public double Calculate(DefaultMutableTreeNode node) {
        if(node.isLeaf()) return Integer.parseInt(node.toString());
        double sum = 0.0;
        switch(node.toString()) {
            case "-" : sum = Calculate(node.getNextNode()) - Calculate(node.getNextNode().getNextSibling()); break;
            case "+" : sum = Calculate(node.getNextNode()) + Calculate(node.getNextNode().getNextSibling()); break;
            case "*" :sum = Calculate(node.getNextNode()) * Calculate(node.getNextNode().getNextSibling()); break;
            case "/" :sum = Calculate(node.getNextNode()) / Calculate(node.getNextNode().getNextSibling()); break;
            default : sum = Calculate(node.getNextNode()) + Calculate(node.getNextNode().getNextSibling()); break;
        }
        return sum;
    }



	public static class ExpressionTree {

		public String postfix;
		public TreeNode root;
		public int sum;

		public ExpressionTree(String postfix){
			this.postfix = postfix;
		}

		public boolean isOperator(char c) {
			if (    c == '+' ||
					c == '-' ||
					c == '*' ||
					c == '/'
					) {
				return true;
			}
			return false;
		}

		// Utility function to do inorder traversal
		public void inorder(TreeNode t) {
			if (t != null) {
				inorder(t.left);
				inorder(t.right);
			}
		}

		public void infix(TreeNode t) {
			System.out.print(t);
		}

		// Returns root of constructed tree for given
		// postfix expression
		TreeNode constructTree(){
			Stack<TreeNode> st = new Stack();
			TreeNode t, t1, t2;


			for (int i = 0; i < postfix.length(); i++) {
				char c = postfix.charAt(i);

				if (!isOperator(c)) {
					t = new TreeNode(c);
					st.push(t);
				} else // operator
				{
					t = new TreeNode(c);

					// Pop two top nodes
					// Store top
					t1 = st.pop();      // Remove top
					t2 = st.pop();

					//  make them children
					t.right = t1;
					t.left = t2;

					// System.out.println(t1 + "" + t2);
					// Add this subexpression to stack
					st.push(t);
				}
			}

			//  only element will be root of expression
			// tree
			t = st.peek();
			st.pop();

			return t;
		}


		public double calculate(TreeNode ptr) {
			if (ptr.left == null && ptr.right == null)
				return toDigit(ptr.c);
			else {
				double result = 0.0;
				double left = calculate(ptr.left);
				double right = calculate(ptr.right);
				char operator = ptr.c;

				switch (operator) {
					case '+' : result = left + right; break;
					case '-' : result = left - right; break;
					case '*' : result = left * right; break;
					case '/' : result = left / right; break;
					default  : result = left + right; break;
				}
				return result;
			}
		}

		private boolean isDigit(char ch) {
			return ch >= '0' && ch <= '9';
		}
		private int toDigit(char ch) {
			return ch - '0';
		}
	}

}
