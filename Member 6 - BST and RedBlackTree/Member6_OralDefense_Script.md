# Member 6 — Oral Defense Script (Learn This, Don't Read It)

Say this in your own words. Each section is a chunk you can rehearse separately.

---

## 1. What is my part of the system? (30 seconds)

"I built the location index for our cleaning service — a Binary Search Tree and a
Red-Black Tree that store all 50+ campus locations, sorted by location code. When
the routing team or the priority scheduler needs details about a location — like
'UG-C-01' — they search my tree instead of scanning a list. That's O(log n) instead
of O(n), which matters once you're checking locations repeatedly during dispatch."

---

## 2. Why two trees, not just one? (30 seconds)

"I built a plain BST first because it's the foundation — easy to understand, easy to
insert, delete, and search. But a plain BST has a weakness: if you insert data that's
already sorted or near-sorted — say locations get added in ID order as the campus
grows — the tree degenerates into basically a straight line. Instead of O(log n)
search, you get O(n), same as searching a list. That defeats the whole point of using
a tree.

The Red-Black Tree fixes that. It's a self-balancing BST — every time you insert, it
checks if the tree became unbalanced and fixes it immediately using rotations and
recoloring. I can prove this: I inserted 15 locations in sorted order. A plain BST
gave height 14 — basically a list. My Red-Black Tree gave height 5. Same data,
completely different performance."

---

## 3. How does insertion work? (BST — 30 seconds)

"Insertion is simple: compare the new key to the current node. If it's smaller, go
left. If it's bigger, go right. Keep going until you hit an empty spot, and put the
new node there. That's it — no balancing, no rotation. It's a plain comparison walk
down the tree."

---

## 4. How does insertion work? (RBT — this is the one to really know, 60 seconds)

"Every node in a Red-Black Tree is colored red or black. New nodes always start red.
After I insert like a normal BST, I check: did I just create two red nodes in a row
(a red node with a red parent)? That's not allowed — it's one of the rules that keeps
the tree balanced.

If there's a violation, I look at the 'uncle' node — my parent's sibling:
- If the uncle is red too, I just recolor: flip parent and uncle to black, flip
  grandparent to red, and move up the tree to check again.
- If the uncle is black or missing, I can't just recolor — I need a rotation. I
  rotate the grandparent to restructure the tree, then fix the colors so the black-height
  stays balanced.

At the very end, no matter what happened, I force the root to be black. That's a rule
that never breaks."

*If they ask you to trace one on the spot: draw 3 nodes, insert them in ascending
order, and walk through exactly this logic out loud. Practice this once with pen and
paper before the defense — don't just memorize the words.*

---

## 5. Why does this matter for OUR project specifically? (20 seconds)

"Our system genuinely could insert locations in a predictable order — building codes
by zone, alphabetically, whatever pattern campus planning uses. That's exactly the
case that breaks a plain BST. Using a Red-Black Tree means our location lookups stay
fast no matter how the data was entered, which matters because Member 9's routing
and Member 5's dispatch both depend on my tree being fast."

---

## 6. If they ask "why not just use a Hash Table?" (20 seconds)

"A hash table gives faster average lookup, that's true. But it doesn't keep anything
sorted — I can't ask it 'give me all locations between UG-C-01 and UG-N-01' or 'print
every location in order.' My tree can do range queries and sorted traversal in one
pass. If the reporting module ever needs locations in order, or a range of a
particular zone, a hash table can't do that without extra work."

---

## 7. If they ask "what's the proof this actually works, not just theory?" (15 seconds)

"I didn't just write the code — I ran it. I have console output showing height 14
for a plain BST versus height 5 for the Red-Black Tree on the same 15-item sorted
insert. I also have 20 passing test cases covering normal inserts, empty-tree edge
cases, and invalid input like null keys."

---

## Quick facts to have cold (in case of a direct question)

- BST worst case: O(n) — degenerates when data is inserted in sorted order
- BST average case: O(log n)
- RBT guaranteed worst case: O(log n) — always, because of the balancing rules
- Red-Black rules: (1) every node red or black, (2) root is always black,
  (3) no red node has a red child, (4) every path from root to a null leaf has the
  same number of black nodes
- My tree is keyed by `locationId` (e.g., "UG-C-01") using `compareTo`

---

## If you freeze on the rotation logic, fall back to this simple version

"A rotation just swaps who's the parent and who's the child between two nodes,
while keeping the BST ordering rule intact — smaller keys still end up on the left,
bigger on the right. It's a local restructuring, not a full rebuild. I use it to
shorten a path that got too long on one side."

That sentence alone will get you through most follow-up pressure.
