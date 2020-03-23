(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [ethereum-provider :as eth-provider]))

(def state-name (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))

(defn render[container content]
  (rdom/render content container))

;;--- HTML compontents ---
;(defn status [message]
;    [:p message])

(defn create-token-stream[]
  [:p "create new token stream"
    [:br] 
    [:label {:for "token-address"} "token address"]
    [:input.token-address {:type "text"}]])

;(defn execute[step-name step sucess-content success-status]
  ;(render state-name step-name)
  ;(-> (step)
      ;(.then (fn[result]
               ;(render state-name (success-status result))
               ;(render content [sucess-content])))
      ;(.catch #(render content (str "error: " %)))))

(defn ^:export main[]
  (render state-name "Connecting to ethereum")
  (if (eth-provider/not-installed?)
    (render state-name "Ethereum provider is not installed. Please install it to continue.")
    ;else
    ;(execute "Connecting to ethereum provider", eth-provider/connect, create-token-stream, #(str "connected to account: " %))))
    (-> (eth-provider/connect)
        (.then  (fn[account]
                  (render state-name (str "connected to account: " account))
                  (render content create-token-stream)))
        (.catch (fn[error]
                  (render state-name (str "error: " error)))))))
