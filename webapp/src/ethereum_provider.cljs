(ns ethereum-provider
  (:require [ethers]))

(def mmask (.-ethereum js/window))

(defn installed?[] 
  (some? mmask))

(defn connect[]
    (if (installed?)
      (.send mmask "eth_requestAccounts")
      (throw "MetaMask is not installed")))
