(ns presentation.components.new-token-stream
    (:require [reagent.core :as r]))

;--- view-model ---
(def token-status (r/atom ""))

(defn new-token-stream[on-token-entered]
  [:p 
    [:br] 
    [:span "create new token stream"]
    [:br] 
    [:label {:for "token-address"} "token address"]
    [:input#token-address {:type "text" :onKeyPress #(when (some #{13} [(.-keyCode %) (.-which %)])
                                          (let [token-id (-> % .-target .-value)]
                                            (on-token-entered token-id)))}]
    [:span @token-status]])

