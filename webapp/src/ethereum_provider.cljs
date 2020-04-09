(ns ethereum-provider
  (:require [ethers]))

(def eth-provider (.-ethereum js/window))
(def eth-client (new ethers/providers.Web3Provider eth-provider))

(def erc20-method {:balanceOf (clj->js[{:constant :true :inputs [{:name :_owner :type :address}] :name :balanceOf :outputs [{:name :balance :type :uint256}] :payable :false :stateMutability :view :type :function}])})

;(def erc20-method{ :balanceOf "[{ \"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"})

(defn not-installed?[]
  (nil? eth-provider))

(defn connect[on-connected, on-disconnected]
  (.on eth-provider "accountsChanged" #(if (empty? %)
                                         (on-disconnected)
                                         (on-connected (first %))))
  (-> (.send eth-provider "eth_requestAccounts")
      (.catch #(throw (.-message %)))))

(defn get-ether-balance[account]
   (-> (.getBalance eth-client account)
       (.then #(ethers/utils.formatEther %))))

(defn get-token-balance[account, token-address]
  (let [contract (new ethers/Contract token-address (get erc20-method :balanceOf) eth-client)]
    (-> contract .balanceOf account)))
