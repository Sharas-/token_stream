(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [ethereum-provider :as eth-provider]))

(defonce dom-root (js/document.getElementById "app"))

(defn render[content]
  (rdom/render content dom-root))

;;--- HTML compontents ---
(defn status[message]
  [:p message])

(defn ethereum-connection[account]
  [:div (str "connected to account: " account)])

(defn ^:export main[]
  (render [status "Connecting to ethereum"])
  (if (eth-provider/not-installed?)
    (render [status "Ethereum provider is not installed. Please install it to continue."])
    ;else
    (-> (eth-provider/connect)
        (.then #(let [current-account (.-result %)]
                     (render [ethereum-connection current-account])))
        (.catch #(render [status (str "Error connecting to ethereum: " %)])))))

