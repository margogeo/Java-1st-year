%hard version 14
data(Key, Value).
node(data, Prior, node, node).

merge(null, NodeR, NodeR) :- !.
merge(NodeL, null, NodeL) :- !.
merge(node(DataL, PriorL, NodeLL, NodeRL), node(DataR, PriorR, NodeLR, NodeRR), node(DataL, PriorL, NodeLL, NewR)) :- 
      PriorL > PriorR, merge(NodeRL, node(DataR, PriorR, NodeLR, NodeRR), NewR), !.
merge(node(DataL, PriorL, NodeLL, NodeRL), node(DataR, PriorR, NodeLR, NodeRR), node(DataR, PriorR, NewL, NodeRR)) :- 
      PriorL =< PriorR, merge(node(DataL, PriorL, NodeLL, NodeRL), NodeLR, NewL), !.

split(null, _, null, null) :- !.
split(node(data(Newkey, Newvalue), Newprior, NewnodeL, NewnodeR), Skey, node(data(Newkey, Newvalue), Newprior, NewnodeL, TansL), AnsR) :- 
      Newkey < Skey, split(NewnodeR, Skey, TansL, AnsR), !.
split(node(data(Newkey, Newvalue), Newprior, NewnodeL, NewnodeR), Skey, AnsL, node(data(Newkey, Newvalue), Newprior, TansR, NewnodeR)) :- 
      Newkey >= Skey, split(NewnodeL, Skey, AnsL, TansR), !.

add(null, Data, node(Data, Y, null, null)) :- rand_int(100000, Y), !.
add(Tree, data(Key, Value), Ans) :-
    split(Tree, Key, L1, R1), split(R1, Key + 1, L2, R2), rand_int(100000, Y), 
	merge(L1, node(data(Key, Value), Y, null, null), Tmp), merge(Tmp, R2, Ans), !.

del(Tree, Key, Ans) :- split(Tree, Key, L, R), split(R, Key + 1, M, RIGHT), merge(L, RIGHT, Ans).

down(node(data(Newkey, _), _, NewnodeL, NewnodeR), Key, NewnodeL) :- Newkey > Key, !.
down(node(data(Newkey, _), _, NewnodeL, NewnodeR), Key, NewnodeR) :- Newkey =< Key, !.

test(node(Data, _, _, _), Data) :- !.
test(In, data(Key, Value)):- down(In, Key, Out), test(Out, data(Key, Value)), !.

build([], Ans, Ans) :- !.
build([(Key, Value)], Tree, Ans) :- add(Tree, data(Key, Value), Ans),!.
build([(Key, Value) | Map], Tree, Ans) :- add(Tree, data(Key, Value), Buf), build(Map, Buf, Ans),!.

map_build(Map, Ans) :- build(Map, null, Ans).
map_remove(Trem, Key, Result) :- del(Trem, Key, Result), !.
map_put(Tree, Key, Value, Ans) :- add(Tree, data(Key, Value), Ans), !.
map_get(Tree, Key, Value) :- test(Tree, data(Key, Value)), !.

map_maxKey(node(data(Key, _), _, _, null), Key) :- !.
map_maxKey(node(_, _, _, R), ANS) :- map_maxKey(R, ANS), !.

map_minKey(node(data(Key, _), _, null, _), Key) :- !.
map_minKey(node(_, _, L, _), ANS) :- map_minKey(L, ANS), !.

map_ceilingKey(Map, Key, CeilingKey) :- split(Map, Key, _, R), map_minKey(R, CeilingKey), !.