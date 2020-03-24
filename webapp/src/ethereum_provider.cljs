(ns ethereum-provider
  (:require [ethers]))

(def ethereum-client (.-ethereum js/window))

(def erc20-method{ :balanceOf "[{ \"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"})

(defn not-installed?[]
  (or (nil? ethereum-client) 
      (not (.-isMetaMask ethereum-client))))

(defn connect[on-accounts-changed]
  (-> (.send ethereum-client "eth_requestAccounts")
      (.then (.on ethereum-client "accountsChanged" on-accounts-changed))
      (.catch #(throw (.-message %)))))

(defn get-token-balance[account, token-address]
  (let [provider (new ethers/providers.Web3Provider ethereum-client)
        contract (new ethers/Contract token-address (:balanceOf erc20-method) provider)]
    (.balanceOf contract account)))
  
