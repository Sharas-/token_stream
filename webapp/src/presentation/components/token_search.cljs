(ns presentation.components.token-search
    (:require [reagent.core :as r]))

;--- view-model ---
(def status (r/atom ""))

(defn token-search[on-token-entered]
  [:p 
    [:label {:for "token-address"} "token address"]
    [:input#token-address {:type "text" :onKeyPress #(when (some #{13} [(.-keyCode %) (.-which %)])
                                          (let [token-id (-> % .-target .-value)]
                                            (on-token-entered token-id)))}]
    [:span @status]])

