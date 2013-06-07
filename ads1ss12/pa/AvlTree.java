package ads1ss12.pa;

/**
 * AVL-Baum-Klasse die die fehlenden Methoden aus {@link AbstractAvlTree}
 * implementiert.
 * 
 * <p>
 * In dieser Klasse m&uuml;ssen Sie Ihren Code einf&uuml;gen und die Methoden
 * {@link #remove remove()} sowie {@link #rotateLeft rotateLeft()} und
 * {@link #rotateRight rotateRight()} implementieren.
 * </p>
 * 
 * <p>
 * Sie k&ouml;nnen beliebige neue Variablen und Methoden in dieser Klasse
 * hinzuf&uuml;gen. Wichtig ist nur, dass die oben genannten Methoden
 * implementiert werden.
 * </p>
 */
public class AvlTree extends AbstractAvlTree {

	/**
	 * Der Default-Konstruktor.
	 * 
	 * Ruft einfach nur den Konstruktor der Oberklasse auf.
	 */
	public AvlTree() {
		super();
	}

	/**
	 * F&uuml;gt ein Element mit dem Schl&uuml;ssel <code>k</code> ein.
	 * 
	 * <p>
	 * Existiert im AVL-Baum ein Knoten mit Schl&uuml;ssel <code>k</code>, soll
	 * <code>insert()</code> einfach nichts machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Einf&uuml;gen muss sichergestellt sein, dass es sich bei dem
	 * resultierenden Baum immer noch um einen AVL-Baum handelt, und dass
	 * {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des
	 * Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel der eingef&uuml;gt werden soll.
	 * @see AbstractAvlTree#insert
	 */
	public void insert(int k) {
		AvlNode newNode = new AvlNode(k);
		newNode.balance = 1; // Balance beinhaltet die Höhe.
		insert(root, newNode);
	}

	/**
	 * Rekursives Einfügen des Kontens q in einen Baum mit der Wurzel p.
	 *
	 * @param p Wurzel des Teilbaums.
	 * @param q Der Knoten der eingefügt werden soll.
	 */
	private void insert(AvlNode p, AvlNode q) {
		if(p == null) {
			// Sonderfall: Baum ist leer.
			root = q;
		}
		else {
			if(q.key < p.key) {
				if(p.left != null) {
					insert(p.left, q);
				}
				else {
					// Basecase: Einfügen des Knotens.
					q.parent = p;
					p.left = q;
				}
			}
			else if(q.key > p.key) {
				if(p.right != null) {
					insert(p.right, q);
				}
				else {
					// Basecase: Einfügen des Knotens.
					q.parent = p;
					p.right = q;
				}
			}
			rebalance(p);
		}
	}

	/**
	 * Entfernt den Knoten mit Schl&uuml;ssel <code>k</code> falls er existiert.
	 * 
	 * <p>
	 * Existiert im AVL-Baum kein Knoten mit Schl&uuml;ssel <code>k</code>, soll
	 * <code>remove()</code> einfach nichts machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Entfernen muss sichergestellt sein, dass es sich bei dem
	 * resultierenden Baum immer noch um einen AVL-Baum handelt, und dass
	 * {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des
	 * Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel dessen Knoten entfernt werden soll.
	 * 
	 * @see AbstractAvlTree#root
	 * @see #rotateLeft rotateLeft()
	 * @see #rotateRight rotateRight()
	 */
	public void remove(int k) {
		AvlNode removeNode = search(root, k);
		if(removeNode != null) {
			remove(removeNode);
		}
	}

	/**
	 * Entfernt den Knoten q aus Baum.
	 *
	 * @param q Knoten der entfernt werden soll.
	 */
	private void remove(AvlNode q) {
 		// Knoten der entfernt werden soll. 
		AvlNode r = null; 
		
		// Fall 1|2: q hat max einen Nachfolger. Wird selbst entfernt.
		if(q.left == null || q.right == null) {
			r = q;
		}
		// Fall 3: q hat 2 Nachfolger. Wird durch Successor ersetzt.
		else {
			r = successor(q);
			q.key = r.key;
		}
		
		// p soll auf Kind von r verweisen (null wenn r keine Kinder hat)
		AvlNode p = null;
		if(r.left != null) {
			p = r.left;
		}
		else {
			p = r.right;
		}
		
		// Erzeugt einen Verweis von p auf seinen neuen Vorgänger.
		if(p != null) {
			p.parent = r.parent;
		}

		// Hänge p anstelle von r in den Baum ein.
		if(r.parent == null) {
			// Sonderfall: Die Wurzel soll entfernt werden.
			// Kann nur der Fall sein wenn weniger als 2 Knoten im Baum sind,
			// deshalb keine Rebalancierung nötig.
			root = p;
		}
		else {
			if(r == r.parent.left) {
				r.parent.left = p;
			}
			else {
				r.parent.right = p;
			}

			// Rebalancierung
			while(r.parent != null) {
				rebalance(r.parent);
				r = r.parent;
			}
		}
	}

	/**
	 * Rebalanciert einen einzelnen Knoten, basierend auf der Höhe (balance)
	 * seiner beiden Nachfolger.
	 * 
	 * @param p Der zu balancierende Knoten.
	 */
	private void rebalance(AvlNode p) {

		// Setze aktuelle Höhe des Knotens.
		p.balance = height(p);

		// Fall 1
		if(bal(p) <= -2) {
			int r = p.left.right != null ? p.left.right.balance : 0;
			int l = p.left.left != null ? p.left.left.balance : 0;
		
			// Fall 1.2 auf 1.1 zurück führen
			if(l < r) {
				p.left = rotateLeft(p.left);
			}
		
			// Fall 1.1 behandeln
			if(p.parent == null) {
				// Sonderfall: Wurzel wird rebalanciert.
				root = rotateRight(p);
				root.parent = null;
			}
			else {
				if(p.parent.right == p) {
					p.parent.right = rotateRight(p);
				}
				else if(p.parent.left == p) {
					p.parent.left = rotateRight(p);
				}
			}
		}
		
		// Fall 2
		else if(bal(p) >= 2) {
			int r = p.right.right != null ? p.right.right.balance : 0;
			int l = p.right.left != null ? p.right.left.balance : 0;

			// Fall 2.2 auf 2.1 zurück führen
			if(r < l) {
				p.right = rotateRight(p.right);
			}

			// Fall 2.1 behandeln
			if(p.parent == null) {
				// Sonderfall: Wurzel wird rebalanciert.
				root = rotateLeft(p);
				root.parent = null;
			}
			else {
				if(p.parent.right == p) {
					p.parent.right = rotateLeft(p);
				}
				else if(p.parent.left == p) {
					p.parent.left = rotateLeft(p);
				}
			}
		}
	}

	/**
	 * F&uuml;hrt eine Links-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param u
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	public AvlNode rotateLeft(AvlNode u) {
		AvlNode v = u.right;
		v.parent = u.parent;

		u.right = v.left;
		if(v.left != null) {
			v.left.parent = u;
		}
		v.left = u;
		u.parent = v;

		u.balance = height(u);
		v.balance = height(v);

		return v;
	}

	/**
	 * F&uuml;hrt eine Rechts-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param u
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	public AvlNode rotateRight(AvlNode u) {
		AvlNode v = u.left;
		v.parent = u.parent;

		u.left = v.right;
		if(v.right != null) {
			v.right.parent = u;
		}
		v.right = u;
		u.parent = v;

		u.balance = height(u);
		v.balance = height(v);

		return v;
	}

	/**
	 * Durchsucht einen Baum mit Wurzel r nach dem Schlüssel s.
	 *
	 * @param r Die Wurzel des (Teil-)Baums in dem gesucht werden soll. 
	 * @param s Der Schlüssel nach dem gesucht werden soll.
	 * @return 
	 *   Der Knoten mit Schlüssel s oder null falls dieser nicht vorhanden ist.
	 */
	private AvlNode search(AvlNode r, int s) {
		while(r != null && r.key != s) {
			if(s < r.key) {
				r = r.left;
			}
			else {
				r = r.right;
			}
		}
		return r;
	}

	/**
	 * Liefert die Balance eines Knotens.
	 *
	 * @param p Der Knoten für den die Balance berechnet werden soll.
	 * @return Die Balance des Knotens.
	 */
	private int bal(AvlNode p) {
		int r = 0;
		int l = 0;

		if(p.left != null) {
			l = p.left.balance;
		}
		if(p.right != null) {
			r = p.right.balance;
		}

		return r - l;
	}

	/**
	 * Liefert das Maximum der Höhe seiner Nachfolger + 1.
	 *
	 * @param p Der Knoten für den die Höhe bestimmt werden soll.
	 * @return Die Höhe des Knotens.
	 */
	private int height(AvlNode p) {
		int r = 0;
		int l = 0;

		if(p.left != null) {
			l = p.left.balance;
		}
		if(p.right != null) {
			r = p.right.balance;
		}

		return (r > l ? r : l) + 1;
	}

	private AvlNode minimum(AvlNode r) {
		if(r == null) {
			return null;
		}
		while(r.left != null) {
			r = r.left;
		}
		return r;
	}

	private AvlNode maximum(AvlNode r) {
		if(r == null) {
			return null;
		}
		while(r.right != null) {
			r = r.right;
		}
		return r;
	}

	private AvlNode successor(AvlNode p) {
		AvlNode q = null;

		if(p.right != null) {
			q = minimum(p.right);
		}
		else {
			q = p.parent;
			while(q != null && p == q.right) {
				p = q;
				q = q.parent;
			}
		}

		return q;
	}

	private AvlNode predecessor(AvlNode p) {
		AvlNode q = null;

		if(p.left != null) {
			q = maximum(p.left);
		}
		else {
			q = p.parent;
			while(q != null && p == q.left) {
				p = q;
				q = q.parent;
			}
		}

		return q;
	}
}
