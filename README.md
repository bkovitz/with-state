# with-state

The with-state macro: when '->' isn't good enough.

## Usage

Add this dependency to your project.clj or build.boot file:

    [farg/with-state "0.0.1-SNAPSHOT"]

To access `with-state`:

    (require '[farg.with-state :refer [with-state]])

(with-state [state-variable expr]
  body ...)

Threads a state variable as the first argument through all the expressions
in body (like ->) but the state variable is named and taken from a binding
vector, the state variable is rebound to the result of each expression (like
as->), and some common Clojure functions are redefined inside with-state to
enable many common operations that can't be done with ->. with-state returns
the value of the state variable when the last expression in body terminates or
the argument to a 'return' statement if reached (see below).

For example, this expression:

    (with-state [state {:a 0, :b 0}]
      (doseq [x [1 2 3 4]]         ;doseq is rewritten as reduce
        (update :a #(+ % x))       ;'state' is implicit first argument
        (when (odd? x)             ;conditional execution
          (update :b #(+ % x)))))  ;'state' is implicit first argument

  returns {:a 10, :b 4}.

  The following are redefined inside with-state:

    (if c t f)
      Conditional execution. c is not rewritten. t and f are rewritten the
      same as any other with-state line.

    (when c exprs ...)
      Conditional execution. c is not rewritten. exprs are rewritten the same
      as any other with-state line.

    (when-let [v c] exprs ...)
      Conditional execution. c is not rewritten. If c is logical true, it
      is assigned to v and exprs are executed. exprs are rewritten the same
      as any other with-state line.

    (return x)
      Premature exit from with-state, returning x. If invoked inside
      conditionally or repeatedly executed code as in an 'if', 'when', or
      'doseq', exits the entire with-state.

    (apply args ...)
      The state variable is not inserted into an 'apply'. The state variable
      variable is rebound to the result of the 'apply'.

    (doseq bindings exprs ...)
      Loops variables through values as in ordinary 'doseq'. The expressions
      assigned in the bindings are not rewritten. exprs are rewritten the same
      as any other with-state line.

    (dotimes [i n] exprs ...)
      Executes exprs n times, successively assigning the numbers 0..n to i
      on each iteration of the loop. n is not rewritten. exprs are rewritten
      the same as any other with-state line.

    (while c exprs ...)
      Executes exprs repeatedly as long as c is logical true. c is not
      rewritten. exprs are rewritten the same as any other with-state line.

    (setq v expr)
      Rewrites expr as normal. expr is expected to return a pair [state x],
      where 'state' is the new value of the state variable and 'x' is the
      value to assign to v.

    (bind v expr)
      Sets v to the value of expr. Does not rewrite expr.

    -- expr
      expr is not rewritten and the state variable is not bound to its result.
      This makes it easy to insert println statements or other statements
      that you don't want to affect the state variable."

## License

Copyright © 2018 Ben Kovitz

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
