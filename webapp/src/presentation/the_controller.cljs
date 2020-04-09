(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [ethereum-provider :as eth]))

(def status (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))

(defn render[container content]
  (rdom/render content container))

;;--- compontents ---
(def token-balance (r/atom ""))

(defn ^:export on-token-entered[account token-id]
  (-> (eth/get-token-balance account token-id)
      (.then #(reset! token-balance %))
      (.catch #(js/window.alert %))))

(defn new-token-stream[account balance]
  [:p 
    [:span "account: " account " balance: " balance]
    [:br] 
    [:span "create new token stream"]
    [:br] 
    [:label {:for "token-address"} "token address"]
    [:input#token-address {:type "text" :onKeyPress #(when (some #{13} [(.-keyCode %) (.-which %)])
                                          (let [token-id (-> % .-target .-value)]
                                            (presentation.the-controller/on-token-entered account token-id)))}]
    [:span @token-balance]])

(defn on-connected[account]
  (->(eth/get-ether-balance account)
     (.then #(render content [new-token-stream account %]))))

(defn ^:export main[]
  (if (eth/not-installed?)
    (render status "Ethereum provider is not installed. Please install it to continue.")
    ;else
    (-> (eth/connect on-connected
                    #(render content "Connect to ethereum provider to continue"))
        (.catch #(render content (str "error connecting to ethereum: " %))))))

