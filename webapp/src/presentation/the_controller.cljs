(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [ethereum-provider :as eth-provider]))

(def provider-state (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))

(defn render[container content]
  (rdom/render content container))

;;--- HTML compontents ---
;(defn status [message]
;    [:p message])

(defn create-token-stream[account]
  [:p "create new token stream"
    [:br] 
    [:label {:for "token-address"} "token address"]
    [:input.token-address {:type "text"}]])

(defn on-accounts-changed[accounts]
  (render content "")
  (if (empty? accounts)
    (render provider-state "Connect to ethereum provider to continue")
    ;else
    (let [account (get accounts 0)]
      (render provider-state (str "Connected to account: " account))
      (render content [create-token-stream account]))))

(defn ^:export main[]
  (if (eth-provider/not-installed?)
    (render provider-state "Ethereum provider is not installed. Please install it to continue.")
    ;else
    (-> (eth-provider/connect on-accounts-changed)
        (.catch #(render provider-state (str "Error: " %))))))

