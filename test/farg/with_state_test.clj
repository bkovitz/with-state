(ns farg.with-state-test
  (:require [clojure.test :refer :all]
            [farg.with-state :refer [with-state]]))

(defn bump-even? [state]
  [(inc state) (even? state)])

(deftest test-with-state
  (is (= ["1 true" "2" "4 false" "4"]
         (clojure.string/split-lines (with-out-str
           (println
             (with-state [state 0]
               (setq ev (bump-even?))
               -- (println state ev)
               inc
               -- (println state)
               inc
               (setq ev bump-even?)
               -- (println state ev))))))))

(deftest test-do-nothing
  (is (= 16 (with-state [state 16]))))

(deftest test-bind
  (is (= 10 (with-state [state 6]
              (bind x 4)
              (+ x)))))

(deftest test-doseq
  (is (= 10 (with-state [state 0]
              (doseq [x [1 2 3 4]]
                (+ x))))))

(deftest test-doseq-keys
  (is (= [[:a 1] [:b 2]]
         (with-state [state []]
           (doseq [{:keys [k v]} [{:k :a, :v 1} {:k :b, :v 2}]]
             (conj [k v]))))))

(deftest test-apply
  (is (= 8 (with-state [state 5]
             (apply + [1 2])))))

(deftest test-if
  (is (= 9 (with-state [state 0]
             (inc)
             (if (zero? state)
                 (+ 1000)
                 (+ 0))
             (if (= 1 state)
                 (+ 8)
                 (+ 100))
             (if (> state 10)
                 (+ 200)
                 (+ 0))))))

(deftest test-return
  (is (= [:returned 2] (with-state [state 0]
                         (inc)
                         (if (even? state)
                             (return [:returned state])
                             (inc))
                         (if (even? state)
                             (return [:returned state])
                             (inc))
                         (if (even? state)
                             (return [:returned state])
                             (inc))))))

(deftest test-when
  (is (= [:returned 5] (with-state [state 0]
                         (doseq [x (repeat 20 1)]
                           (inc)
                           (when (= 5 state)
                             (return [:returned state])))))))

(deftest test-when-let
  (is (= [:returned 5]
         (with-state [state 0]
           (doseq [x (repeat 20 1)]
             (inc)
             (when-let [result (and (= 5 state) [:returned state])]
               (return result)))))))

(deftest test-dotimes
  (is (= [:did :did :did]
         (with-state [state []]
           (dotimes [_ 3] (conj :did))))))

(deftest test-while
  (is (= [0 1 2 :done]
         (:result (with-state [state {:n 0 :result []}]
           (while (< (:n state) 3)
             (update :result #(conj % (:n state)))
             (update :n inc))
           (update :result conj :done))))))
