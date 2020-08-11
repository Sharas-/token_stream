(ns abi
  (:require [ethereumjs-abi]))

(defonce signature {:erc20/balanceOf "(address):(uint256)"
                    :erc20/name "()"
                    :erc20/symbol "()"})

(defn encode-call[method & args]
  (let [method-abi (str (name method) (method signature))
        encoded-buf (apply ethereumjs-abi/simpleEncode method-abi args)]
  (str "0x" (.toString encoded-buf "hex"))))


