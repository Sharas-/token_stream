(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [web3-provider :as provider]
            [abi]
            [presentation.components.token-search :refer [token-search status] :rename {status token-status}]))

(def status (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))
(def account-id)

(defn render[container content]
  (rdom/render content container))

(defn invalid-address?[address]
  (nil? (re-matches #"^0x[0-9a-fA-F]{40}$" address)))

(defn on-token-entered[token-address]
   (if (invalid-address? token-address)
      (reset! token-status (str "invalid address: " token-address))
      (do
       (reset! token-status "searching...")
        (-> (provider/eth-rpc :requestAccounts)
            (.then (fn[accounts]
                      (let[account (first accounts)
                          transaction {:from account :to token-address :data (abi/encode-call :balanceOf account)}]
                      (-> (provider/eth-rpc :call transaction)
                          (.then #(reset! token-status (str "found (balance: "% ")")))))))
            (.catch #(render status (.-message %)))))))

(defn ^:export main[]
  ;(js/window.addEventListener "error" #(render status (.-message %)))
  (if (provider/not-installed?)
    (render status "Ethereum provider is not installed. Please install it to continue.")
   ;else
    (render content [token-search on-token-entered])))
