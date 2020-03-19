(ns ethereum-provider
  (:require [ethers]))

(def mmask (.-ethereum js/window))

(defn not-installed?[] 
  (nil? mmask))

(defn connect[]
    (if (not-installed?)
      (throw "MetaMask is not installed")
      (.send mmask "eth_requestAccounts")))
