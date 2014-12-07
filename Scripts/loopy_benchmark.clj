(ns loopy-benchmark
  (:use [arcadia.core])
  (:import LoopyBenchmark2 LoopyBenchmark3
           ListLoopBenchmark2))

;; make sure this doesn't annoyingly fail to erase itself from clojure
;; code when not profiling
(defmacro profiling [name & body]
  `(do (UnityEngine.Profiler/BeginSample ~name)
       (let [res# (do ~@body)]
         (UnityEngine.Profiler/EndSample)
         res#)))

;; imperative loop decrementing from 1e6
(defn ^long test1 []
  (loop [i 1000000]
    (if (> i 0)
      (recur (unchecked-dec-int i))
      i)))

(defcomponent LoopTester []
  (Update [this]
    (profiling "the test1"
      (test1))))

;; dirt-simple imperative loop decrementing from 1e6, no casts
(def ^LoopyBenchmark2 lb2
  (LoopyBenchmark2.))

(defcomponent LoopTester2 []
  (Update [this]
    (profiling "the LoopyBenchmark2"
      (lb2))))

;; slightly more complex imperative loop decrementing from 1e6, copied
;; from reassembled bytecode of test1
(def ^LoopyBenchmark3 lb3
  (LoopyBenchmark3.))

(defcomponent LoopTester3 []
  (Update [this]
    (profiling "the LoopyBenchmark3"
      (lb3))))

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
    (profiling "the test4"
      (test4))))

;; ============================================================
;; list loop
;; ============================================================

;; checked math, just for reference
(defn list-test-0 []
  (loop [l '(), i 1000000]
    (if (> i 0)
      (recur (conj l i) (dec i))
      (count l))))

(set-unchecked-math true)

(defn list-test-1 []
  (loop [l '(), i 1000000]
    (if (> i 0)
      (recur
        (conj l i)
        (dec i))
      (count l))))

(set-unchecked-math nil)

(defcomponent ListLoopTester1 []
  (Update [this]
    (profiling "the list-test-1"
      (list-test-1))))

;; cobbled together from reassembled bytecode of list-test-1, which
;; turns out to involve an extra function allocation
(def ^ListLoopBenchmark2 llb2
  (ListLoopBenchmark2.))

(defcomponent ListLoopTester2 []
  (Update [this]
    (profiling "the ListLoopBenchmark2"
      (llb2))))
