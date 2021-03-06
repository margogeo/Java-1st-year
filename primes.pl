nth_prime(N, P) :- b(N, P).
prime(N) :- a(N).
composite(N) :- \+ 1 is N, \+ prime(N).

prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :- number(N), get(N, 1, H, 2), N1 is div(N, H), prime_divisors(N1, T), !.
get(N, B, H, C) :- 0 is mod(N, C), H is C; C * C > N, H is N; B1 is B + 1, nth_prime(B1, C1), get(N, B1, H, C1).
prime_divisors(N, [H | T]) :- order(H , T), prime_divisors(N1, T), N is N1 * H.							
order(V, []).
order(V, [H | T]) :- V =< H.