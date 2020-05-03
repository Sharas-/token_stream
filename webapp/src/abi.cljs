(ns abi
  (:require [ethereumjs-abi]))

(defonce signature {:balanceOf "(address):(uint256)"})

(defn encode-call[method-id & args]
  (let [method-abi (str (name method-id) (get signature method-id))
        encoded-buf (apply ethereumjs-abi/simpleEncode method-abi args)]
  (str "0x" (.toString encoded-buf "hex"))))


