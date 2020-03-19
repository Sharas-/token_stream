(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
           [ethereum-provider :as eth-provider]))

(defonce dom-root (js/document.getElementById "app"))

(defn render[content]
  (rdom/render content dom-root))

;;--- HTML compontents ---
(defn status[message]
  [:p message])

(defn ethereum-connection[accounts]
  [:div (str "connected to account: " (aget accounts 0))])

;(defn init-web3-provider[]
;  (let [mmask (.-ethereum js/window)]
;    (if (nil? mmask)
;      (render [status "MetaMask not installed"])
;    ;else
;      (do
;        (render [status "Connecting to Metamask"])
;        (let [accounts-promise (.send mmask "eth_requestAccounts")]
;          (.then accounts-promise 
;             (fn[accounts] (render [metamask-connection accounts])))
;             (fn[error] (render [status (str "error connecting to MetaMask: " error)])))))))
;
(defn ^:export main[]
  (render [status "Connecting to ethereum"])
  (if (eth-provider/installed?)
    (-> (eth-provider/connect)
        (.then  #(render [ethereum-connection (js->clj %)]))
        (.catch #(render [status (str "Error connecting to ethereum: " %)])))
   (render [status "Ethereum provider is not installed. Please install it to continue."])))

