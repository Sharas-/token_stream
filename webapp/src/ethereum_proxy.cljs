(ns ethereum-proxy
  (:require [web3-eth]))

(def the-provider (.-ethereum js/window))

(def erc20-method
  {:balanceOf (clj->js[{:constant :true :inputs [{:name :_owner :type :address}] :name :balanceOf :outputs [{:name :balance :type :uint256}] :payable :false :stateMutability :view :type :function}])})

(defn not-installed?[]
  (nil? the-provider))

(defn connect[on-connected, on-disconnected]
  (.on the-provider "accountsChanged" #(if (empty? %)
                                         (on-disconnected)
                                         (on-connected (first %))))
  (def eth-client (new web3-eth the-provider))
  (-> (.enable the-provider)
      (.catch #(throw (.-message %)))))

;(defn get-token-balance[account, token-address]
;  (let [provider (new ethers/providers.Web3Provider ethereum-client)
;        contract (new ethers/Contract token-address (:balanceOf erc20-method) provider)]
;    (.balanceOf contract account)))

;example token address on rinkeby (BBBcoin)
;0xb7e81d6141079162523fcdbd5962cca61ea2ad6a

(defn get-ether-balance[account]
   (.getBalance eth-client account "latest" #()))

(defn get-token-balance[account, token-address]
 (let [contract (new eth-client.Contract (get erc20-method :balanceOf) token-address)]
   (-> contract .methods.balanceOf.call (clj->js{:_owner account}))))
  
