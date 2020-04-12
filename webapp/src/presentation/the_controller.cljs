(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [ethereum-provider :as eth])
  (:use [presentation.components.new-token-stream :only [new-token-stream token-status]]
        [presentation.components.ethereum-connection :only [ethereum-connection]]
        [presentation.components.disabled-panel :only [disabled-panel]]))

(def status (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))
(def account-id)

(defn render[container content]
  (rdom/render content container))

(defn on-token-entered[token-id]
  (try
    (let [token (eth/get-erc20 token-id)]
      (reset! token-status "searching...")
      (-> (.balanceOf token account-id)
          (.then #(reset! token-status (str "found (balance: "% ")")))
          (.catch #(reset! token-status (str %)))))
  (catch :default e
    (reset! token-status (str e)))))

(defn on-connected[account]
  (set! account-id account)
  (-> (eth/get-ether-balance account-id)
      (.then (fn[balance]
                (render status [ethereum-connection account-id balance])
                (render content [new-token-stream on-token-entered])))))

(defn on-disconnected[]
  (render status "Connect to ethereum provider to continue")
  (render content [disabled-panel [new-token-stream nil]]))

(defn ^:export main[]
  ;(js/window.addEventListener "error" #(render status (.-message %)))
  (if (eth/not-installed?)
    (render status "Ethereum provider is not installed. Please install it to continue.")
    ;else
      (do (on-disconnected)
          (eth/connect on-connected on-disconnected))))

