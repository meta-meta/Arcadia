(ns loopy-benchmark
  (:use [arcadia.core])
  (:import LoopyBenchmark2 LoopyBenchmark3
           ListLoopBenchmark2))

;; imperative loop decrementing from 1e6
(defn ^long test1 []
  (loop [i 1000000]
    (if (> i 0)
      (recur (unchecked-dec-int i))
      i)))

(defcomponent LoopTester []
  (Update [this]
    (test1)))

;; dirt-simple imperative loop decrementing from 1e6, no casts
(def ^LoopyBenchmark2 lb2
  (LoopyBenchmark2.))

(defcomponent LoopTester2 []
  (Update [this]
    (lb2)))

;; slightly more complex imperative loop decrementing from 1e6, copied
;; from reassembled bytecode of test1
(def ^LoopyBenchmark3 lb3
  (LoopyBenchmark3.))

(defcomponent LoopTester3 []
  (Update [this]
    (lb3)))

;; test1, hedged with (set-unchecked-math true); in practice seems to
;; make no difference at all

(defn set-unchecked-math [val]
  (alter-var-root #'*unchecked-math* (constantly val)))

(set-unchecked-math true)

(defn ^long test4 []
  (loop [i 1000000]
    (if (> i 0)
      (recur (unchecked-dec-int i))
      i)))

(set-unchecked-math nil)

(defcomponent LoopTester4 []
  (Update [this]
    (test4)))

;; ============================================================
;; list loop
;; ============================================================

;; checked math, just for reference
(defn list-test-0 []
  (loop [l '(), i 1000000]
    (if (> i 0)
      (recur (conj l i) (dec i))
      (count l))))

(defmacro profiling [name & body]
  `(do (UnityEngine.Profiler/BeginSample ~name)
       (let [res# (do ~@body)]
         (UnityEngine.Profiler/EndSample)
         res#)))

(set-unchecked-math true)

(defn list-test-1 []
  (profiling "the list-test-1"
    (loop [l '(), i 1000000]
      (if (> i 0)
        (recur
          (conj l i)
          (dec i))
        (count l)))))

(set-unchecked-math nil)

(defcomponent ListLoopTester1 []
  (Update [this]
    (list-test-1)))

;; cobbled together from reassembled bytecode of list-test-1, which
;; turns out to involve an extra function allocation
(def ^ListLoopBenchmark2 llb2
  (ListLoopBenchmark2.))

(defcomponent ListLoopTester2 []
  (Update [this]
    (llb2)))
