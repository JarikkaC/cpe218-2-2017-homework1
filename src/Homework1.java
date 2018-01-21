import java.util.Stack;

public class Homework1 {

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
		ExpressionTree T = new ExpressionTree(input);
		TreeNode root = T.constructTree();
		T.inorder(root);
		System.out.printf("infix = ");
		T.infix(root);
		System.out.printf(" = " + T.Calculate(root));

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


		public double Calculate(TreeNode ptr)
		{
			if (ptr.left == null && ptr.right == null)
				return toDigit(ptr.c);
			else
			{
				double result = 0.0;
				double left = Calculate(ptr.left);
				double right = Calculate(ptr.right);
				char operator = ptr.c;

				switch (operator)
				{
					case '+' : result = left + right; break;
					case '-' : result = left - right; break;
					case '*' : result = left * right; break;
					case '/' : result = left / right; break;
					default  : result = left + right; break;
				}
				return result;
			}
		}

		private boolean isDigit(char ch)
		{
			return ch >= '0' && ch <= '9';
		}
		private int toDigit(char ch)
		{
			return ch - '0';
		}

	}
	}
