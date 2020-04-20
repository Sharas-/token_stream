(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [ethereum-provider :as eth])
  (:use [presentation.components.new-token-stream :only [new-token-stream token-status]]
        [presentation.components.ethereum-connection :only [ethereum-connection]]
        [presentation.components.disabled-panel :only [disabled-panel]]))

(def network "rinkeby")
(def status (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))
(def account-id)

;--- components ---
;(def token-actions[token-id token-name

(defn render[container content]
  (rdom/render content container))

(defn get-token-search-error[error]
  (let [error-code (.-code error)]
    (if (= error-code "CALL_EXCEPTION")
      "not an ERC20 token address"
      (str error))))

(defn on-token-entered[token-id]
  (if (eth/address-invalid? token-id)
      (reset! token-status (str "invalid address: " token-id))
      (do
        (reset! token-status "searching...")
        (-> (eth/make-erc20 token-id)
            (.balanceOf account-id)
            (.then #(reset! token-status (str "found (balance: "% ")")))
            (.catch #(reset! token-status (get-token-search-error %)))))))

(defn on-connected[account]
  (set! account-id account) 
  (-> (eth/get-ether-balance account-id)
      (.then (fn[balance]
                (render status [ethereum-connection account-id network balance])
                (render content [new-token-stream on-token-entered])))))

(defn on-disconnected[]
  (render status "Connect to ethereum provider to continue")
  (render content [disabled-panel [new-token-stream nil]]))

(defn ^:export main[]
  ;(js/window.addEventListener "error" #(render status (.-message %)))
  (if (eth/not-installed?)
    (render status "Ethereum provider is not installed. Please install it to continue.")
    ;else
    (eth/connect on-connected on-disconnected network)))
