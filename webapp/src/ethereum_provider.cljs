(ns ethereum-provider
  (:require [ethers]))

(def eth-provider (.-ethereum js/window))
(def eth-client (new ethers/providers.Web3Provider eth-provider))

(def erc20-method {:balanceOf (clj->js ["function balanceOf(address owner) view returns (uint)"])})

;(def erc20-method {:balanceOf (clj->js[{:constant :true :inputs [{:name :_owner :type :address}] :name :balanceOf :outputs [{:name :balance :type :uint256}] :payable :false :stateMutability :view :type :function}])})

;(def erc20-method{ :balanceOf "[{ \"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"})

(defn not-installed?[]
  (nil? eth-provider))

(defn connect[on-connected, on-disconnected]
  (.on eth-provider "accountsChanged" #(if (empty? %)
                                         (on-disconnected)
                                         (on-connected (first %))))
  (-> (.send eth-provider "eth_requestAccounts")
      (.catch #(throw (.-message %)))))

(defn get-ether-balance[account-id]
   (-> (.getBalance eth-client account-id)
       (.then #(ethers/utils.formatEther %))))

;token Avastar
;0x30e011460ab086a0daa117df3c87ec0c283a986e
(defn get-token-balance[account-id, token-address]
  (-> (new ethers/Contract "ss" (get erc20-method :balanceOf) eth-client)
      (.balanceOf account-id)
      (.then #(str %))))
   ;(let [contract (new ethers/Contract token-address (get erc20-method :balanceOf) eth-client)]
    ;(-> contract .balanceOf account-id)))

(defn get-erc20[token-address]
  (let [valid-address (ethers/utils.getAddress token-address)]
    (new ethers/Contract valid-address (get erc20-method :balanceOf) eth-client)))
